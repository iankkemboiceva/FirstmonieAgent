package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.andrognito.pinlockview.IndicatorDots;
import com.andrognito.pinlockview.PinLockListener;
import com.andrognito.pinlockview.PinLockView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import rest.RetrofitInstance;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;

import static security.SecurityLayer.KEY_SIV;
import static security.SecurityLayer.KEY_SKEY;


public class DialogSignInFragment extends DialogFragment implements View.OnClickListener {
    private EditText mEditText;
    Button btnconfirm;
    ProgressDialog pro ;
    TextView txserv;
    String serv,encpin;
    private PinLockView mPinLockView;
    private IndicatorDots mIndicatorDots;
    ImageView imv;
    SessionManagement session;
String finpin;
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
    public DialogSignInFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.signindialog, container, false);
        if (getDialog() != null && getDialog().getWindow() != null) {
            getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            getDialog().getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        }
        pro = new ProgressDialog(getActivity());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        session = new SessionManagement(getActivity());
        txserv = (TextView) view.findViewById(R.id.serv);

        imv = (ImageView) view.findViewById(R.id.imv);
        btnconfirm = (Button) view.findViewById(R.id.signinn);
        btnconfirm.setOnClickListener(this);
        imv.setOnClickListener(this);
       // getDialog().setTitle("Enter Attendant PIN");


        mPinLockView = (PinLockView) view.findViewById(R.id.pin_lock_view);
        mPinLockView.setPinLockListener(mPinLockListener);
        mIndicatorDots = (IndicatorDots) view.findViewById(R.id.indicator_dots);

        mPinLockView.attachIndicatorDots(mIndicatorDots);


        mPinLockView.setPinLength(5);
        mPinLockView.setTextColor(getResources().getColor(R.color.fab_material_blue_grey_900));

        return view;
    }


    private void LogRetro(String params, final String service) {


        pro.show();
        String endpoint= "login/login.action/";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.generalLogin(params,"23322",getActivity(),endpoint);
            //SecurityLayer.Log("cbcurl",url);
            SecurityLayer.Log("RefURL",urlparams);
            SecurityLayer.Log("refurl", urlparams);
            SecurityLayer.Log("params", params);
        } catch (Exception e) {
            SecurityLayer.Log("encryptionerror",e.toString());
        }





        ApiInterface apiService =
                ApiSecurityClient.getClient(getActivity()).create(ApiInterface.class);


        Call<String> call = apiService.setGenericRequestRaw(urlparams);

        call.enqueue(new Callback<String>() {
            @Override
            public void onResponse(Call<String> call, Response<String> response) {
                try {
                    if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
                        pro.dismiss();
                    }
                    // JSON Object
                    SecurityLayer.Log("response..:", response.body());


                    JSONObject obj = new JSONObject(response.body());
                 /*   JSONObject jsdatarsp = obj.optJSONObject("data");
                    SecurityLayer.Log("JSdata resp", jsdatarsp.toString());
                    //obj = Utility.onresp(obj,getActivity()); */
                    obj = SecurityLayer.decryptGeneralLogin(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");




                    JSONObject datas = obj.optJSONObject("data");

                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                        if ((Utility.checkUserLocked(respcode))) {
                          //  ((SignInActivity) getActivity()).LogOut();
                        }
                        SecurityLayer.Log("Response Message", responsemessage);

                        if (respcode.equals("00")) {
                            if (!(datas == null)) {
                                dismiss();
                                String status = datas.optString("status");
                                String cntopen = datas.optString("canOpenAccount");
                                String store = datas.optString("store");
                                session.setString(SessionManagement.KEY_SETCNTOPEN,cntopen);
                                if(status.equals("F")) {
                                    getActivity().finish();
                                    Intent mIntent = new Intent(getActivity(), ForceResetPin.class);
                                    mIntent.putExtra("pinna", encpin);
                                    mIntent.putExtra("type", "ADM");
                                    startActivity(mIntent);
                                }else {

                                   session.setString("store",store);
                                   getActivity().finish();
                                    startActivity(new Intent(getActivity(), SupHomeActivity.class));
                                    getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                }
                            }
                        }   else if(respcode.equals("002")){
                            dismiss();
                            Toast.makeText(
                                    getActivity(), responsemessage,
                                    Toast.LENGTH_LONG).show();

                            ((SignInActivity) getActivity()).showEditDialog();
                        }
                        else {

                           /* Toast.makeText(
                                    getActivity(),
                                    responsemessage,
                                    Toast.LENGTH_LONG).show();*/
                           dismiss();
setDialog(responsemessage);

                        }

                    }
                    else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();

                        setDialog("There was an error on your request");


                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    if (!(getActivity() == null)){
                        Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());
                }

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    if (!(getActivity() == null)) {
                    }
                    // SecurityLayer.Log(e.toString());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());

                if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
                    pro.dismiss();
                    Toast.makeText(
                            getActivity(),
                            "There was an error processing your request",
                            Toast.LENGTH_LONG).show();

                }


            }
        });

    }

    @Override
    public void onClick(View view) {
        if(view.getId() == R.id.signinn){

            if(Utility.isNotNull(finpin)) {
              encpin =   Utility.b64_sha256(finpin);
                SecurityLayer.Log("Enc Pin",encpin);
                session.setString("ENCPIN",encpin);

                String supervis = session.getString("SUPERID");
                String mobnoo = Utility.gettUtilMobno(getActivity());
                SecurityLayer.Log("Base64 Pin",encpin);
              String adminid = session.getString("ADMINID");
                String params = "1" + "/"+adminid+"/" + encpin + "/" + mobnoo;

                AdminLogin(finpin);
            }else{
                Toast.makeText(
                        getActivity(),
                        "Please enter a valid value for Attendant PIN",
                        Toast.LENGTH_LONG).show();
            }
        }


        if(view.getId() == R.id.imv){
dismiss();
        }
    }


    public void setDialog(String message){
        new MaterialDialog.Builder(getActivity())
                .title("Error")
                .content(message)

                .negativeText("Dismiss")
                .callback(new MaterialDialog.ButtonCallback()  {
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




    private void AdminLogin(String pin) {
        pro.show();




        String encrypted = Utility.b64_sha256(pin);

        String skey = session.getString(KEY_SKEY);
        String siv = session.getString(KEY_SIV);
        String adminid = session.getString("ADMINID");
        ApiInterface apiService =
                RetrofitInstance.getClient(getActivity()).create(ApiInterface.class);

        try {
            JSONObject paramObject = new JSONObject();

            paramObject.put("loginUserId", adminid);
            paramObject.put("channel", "1");
            paramObject.put("pin", encrypted);
            paramObject.put("key", "650dc9fc52d141e2");
            paramObject.put("iv", "505ad5e148f7f115");



            Call<String> call = apiService.adminlogin(paramObject.toString());




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
                        JSONObject datas = obj.optJSONObject("data");
                        //session.setString(SecurityLayer.KEY_APP_ID,appid);
                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {

                            if (!(response.body() == null)) {
                                if (respcode.equals("00")) {
                                    if (!(datas == null)) {
                                        dismiss();
                                        String status = datas.optString("status");
                                        String cntopen = datas.optString("canOpenAccount");
                                        String store = datas.optString("store");
                                        String token = datas.optString("token");
                                        JSONArray stores = datas.optJSONArray("stores");
                                        session.setString("STORES",stores.toString());
                                        session.setString("TOKEN",token);
                                        session.setString(SessionManagement.KEY_SETCNTOPEN,cntopen);
                                        if(status.equals("F")) {
                                            getActivity().finish();
                                            Intent mIntent = new Intent(getActivity(), ForceResetPin.class);
                                            mIntent.putExtra("pinna", encpin);
                                            mIntent.putExtra("type", "ADM");
                                            startActivity(mIntent);
                                        }else {

                                            session.setString("store",store);
                                            getActivity().finish();
                                            startActivity(new Intent(getActivity(), SupHomeActivity.class));
                                            getActivity().overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);

                                        }
                                    }
                                } else {
                                    dismiss();
                                    Toast.makeText(
                                            getActivity(), responsemessage,
                                            Toast.LENGTH_LONG).show();

                                    ((SignInActivity) getActivity()).showEditDialog();
                                }
                            } else {

                            }
                        }

                    } catch (JSONException e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        // TODO Auto-generated catch block
                        if(!(getActivity() == null)) {
                            Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                            // SecurityLayer.Log(e.toString());
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                        }
                    } catch (Exception e) {
                        SecurityLayer.Log("encryptionJSONException", e.toString());
                        if(!(getActivity() == null)) {
                            SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                        }
                        // SecurityLayer.Log(e.toString());
                    }
                    try {
                        if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
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


                    if ((pro != null) && pro.isShowing() && !(getActivity() == null)) {
                        pro.dismiss();
                    }
                    if(!(getActivity() == null)) {
                        Toast.makeText(
                                getActivity(),
                                "There was an error processing your request",
                                Toast.LENGTH_LONG).show();
                        SetForceOutDialog(getString(R.string.forceout), getString(R.string.forceouterr), getActivity());
                    }
                }
            });

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }


    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(getActivity())
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
                            getActivity().finish();
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



}
