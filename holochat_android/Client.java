package com.fickinger.arnaud.holochat;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class Client {

    private final String TAG = "Client";

    State state;
    String id;
    String otherClientId;

    public Client(String userId){
        this.id = userId;
        this.state = State.Ready;
    }

    public boolean UserActionCall(String callee) {
        if (!changeState(Event.userActionCall))
            return false;
        sendEvent(Event.messageCallStart, callee);
        return true;
    }

    public boolean UserActionCallAccept(String caller) {
        if (!changeState(Event.userActionCallAccept))
            return false;
        sendEvent(Event.messageCallStartAck, caller);
        return true;
    }

    public boolean UserActionCallReject(String caller) {
        sendEvent(Event.messageCallReject, caller);
        return true;
    }

    public boolean sendEvent(Event e, String ClientId) {

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Map<String, Object> event = new HashMap<>();
        event.put("sender", this.id);
        event.put("event", e.toString());
        DocumentReference eventToSend = db.collection("events").document(ClientId);
        eventToSend.update(event)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully updated!");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error updating document", e);
                    }
                });
        return true;
    }


    public boolean changeState(Event e) {
        if (!state.validChange(e))
            return false;
        state = state.transitions.get(e);
        if(state.eventToSend!=null){
            sendEvent(state.eventToSend, otherClientId);
        }
        return true;
    }
}

