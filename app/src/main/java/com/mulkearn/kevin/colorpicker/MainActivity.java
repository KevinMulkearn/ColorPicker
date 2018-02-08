package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class MainActivity extends AppCompatActivity {

    RelativeLayout myLayout;
    SeekBar redSeeker, greenSeeker, blueSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue;
    private int red_value, green_value, blue_value;
    private String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Main view
        myLayout = (RelativeLayout) findViewById(R.id.mainView);

        //Seek Bars
        redSeeker = (SeekBar) findViewById(R.id.redSeeker);
        greenSeeker = (SeekBar) findViewById(R.id.greenSeeker);
        blueSeeker = (SeekBar) findViewById(R.id.blueSeeker);

        //Text Views
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);
        hexValue = (TextView) findViewById(R.id.hexValue);
        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);


        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                redValue.setText("R: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

        greenSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                greenValue.setText("G: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

        blueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                blueValue.setText("B: " + Integer.toString(progress));
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {
                //Empty
            }
            public void onStopTrackingTouch(SeekBar seekBar) {
                //Empty
            }
        });

    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.hsv:
                Intent i_hsv = new Intent(this, hsvActivity.class);
                startActivity(i_hsv);
                return true;
            case R.id.about:
                Intent i_about = new Intent(this, AboutActivity.class);
                startActivity(i_about);
                return true;
            case R.id.randomHex:
                Random r_red = new Random();
                Random r_green = new Random();
                Random r_blue = new Random();
                int randRed = r_red.nextInt(255 - 0 + 1) + 0;
                int randGreen = r_green.nextInt(255 - 0 + 1) + 0;
                int randBlue = r_blue.nextInt(255 - 0 + 1) + 0;
                redSeeker.setProgress(randRed);
                greenSeeker.setProgress(randGreen);
                blueSeeker.setProgress(randBlue);
                return true;
            case R.id.saveHex:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value","#" + hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(MainActivity.this, "#" + hex + " Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void getBackColor(){
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);
        redValue.setBackgroundColor(0xff000000 + red_value * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green_value * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue_value);
    }

    public void getHexValue(){
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        hex = String.format("%02X%02X%02X",red_value, green_value, blue_value);
        hexValue.setText("Hex: #" + hex);
    }

    public void getHSVValue(){
        float[] hsv = new float[3];
        red_value = redSeeker.getProgress();
        green_value = greenSeeker.getProgress();
        blue_value = blueSeeker.getProgress();

        Color.RGBToHSV(red_value, green_value, blue_value, hsv);

        float hue = hsv[0];
        float sat = hsv[1] * 100;
        float val = hsv[2] * 100;

        hueValue.setText("H: " + Integer.toString((int) hue) + "\u00b0");
        satValue.setText("S: " + Integer.toString((int) sat) + "%");
        valValue.setText("V: " + Integer.toString((int) val) + "%");
    }

}
