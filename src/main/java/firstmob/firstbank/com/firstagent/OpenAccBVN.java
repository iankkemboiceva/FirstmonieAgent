package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OpenAccBVN extends BaseActivity implements View.OnClickListener {
    ProgressDialog pro ;
    Button btnnext,btnopenacc;
    EditText agentid;
    SessionManagement session;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_bvn);
        session = new SessionManagement(this);  Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)

        pro = new ProgressDialog(this);
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);


        agentid  = (EditText) findViewById(R.id.agentid);

        btnnext = (Button) findViewById(R.id.button2);
        btnnext.setOnClickListener(this);

        btnopenacc = (Button) findViewById(R.id.button5);
        btnopenacc.setOnClickListener(this);

        SecurityLayer.Log("plain appid",Utility.getPlainAppid(getApplicationContext()));
        Log.v("plain appid",Utility.getPlainAppid(getApplicationContext()));
      //  Toast.makeText(getApplicationContext(),Utility.getPlainAppid(getApplicationContext()),Toast.LENGTH_LONG).show();

    }


    private void GetBVN(final String bvn) {
        pro.show();
        String endpoint= "bvn/validatebvn.action";
        if(!(getApplicationContext() == null)) {
            String usid = Utility.gettUtilUserId(getApplicationContext());
            String appid = Utility.getFinAppid(getApplicationContext());
            String appvers = Utility.getAppVersion(getApplicationContext());
            String params = "1/" + usid + "/"+bvn;

            SecurityLayer.Log("params", params);
            String urlparams = "";
            try {
                urlparams = SecurityLayer.genURLCBC(params, endpoint, getApplicationContext());
                //SecurityLayer.Log("cbcurl",url);
                SecurityLayer.Log("RefURL", urlparams);
                SecurityLayer.Log("refurl", urlparams);
                SecurityLayer.Log("params", params);
            } catch (Exception e) {
                SecurityLayer.Log("encryptionerror", e.toString());
            }


            ApiInterface apiService =
                    ApiSecurityClient.getClient(getApplicationContext()).create(ApiInterface.class);


            Call<String> call = apiService.setGenericRequestRaw(urlparams);

            call.enqueue(new Callback<String>() {
                @Override
                public void onResponse(Call<String> call, Response<String> response) {
                    try {
                        // JSON Object


                        SecurityLayer.Log("Cable TV Resp", response.body());
                        SecurityLayer.Log("response..:", response.body());
                        JSONObject obj = new JSONObject(response.body());
                        //obj = Utility.onresp(obj,getApplicationContext());
                        obj = SecurityLayer.decryptTransaction(obj, getApplicationContext());
                        SecurityLayer.Log("decrypted_response", obj.toString());


                        JSONObject servdata = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);

                        if (!(response.body() == null)) {
                            String respcode = obj.optString("responseCode");
                            String responsemessage = obj.optString("message");

                            SecurityLayer.Log("Response Message", responsemessage);

                            if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                                if (!(Utility.checkUserLocked(respcode))) {
                                    SecurityLayer.Log("Response Message", responsemessage);
                                    if(respcode.equals("00")){

                                        String fname = servdata.optString("firstName");
                                        String lname = servdata.optString("lastName");

                                        String midname = servdata.optString("midName");
                                        String maritalStatus = servdata.optString("maritalStatus");

                                        String yob = servdata.optString("dob");
                                        String gender = servdata.optString("gender");

                                        String email = servdata.optString("email");
                                        String state = servdata.optString("state");

                                        String address = servdata.optString("address");
                                        String mobileNumber = servdata.optString("mobileNumber");

                                        String salutation = servdata.optString("salutation");
                                        Intent intent  = new Intent(OpenAccBVN.this,OpenAccBVNConfirm.class);



                                        intent.putExtra("fname", fname);
                                        intent.putExtra("lname", lname);
                                        intent.putExtra("midname", midname);
                                        intent.putExtra("yob", yob);
                                        intent.putExtra("gender", gender);
                                        intent.putExtra("city", "NA");
                                        intent.putExtra("state", state);
                                        intent.putExtra("straddr", address);
                                        intent.putExtra("email", email);
                                        intent.putExtra("hmadd", bvn);
                                        intent.putExtra("mobn", mobileNumber);
                                        intent.putExtra("salut", salutation);
                                        intent.putExtra("marstatus", maritalStatus);




                                        startActivity(intent);
                                    }else{
                                        Toast.makeText(
                                                getApplicationContext(),
                                                "" + responsemessage,
                                                Toast.LENGTH_LONG).show();
                                    }


                                } else {
                                  /*  getApplicationContext().finish();
                                    startActivity(new Intent(getApplicationContext(), SignInActivity.class));
                                    Toast.makeText(
                                            getApplicationContext(),
                                            "You have been locked out of the app.Please call customer care for further details",
                                            Toast.LENGTH_LONG).show();*/
                                    if(!(getApplicationContext() == null)) {
                                        ((FMobActivity) getApplicationContext()).LogOut();
                                    }
                                }
                            } else {

                               /* Toast.makeText(
                                        getApplicationContext(),
                                        "There was an error on your request",
                                        Toast.LENGTH_LONG).show();*/


                            }
                        } else {

                          /*  Toast.makeText(
                                    getApplicationContext(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();
*/

                        }
                         pro.dismiss();


                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        if(!(getApplicationContext() == null)) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());

                        }
                        // SecurityLayer.Log(e.toString());

                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if(!(getApplicationContext() == null)) {
                            Toast.makeText(getApplicationContext(), getApplicationContext().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            // SecurityLayer.Log(e.toString());
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    if(!(getApplicationContext() == null)){
                        pro.dismiss();
                    }

                }

                @Override
                public void onFailure(Call<String> call, Throwable t) {
                    // Log error here since request failed
                    // Log error here since request failed
                    SecurityLayer.Log("throwable error", t.toString());

                    
                    


                }
            });
        }
    }


    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.button2) {
            String agid = agentid.getText().toString();

            if (Utility.isNotNull(agid)) {

                if (Utility.checkInternetConnection(getApplicationContext())) {
                    GetBVNMicro(agid);

                }


            } else {
                Toast.makeText(
                        getApplicationContext(),
                        "Please enter a valid value for BVN",
                        Toast.LENGTH_LONG).show();
            }
        }

        if(view.getId() == R.id.button5) {

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


    private void GetBVNMicro(final String bvn) {
        pro.show();

        String usid = Utility.gettUtilUserId(getApplicationContext());

        ApiInterface apiService =
                RetrofitInstance.getClient(getApplicationContext()).create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();

            paramObject.put("channel", "1");
            paramObject.put("userId", usid);
            paramObject.put("bvnNumber", bvn);


            String data = SecurityLayer.encryptdata(paramObject.toString(),getApplicationContext());
            String hash = SecurityLayer.gethasheddata(paramObject);
            String appid = Utility.getNewAppID(getApplicationContext());

            JSONObject finalparam = new JSONObject();
            finalparam.put("data", data);
            finalparam.put("hash", hash);
            finalparam.put("appId", appid);






            Call<String> call = apiService.validatebvn(finalparam.toString());




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


                        JSONObject servdata = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if ((Utility.checkUserLocked(respcode))) {
                                //LogOut();
                            }
                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    String fname = servdata.optString("firstName");
                                    String lname = servdata.optString("lastName");

                                    String midname = servdata.optString("midName");
                                    String maritalStatus = servdata.optString("maritalStatus");

                                    String yob = servdata.optString("dob");
                                    String gender = servdata.optString("gender");

                                    String email = servdata.optString("email");
                                    String state = servdata.optString("state");

                                    String address = servdata.optString("address");
                                    String mobileNumber = servdata.optString("mobileNumber");

                                    String salutation = servdata.optString("salutation");
                                    Intent intent  = new Intent(OpenAccBVN.this,OpenAccBVNConfirm.class);



                                    intent.putExtra("fname", fname);
                                    intent.putExtra("lname", lname);
                                    intent.putExtra("midname", midname);
                                    intent.putExtra("yob", yob);
                                    intent.putExtra("gender", gender);
                                    intent.putExtra("city", "NA");
                                    intent.putExtra("state", state);
                                    intent.putExtra("straddr", address);
                                    intent.putExtra("email", email);
                                    intent.putExtra("hmadd", bvn);
                                    intent.putExtra("mobn", mobileNumber);
                                    intent.putExtra("salut", salutation);
                                    intent.putExtra("marstatus", maritalStatus);




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
                                        "There was an error processing your request",
                                        Toast.LENGTH_LONG).show();
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
                        if ((pro != null) && pro.isShowing() && !(getApplicationContext() == null)) {
                            pro.dismiss();
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


                    if ((pro != null) && pro.isShowing() && !(getApplicationContext() == null)) {
                        pro.dismiss();
                    }
                    if(!(getApplicationContext() == null)) {
                        Toast.makeText(
                                getApplicationContext(),
                                "There was an error processing your request",
                                Toast.LENGTH_LONG).show();
                        // SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getApplicationContext());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


}
