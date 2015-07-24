package com.randdmarketing.amstestimonialsrev2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;


public class SplashActivity extends Activity {

    Button wifiLogo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        wifiLogo = (Button) findViewById(R.id.wifiLogo);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo userWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (userWifi.isConnected()) {
            TextView wifiMsg = (TextView)findViewById(R.id.wifiMsg);
            wifiMsg.setText("WIFI ENABLED AND CURRENTLY ONLINE");
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(i);

        } else {

            Toast.makeText(getBaseContext(), "Please make sure you are on WI-FI", Toast.LENGTH_LONG).show();
            TextView wifiMsg = (TextView)findViewById(R.id.wifiMsg);
            wifiMsg.setText("WIFI IS NOT ENABLED \r\n OR NOT ONLINE \r\n\n If not on WiFi Network \r\n Data Charges may incur when submitting.");

        }

        findViewById(R.id.wifiLogo).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                openWifi();

            }
        });

    }

    private void openWifi() {

        Intent intent = new Intent(Settings.ACTION_WIFI_SETTINGS);
        finish();
        startActivity(intent);

    }

}
