package firstmob.firstbank.com.firstagent;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Vibrator;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
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

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;
import java.util.HashMap;

import rest.ApiInterface;
import rest.ApiSecurityClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import security.SecurityLayer;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FMobActivity extends BaseActivity implements FragmentDrawer.FragmentDrawerListener,View.OnClickListener {
    private Toolbar mToolbar;
    int count = 1;
    private FragmentDrawer drawerFragment;

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
    public void onUserInteraction(){
     //   resetDisconnectTimer();


    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {



        super.onCreate(savedInstanceState);
       // session = new SessionManagement(this);
        pro = new ProgressDialog(getApplicationContext());
        pro.setMessage("Loading...");
        pro.setTitle("");
        pro.setCancelable(false);
        session = new SessionManagement(this);
        setContentView(R.layout.oldactivity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        drawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);


       drawerFragment = (FragmentDrawer)
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

    /* public  void setNewToolbar(){
         mToolbar = (Toolbar) findViewById(R.id.newtoolbar);
         setSupportActionBar(mToolbar);
         getSupportActionBar().setDisplayShowHomeEnabled(true);
     }*/
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

             showEditDialog("INBOX");

           /* android.app.Fragment  fragment = new Inbox();
             addAppFragment(fragment,"Inbox");*/
            /*FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            //  String tag = Integer.toString(title);
            fragmentTransaction.replace(R.id.container_body, fragment,"Inbox");
            fragmentTransaction.addToBackStack("Inbox");
       //   getSupportActionBar().setTitle("Notifications");
            //setActionBarTitle("Inbox");
            fragmentTransaction.commit();*/

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
    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        String list = session.getString("VTYPE");

       boolean checklg = true;
        switch (position) {
           case 40:
               if( list == null){
                   fragment = new NewHomeGrid();
               }
               else{
                   SecurityLayer.Log("View type chosen",list);

if(list.equals("N") || list.equals("grid")) {
    fragment = new NewHomeGrid();
}
               if(list.equals("list")) {
                   fragment = new HomeAccountFragNewUI();
               }}
                title = "Welcome";

                break;
            case 0:

                if( list == null){
                    fragment = new NewHomeGrid();
                }else {
                    if (list.equals("N") || list.equals("grid")) {
                        fragment = new NewHomeGrid();
                    }
                    if (list.equals("list")) {
                        fragment = new HomeAccountFragNewUI();
                    }
                }
                title = "Welcome";

                break;

            case 1:


               /* android.app.Fragment  fragmennt = new SelChartNewVers();
                String titlee = "My Performance";
               addAppFragment(fragmennt,titlee);*/
showEditDialog("MYPERF");
                break;


            case 2:

              /*  fragment = new ChangeACName();
                title = "My Profile";*/
                showEditDialog("PROF");
                break;
            case 3:

                //     session.logoutUser();
                // After logout redirect user to Loing Activity
            /*  Intent ps = new Intent(FMobActivity.this, ChatActivity.class);


                // Staring Login Activity
                startActivity(ps);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);*/


              fragment = new ComingSoon();
                title = "My Profile";

               /* fragment = new ViewComplaints();
                title = "My Profile";*/
                break;
            case 4:



               /*fragment = new ViewAgentRequests();
                title = "Coming Soon";*/
                fragment = new ComingSoon();
                title = "My Profile";
                break;
            case 5:
                this.finish();
                    session.logoutUser();
                // After logout redirect user to Loing Activity
                Intent i = new Intent(FMobActivity.this, SignInActivity.class);


                // Staring Login Activity
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                Toast.makeText(FMobActivity.this, "You have successfully signed out", Toast.LENGTH_LONG).show();
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
    public void setActionBarTitle(String title) {

     //   getSupportActionBar().setTitle(title);
    }


    protected void onDestroy() {

        super.onDestroy();
       // session.logoutUser();
        // Put code here.

    }

    private void replaceFragment (Fragment fragment){
        String backStateName = fragment.getClass().getName();

        FragmentManager manager = getSupportFragmentManager();
        boolean fragmentPopped = manager.popBackStackImmediate (backStateName, 0);

        if (!fragmentPopped){ //fragment not in back stack, create it.
            FragmentTransaction ft = manager.beginTransaction();
            ft.replace(R.id.container_body, fragment);
            ft.addToBackStack(backStateName);
            ft.commit();
        }
    }

    public void showEditDialog(String serv) {
        FragmentManager fm = getSupportFragmentManager();
        DialogSignInFragment editNameDialog = new DialogSignInFragment();
        Bundle bundle = new Bundle();
        bundle.putString("SERV",serv);
        editNameDialog.setArguments(bundle);
        editNameDialog.show(fm, "fragment_edit_name");
    }


    public void showNubanDialog(String recanno) {
        FragmentManager fm = getSupportFragmentManager();
        DialogNubanBanks nubbanks = new DialogNubanBanks();
        Bundle bundle = new Bundle();
        bundle.putString("recanno",recanno);
        nubbanks.setArguments(bundle);
        nubbanks.show(fm, "fragment_edit_name");
    }
    public void showWrongPinDialog(String serv) {
        FragmentManager fm = getSupportFragmentManager();
        WrongPinDialog editNameDialog = new WrongPinDialog();
        Bundle bundle = new Bundle();
        bundle.putString("SERV",serv);
        editNameDialog.setArguments(bundle);
        editNameDialog.show(fm, "fragment_edit_name");
    }

   /* @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();
    }*/
    @Override
    public void onBackPressed()
    {

        FragmentManager fm = getSupportFragmentManager();
        android.app.FragmentManager fm2 = getFragmentManager();
        int bentry = fm.getBackStackEntryCount();
        Log.i("TAG", "Frag Entry: " + bentry);

        int bentry2 = fm2.getBackStackEntryCount();
        Log.i("TAG", "Comm Report Frag Entry: " + bentry2);
        Fragment f = getSupportFragmentManager().findFragmentById(R.id.container_body);
        if(f instanceof NewHomeGrid || f instanceof HomeAccountFragNewUI){
            // do something with f
            SecurityLayer.Log("I am here","I am here");
            finish();

    }

      /*  finish();
        startActivity(new Intent(getApplicationContext(),FMobActivity.class));*/


        if(bentry2 > 0){

        }

        super.onBackPressed();
    }

  public void addFragment( Fragment frag,String title){

      FragmentManager fragmentManager = getSupportFragmentManager();
      FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
      //  String tag = Integer.toString(title);
      fragmentTransaction.replace(R.id.container_body, frag,title);
      fragmentTransaction.addToBackStack(title);
      fragmentTransaction.commit();
     setActionBarTitle(title);
  }

    public void addAppFragment( android.app.Fragment frag,String title){



        android.app.FragmentManager fragmentManager = getFragmentManager();
        android.app.FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        //  String tag = Integer.toString(title);
        fragmentTransaction.replace(R.id.container_body, frag,title);

        fragmentTransaction.addToBackStack(title);

        fragmentTransaction.commit();
    }
    @Override
    public void onClick(View v) {

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
FMobActivity.this.finish();
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
