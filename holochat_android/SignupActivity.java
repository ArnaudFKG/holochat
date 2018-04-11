package com.fickinger.arnaud.holochat;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class SignupActivity extends AppCompatActivity {

    View.OnClickListener signup = new View.OnClickListener() {
        @Override
        public void onClick(View v) {

        }
    };

    private static final String TAG = "Signup";

    EditText email;
    EditText password;
    FirebaseAuth mAuth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        mAuth = FirebaseAuth.getInstance();
        email = (EditText) findViewById(R.id.email);
        password = (EditText) findViewById(R.id.password);
        Button signup_button = (Button) findViewById(R.id.signup_button2);
        signup_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.createUserWithEmailAndPassword(email.getText().toString(), password.getText().toString())
                        .addOnCompleteListener(SignupActivity.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    Log.d("TAG", "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();
                                    String userId = user.getEmail();
                                    FirebaseFirestore db = FirebaseFirestore.getInstance();
                                    Map<String, Object> event = new HashMap<>();
                                    event.put("sender", "");
                                    event.put("event", "");
                                    db.collection("events").document(userId)
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
                                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                                            Toast.LENGTH_SHORT).show();
                                                    Log.w(TAG, "Error writing document", e);
                                                }
                                            });
                                    Intent goToMain = new Intent(SignupActivity.this, MainActivity.class);
                                    startActivity(goToMain);

                                } else {

                                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignupActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();

                                }

                            }
                        });
            }
        });
    }
}
