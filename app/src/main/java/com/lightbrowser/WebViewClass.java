package com.lightbrowser;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.MailTo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Message;
import android.os.Parcelable;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.webkit.DownloadListener;
import android.webkit.URLUtil;
import android.webkit.ValueCallback;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;


import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.crystal.crystalpreloaders.widgets.CrystalPreloader;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.Date;

import butterknife.BindView;
import im.delight.android.webview.AdvancedWebView;

/**
 * Created by JOBY KORAH GEORGE on 13-Mar-18.
 */

public class WebViewClass extends Activity {


    private static final int INPUT_FILE_REQUEST_CODE = 1;
    private static final int FILECHOOSER_RESULTCODE = 1;
    private static final String TAG = WebViewClass.class.getSimpleName();
    private ValueCallback<Uri> mUploadMessage;
    private Uri mCapturedImageURI = null;
    private ValueCallback<Uri[]> mFilePathCallback;
    private String mCameraPhotoPath;
    ValueCallback<Uri[]> filePath1;

    public static WebView myWebView;

    /* ##########new code test###########*/
    private LinearLayout mContentView;
    private FrameLayout mCustomViewContainer;
    private View mCustomView;
    private WebChromeClient.CustomViewCallback mCustomViewCallback;
    FrameLayout.LayoutParams COVER_SCREEN_GRAVITY_CENTER = new FrameLayout.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT, Gravity.CENTER);
    private WebChromeClient mWebChromeClient;
   /* private static WebViewClass instance;*/
    int REQUEST_PHONE_CALL = 1;
    int MY_PERMISSION_REQUEST_CODE = 11;
    int counter=0;
    /* ##########new code test###########*/

    private String urlgo;
    private ProgressDialog progressDialog;
    private ImageView home;

    static Context context;
    //public static final String USER_AGENT_FAKE = "Mozilla/5.0 (Linux; Android 4.1.1; Galaxy Nexus Build/JRO03C) AppleWebKit/535.19 (KHTML, like Gecko) Chrome/18.0.1025.166 Mobile Safari/535.19";
    public static final String USER_AGENT_FAKE_OLD = "Mozilla/5.0 (Linux; U; Android 4.1.1; en-gb; Build/KLP) AppleWebKit/534.30 (KHTML, like Gecko) Version/4.0 Safari/534.30";
    public static final String USER_AGENT_FAKE_kitkat_loli = "Mozilla/5.0 (Linux; Android 4.4; Nexus 5 Build/_BuildID_) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/30.0.0.0 Mobile Safari/537.36";
    public static final String USER_AGENT_FAKE_post_loli = "Mozilla/5.0 (Linux; Android 5.1.1; Nexus 5 Build/LMY48B; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/43.0.2357.65 Mobile Safari/537.36";
    @SuppressLint("WrongViewCast")

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            if (requestCode != INPUT_FILE_REQUEST_CODE || mFilePathCallback == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            Uri[] results = null;
            // Check that the response is a good one
            if (resultCode == Activity.RESULT_OK) {
                if (data == null) {
                    // If there is not data, then we may have taken a photo
                    if (mCameraPhotoPath != null) {
                        results = new Uri[]{Uri.parse(mCameraPhotoPath)};
                    }
                } else {
                    String dataString = data.getDataString();
                    if (dataString != null) {
                        results = new Uri[]{Uri.parse(dataString)};
                    }
                }
            }
            mFilePathCallback.onReceiveValue(results);
            mFilePathCallback = null;
        } else if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.KITKAT) {
            if (requestCode != FILECHOOSER_RESULTCODE || mUploadMessage == null) {
                super.onActivityResult(requestCode, resultCode, data);
                return;
            }
            if (requestCode == FILECHOOSER_RESULTCODE) {
                if (null == this.mUploadMessage) {
                    return;
                }
                Uri result = null;
                try {
                    if (resultCode != RESULT_OK) {
                        result = null;
                    } else {
                        // retrieve from the private variable if the intent is null
                        result = data == null ? mCapturedImageURI : data.getData();
                    }
                } catch (Exception e) {
                    Toast.makeText(getApplicationContext(), "activity :" + e,
                            Toast.LENGTH_LONG).show();
                }
                mUploadMessage.onReceiveValue(result);
                mUploadMessage = null;
            }
        }
        return;
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir
        );
        return imageFile;
    }


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_webview);

        /*########## new code set #########*/

        mContentView = (LinearLayout) findViewById(R.id.linearlayout);
        myWebView = (WebView) findViewById(R.id.webview);
        mCustomViewContainer = (FrameLayout) findViewById(R.id.fullscreen_custom_content);

        /*########## new code set #########*/

        Intent in = getIntent();
        Bundle url2 = in.getExtras();
        urlgo = "" + url2.get("url");

        if (!DetectConnection.checkInternetConnection(WebViewClass.this)) {
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

        home=(ImageView)findViewById(R.id.home);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent= new Intent(WebViewClass.this,MainActivity.class);
                startActivity(intent);
            }
        });

//        myWebView.setWebChromeClient(new WebChromeClient());

        mWebChromeClient = new WebChromeClient() {

            /* ######## new set code  #####*/
            @Override
            public void onShowCustomView(View view, WebChromeClient.CustomViewCallback callback) {
                // if a view already exists then immediately terminate the new one
                if (mCustomView != null) {
                    callback.onCustomViewHidden();
                    return;
                }

                // Add the custom view to its container.
                mCustomViewContainer.addView(view, COVER_SCREEN_GRAVITY_CENTER);
                mCustomView = view;
                mCustomViewCallback = callback;

                // hide main browser view
                mContentView.setVisibility(View.GONE);

                // Finally show the custom view container.
                mCustomViewContainer.setVisibility(View.VISIBLE);
                mCustomViewContainer.bringToFront();
            }

            @Override
            public void onHideCustomView() {
                if (mCustomView == null)
                    return;

                // Hide the custom view.
                mCustomView.setVisibility(View.GONE);
                // Remove the custom view from its container.
                mCustomViewContainer.removeView(mCustomView);
                mCustomView = null;
                mCustomViewContainer.setVisibility(View.GONE);
                mCustomViewCallback.onCustomViewHidden();

                // Show the content view.
                mContentView.setVisibility(View.VISIBLE);
            }

          /* ######## new set code  #####*/

            @Override
            public void onProgressChanged(WebView view, int newProgress) {

                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);

                if (progressDialog == null) {
                    progressDialog= CustomProgressDialog.createProgressDialog(WebViewClass.this, true);
                } else {
                    progressDialog.show();
                    progressDialog.setMessage("Please Wait");
                    progressDialog.setCanceledOnTouchOutside(true);
                }
                if (newProgress == 100) {
                    progressDialog.setCancelable(true);
                    progressDialog.cancel();
                }
            }

            public boolean onShowFileChooser(WebView view, ValueCallback<Uri[]> filePath, WebChromeClient.FileChooserParams fileChooserParams) {
                int rc = PackageManager.PERMISSION_GRANTED;
                if (Build.VERSION.SDK_INT >= 23) {
                    rc = checkSelfPermission(Manifest.permission.CAMERA);
                    if (rc != PackageManager.PERMISSION_GRANTED) {
                        filePath1 = filePath;
                        requestPermissions();
                    } else {
                        uploadFile(filePath);
                    }
                }
                return true;
            }

            // openFileChooser for Android 3.0+
            public void openFileChooser(ValueCallback<Uri> uploadMsg, String acceptType) {
                mUploadMessage = uploadMsg;
                // Create AndroidExampleFolder at sdcard
                // Create AndroidExampleFolder at sdcard
                File imageStorageDir = new File(
                        Environment.getExternalStoragePublicDirectory(
                                Environment.DIRECTORY_PICTURES)
                        , "AndroidExampleFolder");
                if (!imageStorageDir.exists()) {
                    // Create AndroidExampleFolder at sdcard
                    imageStorageDir.mkdirs();
                }
                // Create camera captured image file path and name
                File file = new File(
                        imageStorageDir + File.separator + "IMG_"
                                + String.valueOf(System.currentTimeMillis())
                                + ".jpg");
                mCapturedImageURI = Uri.fromFile(file);
                // Camera capture image intent
                final Intent captureIntent = new Intent(
                        MediaStore.ACTION_IMAGE_CAPTURE);
                captureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCapturedImageURI);
                Intent i = new Intent(Intent.ACTION_GET_CONTENT);
                i.addCategory(Intent.CATEGORY_OPENABLE);
                i.setType("image/*");
                // Create file chooser intent
                Intent chooserIntent = Intent.createChooser(i, "Image Chooser");
                // Set camera intent to file chooser
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS
                        , new Parcelable[]{captureIntent});
                // On select image call onActivityResult method of activity
                startActivityForResult(chooserIntent, FILECHOOSER_RESULTCODE);
            }

            // openFileChooser for Android < 3.0
            public void openFileChooser(ValueCallback<Uri> uploadMsg) {
                openFileChooser(uploadMsg, "");
            }

            //openFileChooser for other Android versions
            public void openFileChooser(ValueCallback<Uri> uploadMsg,
                                        String acceptType,
                                        String capture) {
                openFileChooser(uploadMsg, acceptType);
            }

        };


        WebSettings webSettings = myWebView.getSettings();
        webSettings.setJavaScriptEnabled(true);
        webSettings.setDomStorageEnabled(true);
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setAllowContentAccess(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDatabaseEnabled(true);
        webSettings.setDisplayZoomControls(true);
        webSettings.setLoadsImagesAutomatically(true);
        webSettings.setSupportZoom(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);

        if(Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT){
            myWebView.getSettings().setUserAgentString(USER_AGENT_FAKE_OLD);
        }
        else if(Build.VERSION.SDK_INT > Build.VERSION_CODES.KITKAT && Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP ){
            myWebView.getSettings().setUserAgentString(USER_AGENT_FAKE_kitkat_loli);
        }
        else if(Build.VERSION.SDK_INT >Build.VERSION_CODES.LOLLIPOP){
            myWebView.getSettings().setUserAgentString(USER_AGENT_FAKE_post_loli);
        }



        myWebView.loadUrl(urlgo);

        //### new set code --- https://android--examples.blogspot.in/2017/08/android-webview-file-download-example.html ###
        checkPermission();
        myWebView.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDescription,
                                        String mimetype, long contentLength) {

                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url));

                request.allowScanningByMediaScanner();

                request.setNotificationVisibility(
                        DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);

                String fileName = URLUtil.guessFileName(url,contentDescription,mimetype);

                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS,fileName);

                DownloadManager dManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                dManager.enqueue(request);
            }
        });
        //### new set code --- https://android--examples.blogspot.in/2017/08/android-webview-file-download-example.html ###

        myWebView.setWebViewClient(new HelloWebViewClient());

    }


    protected void checkPermission(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.M){
            if(checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)!= PackageManager.PERMISSION_GRANTED){
                if(shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    // show an alert dialog
                    android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(WebViewClass.this);
                    builder.setMessage("Write external storage permission is required.(Download)");
                    builder.setTitle("Please grant permission");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            ActivityCompat.requestPermissions(
                                    WebViewClass.this,
                                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                    MY_PERMISSION_REQUEST_CODE
                            );
                        }
                    });
                    builder.setNeutralButton("Cancel",null);
                    android.app.AlertDialog dialog = builder.create();
                    dialog.show();
                }else {
                    // Request permission
                    ActivityCompat.requestPermissions(
                            WebViewClass.this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                            MY_PERMISSION_REQUEST_CODE
                    );
                }
            }else {
                // Permission already granted
            }
        }
    }

    private class HelloWebViewClient extends WebViewClient {

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            if (!DetectConnection.checkInternetConnection(WebViewClass.this)) {
                Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
            }
        }

        @Override
        public boolean shouldOverrideUrlLoading(WebView webview, String url) {
            myWebView.setWebChromeClient(mWebChromeClient);
            /*new jkg */
            if (url.startsWith("tel:")) {
                Intent phone = new Intent(Intent.ACTION_CALL);
                phone.setData(Uri.parse(url));
                if (ActivityCompat.checkSelfPermission(WebViewClass.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WebViewClass.this, new String[]{Manifest.permission.CALL_PHONE}, REQUEST_PHONE_CALL);

                }
                else{
                    startActivity(phone);
                }
                return true;
            }

            if (url.startsWith("sms:")) {
                Intent sms = new Intent(Intent.ACTION_VIEW);
                sms.setData(Uri.parse("sms:"));
                sms.setType("vnd.android-dir/mms-sms");
                sms.putExtra("sms_body", url);
                if (ActivityCompat.checkSelfPermission(WebViewClass.this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(WebViewClass.this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_PHONE_CALL);

                }
                else{
                    startActivity(sms);
                }
                return true;
            }

            if(url.startsWith("mailto:")) {
                if (url.contains("body=")) {
                    Intent mail = new Intent(Intent.ACTION_SENDTO);
                    mail.setData(Uri.parse("mailto:"));
                    String body = url.split("body=")[1];
                    if (!TextUtils.isEmpty(body)) {
                        body = body.split("&")[0];
                        // Encode the body text
                        try {
                            body = URLDecoder.decode(body, "UTF-8");
                        } catch (UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                        // Put the mail body into intent
                        mail.putExtra(Intent.EXTRA_TEXT, body);
                        if (mail.resolveActivity(getPackageManager()) != null) {
                            startActivity(mail);
                        }
                        return true;
                    }
                }
            }


            if (url.startsWith("intent://")) {
                try {
                    Context context = webview.getContext();
                    Intent intent = Intent.parseUri(url, Intent.URI_INTENT_SCHEME);

                    if (intent != null) {
                        webview.stopLoading();

                        PackageManager packageManager = context.getPackageManager();
                        ResolveInfo info = packageManager.resolveActivity(intent, PackageManager.MATCH_DEFAULT_ONLY);
                        if (info != null) {
                            context.startActivity(intent);
                        } else {
                            String fallbackUrl = intent.getStringExtra("browser_fallback_url");
                            webview.loadUrl(fallbackUrl);
                        }

                        return true;
                    }
                } catch (URISyntaxException e) {

                }
            }

            /*new jkg */
            myWebView.loadUrl(url);
            return true;
        }
    }



    @Override
    protected void onStop() {

        super.onStop();
        if (mCustomView != null) {
            if (mCustomViewCallback != null)
                mCustomViewCallback.onCustomViewHidden();
            mCustomView = null;
        }

    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
        if (mCustomView != null) {
            mWebChromeClient.onHideCustomView();
          //  progressDialog.dismiss();
        } else {
            finish();
          //  progressDialog.dismiss();
        }

        // #######**##### //

       /* counter++;
        if(counter > 2){
            System.exit(0);
        }else{
            Toast.makeText(WebViewClass.this, "TRIPLE CLICK TO EXIT!", Toast.LENGTH_SHORT).show();
        }

        final long DELAY_TIME = 3000L;
        new Thread(new Runnable() {
            public void run(){
                try {
                    Thread.sleep(DELAY_TIME);
                    counter = 0;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }).start();*/
        // #######**##### //

    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK) && myWebView.canGoBack()) {
            myWebView.goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

    private void uploadFile(ValueCallback<Uri[]> filePath) {

        // Double check that we don't have any existing callbacks
        if (mFilePathCallback != null) {
            mFilePathCallback.onReceiveValue(null);
        }
        mFilePathCallback = filePath;
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
                takePictureIntent.putExtra("PhotoPath", mCameraPhotoPath);
            } catch (IOException ex) {
                // Error occurred while creating the File
                Log.e(TAG, "Unable to create Image File", ex);
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                mCameraPhotoPath = "file:" + photoFile.getAbsolutePath();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(photoFile));
            } else {
                takePictureIntent = null;
            }
        }
        Intent contentSelectionIntent = new Intent(Intent.ACTION_GET_CONTENT);
        contentSelectionIntent.addCategory(Intent.CATEGORY_OPENABLE);
        contentSelectionIntent.setType("*/*");
        Intent[] intentArray;
        if (takePictureIntent != null) {
            intentArray = new Intent[]{takePictureIntent};
        } else {
            intentArray = new Intent[0];
        }
        Intent chooserIntent = new Intent(Intent.ACTION_CHOOSER);
        chooserIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        chooserIntent.putExtra(Intent.EXTRA_INTENT, contentSelectionIntent);
        chooserIntent.putExtra(Intent.EXTRA_TITLE, "Image Chooser");
        chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, intentArray);
        startActivityForResult(chooserIntent, INPUT_FILE_REQUEST_CODE);
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void requestPermissions() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
            showMessageOKCancel("camera and storage permission needed",
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
                        }
                    });
        } else {
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1001);
        }

        //new code
        if (shouldShowRequestPermissionRationale(Manifest.permission.CALL_PHONE)) {
            showMessageOKCancel("camera and storage permission needed",
                    new DialogInterface.OnClickListener() {
                        @RequiresApi(api = Build.VERSION_CODES.M)
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1002);
                        }
                    });
        } else {
            requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, 1002);
        }
        //new code

    }

    private void showMessageOKCancel(String message, DialogInterface.OnClickListener okListener) {
        new AlertDialog.Builder(this)
                .setMessage(message)
                .setPositiveButton("OK", okListener)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface paramDialogInterface, int paramInt) {
                    }
                })
                .show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        if (permissions[0].equals("android.permission.CAMERA")) {

            if (grantResults.length == 2 && grantResults[0] == PackageManager.PERMISSION_GRANTED && grantResults[1] == PackageManager.PERMISSION_GRANTED) {
                uploadFile(filePath1);
            }
        }
        if (requestCode == 1002) {
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            }
        }
    }
}
