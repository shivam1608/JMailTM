import me.shivzee.JMailTM;
import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.exceptions.MessageFetchException;
import me.shivzee.util.Attachment;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;
import me.shivzee.util.Response;

import javax.security.auth.login.LoginException;
import java.util.List;

public class GetMessagesExample {
    public static void main(String[] args) {
        try{
            JMailTM mailer = JMailBuilder.login("someemail@mail.com", "shivzee");
            mailer.fetchMessages(10, new MessageFetchedCallback() {
                @Override
                public void onMessagesFetched(List<Message> messages) {
                    for(Message message : messages){
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
                }

                @Override
                public void onError(Response error) {
                    System.out.println("Error : "+error.getResponse());
                }
            });
        }catch (LoginException | MessageFetchException e){
            e.printStackTrace();
        }
    }
}