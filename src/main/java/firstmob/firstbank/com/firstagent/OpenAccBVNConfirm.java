package firstmob.firstbank.com.firstagent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import model.GetCitiesData;
import model.GetStatesData;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OpenAccBVNConfirm extends AppCompatActivity implements View.OnClickListener {
    String strfname, strlname, strmidnm, stryob, stremail, strhmdd, strmobn, strsalut, strmarst, strcity, strstate, strgender, straddr;
    String strcode,strcitycode = "N/A";
    TextView txtstrfname, txtstrlname, txtstrmidnm, txtstryob, txtstremail, txtstrhmdd, txtstrmobn, txtstrsalut, txtstrmarst, txtstrcity, txtstrstate, txtstrgender, txtstraddr;
    List<GetStatesData> planetsList = new ArrayList<GetStatesData>();
    List<GetStatesData> arrangelist = new ArrayList<GetStatesData>();
    List<GetCitiesData> citylist = new ArrayList<GetCitiesData>();
    ArrayAdapter<GetStatesData> mobadapt;
    Button btnconfirm;
    Spinner sp1,sp3;
    RelativeLayout rlbvn;
    EditText streetno;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_acc_bvnconfirm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        btnconfirm = (Button) findViewById(R.id.button2);
        btnconfirm.setOnClickListener(this);
        setSupportActionBar(toolbar);

        // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)


        txtstrfname = (TextView)findViewById(R.id.fname);
        txtstryob = (TextView)findViewById(R.id.txtstryob);
        txtstremail = (TextView)findViewById(R.id.txtstremail);
        txtstrmobn = (TextView)findViewById(R.id.txtstrmobn);
        txtstrmarst = (TextView)findViewById(R.id.txtstrmarst);
        txtstrstate = (TextView)findViewById(R.id.txtstrstate);
        txtstrgender = (TextView)findViewById(R.id.txtstrgender);
        txtstraddr = (TextView)findViewById(R.id.txtstraddr);
        streetno = (EditText) findViewById(R.id.streetadrr);

        rlbvn = (RelativeLayout) findViewById(R.id.title);

        sp3 = (Spinner)findViewById(R.id.spin3);

        PopStates();





        Intent intent = getIntent();
        if (intent != null) {
            strfname = intent.getStringExtra("fname");
            strlname = intent.getStringExtra("lname");
            strmidnm = intent.getStringExtra("midname");
           String txstringryob = intent.getStringExtra("yob");
            stremail = intent.getStringExtra("email");
            strhmdd = intent.getStringExtra("hmadd");
            strmobn = intent.getStringExtra("mobn");
            strsalut = intent.getStringExtra("salut");
            strmarst = intent.getStringExtra("marstatus");
            strcity = intent.getStringExtra("city");
            strstate = intent.getStringExtra("state");
            strgender = intent.getStringExtra("gender");
            straddr = intent.getStringExtra("straddr");


          //  Toast.makeText(getApplicationContext(),stryob,  Toast.LENGTH_LONG).show();

            txtstrfname.setText(strfname+" "+strlname);
            txtstryob.setText(txstringryob);

            stryob = Utility.convertBVNdate(txstringryob);
            txtstremail.setText(stremail);
            txtstrmobn.setText(strmobn);
            txtstrmarst.setText(strmarst);
            txtstrgender.setText(strgender);

            txtstrstate.setText(strstate);
            txtstraddr.setText(straddr);

            if(strgender.equals("Male")){
                strgender = "M";
            }
            if(strgender.equals("Female")){
                strgender = "F";
            }


            if(Utility.isNotNull(strsalut) || strsalut.equals("")){
                rlbvn.setVisibility(View.VISIBLE);
            }
            sp1 = (Spinner)findViewById(R.id.spinsal);

            ArrayAdapter<CharSequence> adapter = null;
            if(Utility.isNotNull(strgender)) {

                if (strgender.equals("M")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.malesalut, R.layout.my_spinner);
                } else if (strgender.equals("F")) {
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.femsalut, R.layout.my_spinner);
                }else{
                    adapter = ArrayAdapter.createFromResource(
                            OpenAccBVNConfirm.this, R.array.salut, R.layout.my_spinner);
                }
            }else{
                adapter = ArrayAdapter.createFromResource(
                        OpenAccBVNConfirm.this, R.array.salut, R.layout.my_spinner);
            }
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp1.setAdapter(adapter);





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
        if(view.getId() == R.id.button2) {

            String txtstreetno = streetno.getText().toString();
            if (Utility.isNotNull(txtstreetno)) {
                if(!(sp3.getSelectedItemPosition() == 0)){
                    String strstates = arrangelist.get(sp3.getSelectedItemPosition()).getstateCode();
                    strcity = citylist.get(sp3.getSelectedItemPosition()-1).getcitycode();
                Intent intent = new Intent(OpenAccBVNConfirm.this, OpenAccUpPicActivity.class);


                if (strmarst.equals("Single")) {
                    strmarst = "UNMARR";
                }
                if (strmarst.equals("Married")) {
                    strmarst = "MARR";
                }

                intent.putExtra("fname", strfname);
                intent.putExtra("lname", strlname);
                intent.putExtra("midname", strmidnm);
                intent.putExtra("yob", stryob);
                intent.putExtra("gender", strgender);
                intent.putExtra("city", strcity);
                intent.putExtra("state", strstates);
                intent.putExtra("straddr", straddr);
                intent.putExtra("streetno", txtstreetno);
                intent.putExtra("email", stremail);
                intent.putExtra("hmadd", strhmdd);
                intent.putExtra("mobn", strmobn);

                intent.putExtra("marstatus", strmarst);

                if (Utility.isNotNull(strsalut) || strsalut.equals("")) {
                    if (!(sp1.getSelectedItemPosition() == 0)) {
                        strsalut = sp1.getSelectedItem().toString();
                        intent.putExtra("salut", strsalut);


                        startActivity(intent);
                    } else {
                        Toast.makeText(
                                getApplicationContext(),
                                "Please select a valid Title",
                                Toast.LENGTH_LONG).show();
                    }

                } else {

                    intent.putExtra("salut", strsalut);


                    startActivity(intent);
                }
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "Please select a valid state",
                        Toast.LENGTH_LONG).show();
            }
            }else{
                Toast.makeText(
                        getApplicationContext(),
                        "Please select a street number",
                        Toast.LENGTH_LONG).show();
            }
        }
    }


    private void PopStates(){
        String jsarray = "[{'id':'1','stateName':'Abia','stateCode':'23','cityName':'Umuahia','cityCode':'158'},{'id':'2','stateName':'Adamawa','stateCode':'04','cityName':'Yola','cityCode':'166'},{'id':'3','stateName':'Akwa Ibom','stateCode':'01','cityName':'Uyo','cityCode':'161'},{'id':'4','stateName':'Anambra','stateCode':'02','cityName':'Awka','cityCode':'024'},{'id':'5','stateName':'Bauchi','stateCode':'03','cityName':'Bauchi','cityCode':'029'},{'id':'6','stateName':'Bayelsa','stateCode':'32','cityName':'Yenagoa','cityCode':'174'},{'id':'7','stateName':'Benue','stateCode':'05','cityName':'Makurdi','cityCode':'103'},{'id':'8','stateName':'Borno','stateCode':'06','cityName':'Maiduguri','cityCode':'102'},{'id':'9','stateName':'Cross River','stateCode':'07','cityName':'Calabar','cityCode':'033'},{'id':'10','stateName':'Delta','stateCode':'09','cityName':'Asaba','cityCode':'022'},{'id':'11','stateName':'Ebonyi','stateCode':'33','cityName':'Abakaliki','cityCode':'172'},{'id':'12','stateName':'Edo','stateCode':'EDO','cityName':'Benin City','cityCode':'030'},{'id':'13','stateName':'Ekiti','stateCode':'34','cityName':'Ado - Ekiti','cityCode':'008'},{'id':'14','stateName':'Enugu','stateCode':'25','cityName':'Enugu','cityCode':'048'},{'id':'15','stateName':'Gombe','stateCode':'35','cityName':'Gombe','cityCode':'057'},{'id':'16','stateName':'Imo','stateCode':'10','cityName':'Owerri','cityCode':'138'},{'id':'17','stateName':'Jigawa','stateCode':'26','cityName':'Dutse','cityCode':'038'},{'id':'18','stateName':'Kaduna','stateCode':'11','cityName':'Kaduna','cityCode':'087'},{'id':'19','stateName':'Kano','stateCode':'12','cityName':'Kano','cityCode':'090'},{'id':'20','stateName':'Katsina','stateCode':'13','cityName':'Katsina','cityCode':'092'},{'id':'21','stateName':'Kebbi','stateCode':'27','cityName':'Birnin Kebbi','cityCode':'032'},{'id':'22','stateName':'Kogi','stateCode':'28','cityName':'Lokoja','cityCode':'101'},{'id':'23','stateName':'Kwara','stateCode':'14','cityName':'Ilorin','cityCode':'077'},{'id':'24','stateName':'Lagos','stateCode':'15','cityName':'Ikeja','cityCode':'099'},{'id':'25','stateName':'Nasarawa','stateCode':'36','cityName':'Lafia','cityCode':'098'},{'id':'26','stateName':'Niger','stateCode':'16','cityName':'Minna','cityCode':'108'},{'id':'27','stateName':'Ogun','stateCode':'17','cityName':'Abeokuta','cityCode':'005'},{'id':'28','stateName':'Ondo','stateCode':'18','cityName':'Akure','cityCode':'016'},{'id':'29','stateName':'Osun','stateCode':'29','cityName':'Oshogbo','cityCode':'133'},{'id':'30','stateName':'Oyo','stateCode':'19','cityName':'Ibadan','cityCode':'060'},{'id':'31','stateName':'Plateau','stateCode':'20','cityName':'Jos','cityCode':'086'},{'id':'32','stateName':'Rivers','stateCode':'21','cityName':'Port Harcourt','cityCode':'142'},{'id':'33','stateName':'Sokoto','stateCode':'22','cityName':'Sokoto','cityCode':'150'},{'id':'34','stateName':'Taraba','stateCode':'30','cityName':'Jalingo','cityCode':'085'},{'id':'35','stateName':'Yobe','stateCode':'31','cityName':'Damaturu','cityCode':'035'},{'id':'36','stateName':'Zamfara','stateCode':'37','cityName':'Gusau','cityCode':'058'}]";
        JSONArray plan = null;
        try {
            plan = new JSONArray(jsarray);


            if(plan.length() > 0){


                JSONObject json_data = null;

                for (int i = 0; i < plan.length(); i++) {
                    json_data = plan.getJSONObject(i);
                    //String accid = json_data.getString("benacid");




                    String statecode = json_data.optString("stateCode");
                    String statename = json_data.optString("stateName");
                    String citycode = json_data.optString("cityCode");
                    String cityname = json_data.optString("cityName");

                    SecurityLayer.Log("State Name", statename);
                    planetsList.add(new GetStatesData(statecode,statename));
                    citylist.add(new GetCitiesData(citycode,cityname));



                }
                if(!(planetsList == null)) {
                    if(planetsList.size() > 0) {
                        int index = 0;
                        Collections.sort(planetsList, new Comparator<GetStatesData>(){
                            public int compare(GetStatesData d1, GetStatesData d2){
                                return d1.getstateName().compareTo(d2.getstateName());
                            }
                        });
                        GetStatesData sa = new GetStatesData("0000","Select State");

                        arrangelist.add(sa);
                        for(int sd = 0;sd < planetsList.size();sd++){
                            arrangelist.add(planetsList.get(sd));
                            if(Utility.isNotNull(strstate)) {
                                String strstt = planetsList.get(sd).getstateCode();
                                if(strstt.equals(strstate)){
                                    index = sd +1;
                                }

                            }
                        }
                        //  Collections.swap(planetsList,0,planetsList.size() -1);
                        mobadapt = new ArrayAdapter<GetStatesData>(OpenAccBVNConfirm.this, R.layout.my_spinner, arrangelist);
                        mobadapt.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        sp3.setAdapter(mobadapt);
                        if(Utility.isNotNull(strstate)) {
                            sp3.setSelection(index);
                            Log.v("I am in",strstate);
                            Log.v("State index",Integer.toString(index));

                        }
                        //   sp3.setSelection(planetsList.size() -1);
                    }else{
                        Toast.makeText(
                                getApplicationContext(),
                                "No states available  ",
                                Toast.LENGTH_LONG).show();
                    }
                }




            }

        } catch (JSONException e) {
            e.printStackTrace();
        }


    }


}
