package com.prathameshshiralkar.openlinkonpc.openlinkonpc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

public class Login extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.layout_to_custom_button);
        actionBar.show();

        setContentView(R.layout.activity_login);
        TextView tv = (TextView) findViewById(R.id.tv_login);
        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);


        tv.setTextSize(tv.getTextSize());
        TextView email_tv = (TextView) findViewById(R.id.et_email);
        email_tv.setFocusable(true);
        email_tv.requestFocus();

        load_anim.setVisibility(View.INVISIBLE);
        white.setVisibility(View.INVISIBLE);
    }

    public void onlogin(View v)
    {
        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);
        white.setVisibility(View.VISIBLE);
        load_anim.setVisibility(View.VISIBLE);
        new android.os.Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                onLogin();
            }
        }, 1);
    }

    public void onLogin()
    {
        TextView email_tv = (TextView) findViewById(R.id.et_email);
        ProgressBar load_anim = findViewById(R.id.loading);
        ImageView white = findViewById(R.id.white);
        String email = email_tv.getText().toString();
        TextView pwd_tv = (TextView) findViewById(R.id.et_pwd);
        String pwd = pwd_tv.getText().toString();
        InternetConnect addlink = new InternetConnect(this);

        String res=null;
        try {
            res = addlink.execute("2", "http://linkonpc.onlinewebshop.net/login_app.php", "email", email,"pwd", pwd).get().toString();
        } catch (ExecutionException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error#1103", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return;
        } catch (InterruptedException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(), "Error#1104", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return;
        }
        if(res.equals("Error"))
        {
            Toast.makeText(getApplicationContext(), "Unable to connect to server!", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
            return;
        }

        if(res == "update")
        {
            AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
            alertDialog.setTitle("Major Update Missing!");
            alertDialog.setMessage("Please update your app to the latest version to continue!");
        }

        String result[] = res.split(",", 2);
        Integer uid=0;
        if(result[0] !=null)
        {
            uid = Integer.parseInt(result[0]);
        }
        if(uid == 0)
        {
            Toast.makeText(getApplicationContext(), "Invalid Login Credentials", Toast.LENGTH_SHORT).show();
            load_anim.setVisibility(View.INVISIBLE);
            white.setVisibility(View.INVISIBLE);
        }
        else if(uid > 0)
        {
            Toast.makeText(getApplicationContext(), ("Welcome " + email), Toast.LENGTH_SHORT).show();
            SharedPreferences sp=getSharedPreferences("Login", MODE_PRIVATE);
            SharedPreferences.Editor Ed=sp.edit();
            Ed.putString("uid",uid.toString() );
            Ed.putString("pwd",pwd);
            Ed.putString("name",result[1]);
            Ed.apply();
            Intent intent = new Intent(getBaseContext(), MainActivity.class);
            startActivity(intent);


        }
    }

    public void gotoabout(View v)
    {
        Intent intent = new Intent(getBaseContext(), About.class);
        startActivity(intent);
    }

    public void gotosignup(View v)
    {
        Intent intent = new Intent(getBaseContext(), Signup.class);
        startActivity(intent);
    }
}
