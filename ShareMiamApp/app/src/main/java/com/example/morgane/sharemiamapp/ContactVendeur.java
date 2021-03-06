package com.example.morgane.sharemiamapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ContactVendeur extends AppCompatActivity {
public String uidVendeur, uidFood;
public ChatMessage chatMessage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_vendeur);
       final Button btnSend = (Button) findViewById(R.id.btnSend);

        Intent intent = getIntent();
        Bundle extras = intent.getExtras();

        uidVendeur = ((String)extras.get("uidVendeur"));
        uidFood = ((String)extras.get("uidFood"));

        btnSend.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                EditText input = (EditText)findViewById(R.id.input);

                // Read the input field and push a new instance
                // of com.example.morgane.sharemiamapp.ChatMessage to the Firebase database
                chatMessage = new ChatMessage(input.getText().toString(),
                        FirebaseAuth.getInstance()
                                .getCurrentUser().getUid(),
                        uidVendeur,
                        new Date().getTime()

                );
                FirebaseDatabase.getInstance()
                        .getReference("Messages")
                        .push()
                        .setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance()
                                        .getCurrentUser().getUid(),
                                uidVendeur,
                                new Date().getTime()

                                )
                        );

                // Clear the input
                input.setText("");



            }
        });

      ListView listOfMessages = (ListView)findViewById(R.id.list_of_messages);

         FirebaseListAdapter<ChatMessage> adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference("Messages")) {


            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                if (
                        (model.getSender() == FirebaseAuth.getInstance().getCurrentUser().getUid()
                                && model.getReceiver() == uidVendeur) ||
                                (model.getReceiver() == FirebaseAuth.getInstance().getCurrentUser().getUid()
                                        && model.getSender() == uidVendeur)
                        ) {


                    // Get references to the views of message.xml
                    TextView messageText = (TextView) v.findViewById(R.id.message_text);
                    TextView messageUser = (TextView) v.findViewById(R.id.message_user);
                    TextView messageTime = (TextView) v.findViewById(R.id.message_time);

                    // Set their text
                    messageText.setText(model.getMessageText());
                    messageUser.setText(Constant.USERS_ARRAY_LIST.get(model.getSender()).Username );

                    // Format the date before showing it
                    messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                            model.getMessageTime()));
                }
            }
        };

        listOfMessages.setAdapter(adapter);


    }





}
