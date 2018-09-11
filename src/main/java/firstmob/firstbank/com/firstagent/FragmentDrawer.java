package firstmob.firstbank.com.firstagent;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.NavDrawerPOJO;
import adapter.NavigationDrawerAdapter;
import adapter.adapter.NavDrawerItemSignIn;
import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import security.SecurityLayer;


public class FragmentDrawer extends Fragment implements View.OnClickListener {
    File finalFile;
    private static String TAG = FragmentDrawerNewUI.class.getSimpleName();
    TextView tv,home,tvmobno,tvlastl,tvusid;
    String upurl = ApplicationConstants.IMG_URL+"image/profilepic?userId=";
    Button lyhomeid,lysignout;
    RelativeLayout header;
    //  private RecyclerView recyclerView;
    private ActionBarDrawerToggle mDrawerToggle;
    private DrawerLayout mDrawerLayout;
    static public List<NavDrawerPOJO> planetsList = new ArrayList<NavDrawerPOJO>();
    CircleImageView iv;
    ProgressDialog prgDialog,prgDialog2;
    public static TypedArray navMenuIcons,navIcpad;
    ImageView ivclose;
    private NavigationDrawerAdapter adapter;
    private View containerView;
    SessionManagement session;
    RelativeLayout rltransf,rlperf,rlccare,rlvsshop;
    private  static  String[] titles = null;
    private  static int [] dricons = null;
    private static ArrayList<String> vd = new ArrayList<String>();
    protected static ArrayList<Integer> vimges = new ArrayList<Integer>();
    protected static ArrayList<Integer> vicpad = new ArrayList<Integer>();
    private static Integer[] icons = null;
    private static Integer[] iconspad = null;
    String uploadFileName = null,userChoosenTask;
    int SELECT_FILE = 345;
    int REQUEST_CAMERA =3293;
    private FragmentDrawerListener drawerListener;

    public FragmentDrawer() {

    }

    public void setDrawerListener(FragmentDrawerListener listener) {
        this.drawerListener = listener;
    }

    public static List<NavDrawerItemSignIn> getData() {
        List<NavDrawerItemSignIn> data = new ArrayList<>();


        // preparing navigation drawer items
        for (int i = 0; i < vd.size(); i++) {
            NavDrawerItemSignIn navItem = new NavDrawerItemSignIn(vd.get(i),vimges.get(i),vicpad.get(i));

            data.add(navItem);
        }
        return data;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        titles = getActivity().getResources().getStringArray(R.array.nav_drawer_items);
        dricons = getActivity().getResources().getIntArray(R.array.nav_drawer_icons);
        session = new SessionManagement(getActivity());
        vd.clear();
        vd.add("Home");
      /*  vd.add("Deposit");
        vd.add("Transfers");

        vd.add("Cash Withdrawal");
        vd.add("Pay Bills");
        vd.add("Airtime Top Up");
        vd.add("Open Account");*/

        vd.add("My Performance");
        vd.add("My Profile");
        vd.add("Customer Care");
        vd.add("Visit My Agent Shop");
        vd.add("Sign Out");








        vimges.clear();
        vimges.add(R.drawable.home);
        /*vimges.add(R.drawable.deposit);
        vimges.add(R.drawable.transfer);

        vimges.add(R.drawable.cwithdraw);
        vimges.add(R.drawable.paybill);
        vimges.add(R.drawable.airtime);
        vimges.add(R.drawable.openacc);*/

        vimges.add(R.drawable.report);
        vimges.add(R.drawable.myprofile);
        vimges.add(R.drawable.ccare);
        vimges.add(R.drawable.ccare);
        vimges.add(R.drawable.logout);



        vicpad.clear();
        vicpad.add(0);
       /* vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);*/
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);
        vicpad.add(0);

        //   CheckServicesBool();

        navMenuIcons = getResources()
                .obtainTypedArray(R.array.nav_drawer_icons);


        navIcpad = getResources()
                .obtainTypedArray(R.array.nav_pad_icons);
//CheckServicesBool();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflating view layout
        View layout = inflater.inflate(R.layout.newdrawer, container, false);


        //recyclerView = (RecyclerView) layout.findViewById(R.id.drawerList);

        adapter = new NavigationDrawerAdapter(getActivity(), getData());
        //This is the code to provide a sectioned list
        header = (RelativeLayout) layout.findViewById(R.id.nav_header_container);
        lyhomeid = (Button) layout.findViewById(R.id.homebtn);
        lysignout = (Button) layout.findViewById(R.id.signoutbtn);
        rlccare = (RelativeLayout) layout.findViewById(R.id.rl4y);
       /* ivclose = (ImageView) layout.findViewById(R.id.imageView2);
        ivclose.setOnClickListener(this);*/
        rlperf = (RelativeLayout) layout.findViewById(R.id.rl3);
        rltransf = (RelativeLayout) layout.findViewById(R.id.rl4);
        rlvsshop = (RelativeLayout) layout.findViewById(R.id.rl3y);
       // rlvsshop.setOnClickListener(this);
        rltransf.setOnClickListener(this);
        rlperf.setOnClickListener(this);
       // rlccare.setOnClickListener(this);
       lysignout.setOnClickListener(this);
       lyhomeid.setOnClickListener(this);
        tv = (TextView) layout.findViewById(R.id.section_text);
     //   tvmobno = (TextView) layout.findViewById(R.id.sdf2);
        tvlastl = (TextView) layout.findViewById(R.id.sdf2);
        tvusid = (TextView) layout.findViewById(R.id.txuserid);
        String useridd = Utility.gettUtilUserId(getActivity());
        tvusid.setText("User ID: "+useridd);
        String strlastl = Utility.getLastl(getActivity());
        strlastl = Utility.convertDate(strlastl);
        tvlastl.setText("Last Login: "+strlastl.substring(0,strlastl.length() -12));
//        tvlastl.setText(Utility.getLastl(getContext()));
     //   tvmobno.setText('0'+Utility.gettUtilMobno(getContext()));
        String usid = Utility.gettUtilUserId(getActivity());
        upurl = upurl+usid+"&channel=1";
        //   scroll = (ImageView) layout.findViewById(R.id.scroll);

        //   padl = (ImageView) layout.findViewById(R.id.pad);
        iv = (CircleImageView) layout.findViewById(R.id.profile_image);
        //  iv.setImageBitmap(null);
//        iv.setOnClickListener(this);
        boolean checklg = false;
        //   padl.setBackgroundResource(0);
        checklg = session.checkLogin();
        HashMap<String, String> no = session.getMobNo();
        String  numb = no.get(SessionManagement.KEY_MOBILE);

        HashMap<String, Integer> nmob = session.getCurrTheme();
        int pos = nmob.get(SessionManagement.KEY_THEME);

        if(pos == 1){
            header.setBackgroundColor(getResources().getColor(R.color.colorPrimaryDark));
        }
        if(pos == 2){

        }
        if(pos == 3){
            header.setBackgroundResource(R.drawable.goldimg);
        }
        if(pos == 4){
            header.setBackgroundResource(R.drawable.red);

        }
        // uploadFileName = numb;
        uploadFileName = numb;
        prgDialog = new ProgressDialog(getActivity());
        prgDialog.setMessage("Loading....");
        prgDialog.setCancelable(false);
        if(checklg == true) {




        }else{
        }


        home = (TextView) layout.findViewById(R.id.home);
      //  home.setOnClickListener(this);
      //  tv.setOnClickListener(this);
        HashMap<String, String> dis = session.getDisp();
        String names = dis.get(SessionManagement.KEY_DISP);


        HashMap<String, String> sessname = session.getUserDetails();
        String sesn = sessname.get(SessionManagement.KEY_USERID);
String custname = session.getString(SessionManagement.KEY_CUSTNAME);
tv.setText(custname);


      /*  if(!(names == null || names.equals(" "))){
            names = names.replace("_"," ");

            tv.setText( names);

        }else{
            if(!(sesn == null)){
                if(sesn.contains(" ")) {
                    String fname = sesn.substring(0,sesn.indexOf(" "));
                    String lname = sesn.substring(sesn.lastIndexOf(" "),sesn.length() );
                    SecurityLayer.Log(" SessFname is", fname);
                    SecurityLayer.Log("L Sessname is", lname);

                    tv.setText(lname);
                }else {
                    tv.setText(sesn);
                }
            }
        }*/
    /*    recyclerView.setAdapter(adapter);
        //recyclerView.setAdapter(adapter);

        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerView.addOnItemTouchListener(new RecyclerTouchListener(getActivity(), recyclerView, new ClickListener() {
            @Override
            public void onClick(View view, int position) {

                drawerListener.onDrawerItemSelected(view, position);
                mDrawerLayout.closeDrawer(containerView);
            }

            @Override
            public void onLongClick(View view, int position) {

            }
        }));*/
    //    loadImage();
        String urll = ApplicationConstants.IMG_URL+"image/profilepic?userId=";
        String usidd = Utility.gettUtilUserId(getActivity());
        urll = urll+usidd;
        //  Picasso.with(getActivity()).load(urll).into(iv);

        return layout;
    }


    public void setUp(int fragmentId, DrawerLayout drawerLayout, final Toolbar toolbar) {
        containerView = getActivity().findViewById(fragmentId);
        mDrawerLayout = drawerLayout;
        mDrawerToggle = new ActionBarDrawerToggle(getActivity(), drawerLayout, toolbar, R.string.drawer_open, R.string.drawer_close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                //  invalidateOptionsMenu();

                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                //invalidateOptionsMenu();
                Utility.hideKeyboardFrom(getActivity(),drawerView);
            }

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                super.onDrawerSlide(drawerView, slideOffset);
                // toolbar.setAlpha(1 - slideOffset / 2);
            }
        };

        mDrawerLayout.setDrawerListener(mDrawerToggle);
        mDrawerLayout.post(new Runnable() {
            @Override
            public void run() {
                mDrawerToggle.syncState();
            }
        });

    }
    public void showDialog(){
        AlertDialog.Builder builderSingle = new AlertDialog.Builder(getActivity());
        final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(
                getActivity(),
                android.R.layout.select_dialog_singlechoice);
        arrayAdapter.add("Camera");
        arrayAdapter.add("Gallery");


        builderSingle.setNegativeButton(
                "cancel",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

        builderSingle.setAdapter(
                arrayAdapter,
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                        } else if (which == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        }
                    }
                });
        builderSingle.show();
    }
    public Dialog imageAlertDialog() {

        final AlertDialog.Builder builder = new AlertDialog.Builder(
                getActivity()).setAdapter(new ArrayAdapter<String>(
                        getActivity(), android.R.layout.simple_list_item_1,
                        new String[] { "Camera", "Gallery" }),
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (which == 0) {
                            Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                            startActivityForResult(takePicture, 0);//zero can be replaced with any action code

                        } else if (which == 1) {
                            Intent pickPhoto = new Intent(Intent.ACTION_PICK,
                                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                            startActivityForResult(pickPhoto , 1);//one can be replaced with any action code
                        }
                    }
                });

        return builder.create();
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Library",
                "Cancel" };
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result=Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask="Take Photo";
                    if(result) {
                        boolean camresult= Utility.checkCameraPermission(getActivity());
                        if(camresult) {
                            cameraIntent();
                        }

                    }
                } else if (items[item].equals("Choose from Library")) {
                    userChoosenTask="Choose from Library";
                    if(result) {
                        galleryIntent();
                    }
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask.equals("Take Photo")) {

                        cameraIntent();

                    }
                    else if(userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }
    private void cameraIntent()
    {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }
    public void SetDialog(String msg,String title){
        new MaterialDialog.Builder(getActivity())

                .title(title)
                .content(msg)

                .negativeText("Close")
                .show();


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }
    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm=null;
        if (data != null) {
            try {
                bm = getResizedBitmap(MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData()),950,600);
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();bm.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
                finalFile  = new File(Environment.getExternalStorageDirectory(),
                        System.currentTimeMillis()+".jpg");
                FileOutputStream fo;
                try {
                    finalFile.createNewFile();
                    fo = new FileOutputStream(finalFile);
                    fo.write(bytes.toByteArray());
                    fo.close();
                   /* new Thread(new Runnable() {
                        public void run() {


                            uploadFile(finalFile);

                        }
                    }).start();*/
                    new AsyncUplImg().execute("");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


        //    iv.setImageBitmap(bm);
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = getResizedBitmap((Bitmap) data.getExtras().get("data"),300,300);
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        finalFile = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis()+".jpg");
        FileOutputStream fo;
        try {
            finalFile.createNewFile();
            fo = new FileOutputStream(finalFile);
            fo.write(bytes.toByteArray());
            fo.close();
           /* new Thread(new Runnable() {
                public void run() {


                    uploadFile(finalFile);

                }
            }).start();*/
            new AsyncUplImg().execute();
           /* getActivity().finish();
            Toast.makeText(
                    getActivity(),
                    "Image Set Successfully",
                    Toast.LENGTH_LONG).show();
            startActivity(new Intent(getActivity(),FMobActivity.class));*/
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //   iv.setImageBitmap(thumbnail);



    }
    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float neww = ((float)width)*((float)0.6);
        float newh = ((float)height)*((float)0.6);
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // CREATE A MATRIX FOR THE MANIPULATION
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);

        // "RECREATE" THE NEW BITMAP
        Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height,
                matrix, false);

        return resizedBitmap;
    }

    public void uploadImage(File file){

        OkHttpClient client = new OkHttpClient();

        SecurityLayer.Log("Up Image File Name",file.getName());
        SecurityLayer.Log("Up Image File Name",file.getName());
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", "tmp_photo_" + System.currentTimeMillis(),
                        RequestBody.create(MediaType.parse("image/jpg"), file))

                .build();
        SecurityLayer.Log("Upload Url",upurl);
        Request request = new Request.Builder().url(upurl).post(formBody).build();

        Response response = null;
        try {
            response = client.newCall(request).execute();


            if (!response.isSuccessful()) throw new IOException("Unexpected code " + response);
            else{
                SecurityLayer.Log("Success upload","Success Upload");

            }
        } catch (IOException e) {
            e.printStackTrace();
        }


    }
    @Override
    public void onClick(View view) {

        if(view.getId() == R.id.rl4y){

            drawerListener.onDrawerItemSelected(view, 3);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.imageView2){


            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.rl3){

            drawerListener.onDrawerItemSelected(view, 1);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.rl4){

            drawerListener.onDrawerItemSelected(view, 2);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.rl3y){

            drawerListener.onDrawerItemSelected(view, 4);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.home){

            drawerListener.onDrawerItemSelected(view, 40);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.homebtn){

            drawerListener.onDrawerItemSelected(view, 40);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.signoutbtn){

            drawerListener.onDrawerItemSelected(view, 5);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.txt2) {

            drawerListener.onDrawerItemSelected(view, 41);
            mDrawerLayout.closeDrawer(containerView);
        }

        if(view.getId() == R.id.txt3) {

            drawerListener.onDrawerItemSelected(view, 42);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.txt4) {


            drawerListener.onDrawerItemSelected(view, 43);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.txt5) {

            drawerListener.onDrawerItemSelected(view, 44);
            mDrawerLayout.closeDrawer(containerView);
        }
        if(view.getId() == R.id.profile_image) {
           /* drawerListener.onDrawerItemSelected(view, 40);
            mDrawerLayout.closeDrawer(containerView);*/
            //  imageAlertDialog();
            //  SetDialog("Test","Test");
            //  showDialog();
            selectImage();
        }
        if(view.getId() == R.id.section_text) {
            drawerListener.onDrawerItemSelected(view, 40);
            mDrawerLayout.closeDrawer(containerView);
            Fragment fragment = null;
            String title = null;
            boolean checklg = false;
            checklg = session.checkLogin();

            if (fragment != null) {
                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                //  String tag = Integer.toString(title);
                fragmentTransaction.replace(R.id.container_body, fragment,title);
                fragmentTransaction.addToBackStack(title);
                fragmentTransaction.commit();
                ((MainActivity)getActivity())
                        .setActionBarTitle(title);
            }
        }
    }

    public static interface ClickListener {
        public void onClick(View view, int position);

        public void onLongClick(View view, int position);
    }

    static class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

        private GestureDetector gestureDetector;
        private ClickListener clickListener;

        public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
            this.clickListener = clickListener;
            gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
                @Override
                public boolean onSingleTapUp(MotionEvent e) {
                    return true;
                }

                @Override
                public void onLongPress(MotionEvent e) {
                    View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                    if (child != null && clickListener != null) {
                        clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                    }
                }
            });
        }

        @Override
        public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

            View child = rv.findChildViewUnder(e.getX(), e.getY());
            if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
                clickListener.onClick(child, rv.getChildPosition(child));
            }
            return false;
        }

        @Override
        public void onTouchEvent(RecyclerView rv, MotionEvent e) {
        }

        @Override
        public void onRequestDisallowInterceptTouchEvent(boolean b) {

        }
    }

    public interface FragmentDrawerListener {
        public void onDrawerItemSelected(View view, int position);
    }


    public void loadImage(){
        File destination = new File(Environment.getExternalStorageDirectory(),
                "FBNProf.jpg");

        new DownloadImg().execute("");

    }
    class AsyncUplImg extends AsyncTask<String, String, String> {
        Bitmap bmp = null;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            prgDialog.show();
        }

        // Download Music File from Internet
        @Override
        protected String doInBackground(String... f_url) {


            uploadImage(finalFile);
            return "34";
        }

        @Override
        protected void onPostExecute(String file_url) {
            prgDialog.dismiss();
            Toast.makeText(
                    getActivity(),
                    "Image Set Successfully",
                    Toast.LENGTH_LONG).show();
            getActivity().finish();

            startActivity(new Intent(getActivity(),FMobActivity.class));

        }
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
                String url = ApplicationConstants.IMG_URL+"image/profilepic?userId=";
                if(!(getActivity() == null)) {
                    String usid = Utility.gettUtilUserId(getActivity());
                    url = url + usid;
                    bmp = downloadBitmap(url);
                }
            }catch(Exception e){
                SecurityLayer.Log("ERROR While Downloadin", e.getLocalizedMessage());
             //   Toast.makeText(getContext(), "Error While Downloading File", Toast.LENGTH_LONG);
            }
            return "34";
        }


        private Bitmap downloadBitmap(String url) {
            HttpURLConnection urlConnection = null;
            try {
                Log.i("thumb", url);
                URL uri = new URL(url);
                urlConnection = (HttpURLConnection) uri.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setDoInput(true);

                urlConnection.connect();

           /* int statusCode = urlConnection.getResponseCode();
            if (statusCode != HttpStatus.SC_OK) {
                return null;
            }*/
/*

                URL url = new URL("http://upload.wikimedia.org/wikipedia/commons/9/9c/Image-Porkeri_001.jpg");
                InputStream in = new BufferedInputStream(url.openStream());
                OutputStream out = new BufferedOutputStream(new FileOutputStream("Image-Porkeri_001.jpg"));

                for ( int i; (i = in.read()) != -1; ) {
                    out.write(i);
                }
                in.close();
                out.close();
*/

                InputStream inputStream = urlConnection.getInputStream();
                if (inputStream != null) {
                    Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                    ByteArrayOutputStream out = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 100, out);
                    Bitmap decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(out.toByteArray()));
                    inputStream.close();
                    return bitmap;
                }
            } catch (Exception e) {
                urlConnection.disconnect();
                Log.w("thumb dnwld", e);
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

            if(bmp != null)
            {
                iv.setImageBitmap(bmp);
            }


        }
    }


    public void uploadFile(File sourceFile) {



        HttpURLConnection conn = null;
        DataOutputStream dos = null;
        String lineEnd = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";
        int bytesRead, bytesAvailable, bufferSize;
        byte[] buffer;
        int maxBufferSize = 1 * 1024 * 1024;



        try {

            // open a URL connection to the Servlet
            FileInputStream fileInputStream = new FileInputStream(sourceFile);
            URL url = new URL(upurl);
            SecurityLayer.Log("Upload url",upurl);
            // Open a HTTP  connection to  the URL
            conn = (HttpURLConnection) url.openConnection();
            conn.setDoInput(true); // Allow Inputs
            conn.setDoOutput(true); // Allow Outputs
            conn.setUseCaches(false); // Don't use a Cached Copy
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Connection", "Keep-Alive");
            conn.setRequestProperty("ENCTYPE", "multipart/form-data");
            conn.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);


            dos = new DataOutputStream(conn.getOutputStream());

            dos.writeBytes(twoHyphens + boundary + lineEnd);


            // create a buffer of  maximum size
            bytesAvailable = fileInputStream.available();

            bufferSize = Math.min(bytesAvailable, maxBufferSize);
            buffer = new byte[bufferSize];

            // read file and write it into form...
            bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            while (bytesRead > 0) {

                dos.write(buffer, 0, bufferSize);
                bytesAvailable = fileInputStream.available();
                bufferSize = Math.min(bytesAvailable, maxBufferSize);
                bytesRead = fileInputStream.read(buffer, 0, bufferSize);

            }

            // send multipart form data necesssary after file data...
            dos.writeBytes(lineEnd);
            dos.writeBytes(twoHyphens + boundary + twoHyphens + lineEnd);

            // Responses from the server (code and message)
            int   serverResponseCode = conn.getResponseCode();
            String serverResponseMessage = conn.getResponseMessage();

            Log.i("uploadFile", "HTTP Response is : "
                    + serverResponseMessage + ": " + serverResponseCode);

            if(serverResponseCode == 200){

                getActivity().runOnUiThread(new Runnable() {
                    public void run() {

                        String msg = "File Upload Completed.\n\n See uploaded file here : \n\n"
                                +" http://www.androidexample.com/media/uploads/"
                                +uploadFileName;


                        Toast.makeText(getActivity(), "File Upload Complete.",
                                Toast.LENGTH_SHORT).show();
                    }
                });
            }

            //close the streams //
            fileInputStream.close();
            dos.flush();
            dos.close();

        } catch (MalformedURLException ex) {

            prgDialog.dismiss();
            ex.printStackTrace();

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    //    messageText.setText("MalformedURLException Exception : check script url.");
                    Toast.makeText(getActivity(), "MalformedURLException",
                            Toast.LENGTH_SHORT).show();
                }
            });


        } catch (Exception e) {

            prgDialog.dismiss();
            e.printStackTrace();

            getActivity().runOnUiThread(new Runnable() {
                public void run() {
                    SecurityLayer.Log("Got Exception ","");
                    Toast.makeText(getActivity(), "Got Exception : see logcat ",
                            Toast.LENGTH_SHORT).show();
                }
            });
        }
        prgDialog.dismiss();


    } // End else block


}
