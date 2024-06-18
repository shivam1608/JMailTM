package me.shivzee;

import me.shivzee.callbacks.EventListener;
import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.exceptions.MessageFetchException;
import me.shivzee.util.*;

import javax.security.auth.login.LoginException;
import java.util.List;

public class Driver {

    public static void main(String [] args){
        try{

            JMailTM mailer = JMailBuilder.login("jmailtm-dev@navalcadets.com" , "Exotic1@");
            JMailTM mailer2= JMailBuilder.createDefault("helloworld");

            System.out.println("__________________________________________________");
            System.out.println("EMAIL");
            System.out.println(mailer.getSelf().getEmail());
            System.out.println(mailer.getSelf().getId());
            System.out.println(mailer.getSelf().getUsed());
            System.out.println(mailer.getSelf().getQuota());
            System.out.println(mailer.getSelf().getUpdatedAt());
            System.out.println("CREATED AT " +mailer.getSelf().getCreatedAt());

            System.out.println(mailer.getTotalMessages());

            mailer.fetchMessages(new MessageFetchedCallback() {
                @Override
                public void onMessagesFetched(List<Message> messages) {
                    System.out.println(messages.get(0).getContent());
                }

                @Override
                public void onError(Response error) {
                    System.out.println(error.getResponse());
                }
            });
            System.out.println("__________________________________________________");


            mailer.openEventListener(new EventListener() {
                @Override
                public void onError(String error) {
                    System.out.println(error);
                }

                @Override
                public void onReady() {
                    System.out.println("Listening to Events!!!");
                }

                @Override
                public void onMessageReceived(Message message) {
                    System.out.println(message.markAsRead());
                    System.out.println("Received a Message");
                    System.out.println("__________________________________________________");
                    System.out.println("ID : "+message.getId());
                    System.out.println("Receivers Mail : " + message.getReceivers().get(0).getAddress());
                    System.out.println("Receivers Name : " + message.getReceivers().get(0).getName());
                    System.out.println("Sender Address : "+message.getSenderAddress());
                    System.out.println("Sender Name : "+message.getSenderName());
                    System.out.println("Subject : "+message.getSubject());
                    System.out.println("Message Content : "+message.getContent());
                    System.out.println("Message HTML : "+message.getRawHTML());
                    System.out.println("Created At : "+message.getCreatedAt());
                    System.out.println("Updated At : "+message.getUpdatedAt());
                    System.out.println("Retention Date : "+message.getRetentionDate());
                    System.out.println("Raw JSON : "+message.getRawJson());
                    System.out.println("Size : "+message.getSize());
                    System.out.println("Download URL : "+message.getDownloadUrl());

                    System.out.println(message.markAsRead());
                    System.out.println("Has Attachment? "+message.hasAttachments());
                    if(message.hasAttachments()){
                        System.out.println("Files > ");
                        for(Attachment attachment : message.getAttachments()){
                            System.out.println("Filename : "+attachment.getFilename());
                            System.out.println("ID : "+attachment.getId());
                            System.out.println("Disposition : "+attachment.getDisposition());
                            System.out.println("Content-Type : "+attachment.getContentType());
                            System.out.println("Download Url : "+attachment.getDownloadUrl());
                        }
                    }
                }

                @Override
                public void onAccountUpdate(Account account) {
                    System.out.println(account.getUsed() +" / "+account.getQuota());
                }

                @Override
                public void onMessageDelete(String id) {
                    System.out.println("Message Deleted "+id);
                }
            });


            mailer2.openEventListener(new EventListener() {

                @Override
                public void onReady() {
                    System.out.println("Listener 2 Active");
                    mailer2.delete();
                }

                @Override
                public void onAccountDelete(Account account) {
                    System.out.println("Account Deleted "+ account.getEmail());
                }

                @Override
                public void onError(String error) {
                    System.out.println(error);
                }
            });

        }catch (LoginException | MessageFetchException e){
            System.out.println(e);
        }
    }
}
