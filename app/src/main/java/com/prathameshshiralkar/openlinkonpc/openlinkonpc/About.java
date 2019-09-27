package com.prathameshshiralkar.openlinkonpc.openlinkonpc;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;

public class About extends AppCompatActivity {


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);



    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setCustomView(R.layout.layout_to_custom_button);
        actionBar.setDisplayHomeAsUpEnabled(true);
        findViewById(R.id.btn_about).setVisibility(View.INVISIBLE);
        actionBar.show();

        setContentView(R.layout.activity_about);
        }

    public void gotoinsta(View v)
    {
        Uri uri = Uri.parse("https://www.instagram.com/pratham__99/"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }

    public void gotofb(View v)
    {
        Uri uri = Uri.parse("https://www.facebook.com/prathamesh.shiralkar"); // missing 'http://' will cause crashed
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        startActivity(intent);
    }


}
