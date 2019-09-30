package firstmob.firstbank.com.firstagent;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.vipul.hp_hp.library.Layout_to_Image;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Iterator;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class FinalConfAccountOpening extends BaseActivity implements View.OnClickListener{
    TextView reccustid,recamo,rectelco,recfee,rectrref,recagcmn,txtrfcd,recdatetimee;
    Button btnsub;
    String accountno;
    ProgressDialog prgDialog,prgDialog2;
    String telcoop;
    EditText amon, edacc,pno,txtamount,txtnarr,edname,ednumber;


    Button shareImage,repissue;
    // android built in classes for bluetooth operations
    BluetoothAdapter mBluetoothAdapter;
    BluetoothSocket mmSocket;
    BluetoothDevice mmDevice;

    // needed for communication to bluetooth device / network
    OutputStream mmOutputStream;
    InputStream mmInputStream;
    Thread workerThread;
    String   txtrfc;
    byte[] readBuffer;
    int readBufferPosition;
    volatile boolean stopWorker;



    RelativeLayout rlagfee,rlaccom;


    Layout_to_Image layout_to_image;
    LinearLayout relativeLayout;
    Bitmap bitmap;
    RelativeLayout rlsave,rlshare;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_final_conf_account_opening);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);

  /*      // Get the ActionBar here to configure the way it behaves.
        final ActionBar ab = getSupportActionBar();
        //ab.setHomeAsUpIndicator(R.drawable.ic_menu); // set a custom icon for the default home button
        ab.setDisplayShowHomeEnabled(true); // show or hide the default home button
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setDisplayShowCustomEnabled(true); // enable overriding the default toolbar layout
        ab.setDisplayShowTitleEnabled(false); // disable the default title element here (for centered title)
*/

        reccustid = (TextView)findViewById(R.id.recip);

        prgDialog2 = new ProgressDialog(this);
        prgDialog2.setMessage("Loading....");
        prgDialog2.setCancelable(false);

        rlagfee = (RelativeLayout) findViewById(R.id.rlagfee);
        rlaccom = (RelativeLayout) findViewById(R.id.rlaccom);


        btnsub = (Button) findViewById(R.id.button2);
        btnsub.setOnClickListener(this);

        Intent intent = getIntent();
        if (intent != null) {



            accountno = intent.getStringExtra("accountno");

            reccustid.setText(accountno);

        /*   try {
                findBT();
                openBT();
            } catch (IOException ex) {
                ex.printStackTrace();
            }*/

        }

        shareImage = (Button) findViewById(R.id.share_image);
        shareImage.setOnClickListener(this);

        repissue = (Button) findViewById(R.id.reportiss);
        repissue.setOnClickListener(this);




        rlsave = (RelativeLayout) findViewById(R.id.rlsave);
        rlshare = (RelativeLayout) findViewById(R.id.rlshare);





    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private void shareImage(Uri imagePath) {
        Intent sharingIntent = new Intent(Intent.ACTION_SEND);
        sharingIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
        sharingIntent.setType("image/*");
        sharingIntent.putExtra(Intent.EXTRA_STREAM, imagePath);
        startActivity(Intent.createChooser(sharingIntent, "Share Image Using"));
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    @Override
    public void onClick(View view) {

        if (view.getId() == R.id.button2) {
            finish();


            Intent i = new Intent(getApplicationContext(), FMobActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            // Staring Login Activity
            startActivity(i);
        }

        if(view.getId() == R.id.share_image){
          /*  String userid = Utility.gettUtilUserId(getApplicationContext());
            String test =  "   \n \n    AIRTIME  \nUSERID: " + userid + " \nTelco: " + telcoop + " \nRef Number:" + txtrfc + " \nMobile Number:" + txtcustid + " \nAmount:" + amou + " Naira\n Sender Name:" + ednamee + " \n Fee:" + strfee + " Naira \n \n \n \n";


            print(mConnection, mInterface,test);
*/
            /*if(Utility.checkPermission(this)) {
                shareImage(getImageUri(getApplicationContext(), bitmap));
            }*/



            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("SELECT AN OPTION");

            // add a list
            String[] animals = {"Save to Gallery", "Share Receipt"};
            builder.setItems(animals, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    switch (which) {
                        case 0: // horse

                            rlaccom.setVisibility(View.GONE);
                            rlagfee.setVisibility(View.GONE);

                            layout_to_image=new Layout_to_Image(getApplicationContext(),relativeLayout);

                            //now call the main working function ;) and hold the returned image in bitmap

                            bitmap=layout_to_image.convert_layout();

                            String filename = "ShareRec"+System.currentTimeMillis()+".jpg";
                            if(Utility.checkPermission(FinalConfAccountOpening.this)) {
                                saveImage(bitmap, filename);
                                Toast.makeText(
                                        getApplicationContext(),
                                        "Receipt downloaded successfully to gallery",
                                        Toast.LENGTH_LONG).show();
                            }
                            break;
                        case 1: // cow
                            rlaccom.setVisibility(View.GONE);
                            rlagfee.setVisibility(View.GONE);
                            layout_to_image=new Layout_to_Image(getApplicationContext(),relativeLayout);

                            //now call the main working function ;) and hold the returned image in bitmap

                            bitmap=layout_to_image.convert_layout();
                            if(Utility.checkPermission(FinalConfAccountOpening.this)) {
                                shareImage(getImageUri(getApplicationContext(), bitmap));
                            }

                            rlaccom.setVisibility(View.VISIBLE);
                            rlagfee.setVisibility(View.VISIBLE);
                            break;
                    }
                }
            });

            // create and show the alert dialog
            AlertDialog dialog = builder.create();
            dialog.show();
        }

    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(getApplicationContext(), FMobActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);
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



    public File getAlbumStorageDir(String albumName) {
        // Get the directory for the user's public pictures directory.
        File file = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), albumName);
        if (!file.mkdirs()) {
            Log.e("SignaturePad", "Directory not created");
        }
        return file;
    }
    public void saveBitmapToJPG(Bitmap bitmap, File photo) throws IOException {
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(Color.WHITE);
        canvas.drawBitmap(bitmap, 0, 0, null);
        OutputStream stream = new FileOutputStream(photo);
        newBitmap.compress(Bitmap.CompressFormat.JPEG, 80, stream);
        stream.close();


    }
    public boolean addJpgSignatureToGallery(Bitmap signature) {
        boolean result = false;
        try {


            String flname = String.format("ShareRec_%d", System.currentTimeMillis());
            File photo = new File(getAlbumStorageDir("AccessAgent"), String.format("ShareR%d.jpg", System.currentTimeMillis()));
            File filename = photo;
            saveBitmapToJPG(signature, photo);
            scanMediaFile(photo);
            result = true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

    private void scanMediaFile(File photo) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        Uri contentUri = Uri.fromFile(photo);
        mediaScanIntent.setData(contentUri);
        FinalConfAccountOpening.this.sendBroadcast(mediaScanIntent);
    }


    private void saveImage(Bitmap finalBitmap, String image_name) {


        addJpgSignatureToGallery(finalBitmap);


        rlaccom.setVisibility(View.VISIBLE);
        rlagfee.setVisibility(View.VISIBLE);
    }

}
