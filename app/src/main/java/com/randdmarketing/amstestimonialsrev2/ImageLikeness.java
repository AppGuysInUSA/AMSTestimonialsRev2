package com.randdmarketing.amstestimonialsrev2;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class ImageLikeness extends Activity {

    Button btnBackToMain;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_likeness);

        ///////////////////////////////// Start of Test //////////////////////////////
        //Test user name input being passed to this activity
        /*
        Bundle userData = getIntent().getExtras();
        if (userData == null) {
            return;
        }

        String userName = userData.getString("userName");
        final TextView showName = (TextView) findViewById(R.id.showName);
        showName.setText(userName);
        */
        ///////////////////////////////// End of Test //////////////////////////////

        // Locate the button in image_likeness.xml
        btnBackToMain = (Button) findViewById(R.id.btnBack);
    }

        public void onClick (View view) {
            Intent backBtnIntent = new Intent(this, MainActivity.class);
            startActivity(backBtnIntent);

    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_image_likeness, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
