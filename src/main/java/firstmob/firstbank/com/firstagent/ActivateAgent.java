package firstmob.firstbank.com.firstagent;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.InputFilter;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;

import com.google.android.gms.security.ProviderInstaller;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import adapter.RegIDPojo;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ActivateAgent extends AppCompatActivity implements View.OnClickListener {
Button btnnext;
TextView btnresp;
    EditText agentid,agentpin,phonenumber;


    //Context applicationContext;
    SessionManagement session;
    String regId ;
    String encrypted;
    final private int REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS = 124;
    public static final String AGMOB = "agmobno";
    ProgressDialog pDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate_agent);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        agentid  = (EditText) findViewById(R.id.agentid);
        agentpin  = (EditText) findViewById(R.id.agentpin);
        phonenumber  = (EditText) findViewById(R.id.agentphon);
        btnnext = (Button) findViewById(R.id.button2);
        btnnext.setOnClickListener(this);
        btnresp = (TextView) findViewById(R.id.button5);
        btnresp.setOnClickListener(this);
        agentid.setFilters(new InputFilter[] {new InputFilter.AllCaps()});
        session = new SessionManagement(getApplicationContext());
        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Loading ....");
        // Set Cancelable as False

        pDialog.setCancelable(false);



       // testResp();
    }
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void updateAndroidSecurityProvider(Activity callingActivity) {
        try {
            ProviderInstaller.installIfNeeded(this);
        } catch (GooglePlayServicesRepairableException e) {
            // Thrown when Google Play Services is not installed, up-to-date, or enabled
            // Show dialog to allow users to install, update, or otherwise enable Google Play services.
            GooglePlayServicesUtil.getErrorDialog(e.getConnectionStatusCode(), callingActivity, 0);
        } catch (GooglePlayServicesNotAvailableException e) {
            SecurityLayer.Log("SecurityException", "Google Play Services not available.");
        }
    }
private void checkPlayServices(){
    GoogleApiAvailability api = GoogleApiAvailability.getInstance();
    int code = api.isGooglePlayServicesAvailable(getApplicationContext());
    if (code == ConnectionResult.SUCCESS) {
        // Do Your Stuff Here
        registerInBackground();
    } else {
        Toast.makeText(
                getApplicationContext(),
                "Please ensure you have installed Google Play Services"
                     , Toast.LENGTH_LONG).show();
        registerInBackground();
    }
}

    public void receivedSMS(String message)
    {
        try
        {

            phonenumber.setText(message);
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }

    private void registerInBackground() {
        pDialog.show();
        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {
                String msg = "";

                return msg;
            }

            @Override
            protected void onPostExecute(String msg) {
            /*    if (!TextUtils.isEmpty(regId)) {*/

                    String ip = Utility.getIP(getApplicationContext());
                    String mac = Utility.getMacAddress(getApplicationContext());
                    String serial = Utility.getSerial();
                    String version = Utility.getDevVersion();
                    String devtype = Utility.getDevModel();
                    String imei = Utility.getDevImei(getApplicationContext());
                    if (Utility.checkInternetConnection(getApplicationContext())){
                        if (Utility.isNotNull(imei) && Utility.isNotNull(serial)) {
                            if(regId == null){
                                regId = "JKKS";
                            }



                         //   final   String agid = agentid.getText().toString();
                            String agid = Utility.gettUtilUserId(getApplicationContext());
                           String agpin = agentpin.getText().toString();
                            String phnnumb = phonenumber.getText().toString();
                            phnnumb = Utility.CheckNumberZero(phnnumb);

                            encrypted = Utility.b64_sha256(agpin);
                            SecurityLayer.Log("Encrypted Pin",encrypted);
                           String params = "1/"+agid+"/"+phnnumb+"/"+encrypted+"/secans1/"+ "secans2"+"/secans3/"+mac+"/"+ip+"/"+imei+"/"+serial+"/"+version+"/"+devtype+"/"+regId;

 //String params = "1/MUNENEM/8121210402/309B3E7906BFF7C8/secans1/secans2/secans3/02:00:00:00:00:00/192.168.0.55/358812060537681/FA55BYN01623/23/HTC_M9pw/JKKS";
                            RetroDevReg(params);

                        }
                    } else {

                        Toast.makeText(
                                getApplicationContext(),
                                "Please ensure this device has an IMEI number",
                                Toast.LENGTH_LONG).show();

pDialog.hide();

                    }

            }
        }.execute(null, null, null);
    }
    @Override
    public void onClick(View v) {
        if(v.getId() == R.id.button2){


            String agpin = agentpin.getText().toString();
            String phnnumb = phonenumber.getText().toString();

                if (Utility.isNotNull(agpin)) {
                    if (Utility.isNotNull(phnnumb)) {
                        if (Utility.checkInternetConnection(getApplicationContext())) {
                            if (Build.VERSION.SDK_INT >= 23) {
                                insertDummyContactWrapper();
                            } else {
                                // Pre-Marshmallow
                             //   registerInBackground();
                                checkPlayServices();
                            }



                        }

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please enter a valid value for OTP",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(
                            getApplicationContext(),
                            "Please enter a valid value for activation key",
                            Toast.LENGTH_LONG).show();
                }


        }


        if(v.getId() == R.id.button5){
            String agid = Utility.gettUtilUserId(getApplicationContext());
            String params = "1/"+agid;
            pDialog.show();
            GenerateOTPParams(params);



        }
    }

    private void GenerateOTPParams(String params) {



        String endpoint= "otp/generateotp.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.firstLogin(params,endpoint,getApplicationContext());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
                    obj = SecurityLayer.decryptFirstTimeLogin(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");



                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            Toast.makeText(
                                    getApplicationContext(),
                                    "OTP has been successfully resent",
                                    Toast.LENGTH_LONG).show();

                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                if ((pDialog != null) && pDialog.isShowing()) {
                    pDialog.dismiss();
                }
                //SetForceOutDialog(getString(R.string.forceout),getString(R.string.forceouterr));
            }
        });

    }
    private void insertDummyContactWrapper() {
        List<String> permissionsNeeded = new ArrayList<String>();

        final List<String> permissionsList = new ArrayList<String>();


        if (!addPermission(permissionsList, Manifest.permission.READ_PHONE_STATE))
            permissionsNeeded.add("Read Phone State");

        if (permissionsList.size() > 0) {
            if (permissionsNeeded.size() > 0) {
                // Need Rationale
                String message = "You need to grant access to " + permissionsNeeded.get(0);
                for (int i = 1; i < permissionsNeeded.size(); i++)
                    message = message + ", " + permissionsNeeded.get(i);
                showMessageOKCancel(message,
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                                        REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
                            }
                        });
                return;
            }

            requestPermissions(permissionsList.toArray(new String[permissionsList.size()]),
                    REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS);
            return;
        }

      //  registerInBackground();
        checkPlayServices();
    }

    private boolean addPermission(List<String> permissionsList, String permission) {
        if (checkSelfPermission(permission) != PackageManager.PERMISSION_GRANTED) {
            permissionsList.add(permission);


            // Check for Rationale Option
            if (!shouldShowRequestPermissionRationale(permission))
                return false;
        }
        return true;
    }


    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(ActivateAgent.this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", null)
                .create()
                .show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case REQUEST_CODE_ASK_MULTIPLE_PERMISSIONS: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    checkPlayServices();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    Toast.makeText(
                            getApplicationContext(),
                            "Please note we need to allow this permission to activate the app",
                            Toast.LENGTH_LONG).show();
                }
                return;
            }

        }
    }


    public void ClearFields(){

        agentid.setText("");
        agentpin.setText("");
        phonenumber.setText("");
    }

    private void RetroDevReg(String params) {

        SecurityLayer.Log("","Inside Retro Dev Reg");

        String endpoint= "reg/devReg.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.firstLogin(params,endpoint,getApplicationContext());
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    // JSON Object
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
                    obj = SecurityLayer.decryptFirstTimeLogin(obj, getApplicationContext());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");



                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            JSONObject datas = obj.optJSONObject("data");
                            String agent = datas.optString("agent");
                            if (!(datas == null)) {
                                final   String agid = agentid.getText().toString();
                                final   String mobno = phonenumber.getText().toString();
                                String status = datas.optString("status");
                                //    session.SetUserID(agid);
                                session.SetAgentID(agent);
                                session.setString(AGMOB,mobno);
                                /*JSONArray admusers = datas.optJSONArray("adminUsers");
                                JSONObject json_data = null;
                                for (int i = 0; i < admusers.length(); i++) {
                                    json_data = admusers.getJSONObject(i);
                                    String role = json_data.optString("role");
                                    String superid = json_data.optString("userid");
                                    if(role.equals("MS")){
                                        session.setString("SUPERID",superid);
                                    }
                                }*/
                                if(status.equals("F")) {
                                    finish();
                                    Intent mIntent = new Intent(getApplicationContext(), ForceChangePin.class);
                                    mIntent.putExtra("pinna", encrypted);
                                    startActivity(mIntent);
                                }else{
                                    session.setString(SessionManagement.SESS_REG,"Y");

                                    finish();
                                    Intent mIntent = new Intent(getApplicationContext(), SignInActivity.class);
                                    mIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                    startActivity(mIntent);
                                }
                            }
                        }
                        else {

                            Toast.makeText(
                                    getApplicationContext(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();


                        }

                    }
                    else {

                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
pDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getApplicationContext(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
pDialog.dismiss();


            }
        });

    }



}
