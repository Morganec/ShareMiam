package com.example.morgane.sharemiamapp;

import java.util.Date;

/**
 * Created by morgane on 02/12/2017.
 */

public class ChatMessage {


        private String messageText;
        private String sender;
        private String receiver;
        private long messageTime;

        public ChatMessage(String messageText, String sender, String receiver) {
            this.messageText = messageText;
            this.sender = sender;
            this.receiver = receiver;

            // Initialize to current time
            messageTime = new Date().getTime();
        }

        public ChatMessage(){

        }

        public String getMessageText() {
            return messageText;
        }

        public void setMessageText(String messageText) {
            this.messageText = messageText;
        }

        public String getSender() {
            return sender;
        }
        public String getReceiver(){
            return receiver;
        }

        public void setSender(String sender) {
            this.sender = sender;
        }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }
        public long getMessageTime() {
            return messageTime;
        }

        public void setMessageTime(long messageTime) {
            this.messageTime = messageTime;
        }

}
