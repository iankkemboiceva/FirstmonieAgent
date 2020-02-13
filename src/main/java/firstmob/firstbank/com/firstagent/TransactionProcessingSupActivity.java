package firstmob.firstbank.com.firstagent;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;
import com.crashlytics.android.answers.Answers;
import com.crashlytics.android.answers.CustomEvent;
import com.google.firebase.analytics.FirebaseAnalytics;
import com.vipul.hp_hp.library.Layout_to_Image;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.SocketTimeoutException;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class TransactionProcessingSupActivity extends BaseActivity implements View.OnClickListener {
    Button btnsub;
    String recanno, amou ,narra, ednamee,ednumbb,txtname,strfee,stragcms,bankname,bankcode,txpin,newparams,serv,storeid,pin;
    String txtcustid,serviceid,billid,txtfee,strtref,strlabel,strbillnm,fullname,telcoop,marketnm;

    ProgressDialog prgDialog2;
    RelativeLayout rlsendname,rlsendno;
    EditText etpin;
    private FirebaseAnalytics mFirebaseAnalytics;
    String   txtrfc,txref;
    Layout_to_Image layout_to_image;  //Create Object of Layout_to_Image Class
    TextView txstatus,txdesc;
    LinearLayout relativeLayout;   //Define Any Layout
    Button shareImage,repissue;
    Bitmap bitmap;                  //Bitmap for holding Image of layout

    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    Button btnconfirm;
    ProgressDialog pro ;
    String finpin;
    SessionManagement session;
    private PinLockListener mPinLockListener = new PinLockListener() {
        @Override
        public void onComplete(String pin) {

            //SecurityLayer.Log(TAG, "Pin complete: " + pin);
            finpin = pin;
        }

        @Override
        public void onEmpty() {
            SecurityLayer.Log("Pin Empty", "Pin empty");
        }

        @Override
        public void onPinChange(int pinLength, String intermediatePin) {
            //	SecurityLayer.Log(TAG, "Pin changed, new length " + pinLength + " with intermediate pin " + intermediatePin);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_processing);

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
        rlsendname = (RelativeLayout)findViewById(R.id.rlsendnam);
        rlsendno = (RelativeLayout) findViewById(R.id.rlsendnum);



        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        txstatus = (TextView) findViewById(R.id.txstatus);
        txdesc = (TextView) findViewById(R.id.txdesc);
        btnsub = (Button)findViewById(R.id.button2);
        btnsub.setOnClickListener(this);
        session = new SessionManagement(this);
        relativeLayout=(LinearLayout)findViewById(R.id.receipt);
        Intent intent = getIntent();
        if (intent != null) {
            serv = intent.getStringExtra("serv");
            if (serv.equals("LOANREQ")) {
                amou = intent.getStringExtra("amount");
                storeid = intent.getStringExtra("storeid");
                pin = intent.getStringExtra("pin");
                ConfirmRequest(pin);

            }
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void ConfirmRequest(String pin) {
        prgDialog2.show();


        ApiInterface apiService =
                RetrofitInstance.getClient(getApplicationContext()).create(ApiInterface.class);
        String adminid = session.getString("SUPERID");

        String encpin = Utility.b64_sha256(pin);

        try {
            JSONObject paramObject = new JSONObject();

            paramObject.put("userId", adminid);
            paramObject.put("channel", "1");
            paramObject.put("storeId", storeid);
            paramObject.put("amount", amou);
            paramObject.put("pin", encpin);



            Call<String> call = apiService.loanrequest(paramObject.toString());


            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object

                        SecurityLayer.Log("response..:", response.body());
                    //  String jsstr = "{\"message\":\"00\",\"responseCode\":\"Success\",\"data\":{\"reference\":\"00001068CEVAAGCRED\",\"insurance\":\"1000.0\",\"interestRate\":\"0.0\",\"repaymentAmount\":\"11000.0\"},\"fee\":\"0.0\",\"commission\":\"0.0\"}";
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getApplicationContext());

                        SecurityLayer.Log("decrypted_response", obj.toString());

                        String respcode = obj.optString("responseCode");

                        String responsemessage = obj.optString("message");


                        JSONObject plan = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00") || respcode.equals("Success")) {

String refcode = plan.optString("reference");
                                    String insurance = plan.optString("insurance");
                                    String interestRate = plan.optString("interestRate");
                                    String repaymentAmount = plan.optString("repaymentAmount");
                                    String repaymentdate = plan.optString("repaymentDate");

                                    Intent intent = new Intent(TransactionProcessingSupActivity.this, FinalConfirmLoanRequest.class);
                                    intent.putExtra("refcode", refcode);
                                    intent.putExtra("repamo", repaymentAmount);
                                    intent.putExtra("reqamo", amou);
                                    intent.putExtra("insurance", insurance);
                                    intent.putExtra("irate", interestRate);
                                    intent.putExtra("repaydate", repaymentdate);

                                    startActivity(intent);



                                } else {


                                    Toast.makeText(
                                            getApplicationContext(),
                                            responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {

                                Toast.makeText(
                                        getApplicationContext(),
                                        "There was an error processing your request ",
                                        Toast.LENGTH_LONG).show();
                            }
                        }

                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        if (!(getApplicationContext() == null)) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            // SecurityLayer.Log(e.toString());
                        //    SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if (!(getApplicationContext() == null)) {
                         //   SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    try {
                        if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                            prgDialog2.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                        //   prgDialog = null;
                    }

                    //   prgDialog.dismiss();
                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    SecurityLayer.Log("Throwable error", t.toString());


                    if ((prgDialog2 != null) && prgDialog2.isShowing() && !(getApplicationContext() == null)) {
                        prgDialog2.dismiss();
                    }
                    if (!(getApplicationContext() == null)) {
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error processing your request",
                                Toast.LENGTH_LONG).show();
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }



    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub

        if(prgDialog2!=null && prgDialog2.isShowing()){

            prgDialog2.dismiss();
        }
        super.onDestroy();
    }


    public  void LogOut(){
        session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button2) {
            finish();
            Intent intent = new Intent(getApplicationContext(), FMobActivity.class);

            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }


}
