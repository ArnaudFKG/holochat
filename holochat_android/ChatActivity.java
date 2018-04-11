package com.fickinger.arnaud.holochat;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class ChatActivity extends AppCompatActivity {
    String otherClientId;
    private View.OnClickListener send = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            return;
        }
    };

    private View.OnClickListener end = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            return;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        otherClientId = MainActivity.otherClientId;
        Button send_button = (Button) findViewById(R.id.send);
        send_button.setOnClickListener(send);
        Button end_button = (Button) findViewById(R.id.end);
        end_button.setOnClickListener(end);
    }



}
