package me.shivzee.io;

import com.launchdarkly.eventsource.EventHandler;
import com.launchdarkly.eventsource.MessageEvent;
import me.shivzee.Config;
import me.shivzee.JMailTM;
import me.shivzee.callbacks.EventListener;
import me.shivzee.callbacks.MessageListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

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
    public void onOpen() throws Exception {
        listener.onReady();
    }

    @Override
    public void onClosed() throws Exception {
        listener.onClose();
    }

    @Override
    public void onMessage(String s, MessageEvent messageEvent) throws Exception {
        String data = messageEvent.getData().trim();
        try{
            if(!data.equals("")){
                JSONObject json = (JSONObject) parser.parse(messageEvent.getData());
                if(json.get("@type").toString().equals("Message")){
                    listener.onMessageReceived(mailTM.getMessageById(json.get("id").toString()));
                }
            }

        }catch (Exception e){
            listener.onError("Error While Parsing "+e);
        }

    }

    @Override
    public void onComment(String s) throws Exception {
        // TODO -> Add this Feature Afterwards
    }

    @Override
    public void onError(Throwable throwable) {
        listener.onError(throwable.getMessage());
    }

}
