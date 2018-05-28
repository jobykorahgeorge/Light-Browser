package com.lightbrowser;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.webkit.WebView;
import android.widget.ListView;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.fabric.sdk.android.Fabric;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;
import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

import static com.lightbrowser.WebViewClass.context;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.facebook)
    ImageView facebook;
    @BindView(R.id.twitter)
    ImageView twitter;
    @BindView(R.id.skype)
    ImageView skype;
    @BindView(R.id.Linkedin)
    ImageView linkedin;
    @BindView(R.id.google)
    ImageView google;
    @BindView(R.id.fb_lite)
    ImageView fb_lite;
    @BindView(R.id.youtube)
    ImageView youtube;
    @BindView(R.id.gmail)
    ImageView gmail;
    @BindView(R.id.yahoo)
    ImageView yahoo;
    @BindView(R.id.instagram)
    ImageView insta;
    @BindView(R.id.telegram)
    ImageView telegram;
    @BindView(R.id.flipkart)
    ImageView flipkart;
    @BindView(R.id.amazon)
    ImageView amazon;
    @BindView(R.id.ebay)
    ImageView ebay;
    @BindView(R.id.paytm)
    ImageView paytm;
    @BindView(R.id.go)
    ImageView go;
    ImageView menu;
    @BindView(R.id.url)
    EditText url;
    @BindView(R.id.keyword)
    EditText keyword;
    @BindView(R.id.search)
    ImageView search;
    @BindView(R.id.listview)
    ListView listView;
    public static  Set<String> history = new HashSet<>();
    SearchArrayAdapter adapter;

    private String url_google="https://www.google.co.in/?gfe_rd=cr&dcr=0&ei=7wepWp-GApLK8Aer9ImoCw";
    private String url_facebook="https://www.facebook.com/";
    private String url_twitter="https://twitter.com/login?lang=en";
    private String url_skype="https://web.skype.com/";
    private String url_linkedin="https://in.linkedin.com";
    private String url_fblite="https://mbasic.facebook.com";
    private String url_youtube="https://www.youtube.com";
    private String url_gmail="https://mail.google.com";
    private String url_yahoo="https://login.yahoo.com/m";
    private String url_insta="https://www.instagram.com/";
    private String url_tele="https://web.telegram.org/#/login";
    private String url_flipkart="https://www.flipkart.com/";
    private String url_amazon_in="https://www.amazon.in/";
    private String url_ebay="https://www.ebay.com/";
    private String url_paytm="https://paytm.com/";

    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getCacheDir().deleteOnExit();
        Stetho.initializeWithDefaults(this);

        sharedPreferences=getSharedPreferences("HISTORY",MODE_PRIVATE);
        history=new HashSet<>(sharedPreferences.getStringSet("history",new HashSet<String>()));

      /*  CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Asul-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build());*/
        menu=(ImageView)findViewById(R.id.menu);
        menu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(MainActivity.this,menu);
                popup.getMenuInflater().inflate(R.menu.popup_menu,popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {



                        if(item.getTitle().equals("Clear App Cache")){
                            AlertDialog.Builder builder1 = new AlertDialog.Builder(MainActivity.this);
                            builder1.setMessage("Clearing app data will force stop the app.Would you like to continue?");
                            builder1.setCancelable(true);

                            builder1.setPositiveButton(
                                    "Yes",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            try {
                                                // clearing app data
                                                Runtime runtime = Runtime.getRuntime();
                                                runtime.exec("pm clear com.lightbrowser");

                                            } catch (Exception e) {
                                                e.printStackTrace();
                                            }
                                            dialog.cancel();
                                        }
                                    });

                            builder1.setNegativeButton(
                                    "No",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {
                                            dialog.cancel();
                                        }
                                    });

                            AlertDialog alert11 = builder1.create();
                            alert11.show();
                        }
                        if(item.getTitle().equals("Clear Web Cache")){

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                                CookieManager.getInstance().removeAllCookies(null);
                                CookieManager.getInstance().flush();
                                Toast.makeText(MainActivity.this,"Cleared web cache",Toast.LENGTH_SHORT).show();
                            } else
                            {
                                CookieSyncManager cookieSyncMngr=CookieSyncManager.createInstance(context);
                                cookieSyncMngr.startSync();
                                CookieManager cookieManager=CookieManager.getInstance();
                                cookieManager.removeAllCookie();
                                cookieManager.removeSessionCookie();
                                cookieSyncMngr.stopSync();
                                cookieSyncMngr.sync();
                            }

                        }

                        return true;
                    }
                });
                popup.show();
            }
        });


        if (!DetectConnection.checkInternetConnection(this)) {
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }

        url.setSingleLine();
        url.clearFocus();
        go.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent search=new Intent(MainActivity.this,WebViewClass.class);
                search.putExtra("url","https://"+url.getText());
                startActivity(search);
            }
        });
        url.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent search=new Intent(MainActivity.this,WebViewClass.class);
                    search.putExtra("url","https://"+url.getText());
                    startActivity(search);
                    return true;
                }
                return false;
            }
        });

       keyword.setSingleLine();
         search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               searchAdd();
            }
        });

        keyword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    searchAdd();
                    return true;
                }
                return false;
            }
        });





        keyword.addTextChangedListener(new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                int k=history.size();
                if(s.length()==0)
                    listView.setVisibility(View.GONE);

                Set<String> match = new HashSet<>();
                if(history.size()>0 && s.length()>0){
                        for(String word : history){
                            if(word.contains(s)){
                                match.add(word);
                            }
                        }
                        ArrayList<String> list = new ArrayList<String>(match);

                        adapter = new SearchArrayAdapter(MainActivity.this,
                                R.layout.list_view_item, list);
                        listView.setAdapter(adapter);
                        listView.setVisibility(View.VISIBLE);
                    }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String searchItem = (String)parent.getItemAtPosition(position);
                Intent search=new Intent(MainActivity.this,WebViewClass.class);
                search.putExtra("url","http://www.google.com/#q="+searchItem);
                startActivity(search);
            }
        });

        facebook.setOnClickListener(this);
        twitter.setOnClickListener(this);
        skype.setOnClickListener(this);
        linkedin.setOnClickListener(this);
        fb_lite.setOnClickListener(this);
        google.setOnClickListener(this);
        youtube.setOnClickListener(this);
        gmail.setOnClickListener(this);
        yahoo.setOnClickListener(this);
        insta.setOnClickListener(this);
        telegram.setOnClickListener(this);
        flipkart.setOnClickListener(this);
        amazon.setOnClickListener(this);
        ebay.setOnClickListener(this);
        paytm.setOnClickListener(this);
    }


/*
    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
*/


    @Override
    public void onClick(View v) {
         switch (v.getId()) {
             case R.id.facebook: callurl(url_facebook);break;
             case R.id.google: callurl(url_google);break;
             case R.id.twitter: callurl(url_twitter);break;
             case R.id.skype: callurl(url_skype);break;
             case R.id.Linkedin: callurl(url_linkedin);break;
             case R.id.fb_lite: callurl(url_fblite);break;
             case R.id.youtube: callurl(url_youtube);break;
             case R.id.gmail: callurl(url_gmail);break;
             case R.id.yahoo: callurl(url_yahoo);break;
             case R.id.instagram: callurl(url_insta);break;
             case R.id.telegram: callurl(url_tele);break;
             case R.id.flipkart: callurl(url_flipkart);break;
             case R.id.amazon: callurl(url_amazon_in);break;
             case R.id.ebay: callurl(url_ebay);break;
             case R.id.paytm: callurl(url_paytm);break;


        }
    }

    private void callurl(String url) {
        Intent search=new Intent(MainActivity.this,WebViewClass.class);
        search.putExtra("url",url);
        startActivity(search);
    }

    private class SearchArrayAdapter extends ArrayAdapter<String>{

        ArrayList<String> newArraay = new ArrayList<String>();
        Context context;
        SearchArrayAdapter(Context context,int listviewitem,ArrayList<String> hist){
            super(context,listviewitem,hist);
            newArraay=hist;
            this.context=context;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater =  getLayoutInflater();
             convertView =inflater.inflate(R.layout.list_view_item, null);
            TextView textView = (TextView) convertView.findViewById(R.id.history_search);
           // ImageView imageView = (ImageView) convertView.findViewById(R.id.icon);
            textView.setText(newArraay.get(position));


            return convertView;
        }
    }

    public void searchAdd(){

        if(!DetectConnection.checkInternetConnection(this)){
            Toast.makeText(getApplicationContext(), "No Internet Connection!", Toast.LENGTH_LONG).show();
        }
        else {
            Intent search=new Intent(MainActivity.this,WebViewClass.class);
            history.add(keyword.getText().toString());
            sharedPreferences.edit().remove("history").putStringSet("history",history).commit();
            search.putExtra("url","http://www.google.com/#q="+keyword.getText());
            startActivity(search);
        }

    }
}