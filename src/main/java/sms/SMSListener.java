package sms;

/**
 * Created by ian on 1/12/2018.
 */

public interface SMSListener {
    public void messageReceived(String messageText);
}
