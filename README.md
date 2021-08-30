# JMailTM
JMailTM is a Lightweight Java Wrapper for https://mail.tm API (A Temp Mail Service). It has a easy to use interface and callbacks with javaDoc.

### Version : 0.1

## Add to your projects
Easy to add in your projects using gradle, maven or jar

### Gradle
- Add it in your root build.gradle at the end of repositories:
```gradle
allprojects {
		repositories {
			maven { url 'https://jitpack.io' }
		}
	}
```
- Add the dependency (replace version with the version on top)
```gradle
dependencies {
	        implementation 'com.github.shivam1608:JMailTM:0.1'
	}
```

### Maven
- Add the repository in pom.xml file
```maven
	<repositories>
		<repository>
		    <id>jitpack.io</id>
		    <url>https://jitpack.io</url>
		</repository>
	</repositories>

```
- Add the dependency (replace version with the version on top)
```maven 

	<dependency>
	    <groupId>com.github.shivam1608</groupId>
	    <artifactId>JMailTM</artifactId>
	    <version>0.1</version>
	</dependency>


```

### Jar 
Download the jar from this repo 
```
out/artifact/JMailTM.jar or Use the Release Section
```

## Looking for JavaDoc? 
[JMailTM JavaDocs](https://shivam1608.github.io/JMailTM/)

# Ouick Start
- Open a Message Listener with a new Temp EMail
```java
import me.shivzee.JMailTM;
import me.shivzee.callbacks.MessageListener;
import me.shivzee.util.JMailBuilder;
import me.shivzee.util.Message;

import javax.security.auth.login.LoginException;

public class JMailService {
    public static void main(String[] args)  {
        try {
            JMailTM mailer = JMailBuilder.createDefault("randomPassword");
            mailer.init();
            System.out.println("Email : "+mailer.getSelf().getEmail());

            mailer.openMessageListener(new MessageListener() {
                @Override
                public void onMessageReceived(Message message) {
                    System.out.println("Message Has Attachments ?  : "+message.hasAttachments());
                    System.out.println("Message Content : "+message.getContent());
                    System.out.println("Message RawHTML : "+message.getRawHTML());
                    // To Mark Message As Read
                    message.markAsRead(status->{
                        System.out.println("Message "+message.getId()+" Marked As Read");
                    });
                }

                @Override
                public void onError(String error) {
                    System.out.println("Some Error Occurred "+error);
                }
            });

        }catch (LoginException exception){
            System.out.println("Exception Caught "+exception);
        }
    }

}
```

## Some Common Methods/Functions
- Get Details of Email
```java


```
