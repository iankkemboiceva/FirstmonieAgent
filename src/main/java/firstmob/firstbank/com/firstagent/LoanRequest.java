package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import model.GetStatesData;
import model.GetStores;
import model.ObservableWebView;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class LoanRequest extends BaseSupActivity implements View.OnClickListener {
    ImageView imageView1;
    EditText amon, edacc,pno,txtamount,txtnarr,pin;
    Button btnsub;
    SessionManagement session;
    List<GetStores> storelist = new ArrayList<GetStores>();
    ArrayAdapter<GetStores> maradapt;

    TextView accountoname;
    String depositid;
    String acname,storeidd;
    String amolimit = "NA";
    RelativeLayout rlid,lybut;
    LinearLayout lyamo;

    ProgressDialog prgDialog;
    TextView txelig,txstorename;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loanreq);
        session = new SessionManagement(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        rlid = (RelativeLayout)findViewById(R.id.rlid);
        accountoname = (TextView) findViewById(R.id.cname);
        txstorename = (TextView) findViewById(R.id.storid);
        txelig = (TextView) findViewById(R.id.txtelig);


        amon = (EditText) findViewById(R.id.amount);

        prgDialog = new ProgressDialog(this);
        prgDialog.setMessage("Loading ....");
        prgDialog.setCancelable(false);
        txtamount = (EditText) findViewById(R.id.amount);
        txtnarr = (EditText) findViewById(R.id.ednarr);

        lyamo = (LinearLayout) findViewById(R.id.lyamo);
        lybut = (RelativeLayout) findViewById(R.id.rl5);
        View.OnFocusChangeListener ofcListener = new MyFocusChangeListener();
        txtamount.setOnFocusChangeListener(ofcListener);





        SetStores();



        btnsub = (Button)findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        NameInquirySec();





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
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
    private class MyFocusChangeListener implements View.OnFocusChangeListener {

        public void onFocusChange(View v, boolean hasFocus){

            if(v.getId() == R.id.amount && !hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                String txt = txtamount.getText().toString();
                String fbal = Utility.returnNumberFormat(txt);
                Log.v("Amount",txt);
                txtamount.setText(fbal);

            }

            if(v.getId() == R.id.ednarr && !hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
            if(v.getId() == R.id.sendname && !hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
            if(v.getId() == R.id.sendnumber && !hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
            if(v.getId() == R.id.input_payacc && !hasFocus) {
                InputMethodManager imm =  (InputMethodManager) getApplicationContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(v.getWindowToken(), 0);


            }
        }
    }
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.button4) {
            rlid.setVisibility(View.VISIBLE);
            //   checkInternetConnection2();

        }
        if (view.getId() == R.id.button2) {
            final String amont = amon.getText().toString();
            final String storeid  = storeidd;
            if(Utility.isNotNull(amont)) {

                Double inpamo = Double.parseDouble(amont);
                if(inpamo >= 100) {
                    Double dbamolimit = Double.parseDouble(amolimit);
                    if (inpamo <= dbamolimit) {
                       boolean blchkend = false;
                        final ObservableWebView webView = new ObservableWebView(getApplicationContext());

                        webView.loadUrl("file:///android_asset/agentcredittc.html");


                        AlertDialog.Builder builder = new AlertDialog.Builder(LoanRequest.this);
                        builder.setTitle("Terms and Conditions")
                                .setView(webView)
                                .setNeutralButton("Reject", null)
                                .setPositiveButton("Accept", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        Intent intent = new Intent(LoanRequest.this, ConfirmLoanRequest.class);
                                        intent.putExtra("amount", amont);
                                        intent.putExtra("storeid", storeid);
                                        startActivity(intent);
                                    }
                                });

                        AlertDialog dialog = builder.show();

                        final Button button = dialog.getButton(AlertDialog.BUTTON_POSITIVE);

                        button.setVisibility(View.GONE);



                        webView.setOnScrollChangedCallback(new ObservableWebView.OnScrollChangedCallback() {

                            @Override
                            public void onScroll(int l, int t, int oldl, int oldt) {
                                if(t> oldt){
                                    //Do stuff
                                    System.out.println("Swipe UP");
                                    //Do stuff
                                }
                                else if(t< oldt){
                                    System.out.println("Swipe Down");
                                }

                                int height = (int) Math.floor(webView.getContentHeight() * webView.getScale());
                                int webViewHeight = webView.getHeight();
                                int cutoff = height - webViewHeight - 10; // Don't be too strict on the cutoff point
                                if (t >= cutoff) {
                                    button.setVisibility(View.VISIBLE);
                                }
                            }

                            @Override
                            public void onScroll(int l, int t) {

                            }
                        });

                    } else {
                        Toast.makeText(getApplicationContext(), "Kindly enter an amount below your loan limit", Toast.LENGTH_LONG).show();
                    }
                }else {
                    Toast.makeText(getApplicationContext(), "Please enter credit amount greater than 100"+ApplicationConstants.KEY_NAIRA, Toast.LENGTH_LONG).show();
                }
            }
        }


    }


    public void SetDialog(String msg,String title){
        new MaterialDialog.Builder(getApplicationContext())
                .title(title)
                .content(msg)

                .negativeText("Close")
                .show();
    }


    private void NameInquirySec() {
prgDialog.show();







         String adminid = session.getString("SUPERID");
        ApiInterface apiService =
                RetrofitInstance.getClient(getApplicationContext()).create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();


            paramObject.put("userId", adminid);
            paramObject.put("channel", "1");
            paramObject.put("storeId", storeidd);

            Call<String> call = apiService.loaneligibility(paramObject.toString());




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

                    String responsemessage = obj.optString("message");


                    JSONObject plan = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);
                    if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                        if ((Utility.checkUserLocked(respcode))) {
                           LogOut();
                        }
                        if (!(response.body() == null)) {
                            if (respcode.equals("00") || respcode.equals("Success")) {

                                SecurityLayer.Log("Response Message", responsemessage);

                                amolimit = plan.optString("creditLimit");
                                amolimit = Utility.lnroundto2dp(amolimit);


                                txelig.setText("Congratulations,you are eligible for a loan upto "+amolimit+ApplicationConstants.KEY_NAIRA);
lyamo.setVisibility(View.VISIBLE);
lybut.setVisibility(View.VISIBLE);
//                                    SecurityLayer.Log("Respnse getResults",datas.toString());


                            } else {

                            txelig.setText(responsemessage);
                            txelig.setTextColor(R.color.black);
                                Toast.makeText(
                                        getApplicationContext(),
                                        responsemessage,
                                        Toast.LENGTH_LONG).show();
                                lyamo.setVisibility(View.GONE);
                                lybut.setVisibility(View.GONE);
                            }
                        } else {
                            txelig.setText("There was an error processing your loan eligibility request");
                            Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error processing your request ",
                                    Toast.LENGTH_LONG).show();
                            lyamo.setVisibility(View.GONE);
                            lybut.setVisibility(View.GONE);
                        }
                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    if(!(getApplicationContext() == null)) {
                        Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                        // SecurityLayer.Log(e.toString());
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    if(!(getApplicationContext() == null)) {
                       SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                    // SecurityLayer.Log(e.toString());
                }
                try {
                    if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                        prgDialog.dismiss();
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
                SecurityLayer.Log("Throwable error",t.toString());


                if ((prgDialog != null) && prgDialog.isShowing() && !(getApplicationContext() == null)) {
                    prgDialog.dismiss();
                }
                if(!(getApplicationContext() == null)) {
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


    private void dismissProgressDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @Override
    protected void onDestroy() {
        dismissProgressDialog();
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


    public void SetStores() {
        String strjsarray = session.getString("STORES");
        SecurityLayer.Log("stores",strjsarray);

        JSONArray servdata = null;
        try {
            servdata = new JSONArray(strjsarray);
            if (servdata.length() > 0) {
                JSONObject json_data = null;

                for (int i = 0; i < servdata.length(); i++) {
                    json_data = servdata.getJSONObject(i);


                    String storeid = json_data.optString("id");
                    String storename = json_data.optString("name");



                    storelist.add(new GetStores(storeid, storename));
                }
                if (!(storelist == null)) {
                    if (storelist.size() > 0) {

storeidd = storelist.get(0).getstoreid();
String storename = storelist.get(0).getstorename();
txstorename.setText(storename);

                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "No stores available  ",
                                Toast.LENGTH_LONG).show();
                    }
                }

            }
        } catch (JSONException e) {
            e.printStackTrace();
        }

    }
}
