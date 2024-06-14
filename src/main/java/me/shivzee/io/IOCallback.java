package me.shivzee.io;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import me.shivzee.Config;
import me.shivzee.JMailTM;
import me.shivzee.callbacks.EventListener;
import me.shivzee.util.Account;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.lang.reflect.Method;

public class IOCallback implements EventHandler {

    private final EventListener listener;
    private final JSONParser parser;
    private final JMailTM mailTM;


    public IOCallback(EventListener listener , JMailTM mailTM){
        this.listener = listener;
        this.parser = Config.parser;
        this.mailTM = mailTM;
    }


    @Override
    public void onOpen() {
        listener.onReady();
    }

    @Override
    public void onClosed() {
        listener.onClose();
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) {
        String data = messageEvent.getData().trim();
        try{
            if(!data.equals("")){
                JSONObject json = (JSONObject) parser.parse(messageEvent.getData());
                if(json.get("@type").toString().equals("Message")){
                    boolean seen = (boolean) json.get("seen");
                    boolean isDeleted = (boolean) json.get("isDeleted");

                    String id = json.get("id").toString();
                    if(isDeleted){
                        listener.onMessageDelete(id);
                    }
                    else if(seen){
                        listener.onMessageSeen(mailTM.getMessageById(id));
                    }else{
                        listener.onMessageReceived(mailTM.getMessageById(id));
                    }
                }else{
                    Method mailUtility = mailTM.getClass().getDeclaredMethod("mailUtility", JSONObject.class);
                    mailUtility.setAccessible(true);
                    Account account = (Account) mailUtility.invoke(mailTM , json);
                    if(account.isDeleted()){
                        listener.onAccountDelete(account);
                    }else{
                        listener.onAccountUpdate(account);
                    }
                }
            }

        }catch (Exception e){
            listener.onError(""+e.getMessage());
        }

    }

    @Override
    public void onComment(String s) {
        listener.onSSEComment(s);
    }

    @Override
    public void onError(Throwable throwable) {
        listener.onError(throwable.getMessage());
    }

}
