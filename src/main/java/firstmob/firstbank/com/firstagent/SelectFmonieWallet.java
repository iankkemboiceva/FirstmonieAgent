package firstmob.firstbank.com.firstagent;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import adapter.adapter.DepoMenuAdapt;
import adapter.adapter.OTBList;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SelectFmonieWallet extends BaseActivity {
    GridView gridView;
    List<OTBList> planetsList = new ArrayList<OTBList>();
    String ptype;
    ListView lv;
    DepoMenuAdapt aAdpt;
    ProgressDialog prgDialog, prgDialog2;
    SessionManagement session;
    String sbpam = "0", pramo = "0";
    boolean blsbp = false, blpr = false, blpf = false, bllr = false, blms = false, blmpesa = false, blcash = false;
    ArrayList<String> ds = new ArrayList<String>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ftmenu);
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
        prgDialog = new ProgressDialog(this);

        prgDialog.setMessage("Please wait...");

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");

        gridView = (GridView) findViewById(R.id.gridView1);

        prgDialog.setCancelable(false);

        //checkInternetConnection2();
        lv = (ListView) findViewById(R.id.lv);
        SetPop();
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                Fragment fragment = null;
                String title = null;

                if (position == 0) {
                    /*fragment = new CashDepoTrans();
                    title = "Cash Deposit";*/
                   startActivity(new Intent(SelectFmonieWallet.this, DepositToFmonieWallet.class));
                } else if (position == 1) {
                /*    fragment = new SendOTB();
                    title = "Other Bank";*/

             //       startActivity(new Intent(SelectFmonieWallet.this, SendOtherWalletActivity.class));
                }
            }
        });

        session.setString("bankname",null);
        session.setString("bankcode",null);
        session.setString("recanno",null);
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


    public void SetPop(){
        planetsList.clear();
       /* planetsList.add(new Dashboard("FirstBank", R.drawable.ic_ftblue));
        planetsList.add(new Dashboard("Other Banks", R.drawable.ic_ftblue));
        planetsList.add(new Dashboard("FirstMonie",R.drawable.ic_ftblue));


        planetsList.add(new Dashboard("Other Wallets",R.drawable.ic_ftblue));



       *//* planetsList.add(new Dashboard("My Profile",R.drawable.icons40));*//*

        aAdpt = new DashboardAdapter( planetsList,getActivity());
        gridView.setAdapter(aAdpt);*/

        planetsList.add(new OTBList("Deposit to Firstmonie Wallet","057"));
        planetsList.add(new OTBList("Withdraw from Firstmonie Wallet","058"));

        aAdpt = new DepoMenuAdapt(planetsList, this);
        lv.setAdapter(aAdpt);
    }
}
