package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;
import java.security.KeyStore;

import model.DeviceActivation;
import okhttp3.OkHttpClient;
import rest.ApiClient;
import rest.ApiInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.EncryptTransactionPin;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivateSecrQn extends AppCompatActivity implements View.OnClickListener {
    Button btnnext;
    String agid,agpin,agphn;
    GoogleCloudMessaging gcmObj;
    //Context applicationContext;
    String regId = "";
    SessionManagement session;
    ProgressDialog prgDialog,prgDialog2;
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_secr_qn);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        agid = getIntent().getStringExtra("agid");
        agphn = getIntent().getStringExtra("agphnno");
        agpin = getIntent().getStringExtra("agpin");
        session = new SessionManagement(getApplicationContext());

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
       registerInBackground();
        btnnext = (Button) findViewById(R.id.button1);
        btnnext.setOnClickListener(this);

         pDialog = new ProgressDialog(this);
        pDialog.setTitle("Loading");
        pDialog.setCancelable(false);

    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void registerInBackground() {
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";
                try {
                    if (gcmObj == null) {

                        gcmObj = GoogleCloudMessaging
                                .getInstance(getApplicationContext());
                    }
                    regId = gcmObj
                            .register(ApplicationConstants.GOOGLE_PROJ_ID);
                    msg = "Registration ID :" + regId;
                    SecurityLayer.Log("Reg Gotten ID",msg);

                } catch (IOException ex) {
                    msg = "Error :" + ex.getMessage();
                }
                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
                if (!TextUtils.isEmpty(regId)) {

                   /* Toast.makeText(
                            getApplicationContext(),
                            "Registered with GCM Server successfully.\n\n"
                                    + msg, Toast.LENGTH_SHORT).show();*/
                } else {
                    Toast.makeText(
                           getApplicationContext(),
                            "Reg ID Creation Failed.\n\nEither you haven't enabled Internet or GCM server is busy right now. Make sure you enabled Internet and try registering again after some time."
                                    + msg, Toast.LENGTH_LONG).show();
                }
            }
        }.execute(null, null, null);
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.button1) {


        }
    }

}
