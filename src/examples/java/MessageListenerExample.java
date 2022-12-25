import me.shivzee.JMailTM;
import me.shivzee.callbacks.EventListener;
import me.shivzee.util.Account;
import me.shivzee.util.Attachment;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;

import javax.security.auth.login.LoginException;

public class MessageListenerExample {
    public static void main(String [] args){
        try{
            JMailTM mailer = JMailBuilder.createDefault("shivzee");
            System.out.println();
            System.out.println("Logged into "+mailer.getSelf().getEmail());
            System.out.println();
            mailer.openEventListener(new EventListener() {
                @Override
                public void onReady() {
                    System.out.println("Server is Ready to listen to events");
                }

                @Override
                public void onClose() {
                    System.out.println("Server is closing the connection!");
                }

                @Override
                public void onMessageReceived(Message message) {
                    System.out.println("Message Received!");
                    System.out.println("ID : "+message.getId());
                    System.out.println();
                    System.out.println("From Name : "+message.getSenderName());
                    System.out.println("From Address : "+message.getSenderAddress());
                    System.out.println("Subject : " +message.getSubject());
                    System.out.println("Content : " +message.getContent());
                    System.out.println("Has Attachments? : " +message.hasAttachments());
                    System.out.println();
                    if(message.hasAttachments()){
                        for(Attachment x : message.getAttachments()){
                            System.out.println("Name : "+x.getFilename());
                            System.out.println("Size : "+x.getSize());
                            System.out.println("Type : "+x.getDisposition());
                            System.out.println();
                        }
                    }
                    message.asyncMarkAsRead(System.out::println);
                }

                @Override
                public void onMessageDelete(String id) {
                    // Does not fires during weekly server purging of message
                    System.out.println("Message with ID "+id+" was deleted from the server!");
                }

                @Override
                public void onMessageSeen(Message message) {
                    System.out.println("Message with ID "+message.getId()+" was marked as read");
                }

                @Override
                public void onAccountDelete(Account account) {
                    System.out.println("Message with ID "+account.getId()+" was marked as read");
                }

                @Override
                public void onAccountUpdate(Account account) {
                    System.out.println("Now account is using "+account.getUsed()+" killobytes of storage");
                    System.out.println("out of "+account.getQuota()+" killobytes of storage");
                }

                @Override
                public void onError(String error) {
                    System.out.println("Something went wrong "+error);
                }
            });
        }catch (LoginException e){
            System.out.println("Login Failed "+e.getMessage());
        }
    }
}
