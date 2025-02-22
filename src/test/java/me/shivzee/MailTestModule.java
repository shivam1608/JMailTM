package me.shivzee;

import me.shivzee.callbacks.MessageFetchedCallback;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;
import me.shivzee.util.Response;

import java.util.ArrayList;
import java.util.List;

public class MailTestModule {

    public List<Message> fetchMails(String USERNAME, String PASSWORD) throws Exception {
        JMailTM mailer = JMailBuilder.login(USERNAME, PASSWORD);
        final List<Message> messages = new ArrayList<>();
        mailer.fetchMessages(new MessageFetchedCallback() {
            @Override
            public void onMessagesFetched(List<Message> list) {
                messages.addAll(list);
            }

            @Override
            public void onError(Response response) {
                throw new RuntimeException("Fetch Message - onError" + response.getResponse());
            }
        });
        return messages;
    }
}
