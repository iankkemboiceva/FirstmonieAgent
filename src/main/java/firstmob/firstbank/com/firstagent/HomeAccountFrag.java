package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import adapter.ProdAdapter;
import adapter.ProdList;
import model.BalInquiryData;
import model.BalanceInquiry;
import model.GetAgentIdData;
import rest.ApiClient;
import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;


public class HomeAccountFrag extends Fragment implements View.OnClickListener {
    ImageView imageView1;
    ListView lv;
    TextView tv,tvacco,tvcomm;
    List<ProdList> planetsList = new ArrayList<ProdList>();
    ProdAdapter aAdpt;
    Button btn1,btn2,btn3;
    RelativeLayout rlbuttons,rlagbal,rlcomm;
    TextView curbal,lastl,greet,commamo;
    SessionManagement session;
    ProgressDialog prgDialog;
    ProgressBar prgbar;
    ImageView iv;
    ProgressDialog prgDialog2;
//
// ProgressBar pDialog;
    List<GetAgentIdData> plan = new ArrayList<GetAgentIdData>();
    String agid;
    public HomeAccountFrag() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        ViewGroup root = (ViewGroup) inflater.inflate(R.layout.my_account2, null);
        btn1 = (Button) root.findViewById(R.id.btn1);
        curbal = (TextView) root.findViewById(R.id.currentbal);
        tvacco = (TextView) root.findViewById(R.id.hfjdj);
        lastl = (TextView) root.findViewById(R.id.txt2);
        greet = (TextView) root.findViewById(R.id.greet);
        tvcomm = (TextView) root.findViewById(R.id.comm);
        commamo = (TextView) root.findViewById(R.id.accountbal);

        prgDialog2 = new ProgressDialog(getActivity());
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);
        iv = (ImageView) root.findViewById(R.id.img);
        prgDialog = new ProgressDialog(getActivity());
      /*  prgbar = (ProgressBar) root.findViewById(R.id.prgbar);
        prgbar.getIndeterminateDrawable().setColorFilter(getActivity().getResources().getColor(R.color.nbkyellow), android.graphics.PorterDuff.Mode.MULTIPLY);
      */  prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);
//pDialog = new ProgressBar(getActivity());
        btn2 = (Button) root.findViewById(R.id.btn2);
        btn3 = (Button) root.findViewById(R.id.btn3);
        btn1.setOnClickListener(this);
        btn2.setOnClickListener(this);
        btn3.setOnClickListener(this);
        rlbuttons = (RelativeLayout) root.findViewById(R.id.balances);
        rlagbal = (RelativeLayout) root.findViewById(R.id.rlagentaccount);
        rlcomm = (RelativeLayout) root.findViewById(R.id.rlcommisionaccount);
        rlagbal.setOnClickListener(this);
        rlcomm.setOnClickListener(this);
        session = new SessionManagement(getActivity());
     //   pDialog.setBackgroundColor(getActivity().getResources().getColor(R.color.nbkyellow));
        boolean  checktpref = session.checkShwBal();
        SecurityLayer.Log("Boolean checkpref", String.valueOf(checktpref));
        if(checktpref == false){
setBalInquSec();
        }else{
rlbuttons.setVisibility(View.GONE);
        }

        String strlastl = Utility.getLastl(getActivity());
        strlastl = Utility.convertDate(strlastl);
        lastl.setText("Last Login: "+strlastl);
        String accnoo = Utility.getAcountno(getActivity());
        tvacco.setText(""+accnoo);
        Calendar cal = Calendar.getInstance();
        String time = "";
        int v =  cal.getTime().getHours();
        if(v < 12){
            time = "Morning";

        }
        if(v >= 12 && v < 18){
            time = "Afternoon";


        }
        if(v >= 18 && v <24){
            time = "Evening";

        }
        String custname = Utility.gettUtilCustname(getActivity());
        greet.setText("Good "+time+" "+Utility.returnCustname(custname));
        String agentid = Utility.gettUtilAgentId(getActivity());
        if(Utility.isNotNull(agentid)){
            tvcomm.setText(agentid);
        }

        return root;
    }


    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn1) {
            //   SetDialog("Select Bank");

            Fragment  fragment = new CashDepo();
            String title = "Deposit";
            ((FMobActivity)getActivity()).addFragment(fragment,title);

          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Deposit");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Deposit");
            fragmentTransaction.commit();*/

        }
        if (view.getId() == R.id.btn2) {
            //   SetDialog("Select Bank");

            Fragment  fragment = new FTMenu();
String title = "Transfer";
            ((FMobActivity)getActivity()).addFragment(fragment,title);

         /*   FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Transfer");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Transfer");
            fragmentTransaction.commit();*/

        }
        if (view.getId() == R.id.btn3) {
            //   SetDialog("Select Bank");

            Fragment  fragment = new Withdraw();
String title = "Withdraw";
            ((FMobActivity)getActivity()).addFragment(fragment,title);
          /*  FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Withdraw");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Withdraw");
            fragmentTransaction.commit();*/

        }
        if (view.getId() == R.id.rlagentaccount) {
            //   SetDialog("Select Bank");

            android.app.Fragment  fragment = new Minstat();
            String title = "Mini Statement";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);

           /* FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Mini Statement");

            ((FMobActivity)getActivity())
                    .setActionBarTitle("Mini Statement");
            fragmentTransaction.commit();*/

        }
        if (view.getId() == R.id.rlcommisionaccount) {
            //   SetDialog("Select Bank");

            android.app.Fragment  fragment = new CommReport();


            String title = "Commissions Report";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);


        }
        if (view.getId() == R.id.imageView2) {
            //   SetDialog("Select Bank");

            android.app.Fragment  fragment = new CommReport();


            String title = "Commissions Report";
            ((FMobActivity)getActivity()).addAppFragment(fragment,title);


        }
    }
    public void StartChartAct(int i){


    }
    @Override
    public void onResume(){
        super.onResume();
        // put your code here...

    }
public void setBalInqu(){
    if (Utility.checkInternetConnection(getActivity())) {
        prgDialog.show();
        ApiInterface apiService =
                ApiClient.getClient().create(ApiInterface.class);
        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Utility.gettUtilMobno(getActivity());
        Call<BalanceInquiry> call = apiService.getBalInq("1", usid, agentid, "0000");
        call.enqueue(new Callback<BalanceInquiry>() {
            @Override
            public void onResponse(Call<BalanceInquiry> call, Response<BalanceInquiry> response) {
                if(!(response.body() == null)) {
                    String responsemessage = response.body().getMessage();
                    BalInquiryData baldata = response.body().getData();
                    SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                    if (!(baldata == null)) {
                        String balamo = baldata.getbalance();
                        String comamo =baldata.getcommision();


                        String fbal = Utility.returnNumberFormat(balamo);
                        curbal.setText(Html.fromHtml("&#8358") + " " + fbal);

                        String cmbal = Utility.returnNumberFormat(comamo);
                        commamo.setText(Html.fromHtml("&#8358") + " " + cmbal);
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "There was an error retrieving your balance ",
                                Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(
                            getActivity(),
                            "There was an error retrieving your balance ",
                            Toast.LENGTH_LONG).show();
                }  try {
                    if ((prgDialog != null) && prgDialog.isShowing()) {
                        prgDialog.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                    prgDialog = null;
                }

              //  prgDialog.dismiss();
            }

            @Override
            public void onFailure(Call<BalanceInquiry> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getActivity(),
                        "There was an error retrieving your balance ",
                        Toast.LENGTH_LONG).show();
                prgDialog.dismiss();
            }
        });
    }
}
    private void dismissProgressDialog() {
        if (prgDialog != null && prgDialog.isShowing()) {
            prgDialog.dismiss();
        }
    }

    @Override
    public void onDestroy() {
        dismissProgressDialog();
        super.onDestroy();
    }
    private void fetchAds() {


        String endpoint= "adverts/ads.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Utility.gettUtilMobno(getActivity());
        String params = "1/"+usid+"/"+agentid+"/"+mobnoo;
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
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
                    // JSON Object

                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");


                    JSONArray plan = obj.optJSONArray("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);





                    SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(responsemessage)) {
                            SecurityLayer.Log("Response Message", responsemessage);

                            if (respcode.equals("00")) {
                                JSONObject json_data = null;
                                Picasso.with(getActivity()).load(R.drawable.fbankdebitcard).into(iv);
                                prgbar.setVisibility(View.GONE);

                            } else {

                                Toast.makeText(
                                        getActivity(),
                                        "There was an error on your request",
                                        Toast.LENGTH_LONG).show();


                            }
                        }

                    try {
                        if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                            prgDialog2.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    }

                    if(!(getActivity() == null)) {
                    if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                        GetServv();
                    }

                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }

            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getActivity(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                //   pDialog.dismiss();
                prgbar.setVisibility(View.GONE);
                try {
                    if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {

                }
                if(!(getActivity() == null)) {
                    if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                        GetServv();
                    }

                }
            }
        });
    }




    private void setBalInquSec() {

prgDialog2.show();
        String endpoint= "core/balenquirey.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Utility.gettUtilMobno(getActivity());
        String params = "1/"+usid+"/"+agentid+"/"+mobnoo;
        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
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
                    // JSON Object

                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivffeity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());

                    String respcode = obj.optString("responseCode");
                    String responsemessage = obj.optString("message");


                    JSONObject plan = obj.optJSONObject("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);


                    if(!(response.body() == null)) {
                       if (respcode.equals("00")) {

                           SecurityLayer.Log("Response Message", responsemessage);

//                                    SecurityLayer.Log("Respnse getResults",datas.toString());
                           if (!(plan == null)) {
                               String balamo = plan.optString("balance");
                               String comamo = plan.optString("commision");


                               String fbal = Utility.returnNumberFormat(balamo);
                               curbal.setText(Html.fromHtml("&#8358") + " " + fbal);

                               String cmbal = Utility.returnNumberFormat(comamo);
                               commamo.setText(Html.fromHtml("&#8358") + " " + cmbal);
                           } else {
                               Toast.makeText(
                                       getActivity(),
                                       "There was an error retrieving your balance ",
                                       Toast.LENGTH_LONG).show();
                           }
                       }else{
                           Toast.makeText(
                                   getActivity(),
                                   "There was an error retrieving your balance ",
                                   Toast.LENGTH_LONG).show();
                       }
                    } else {
                        Toast.makeText(
                                getActivity(),
                                "There was an error retrieving your balance ",
                                Toast.LENGTH_LONG).show();
                    }


                    try {
                        if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                            prgDialog2.dismiss();
                        }
                    } catch (final IllegalArgumentException e) {
                        // Handle or log or ignore
                    } catch (final Exception e) {
                        // Handle or log or ignore
                    } finally {
                      //  prgDialog2 = null;
                    }
                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if(!(getActivity() == null)) {
                  /*  if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                        GetServv();
                    }*/
                   if (Utility.checkInternetConnection(getActivity())) {
                     //   fetchAds();
                       if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                           GetServv();
                       }
                    }
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                SecurityLayer.Log("Throwable error",t.toString());
                Toast.makeText(
                        getActivity(),
                        "There was an error processing your request",
                        Toast.LENGTH_LONG).show();
                //   pDialog.dismiss();
//               // prgbar.setVisibility(View.GONE);
                try {
                    if ((prgDialog2 != null) && prgDialog2.isShowing()) {
                        prgDialog2.dismiss();
                    }
                } catch (final IllegalArgumentException e) {
                    // Handle or log or ignore
                } catch (final Exception e) {
                    // Handle or log or ignore
                } finally {
                  //  prgDialog2 = null;
                }
                if(!(getActivity() == null)) {
                    if (Utility.checkInternetConnection(getActivity())) {
                        if(session.getString(SessionManagement.KEY_SETBANKS).equals("N")) {
                            GetServv();
                        }
                    }
                }
            }
        });

    }


    private void GetServv() {
        prgDialog2.show();
        String endpoint= "transfer/getbanks.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Utility.gettUtilMobno(getActivity());

        String params = usid+"/"+agentid+"/"+mobnoo;

        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
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
                    // JSON Object


                    SecurityLayer.Log("Get Banks Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());





                    JSONArray servdata = obj.optJSONArray("data");
                    String strservdata = servdata.toString();
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");

                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);

                                if (respcode.equals("00")){
                                    SecurityLayer.Log("JSON Aray", servdata.toString());
                                    session.setString(SessionManagement.KEY_SETBANKS,"Y");
                                    session.setString(SessionManagement.KEY_BANKS,strservdata);

                                }else{
                                    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                Toast.makeText(
                                        getActivity(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(
                                    getActivity(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();


                        }
                    } else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                if(!(prgDialog2 == null) && prgDialog2.isShowing()) {
                    prgDialog2.dismiss();
                }
                if(session.getString(SessionManagement.KEY_SETBILLERS).equals("N")) {
                    GetBillPay();
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getActivity(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();


                if(!(prgDialog2 == null)) {
                    prgDialog2.dismiss();
                }
                if(session.getString(SessionManagement.KEY_SETBILLERS).equals("N")) {
                    GetBillPay();
                }
            }
        });

    }


    private void GetBillPay() {
        prgDialog2.show();
        String endpoint= "billpayment/services.action";

        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());
        String mobnoo = Utility.gettUtilMobno(getActivity());
        String params = "1/"+usid+"/"+agentid+"/"+mobnoo;




        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
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
                    // JSON Object


                    SecurityLayer.Log("Cable TV Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());





                    JSONArray servdata = obj.optJSONArray("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");

                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);

                                if (respcode.equals("00")){
                                    SecurityLayer.Log("JSON Aray", servdata.toString());
                                    String billerdata = servdata.toString();
                                    session.setString(SessionManagement.KEY_SETBILLERS,"Y");
                                    session.setString(SessionManagement.KEY_BILLERS,billerdata);
                                }else{
                                    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                Toast.makeText(
                                        getActivity(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(
                                    getActivity(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();


                        }
                    } else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();


                    if(!(getActivity() == null)) {
                        if(session.getString(SessionManagement.KEY_SETWALLETS).equals("N")) {
                            GetWallets();
                        }

                    }

                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                prgDialog2.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getActivity(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();

                if(!(getActivity() == null)) {
                    if(session.getString(SessionManagement.KEY_SETWALLETS).equals("N")) {
                        GetWallets();
                    }

                }

                prgDialog2.dismiss();
            }
        });

    }
    class DownloadImg extends AsyncTask<String, String, String> {
        Bitmap bmp=null;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // prgDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {


            try{

                String usid = Utility.gettUtilUserId(getActivity());
                String agentid = Utility.gettUtilAgentId(getActivity());
                String mobnoo = Utility.gettUtilMobno(getActivity());

                String url = ApplicationConstants.UNENC_URL+"adverts/pic.action/1/"+usid+"/"+agentid+"/"+mobnoo+"/"+agid;
                bmp = downloadBitmap(url);

                SecurityLayer.Log("Download Pic Url",url);
            }catch(Exception e){
                SecurityLayer.Log("ERROR While Downloadin", e.getLocalizedMessage());
//                Toast.makeText(getActivity(), "Error While Downloading File", Toast.LENGTH_LONG);
            }
            return "34";
        }


        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                Log.i("thumb", url);
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("POST");
                urlConnection.setDoInput(true);
                urlConnection.setDoOutput(true);
                urlConnection.setUseCaches(false);
                urlConnection.connect();

           /* int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }*/

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("thumb dnwld", "Error downloading image from " + url);
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
            return null;
        }





        @Override
        protected void onPostExecute(String file_url) {
            //  prgDialog.dismiss();
       //     pDialog.setVisibility(View.INVISIBLE);
            if(bmp != null)
            {

                iv.setVisibility(View.VISIBLE);
                iv.setImageBitmap(bmp);
            }
            else
            {
                iv.setImageBitmap(null);
            }
            prgbar.setVisibility(View.GONE);

        }
    }



    private void GetWallets() {
        prgDialog2.show();
        String endpoint= "transfer/getwallets.action";


        String usid = Utility.gettUtilUserId(getActivity());
        String agentid = Utility.gettUtilAgentId(getActivity());


        String params = usid+"/"+agentid+"/93939393";

        String urlparams = "";
        try {
            urlparams = SecurityLayer.genURLCBC(params,endpoint,getActivity());
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
                    // JSON Object


                    SecurityLayer.Log("Get Wallets Resp", response.body());
                    SecurityLayer.Log("response..:", response.body());
                    JSONObject obj = new JSONObject(response.body());
                    //obj = Utility.onresp(obj,getActivity());
                    obj = SecurityLayer.decryptTransaction(obj, getActivity());
                    SecurityLayer.Log("decrypted_response", obj.toString());





                    JSONArray servdata = obj.optJSONArray("data");
                    //session.setString(SecurityLayer.KEY_APP_ID,appid);

                    if(!(response.body() == null)) {
                        String respcode = obj.optString("responseCode");
                        String responsemessage = obj.optString("message");

                        SecurityLayer.Log("Response Message", responsemessage);

                        if (Utility.isNotNull(respcode) && Utility.isNotNull(respcode)) {
                            if (!(Utility.checkUserLocked(respcode))) {
                                SecurityLayer.Log("Response Message", responsemessage);

                                if (respcode.equals("00")){
                                    SecurityLayer.Log("JSON Aray", servdata.toString());
                                    if(servdata.length() > 0){

                                        SecurityLayer.Log("JSON Aray", servdata.toString());
                                        String billerdata = servdata.toString();
                                        session.setString(SessionManagement.KEY_SETWALLETS,"Y");
                                        session.setString(SessionManagement.KEY_WALLETS,billerdata);

                                    }else{
                                        Toast.makeText(
                                                getActivity(),
                                                "No services available  ",
                                                Toast.LENGTH_LONG).show();
                                    }

                                }else{
                                    Toast.makeText(
                                            getActivity(),
                                            "" + responsemessage,
                                            Toast.LENGTH_LONG).show();
                                }
                            } else {
                                getActivity().finish();
                                startActivity(new Intent(getActivity(), SignInActivity.class));
                                Toast.makeText(
                                        getActivity(),
                                        "You have been locked out of the app.Please call customer care for further details",
                                        Toast.LENGTH_LONG).show();

                            }
                        } else {

                            Toast.makeText(
                                    getActivity(),
                                    "There was an error on your request",
                                    Toast.LENGTH_LONG).show();


                        }
                    } else {

                        Toast.makeText(
                                getActivity(),
                                "There was an error on your request",
                                Toast.LENGTH_LONG).show();


                    }
                    // prgDialog2.dismiss();




                } catch (JSONException e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // TODO Auto-generated catch block
                    Toast.makeText(getActivity(), getActivity().getText(R.string.conn_error), Toast.LENGTH_LONG).show();
                    // SecurityLayer.Log(e.toString());

                } catch (Exception e) {
                    SecurityLayer.Log("encryptionJSONException", e.toString());
                    // SecurityLayer.Log(e.toString());
                }
                prgDialog.dismiss();
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                // Log error here since request failed
                // Log error here since request failed
                SecurityLayer.Log("throwable error",t.toString());


                Toast.makeText(
                        getActivity(),
                        "There was an error on your request",
                        Toast.LENGTH_LONG).show();



                prgDialog.dismiss();
            }
        });

    }
    @Override
    public void onPause() {
        super.onPause();

        if ((prgDialog != null) && prgDialog.isShowing()) {
            prgDialog.dismiss();
            prgDialog = null;
        }
    }

}
