package com.prathameshshiralkar.openlinkonpc.openlinkonpc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {

    public AdView mAdView;
    public InterstitialAd mInterstitialAd;
    public int flag_ads = 0;

    public static String getDeviceId() {
        return deviceId;
    }

    private static String deviceId;



    public String MD5(String md5) {
        try {
            java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
            byte[] array = md.digest(md5.getBytes());
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < array.length; ++i) {
                sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1,3));
            }
            return sb.toString();
        } catch (java.security.NoSuchAlgorithmException e) {
        }
        return null;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.layout_to_custom_button);
        actionBar.show();

        TextView lnk_display = (TextView) findViewById(R.id.lnk);
        TextView t1 = (TextView) findViewById(R.id.user_info);
        TextView t2 = (TextView) findViewById(R.id.rl);
        TextView h1 = (TextView) findViewById(R.id.hide1);
        TextView h2 = (TextView) findViewById(R.id.hide2);
        EditText h3 = (EditText) findViewById(R.id.openlink);
        ImageView h4 = (ImageView) findViewById(R.id.btn_ol);
        ImageView ar2 = findViewById(R.id.arrow_down);
        TextView h5 = (TextView) findViewById(R.id.hide3);
        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);

        Button btn_logout = findViewById(R.id.btn_logout);
        Button btn_login = findViewById(R.id.btn_login);

        h2.setVisibility(View.INVISIBLE);
        h5.setVisibility(View.INVISIBLE);
        ar2.setVisibility(View.INVISIBLE);
        load_anim.setVisibility(View.INVISIBLE);
        white.setVisibility(View.INVISIBLE);

        //uid and password
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String uid = sp1.getString("uid", null);
        String pwd = sp1.getString("pwd", null);
        String name = sp1.getString("name", null);

        if(uid == null || pwd == null || name == null)
        {
            t1.setText("Please Login first!");
            t2.setVisibility(View.INVISIBLE);
            btn_logout.setVisibility(View.INVISIBLE);
            h1.setVisibility(View.INVISIBLE);
            h2.setVisibility(View.INVISIBLE);
            h3.setVisibility(View.INVISIBLE);
            h4.setVisibility(View.INVISIBLE);

            Intent intent = new Intent(getBaseContext(), Login.class);
            startActivity(intent);
            Toast.makeText(getApplicationContext(), "Please Login first!", Toast.LENGTH_LONG).show();
        }
        else {
            t1.setText("Welcome " + name);
            btn_login.setVisibility(View.INVISIBLE);


            //Applink
            Intent appLinkIntent = getIntent();
            String appLinkAction = appLinkIntent.getAction();
            Uri appLinkData = appLinkIntent.getData();
            if (appLinkData != null) {
                    String applinkDatastr = appLinkData.toString();
                    lnk_display.setText(applinkDatastr);
                    onpreshare(applinkDatastr, uid, pwd);

            }

            ///ads///


            mAdView = (AdView) findViewById(R.id.adView);

            AdRequest adRequest = new AdRequest.Builder().build(); //.addTestDevice("B6E0E8263C1377346479B0D220194235")
            if(mAdView != null) {
                mAdView.loadAd(adRequest);
            }
            else {
                Toast.makeText(getApplicationContext(), "Null Adview! Make sure content view is set!" + deviceId, Toast.LENGTH_SHORT).show();
            }

            mAdView.setAdListener(new AdListener() {
                @Override
                public void onAdOpened() {
                    flag_ads = 1;
                }
            });

        }

    }

    public String last_link = null;



    @Override
    public void onResume()
    {
        //uid and password
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String uid = sp1.getString("uid", null);
        String pwd = sp1.getString("pwd", null);

        super.onResume();
        //share with
        Intent intent = getIntent();
        String action = intent.getAction();
        String type = intent.getType();

        if (Intent.ACTION_SEND.equals(action) && type != null) {
            String sharedText = intent.getStringExtra(Intent.EXTRA_TEXT);
            if (sharedText != null && !sharedText.equals(last_link)) {
                last_link = sharedText;
                onpreshare(sharedText, uid, pwd);
            }

        }
    }

    public boolean bool = false;

    public String links = "";

    public void onpreshare(String link, String uid, String pwd)
    {
        final String link_f = link;
        final String uid_f = uid;
        final String pwd_f = pwd;

        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);
        white.setVisibility(View.VISIBLE);
        load_anim.setVisibility(View.VISIBLE);

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onshare(link_f, uid_f, pwd_f);
            }
        }, 200);
    }

    public int onshare(String link, String uid, String pwd) {

        InternetConnect addlink = new InternetConnect(this);
        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);
        String result = null;
        try {
            result = addlink.execute("3", "http://linkonpc.onlinewebshop.net/addlink.php", "uid", uid,"pwd", pwd, "link", link).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error#1103", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return 0;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error#1104", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return 0;
        }
        Integer res=0;
        if(result == "Error")
        {
            Toast.makeText(getApplicationContext(), "Unable to connect to server!", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return 0;
        }

        if(result == "update")
        {
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Major Update Missing!");
            alertDialog.setMessage("Please update your app to the latest version to continue!");
        }

        if(result!=null){
            try{
                res = Integer.parseInt(result);
            }
        catch (NumberFormatException nfe)
        {
            Toast.makeText(getApplicationContext(), "Server error!", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return 0;
        }
        }

        if(res == 0){Toast.makeText(getApplicationContext(), "Authentication failed!\nTry logging in again", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);}
        else if(res > 0){
            TextView lnk_txt = (TextView) findViewById(R.id.lnk);
            links = "\n" + link;
            //lnk_txt.append(links);

            interstitialad();
            Toast.makeText(getApplicationContext(), "Sent link!", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);



            final Integer res_f = res;
            final String uid_f = uid;

            chk_status(res_f, uid_f, 0);



        }
        return 0;
    }
    public Boolean flag=true;
    public void chk_status(Integer linkid, String uid, Integer attempts) {
        if (attempts <= 10) {
            attempts++;
            try {
                InternetConnect addlink = new InternetConnect(this);
                String res1 = addlink.execute("2", "http://linkonpc.onlinewebshop.net/status.php", "linkid", (linkid.toString()), "uid", uid).get().toString();

                flag = true;

                if (res1.equals("0")) {
                    flag = true;
                } else {
                    Toast.makeText(getApplicationContext(), res1, Toast.LENGTH_LONG).show();
                    flag = false;
                }
            } catch (ExecutionException e) {

                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error#1103", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error#1104", Toast.LENGTH_SHORT).show();
            }
            final Integer res_f = linkid;
            final String uid_f = uid;
            final Integer attempts_f = attempts;
            if (flag) {
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        chk_status(res_f, uid_f, attempts_f);
                    }
                }, 3000);
            }
        }
    }

    public void gotologin(View v)
    {
        Intent intent = new Intent(getBaseContext(), Login.class);
        startActivity(intent);
        finish();
    }


    public void logout(View v)
    {
        SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
        SharedPreferences.Editor Ed=sp.edit();
        Ed.clear();
        Ed.apply();
        Intent intent = new Intent(getBaseContext(), MainActivity.class);
        startActivity(intent);
    }

    public void openlnk(View v)
    {
        SharedPreferences sp1=this.getSharedPreferences("Login", MODE_PRIVATE);
        String uid = sp1.getString("uid", null);
        String pwd = sp1.getString("pwd", null);

        TextView tv_lnk = (TextView) findViewById(R.id.openlink);
        String link = tv_lnk.getText().toString();
        if(link.indexOf("http") == 0) {
            onpreshare(link, uid, pwd);
        }
        else {
            Toast.makeText(getApplicationContext(), "Invalid Link!\nPlease enter link starting with http or https", Toast.LENGTH_LONG).show();
        }
        tv_lnk.setText("");

    }

    public void gotoabout(View v)
    {
        Intent intent = new Intent(getBaseContext(), About.class);
        startActivity(intent);
    }
    public int howto_flag = 0;
    public void howto(View v)
    {
        TextView h1 = (TextView) findViewById(R.id.hide1);
        TextView h2 = (TextView) findViewById(R.id.hide2);
        TextView h3 = (TextView) findViewById(R.id.hide3);
        TextView h4 = (TextView) findViewById(R.id.openlink);
        ImageView ar1 = findViewById(R.id.arrow_up);
        ImageView ar2 = findViewById(R.id.arrow_down);
        if(howto_flag == 0)
        {
            h1.setY(h3.getY() - 80);
            ar2.setY(h3.getY() - 110);
            h2.setVisibility(View.VISIBLE);
            h3.setVisibility(View.VISIBLE);
            ar2.setVisibility(View.VISIBLE);
            ar1.setVisibility(View.INVISIBLE);
            howto_flag = 1;
        }
        else
        {
            h1.setY(h4.getY() - 80);
            ar2.setY(h4.getY() - 110);
            h2.setVisibility(View.INVISIBLE);
            h3.setVisibility(View.INVISIBLE);
            ar2.setVisibility(View.INVISIBLE);
            ar1.setVisibility(View.VISIBLE);
            howto_flag = 0;
        }
    }

    public void interstitialad()
    {
        mInterstitialAd = new InterstitialAd(getApplicationContext());
        mInterstitialAd.setAdUnitId("ca-app-pub-5644373101085191/7000169887");  //My - ca-app-pub-5644373101085191/7000169887  //Sample - ca-app-pub-3940256099942544/1033173712
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mInterstitialAd.isLoaded()) {
                    mInterstitialAd.show();
                }
                else {
                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (mInterstitialAd.isLoaded()) {
                                mInterstitialAd.show();
                            }
                            else {
                                interstitialad();
                                //Toast.makeText(getApplicationContext(), "Error#1105", Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 5000);
                    //Toast.makeText(getApplicationContext(), "Error#1105", Toast.LENGTH_LONG).show();
                }
            }
        }, 5000);
    }

    int back=0;
    @Override
    public void onBackPressed()
    {
        if(back == 0)
        {
            Toast.makeText(getApplicationContext(), "Press again to exit", Toast.LENGTH_SHORT).show();
            back = 1;
        }
        else {
            finish();
        }
    }

}







