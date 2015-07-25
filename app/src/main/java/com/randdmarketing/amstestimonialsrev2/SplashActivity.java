package com.randdmarketing.amstestimonialsrev2;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import android.widget.TextView;


public class SplashActivity extends Activity {

    Button wifiLogo;
    Button enterBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash);

        wifiLogo = (Button) findViewById(R.id.wifiLogo);
        enterBtn = (Button) findViewById(R.id.enterBtn);
        enterBtn.setVisibility(View.INVISIBLE);
        wifiLogo.setVisibility(View.INVISIBLE);

        ConnectivityManager connManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo userWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);

        // If WiFi is turned on and is connected to Network...erythang all good
        if (userWifi.isConnected()) {
            TextView wifiMsg = (TextView) findViewById(R.id.wifiMsg);
            wifiMsg.setText("WIFI ENABLED AND CURRENTLY ONLINE");
            Intent i = new Intent(SplashActivity.this, MainActivity.class);
            finish();
            startActivity(i);

        }

        // If WiFi is not turned on... turn it on
        if (wifi.setWifiEnabled(false)) {
            //-Toast.makeText(getBaseContext(), "Please make sure you are on WI-FI", Toast.LENGTH_LONG).show();
            TextView wifiMsg = (TextView) findViewById(R.id.wifiMsg);
            wifiMsg.setText("WIFI IS NOT ENABLED \r\n Click above to turn on Wi-Fi");
            wifiLogo.setVisibility(View.VISIBLE);
            enterBtn.setVisibility(View.VISIBLE);

            findViewById(R.id.wifiLogo).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    // Call openWifi method defined below
                    openWifi();
                }
            });
        }

        // If WiFi is turned on but is not connected to Network...press to open Available Networks and return back
        if (wifi.setWifiEnabled(true) && (userWifi.getState()) == NetworkInfo.State.DISCONNECTED) {
            TextView wifiMsg = (TextView) findViewById(R.id.wifiMsg);
            wifiMsg.setText("WIFI ENABLED BUT NOT ONLINE \r\n Click above to open WiFi Network Availability \r\n Or click below to Continue \r\n Data charges may apply when Submitting Testimony");

            findViewById(R.id.wifiLogo).setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    Intent openWiFiintent = (new Intent(Settings.ACTION_WIFI_SETTINGS));
                    startActivityForResult(openWiFiintent, 0);
                    Toast.makeText(getBaseContext(), "Please connect to a hotspot",
                            Toast.LENGTH_LONG).show();
                }

            });

            // if they don't care about wifi data
            findViewById(R.id.enterBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent i = new Intent(SplashActivity.this, MainActivity.class);
                    // finish(); ...turned it off just in case they hit back button and want to connect to wifi
                    startActivity(i);

                }
            });
        }
    }

    // Open wifi method called from above
    private void openWifi() {

        WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
        wifi.setWifiEnabled(true);

    }

    // Return from setting network and refresh Activity
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        Intent splashIntent = getIntent();
        finish();
        startActivity(splashIntent);

    }
}
