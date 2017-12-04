package com.example.morgane.sharemiamapp;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseListAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MesMessagesActivity extends AppCompatActivity {
    private FirebaseAuth auth;
    private ArrayList<ChatMessage> sendMessages = new ArrayList<ChatMessage>();
    private ArrayList<ChatMessage> receiveMessages = new ArrayList<ChatMessage>();
    private ListView lvSendMess, lvReceivMess;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mes_messages);

lvReceivMess = (ListView) findViewById(R.id.lvReceivedMess);
lvSendMess = (ListView) findViewById(R.id.lvSendMess);

        auth = FirebaseAuth.getInstance();

       /* final DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        final Query queryReceiver = reference.child("Messages").orderByChild("receiver").equalTo(auth.getCurrentUser().getUid());
        final Query querySender = reference.child("Messages").orderByChild("sender").equalTo(auth.getCurrentUser().getUid());


        queryReceiver.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot chatMessage : dataSnapshot.getChildren()) {
                       ChatMessage cm = new ChatMessage(chatMessage.getValue(ChatMessage.class).getMessageText(),
                               chatMessage.getValue(ChatMessage.class).getSender(),
                               chatMessage.getValue(ChatMessage.class).getReceiver(),
                               chatMessage.getValue(ChatMessage.class).getMessageTime());

                        receiveMessages.add(cm);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        querySender.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // dataSnapshot is the "issue" node with all children with id 0
                    for (DataSnapshot chatMessage : dataSnapshot.getChildren()) {
                        ChatMessage cm = new ChatMessage(chatMessage.getValue(ChatMessage.class).getMessageText(),
                                chatMessage.getValue(ChatMessage.class).getSender(),
                                chatMessage.getValue(ChatMessage.class).getReceiver(),
                                chatMessage.getValue(ChatMessage.class).getMessageTime());

                        sendMessages.add(cm);
                    }
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/
        FirebaseListAdapter<ChatMessage> adapterSend = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("Messages").orderByChild("sender").equalTo(auth.getCurrentUser().getUid())){
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(Constant.USERS_ARRAY_LIST.get(model.getSender()).Username );

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };



        lvSendMess.setAdapter(adapterSend);

        FirebaseListAdapter<ChatMessage> adapterReceiv = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.message, FirebaseDatabase.getInstance().getReference().child("Messages").orderByChild("receiver").equalTo(auth.getCurrentUser().getUid())) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageText = (TextView)v.findViewById(R.id.message_text);
                TextView messageUser = (TextView)v.findViewById(R.id.message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.message_time);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(Constant.USERS_ARRAY_LIST.get(model.getSender()).Username );

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        lvReceivMess.setAdapter(adapterReceiv);

    }
}
