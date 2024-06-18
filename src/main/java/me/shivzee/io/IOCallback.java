package me.shivzee.io;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import me.shivzee.JMailTM;
import me.shivzee.callbacks.EventListener;
import me.shivzee.util.Account;

import java.lang.reflect.Field;

public class IOCallback implements EventHandler {

    private final EventListener listener;
    private final JMailTM mailTM;


    public IOCallback(EventListener listener , JMailTM mailTM){
        this.listener = listener;
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
            if(!data.isEmpty()){
                JsonObject json = JsonParser.parseString(messageEvent.getData()).getAsJsonObject();
                if(json.get("@type").getAsString().equals("Message")){
                    boolean seen = json.get("seen").getAsBoolean();
                    boolean isDeleted = json.get("isDeleted").getAsBoolean();

                    String id = json.get("id").getAsString();
                    if(isDeleted){
                        listener.onMessageDelete(id);
                    }
                    else if(seen){
                        listener.onMessageSeen(mailTM.getMessageById(id));
                    }else{
                        listener.onMessageReceived(mailTM.getMessageById(id));
                    }
                }else{
                    Field field = mailTM.getClass().getDeclaredField("gson");
                    field.setAccessible(true);
                    Gson gson = (Gson) field.get(mailTM);
                    Account account = gson.fromJson(json , Account.class);
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
