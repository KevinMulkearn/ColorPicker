package com.mulkearn.kevin.colorpicker;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NavUtils;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ImageActivity extends AppCompatActivity {

    ImageView imageView;
    TextView hexText, rgbText, hsvText, colorDisplay;
    Button captureButton, searchButton;

    DatabaseHelper mDatabaseHelper;

    public static final int RequestPermissionCode = 1;
    private int REQUEST_CODE = 0;
    Uri uri = null, photoURI = null;
    Bitmap imageBitmap = null, touchBitmap = null;
    String mCurrentPhotoPath;
    boolean imageOpened = false;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image);

        imageView = findViewById(R.id.imageView);
        hexText = findViewById(R.id.hexText);
        rgbText = findViewById(R.id.rgbText);
        hsvText = findViewById(R.id.hsvText);
        colorDisplay = findViewById(R.id.colorDisplay);
        captureButton = findViewById(R.id.captureButton);
        searchButton = findViewById(R.id.searchButton);

        mDatabaseHelper = new DatabaseHelper(this);

        imageView.setDrawingCacheEnabled(true);
        imageView.buildDrawingCache();
        touchBitmap = imageView.getDrawingCache();

        EnableRuntimePermission();

        imageView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                float x = event.getX();
                float y = event.getY();
                int pixel = 16771304;
                if(touchBitmap != null){
                    pixel = touchBitmap.getPixel((int) x, (int) y);
                }

                int redValue = Color.red(pixel);
                int blueValue = Color.blue(pixel);
                int greenValue = Color.green(pixel);

                hexText.setText("Hex: " + rgbToHex(redValue, greenValue, blueValue));
                rgbText.setText("rgb(" + redValue + ", " + greenValue + ", " + blueValue + ")");
                hsvText.setText(getHSVValue(redValue, greenValue, blueValue));
                colorDisplay.setBackgroundColor(pixel);

                return false;
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_image, menu);
        if (!imageOpened){
            menu.getItem(0).setVisible(false);
            menu.getItem(1).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
            menu.getItem(1).setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.rotate:
                rotateImage();
                return true;
            case R.id.save:
                String saveValue = hexText.getText().toString();
                saveValue = saveValue.substring(saveValue.indexOf("#"));
                AddData(saveValue);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addColor(newEntry);
        if (insertData) {
            Toast.makeText(ImageActivity.this, newEntry + " " + getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(ImageActivity.this, "Error Saving Data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void onCaptureClick(View view) {
        imageOpened = true;
        REQUEST_CODE = 2;
        Intent i_capture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (i_capture.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(ImageActivity.this, getString(R.string.error_saving), Toast.LENGTH_LONG).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                photoURI = FileProvider.getUriForFile(this,
                        "com.mulkearn.kevin.rgbfileprovider", photoFile);
                i_capture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                uri = photoURI;
                startActivityForResult(i_capture, REQUEST_CODE);
            }
        }
    }

    public void onOpenClick(View view) {
        imageOpened = true;
        REQUEST_CODE = 1;
        Intent i_open = new Intent();
        i_open.setType("image/*");
        i_open.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(i_open, "Select Image"), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 1 && resultCode == Activity.RESULT_OK && data != null && data.getData() != null){
            uri = data.getData();
            try{
                imageBitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), uri);
                imageView.setImageBitmap(imageBitmap);
                touchBitmap = imageView.getDrawingCache();
            } catch (IOException e) {
                Toast.makeText(ImageActivity.this, "Unexpected Error", Toast.LENGTH_LONG).show();
            }
        } else if (requestCode == 2 && resultCode == Activity.RESULT_OK) {
            setPic();
            touchBitmap = imageView.getDrawingCache();
            galleryAddPic();
        }
        invalidateOptionsMenu();//Recall menu create function
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "PC_" + timeStamp;
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);

        File image = File.createTempFile(
                imageFileName, ".jpg", storageDir); // prefix, suffix, directory

        mCurrentPhotoPath = image.getAbsolutePath();

        return image;
    }

    private void setPic() {
        // Get the dimensions of the View
        int targetW = imageView.getWidth();
        int targetH = imageView.getHeight();

        // Get the dimensions of the bitmap
        BitmapFactory.Options bmOptions = new BitmapFactory.Options();
        bmOptions.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        int photoW = bmOptions.outWidth;
        int photoH = bmOptions.outHeight;

        // Determine how much to scale down the image
        int scaleFactor = Math.min(photoW/targetW, photoH/targetH);

        // Decode the image file into a Bitmap sized to fill the View
        bmOptions.inJustDecodeBounds = false;
        bmOptions.inSampleSize = scaleFactor;
        bmOptions.inPurgeable = true;

        imageBitmap = BitmapFactory.decodeFile(mCurrentPhotoPath, bmOptions);
        imageBitmap = RotateBitmap(imageBitmap, 90f);
        imageView.setImageBitmap(imageBitmap);
    }

    private void galleryAddPic() {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(mCurrentPhotoPath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        this.sendBroadcast(mediaScanIntent);
    }

    public String rgbToHex(int r, int g, int b){
        return String.format("#%02X%02X%02X",r, g, b);
    }

    public String getHSVValue(int r, int g, int b){
        float[] hsv = new float[3];
        Color.RGBToHSV(r, g, b, hsv);
        float h = hsv[0];
        float s = hsv[1] * 100;
        float v = hsv[2] * 100;
        String hue = Math.round(h) + "\u00b0";
        String sat = Math.round(s) + "%";
        String val = Math.round(v) + "%";

        return "hsv(" + hue + ", " + sat + ", " + val + ")";
    }

    public static Bitmap RotateBitmap(Bitmap source, float angle){
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public void rotateImage() {
        if(imageBitmap != null && touchBitmap != null){
            imageBitmap = RotateBitmap(imageBitmap, 90f);
            imageView.setImageBitmap(imageBitmap);
            touchBitmap = imageView.getDrawingCache();
        }
    }

    public void EnableRuntimePermission(){
        if (ActivityCompat.shouldShowRequestPermissionRationale(ImageActivity.this, Manifest.permission.CAMERA))
        {
            captureButton.setVisibility(View.INVISIBLE);
            searchButton.setVisibility(View.INVISIBLE);
            Toast.makeText(ImageActivity.this,getString(R.string.enable_permissions), Toast.LENGTH_LONG).show();
        } else {
            // Requesting Permission
            ActivityCompat.requestPermissions(ImageActivity.this, new String[]{
                    Manifest.permission.CAMERA}, RequestPermissionCode);
        }
    }

}
