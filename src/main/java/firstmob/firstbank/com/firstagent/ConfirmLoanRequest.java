package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.firebase.analytics.FirebaseAnalytics;

import org.json.JSONException;
import org.json.JSONObject;

import model.GetFee;
import rest.ApiClient;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class ConfirmLoanRequest extends BaseSupActivity implements View.OnClickListener {
    TextView recacno, recname, recamo, recnarr, txtfee, acbal;
    Button btnsub;
    String recanno, amou, narra, ednamee, ednumbb, txtname, finalfee = null, agbalance, storeid;
    ProgressDialog prgDialog2;
    EditText etpin;
    private FirebaseAnalytics mFirebaseAnalytics;
    TextView step1;
    SessionManagement session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_loanreq);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
        session = new SessionManagement(this);
        recacno = (TextView) findViewById(R.id.textViewnb2);
        recname = (TextView) findViewById(R.id.textViewcvv);
        etpin = (EditText) findViewById(R.id.pin);
        acbal = (TextView) findViewById(R.id.txtacbal);
        recamo = (TextView) findViewById(R.id.txloamount);
        recnarr = (TextView) findViewById(R.id.textViewrr);
        txtfee = (TextView) findViewById(R.id.txtfee);
        step1 = (TextView) findViewById(R.id.tv);
        step1.setOnClickListener(this);


        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);

        btnsub = (Button) findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {
            amou = intent.getStringExtra("amount");
            storeid = intent.getStringExtra("storeid");
            recamo.setText(amou + ApplicationConstants.KEY_NAIRA);
        }


    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();    //Call the back button's method
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            String pin = etpin.getText().toString();
            if (Utility.isNotNull(pin)) {
                ConfirmRequest(pin);
            } else {
                Toast.makeText(getApplicationContext(), "Please enter a valid value for pin", Toast.LENGTH_LONG).show();
            }

        }
        if (view.getId() == R.id.tv) {
         /*   Fragment  fragment = new CashDepo();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Cash Depo");
            fragmentTransaction.addToBackStack("Cash Depo");
            ((FMobActivity)getApplicationContext())
                    .setActionBarTitle("Cash Depo");
            fragmentTransaction.commit();*/



        }
        if (view.getId() == R.id.tv2) {
           /* Fragment  fragment = new FTMenu();


            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Confirm Transfer");
            fragmentTransaction.addToBackStack("Confirm Transfer");
            ((FMobActivity)getApplicationContext())
                    .setActionBarTitle("Confirm Transfer");
            fragmentTransaction.commit();*/
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        // put your code here...


    }

    public void ClearPin() {
        etpin.setText("");
    }

    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(this)
                    .title(title)
                    .content(msg)

                    .negativeText("CONTINUE")
                    .callback(new MaterialDialog.ButtonCallback() {
                        @Override
                        public void onPositive(MaterialDialog dialog) {
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegative(MaterialDialog dialog) {

                            dialog.dismiss();
                            finish();
                            session.logoutUser();

                            // After logout redirect user to Loing Activity
                            Intent i = new Intent(c, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            // Staring Login Activity
                            startActivity(i);

                        }
                    })
                    .show();
        }
    }

    public void setDialog(String message) {
        new MaterialDialog.Builder(getApplicationContext())
                .title("Error")
                .content(message)

                .negativeText("Dismiss")
                .callback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        dialog.dismiss();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        dialog.dismiss();
                    }
                })
                .show();
    }


    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub


        if (prgDialog2 != null && prgDialog2.isShowing()) {

            prgDialog2.dismiss();
        }

        super.onDestroy();
    }


    private void ConfirmRequest(String pin) {
        prgDialog2.show();


        ApiInterface apiService =
                RetrofitInstance.getClient(getApplicationContext()).create(ApiInterface.class);
        String adminid = session.getString("ADMINID");

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
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getApplicationContext());

                        SecurityLayer.Log("decrypted_response", obj.toString());

                        String respcode = obj.optString("responseCode");

                        String responsemessage = obj.optString("responseMessage");


                        JSONObject plan = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {

                                    Intent intent = new Intent(ConfirmLoanRequest.this, LoanRequestConfirm.class);
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
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if (!(getApplicationContext() == null)) {
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
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
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void LogOut() {
        session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(ConfirmLoanRequest.this, SignInActivity.class);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }
}
