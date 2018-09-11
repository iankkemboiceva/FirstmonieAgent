package sms;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;

import firstmob.firstbank.com.firstagent.ActivateAgent;
import security.SecurityLayer;


/**
 * Created by brian on 20/07/2016.
 */


public class IncomingOTPSMS extends BroadcastReceiver {
    ActivateAgent main = null;
    //interface
    private static SmsListener mListener;
    @Override
    public void onReceive(Context context, Intent intent)
    {

        final Bundle bundle = intent.getExtras();
        try {
            if (bundle != null)
            {
                final Object[] pdusObj = (Object[]) bundle.get("pdus");
                String message = "";
                String senderNum = "";
                String phoneNumber = "";

                for (int i = 0; i < pdusObj .length; i++) {
                    message += SmsMessage.createFromPdu((byte[])pdusObj[i]).getDisplayMessageBody();

                    SmsMessage currentMessage = SmsMessage.createFromPdu((byte[]) pdusObj[i]);
                   phoneNumber = currentMessage.getDisplayOriginatingAddress();
                    senderNum = phoneNumber;
                  //  message = currentMessage.getDisplayMessageBody();

                }
                SecurityLayer.Log("Message received:", message);
                SecurityLayer.Log("Sender Number:", senderNum);
                    try
                    {
                        if (senderNum .equals("FirstBank"))
                        {
                           // "Dear customer, your One-Time-Password (OTP) to register new device is: 95323. Please keep it secret"
                            message = message.toLowerCase();



                                message = message.substring(message.length() -28,message.length()-23);
                              //  RegDev_Confirm signotp = new RegDev_Confirm();
                               SecurityLayer.Log("OTP Pin:",message);


                            //Pass the message text to interface
                            mListener.messageReceived(message);

                        }
                    }
                    catch(Exception e){}


            }

        } catch (Exception e)
        {

        }


    }

    public interface SmsListener {
        public void messageReceived(String messageText);
    }

    public static void bindListener(SmsListener listener) {
        mListener = listener;
    }
    void setMainActivityHandler(ActivateAgent main){
        this.main=main;
    }
}
