package firstmob.firstbank.com.firstagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.security.ProviderInstaller;

import java.util.Date;

import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class SupHomeActivity extends BaseSupActivity implements FragmentSupDrawer.FragmentDrawerListener,View.OnClickListener {
    private Toolbar mToolbar;
    int count = 1;
    private FragmentSupDrawer drawerFragment;

    //  public ResideMenu resideMenuClass;

    private static int SPLASH_TIME_OUT = 2000;
    TextView greet;
    SessionManagement session;
    ProgressDialog prgDialog;
    ProgressDialog pro ;
    DrawerLayout drawerLayout;
    ActionBarDrawerToggle mDrawerToggle;
    public static final long DISCONNECT_TIMEOUT = 180000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sup_home);
        pro = new ProgressDialog(getApplicationContext());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        session = new SessionManagement(this);


        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


        drawerFragment = (FragmentSupDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        //   drawerFragment.setArguments(bundle);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, drawerLayout, mToolbar);
        drawerFragment.setDrawerListener(this);

        //   mDrawerToggle = new ActionBarDrawerToggle (this, drawerLayout, mToolbar, R.string.open, R.string.close);

        prgDialog = new ProgressDialog(getApplicationContext());
        prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);
        updateAndroidSecurityProvider(this);

        displayView(40);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();

        inflater.inflate(R.menu.main, menu);

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        if(id == R.id.inbox) {
            startActivity(new Intent(getApplicationContext(), InboxActivity.class));


            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
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

    @Override
    public void onClick(View view) {

    }



    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String list = session.getString("VTYPE");

        boolean checklg = true;
        switch (position) {
            case 40:
                if( list == null){
                    fragment = new HomeGridSup();
                }
                else{
                    SecurityLayer.Log("View type chosen",list);

                    if(list.equals("N") || list.equals("grid")) {
                        fragment = new HomeGridSup();
                    }
                    if(list.equals("list")) {
                     //   fragment = new HomeAccountFragNewUI();
                    }}
                title = "Welcome";

                break;
            case 0:

                if( list == null){
                    fragment = new HomeGridSup();
                }else {
                    if (list.equals("N") || list.equals("grid")) {
                        fragment = new HomeGridSup();
                    }
                    if (list.equals("list")) {
                    //    fragment = new HomeAccountFragNewUI();
                    }
                }
                title = "Welcome";

                break;

            case 1:


                startActivity(new Intent(getApplicationContext(), MyPerfActivity.class));
                break;


            case 2:
                Intent intchgnm = new Intent(getApplicationContext(), ChangeAcNameActivity.class);
                intchgnm.putExtra("type", "SUP");
                startActivity(intchgnm);
                break;
            case 3:

                fragment = new ComingSoon();
                title = "My Profile";

                break;
            case 4:

                fragment = new ComingSoon();
                title = "My Profile";
                break;
            case 5:
                this.finish();
                session.logoutUser();
                // After logout redirect user to Loing Activity
                Intent i = new Intent(SupHomeActivity.this, SignInActivity.class);


                // Staring Login Activity
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(SupHomeActivity.this, "You have successfully signed out", Toast.LENGTH_LONG).show();
                break;


            default:
                break;
        }

        if (fragment != null) {
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,title);

            fragmentTransaction.addToBackStack(title);

            fragmentTransaction.commit();

            // set the toolbar title
            //   getSupportActionBar().setTitle(title);
        }
    }

    protected void onDestroy() {

        super.onDestroy();
        // session.logoutUser();
        // Put code here.

    }
    @Override
    public void onPause()
    {

        super.onPause();

        long secs = (new Date().getTime())/1000;
        SecurityLayer.Log("Seconds Loged",Long.toString(secs));
        session.putCurrTime(secs);
    }
    @Override
    public void onResume() {
        super.onResume();

    }


    public void addFragment( Fragment frag,String title){

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.container_body, frag,title);
        fragmentTransaction.addToBackStack(title);
        fragmentTransaction.commit();

    }

    public  void LogOut(){
        session.logoutUser();

        // After logout redirect user to Loing Activity
        finish();
        Intent i = new Intent(getApplicationContext(), SignInActivity.class);

        // Staring Login Activity
        startActivity(i);
        Toast.makeText(
                getApplicationContext(),
                "You have been locked out of the app.Please call customer care for further details",
                Toast.LENGTH_LONG).show();
        // Toast.makeText(getApplicationContext(), "You have logged out successfully", Toast.LENGTH_LONG).show();

    }

    public void SetForceOutDialog(String msg, final String title, final Context c) {
        if (!(c == null)) {
            new MaterialDialog.Builder(c)
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
                            SupHomeActivity.this.finish();
                            session.logoutUser();

                            // After logout redirect user to Loing Activity
                            Intent i = new Intent(c, SignInActivity.class);
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            // Staring Login Activity
                            startActivity(i);

                        }
                    })
                    .show();
        }
    }
}
