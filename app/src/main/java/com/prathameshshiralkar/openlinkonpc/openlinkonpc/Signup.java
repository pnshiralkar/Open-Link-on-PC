package com.prathameshshiralkar.openlinkonpc.openlinkonpc;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Signup extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.layout_to_custom_button);
        actionBar.show();



        setContentView(R.layout.activity_signup);

        Button createacc = findViewById(R.id.createacc);
        createacc.setEnabled(false);
    }

    public void createacc(View v)
    {
        EditText name = findViewById(R.id.et_name);
        EditText email = findViewById(R.id.et_email);
        EditText pwd = findViewById(R.id.et_pwd);
        EditText pwdc = findViewById(R.id.et_pwdc);
        CheckBox tncbox = findViewById(R.id.tncbox);

        Boolean flag=true;

        if(!tncbox.isChecked())
        {
            Toast.makeText(getApplicationContext(), "You need to agree to our terms and conditions!", Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(!pwd.getText().toString().equals(pwdc.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT).show();
            flag=false;
        }

        if(name.getText().toString().equals("") || email.getText().toString().equals("") || pwd.getText().toString().equals("") || pwdc.getText().toString().equals(""))
        {
            Toast.makeText(getApplicationContext(), "Please fill out all the fields!", Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(!isEmailValid(email.getText().toString()))
        {
            Toast.makeText(getApplicationContext(), "Please enter a valid email address", Toast.LENGTH_LONG).show();
            flag=false;
        }

        if(flag)
        {
            /*////////////////mySQL method///////////
            String res=null;
            InternetConnect internetConnect = new InternetConnect(this);
            try {
                res = internetConnect.execute("3", "http://linkonpc.onlinewebshop.net/signup_app.php", "name", name.getText().toString(), "email", email.getText().toString(), "pwd", pwd.getText().toString()).get().toString();
            } catch (ExecutionException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error#1103", Toast.LENGTH_SHORT).show();
            } catch (InterruptedException e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Error#1104", Toast.LENGTH_SHORT).show();
            }

            if(res == "update")
            {
                AlertDialog alertDialog = new AlertDialog.Builder(getApplicationContext()).create();
                alertDialog.setTitle("Major Update Missing!");
                alertDialog.setMessage("Please update your app to the latest version to continue!");
            }

            Toast.makeText(getApplicationContext(), res, Toast.LENGTH_LONG).show();
            email.setText(res);
            if(res.equals("Account created successfully!"))
            {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
            }
            ////////////////////////////////////*/


            /////////////Firebase////////////////
            final String sname = name.getText().toString();
            String semail = email.getText().toString();
            String spwd = pwd.getText().toString();

            
        }
    }

    public void gotoabout(View v)
    {
        Intent intent = new Intent(getBaseContext(), About.class);
        startActivity(intent);
    }

    public void tncf(View v)
    {
            Uri uri = Uri.parse("http://linkonpc.onlinewebshop.net/termsandconditions.html"); // missing 'http://' will cause crashed
            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
            startActivity(intent);
    }

    public void ppf(View v)
    {
        Uri uri = Uri.parse("http://linkonpc.onlinewebshop.net/privacypolicy.html"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void tncboxf(View v)
    {
        CheckBox tncbox = findViewById(R.id.tncbox);
        Button createacc = findViewById(R.id.createacc);
        if(tncbox.isChecked())
        {
            createacc.setEnabled(true);
        }else
        {
            createacc.setEnabled(false);
        }
    }

    public static boolean isEmailValid(String email) {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
