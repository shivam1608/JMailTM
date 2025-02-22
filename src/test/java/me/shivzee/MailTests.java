package me.shivzee;

import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class MailTests {
    private static String EMAIL;
    private static final String PASSWORD = "Ahamed@1234";

    @BeforeClass
    public void beforeClass() throws Exception {
        JMailTM jMailTm = JMailBuilder.createDefault(PASSWORD);
        EMAIL = jMailTm.getSelf().getEmail();
        System.out.println("Created new email : " + EMAIL);
    }

    @Test(invocationCount = 10, threadPoolSize = 10)
    public void testFetchMessageOnANewInbox() throws Exception {
        //Created a new object everytime to avoid threading issues
        List<Message> messages = new MailTestModule().fetchMails(EMAIL, PASSWORD);
        System.out.println("Mails in a new inbox : " + messages.size());
        Assert.assertEquals(messages.size(), 0, "Mails in a new inbox should be 0");
    }
}
