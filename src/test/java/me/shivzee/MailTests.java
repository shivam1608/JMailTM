package me.shivzee;

import me.shivzee.util.Account;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.List;

public class MailTests {
    private static JMailTM jMailTm;
    private static String EMAIL;
    private static final String PASSWORD = "Ahamed@1234";

    @BeforeClass
    public void beforeClass() throws Exception {
        jMailTm = JMailBuilder.createDefault(PASSWORD);
        Account acc = jMailTm.getSelf();
        EMAIL = acc.getEmail();

        System.out.println("=== Account Created ===");
        System.out.println("Email     : " + EMAIL);
        System.out.println("ID        : " + acc.getId());
        System.out.println("Quota     : " + acc.getQuota());
        System.out.println("Used      : " + acc.getUsed());
        System.out.println("Disabled  : " + acc.isDisabled());
        System.out.println("Deleted   : " + acc.isDeleted());
        System.out.println("Created At: " + acc.getCreatedAt());
        System.out.println("Updated At: " + acc.getUpdatedAt());
        System.out.println("=======================");

        Assert.assertFalse(acc.isDisabled(), "Newly created account should not be disabled");
        Assert.assertFalse(acc.isDeleted(), "Newly created account should not be deleted");
    }

    @AfterClass
    public void afterClass() {
        if (jMailTm == null) return;
        Assert.assertTrue(jMailTm.delete(), "Failed to delete account " + EMAIL);
        System.out.println("Deleted account : " + EMAIL);
    }

    @Test(invocationCount = 10, threadPoolSize = 10)
    public void testFetchMessageOnANewInbox() throws Exception {
        //Created a new object everytime to avoid threading issues
        List<Message> messages = new MailTestModule().fetchMails(EMAIL, PASSWORD);
        System.out.println("Mails in a new inbox : " + messages.size());
        Assert.assertEquals(messages.size(), 0, "Mails in a new inbox should be 0");
    }
}
