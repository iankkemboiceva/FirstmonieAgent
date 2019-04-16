package firstmob.firstbank.com.firstagent;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class OpenAccBVNConfirm extends AppCompatActivity implements View.OnClickListener {
    String strfname, strlname, strmidnm, stryob, stremail, strhmdd, strmobn, strsalut, strmarst, strcity, strstate, strgender, straddr;
    TextView txtstrfname, txtstrlname, txtstrmidnm, txtstryob, txtstremail, txtstrhmdd, txtstrmobn, txtstrsalut, txtstrmarst, txtstrcity, txtstrstate, txtstrgender, txtstraddr;

    Button btnconfirm;
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




        Intent intent = getIntent();
        if (intent != null) {
            strfname = intent.getStringExtra("fname");
            strlname = intent.getStringExtra("lname");
            strmidnm = intent.getStringExtra("midname");
            stryob = intent.getStringExtra("yob");
            stremail = intent.getStringExtra("email");
            strhmdd = intent.getStringExtra("hmadd");
            strmobn = intent.getStringExtra("mobn");
            strsalut = intent.getStringExtra("salut");
            strmarst = intent.getStringExtra("marstatus");
            strcity = intent.getStringExtra("city");
            strstate = intent.getStringExtra("state");
            strgender = intent.getStringExtra("gender");
            straddr = intent.getStringExtra("straddr");

            txtstrfname.setText(strfname+" "+strlname);
            txtstryob.setText(stryob);
            txtstremail.setText(stremail);
            txtstrmobn.setText(strmobn);
            txtstrmarst.setText(strmarst);
            txtstrgender.setText(strgender);

            txtstrstate.setText(strstate);
            txtstraddr.setText(straddr);
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
            Intent intent  = new Intent(OpenAccBVNConfirm.this,OpenAccUpPicActivity.class);



            intent.putExtra("fname", strfname);
            intent.putExtra("lname", strlname);
            intent.putExtra("midname", strmidnm);
            intent.putExtra("yob", stryob);
            intent.putExtra("gender", strgender);
            intent.putExtra("city", "NA");
            intent.putExtra("state", strstate);
            intent.putExtra("straddr", straddr);
            intent.putExtra("email", stremail);
            intent.putExtra("hmadd", "NA");
            intent.putExtra("mobn", strmobn);
            intent.putExtra("salut", strsalut);
            intent.putExtra("marstatus", strmarst);




            startActivity(intent);
        }
    }
}
