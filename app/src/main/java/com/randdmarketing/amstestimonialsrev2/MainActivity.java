package com.randdmarketing.amstestimonialsrev2;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.app.Activity;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Environment;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.VideoView;
import android.view.View.OnClickListener;


public class MainActivity extends Activity implements OnClickListener{

    //Capture User Inputs
    EditText nameInput;
    EditText emailInput;
    EditText testimonyInput;


    // Activity request codes
    private static final int CAMERA_CAPTURE_IMAGE_REQUEST_CODE = 100;
    private static final int CAMERA_CAPTURE_VIDEO_REQUEST_CODE = 200;
    public static final int MEDIA_TYPE_IMAGE = 1;
    public static final int MEDIA_TYPE_VIDEO = 2;

    // directory name to store captured images and videos
    public static final String IMAGE_DIRECTORY_NAME = "AMS Testimonial";

    private Uri fileUri; // file url to store image/video

    private ImageView imgPreview;
    private VideoView videoPreview;
    private Button btnCapturePicture, btnRecordVideo, btnSubmitFile;

    private final static String storeText = "testimony.txt";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        imgPreview = (ImageView) findViewById(R.id.imgPreview);
        videoPreview = (VideoView) findViewById(R.id.videoPreview);
        btnCapturePicture = (Button) findViewById(R.id.btnCapturePicture);
        btnRecordVideo = (Button) findViewById(R.id.btnRecordVideo);
        btnSubmitFile = (Button) findViewById(R.id.btnSubmitFile);

        //////////////// Create folder under internal root to store files ///////////
        File storagePath = new File(Environment.getExternalStorageDirectory()+ "/AMS Testimonial");
        if (!storagePath.exists()) {
            storagePath.mkdirs();
        }


        //////////////   Start of User info capture  /////////////////////
        nameInput = (EditText) findViewById(R.id.nameInput);
        emailInput = (EditText) findViewById(R.id.emailInput);
        testimonyInput = (EditText) findViewById(R.id.testimonyInput);
        //////////////   End of User info capture  /////////////////////


        findViewById(R.id.btnSubmitFile).setOnClickListener(new OnClickListener() {


            @Override
            public void onClick(View arg0) {

                final String name = nameInput.getText().toString();
                if (!isValidName(name)) {
                    nameInput.setError("Name Must Be Entered!");
                }

                final String email = emailInput.getText().toString();
                if (!isValidEmail(email)) {
                    emailInput.setError("Invalid Email!");
                }

                ////////////// Begin file capture after validation /////////////////////
                if (isValidName(name) && (isValidEmail(email))) {

                    String userFileData = nameInput.getText().toString() + "\r\n" + emailInput.getText().toString() + "\r\n" + testimonyInput.getText().toString();
                    FileOutputStream fOut = null;
                    //Since you are creating a subdirectory, you need to make sure it's there first

                    File directory = new File( Environment.getExternalStorageDirectory()+ "/AMS Testimonial/", nameInput.getText().toString());
                    if (!directory.exists()) {
                        directory.mkdirs();
                    }

                    try {
                        //Create the stream pointing at the file location
                        fOut = new FileOutputStream(new File(directory, "Testimony.txt"));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    OutputStreamWriter osw = new OutputStreamWriter(fOut);
                    try {
                        osw.write(userFileData);

                        osw.flush();
                        osw.close();

                        Toast.makeText(getBaseContext(), "Thank you...Testimony saved successfully!",
                                Toast.LENGTH_LONG).show();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ///////////////// End file capture ////////////////////////
                    Intent restartIntent = getIntent();
                    finish();
                    startActivity(restartIntent);
                }
            }
        });

        CheckBox cb = (CheckBox) findViewById(R.id.amsCheckBox);

        cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                TextView tosAgreement = (TextView) findViewById(R.id.tosAgreement);
                if (isChecked) {
                    btnSubmitFile.setVisibility(View.VISIBLE);
                    tosAgreement.setVisibility(View.INVISIBLE);
                }
                if (!isChecked) {
                    btnSubmitFile.setVisibility(View.INVISIBLE);
                    tosAgreement.setVisibility(View.VISIBLE);
                }
            }
        });

        /**
         * Capture image button click event
         */
        btnCapturePicture.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // capture picture
                captureImage();
            }
        });

        /**
         * Record video button click event
         */
        btnRecordVideo.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // record video
                recordVideo();
            }
        });

        // Checking camera availability
        if (!isDeviceSupportCamera()) {
            Toast.makeText(getApplicationContext(),
                    "Sorry! Your device doesn't support camera",
                    Toast.LENGTH_LONG).show();
            // will close the app if the device doesn't have camera
            finish();
        }
    }

    /**
     * Checking device has camera hardware or not
     * */
    private boolean isDeviceSupportCamera() {
        if (getApplicationContext().getPackageManager().hasSystemFeature(
                PackageManager.FEATURE_CAMERA_ANY)) {
            // this device has a camera
            return true;
        } else {
            // no camera on this device
            return false;
        }
    }

    /**
     * Capturing Camera Image will launch camera app request image capture
     */
    private void captureImage() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri);

        // start the image capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_IMAGE_REQUEST_CODE);
    }

    /**
     * Here we store the file url as it will be null after returning from camera
     * app
     */
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        // save file url in bundle as it will be null on screen orientation
        // changes
        outState.putParcelable("file_uri", fileUri);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);

        // get the file url
        fileUri = savedInstanceState.getParcelable("file_uri");
    }

    /**
     * Recording video
     */
    private void recordVideo() {
        Intent intent = new Intent(MediaStore.ACTION_VIDEO_CAPTURE);

        fileUri = getOutputMediaFileUri(MEDIA_TYPE_VIDEO);

        // set video quality
        intent.putExtra(MediaStore.EXTRA_VIDEO_QUALITY, 1);

        intent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
        // name

        // start the video capture Intent
        startActivityForResult(intent, CAMERA_CAPTURE_VIDEO_REQUEST_CODE);
    }

    /**
     * Receiving activity result method will be called after closing the camera
     * */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // if the result is capturing Image
        if (requestCode == CAMERA_CAPTURE_IMAGE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // successfully captured the image
                // display it in image view
                previewCapturedImage();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled Image capture
                Toast.makeText(getApplicationContext(),
                        "User cancelled image capture", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to capture image
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to capture image", Toast.LENGTH_SHORT)
                        .show();
            }
        } else if (requestCode == CAMERA_CAPTURE_VIDEO_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                // video successfully recorded
                // preview the recorded video
                previewVideo();
            } else if (resultCode == RESULT_CANCELED) {
                // user cancelled recording
                Toast.makeText(getApplicationContext(),
                        "User cancelled video recording", Toast.LENGTH_SHORT)
                        .show();
            } else {
                // failed to record video
                Toast.makeText(getApplicationContext(),
                        "Sorry! Failed to record video", Toast.LENGTH_SHORT)
                        .show();
            }
        }
    }

    /**
     * Display image from a path to ImageView
     */
    private void previewCapturedImage() {
        try {
            // hide video preview
            videoPreview.setVisibility(View.VISIBLE);

            imgPreview.setVisibility(View.VISIBLE);

            // bitmap factory
            BitmapFactory.Options options = new BitmapFactory.Options();

            // downsizing image as it throws OutOfMemory Exception for larger
            // images
            options.inSampleSize = 8;

            final Bitmap bitmap = BitmapFactory.decodeFile(fileUri.getPath(),
                    options);

            imgPreview.setImageBitmap(bitmap);
        } catch (NullPointerException e) {
            e.printStackTrace();
        }
    }

    /**
     * Previewing recorded video
     */
    private void previewVideo() {
        try {
            // hide image preview
            imgPreview.setVisibility(View.VISIBLE);

            videoPreview.setVisibility(View.VISIBLE);
            videoPreview.setVideoPath(fileUri.getPath());
            // start playing
            videoPreview.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private boolean isValidEmail(String email) {
        String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isValidName(String name) {
        String NAME = "^[a-zA-Z\\s]+$";

        Pattern pattern = Pattern.compile(NAME,Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(name);
        return matcher.matches();
    }

    /**
     * ------------ Helper Methods ----------------------
     * */

    /**
     * Creating file uri to store image/video
     */
    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    /**
     * returning image / video
     */
    private File getOutputMediaFile(int type) {

        // External sdcard location
        File mediaStorageDir = new File(
                Environment.getExternalStorageDirectory()+ "/AMS Testimonial/" + nameInput.getText().toString());

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(IMAGE_DIRECTORY_NAME, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "IMG_" + timeStamp + ".jpg");
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator
                    + "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
    }

    public abstract class TextValidator implements TextWatcher {
        private final EditText editText;

        public TextValidator(EditText editText) {
            this.editText = editText;
        }

        public abstract void validate(EditText editText, String text);

        @Override
        final public void afterTextChanged(Editable s) {
            String text = editText.getText().toString();
            validate(editText, text);
        }

        @Override
        final public void beforeTextChanged(CharSequence s, int start, int count, int after) { /* Don't care */ }

        @Override
        final public void onTextChanged(CharSequence s, int start, int before, int count) { /* Don't care */ }
    }
    public void onClick(View v){
        Intent intent = new Intent(this, ImageLikeness.class);
        startActivity(intent);

    }

}