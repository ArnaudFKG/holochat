package com.fickinger.arnaud.holochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;

import org.w3c.dom.Text;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;



public class LoadingActivity extends AppCompatActivity {

    Client user;
    String otherClientId;
    private final String TAG ="Loading";
    public final static String Id = "com.fickinger.arnaud.holochat.Id";
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    DocumentReference events;
    TextView currentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        currentState = (TextView) findViewById(R.id.state);
        otherClientId = MainActivity.otherClientId;
        user = MainActivity.user;
        events = db.collection("events").document(this.user.id);
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
                    if(snapshot.get("sender").toString().equals(otherClientId)){
                        user.sendEvent(Event.messageCallBusy, snapshot.get("sender").toString());
                        return;
                    }
                    if(!user.changeState(Event.valueOf(snapshot.get("event").toString()))){
                        user.sendEvent(Event.notValid, otherClientId);
                        return;
                    }
                    if(user.state==State.InCall){
                        Intent goToCall = new Intent(LoadingActivity.this, ChatActivity.class);
                        goToCall.putExtra(LoadingActivity.Id, otherClientId);
                        startActivity(goToCall);
                    }
                    else currentState.setText(user.state.toString());
                } else {
                    Log.d(TAG, "Current data: null");
                }
            }
        });

    }
}
