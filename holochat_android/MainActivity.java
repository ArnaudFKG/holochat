package com.fickinger.arnaud.holochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.WriteBatch;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private final String TAG = "Main";
    TextView caller;
    public static Client user;
    public static String otherClientId;

    FirebaseFirestore db = FirebaseFirestore.getInstance();

    private DocumentReference events;



    FirebaseAuth mAuth;
    FirebaseUser currentUser;

    private View.OnClickListener call = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(user.UserActionCall("test@test.com")){
                Intent goToLoading = new Intent(MainActivity.this, LoadingActivity.class);
                startActivity(goToLoading);
            }
        }
    };

    private View.OnClickListener accept = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(user.UserActionCallAccept(otherClientId)){
                Intent goToLoading = new Intent(MainActivity.this, LoadingActivity.class);
                startActivity(goToLoading);
            }

        }
    };

    private View.OnClickListener reject = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(user.UserActionCallReject(otherClientId))
                findViewById(R.id.callinglayout).setVisibility(View.GONE);
        }
    };



    @Override
    public void onStart() {
        super.onStart();
        if (currentUser == null) {
            Intent goToLogin = new Intent(MainActivity.this, LoginActivity.class);
            startActivity(goToLogin);
        }

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> event = new HashMap<>();
        event.put("sender", "sender_test");
        event.put("event", "event_test");
        /*db.collection("events").document("a.fickinger@hotmail.fr")
                .set(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });*/
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        String userId;
        caller = (TextView) findViewById(R.id.caller);
        if(currentUser!=null){
            userId = currentUser.getEmail();
            user = new Client(userId);
            events = db.collection("events").document(userId);
            events.addSnapshotListener(new EventListener<DocumentSnapshot>() {
                @Override
                public void onEvent(@Nullable DocumentSnapshot snapshot,
                                    @Nullable FirebaseFirestoreException e) {
                    if (e != null) {
                        Log.w(TAG, "Listen failed.", e);
                        return;
                    }

                    if (snapshot != null && snapshot.exists()) {
                        Log.d(TAG, "Current data: " + snapshot.getData());

                        if(!snapshot.get("event").toString().equals(Event.messageCallStart.toString())){
                            user.sendEvent(Event.notValid, snapshot.get("sender").toString());
                            return;
                        }

                        else{
                            otherClientId = snapshot.get("sender").toString();
                            caller.setText(otherClientId);
                            findViewById(R.id.callinglayout).setVisibility(View.VISIBLE);
                        }

                    } else {
                        Log.d(TAG, "Current data: null");
                    }
                }
            });
        }

        Button call_button = (Button) findViewById(R.id.call);
        call_button.setOnClickListener(call);

        Button accept_button = (Button) findViewById(R.id.accept_button);
        Button reject_button = (Button) findViewById(R.id.reject_button);



    }
}
