package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class RGBActivity extends AppCompatActivity {

    RelativeLayout myLayout;
    SeekBar redSeeker, greenSeeker, blueSeeker;
    TextView redValue, greenValue,  blueValue, hexValue, hueValue, satValue, valValue;
    DBHandler dbHandler;

    float hue = 0, sat = 0, val = 0;
    int red_value = 0 , green_value = 0, blue_value = 0;
    String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rgb);

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

        dbHandler = new DBHandler(this, null, null, 1);

        //Color data from hsv activity
        red_value = getIntent().getIntExtra("red", 0);
        green_value = getIntent().getIntExtra("green", 0);
        blue_value = getIntent().getIntExtra("blue", 0);
        redValue.setText("R: " + red_value);
        greenValue.setText("G: " + green_value);
        blueValue.setText("B: " + blue_value);
        redSeeker.setProgress(red_value);
        greenSeeker.setProgress(green_value);
        blueSeeker.setProgress(blue_value);
        getBackColor();
        getHexValue();
        getHSVValue();

        redSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                redValue.setText("R: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        greenSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                greenValue.setText("G: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        blueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                red_value = redSeeker.getProgress();
                green_value = greenSeeker.getProgress();
                blue_value = blueSeeker.getProgress();
                blueValue.setText("B: " + progress);
                getBackColor();
                getHexValue();
                getHSVValue();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.saveHex:
                Colors color = new Colors(hex);
                dbHandler.addColor(color);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value", hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(RGBActivity.this, hex + " " + getString(R.string.copied), Toast.LENGTH_SHORT).show();
                return true;
            case R.id.random:
                Random r_red = new Random();
                Random r_green = new Random();
                Random r_blue = new Random();
                int randRed = r_red.nextInt(256);
                int randGreen = r_green.nextInt(256);
                int randBlue = r_blue.nextInt(256);
                redSeeker.setProgress(randRed);
                greenSeeker.setProgress(randGreen);
                blueSeeker.setProgress(randBlue);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void getBackColor(){
        myLayout.setBackgroundColor( 0xff000000 + red_value * 0x10000 + green_value * 0x100 + blue_value);
        redValue.setBackgroundColor(0xff000000 + red_value * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green_value * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue_value);
    }

    public void getHexValue(){
        hex = String.format("#%02X%02X%02X",red_value, green_value, blue_value);
        hexValue.setText("Hex: " + hex);
    }

    public void getHSVValue(){
        float[] hsv = new float[3];
        Color.RGBToHSV(red_value, green_value, blue_value, hsv);
        hue = hsv[0];
        sat = hsv[1] * 100;
        val = hsv[2] * 100;
        hueValue.setText("H: " + Math.round(hue) + "\u00b0");
        satValue.setText("S: " + Math.round(sat)  + "%");
        valValue.setText("V: " + Math.round(val)  + "%");
    }

    public void hsvNavClick(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        i_hsv.putExtra("hue", hue);
        i_hsv.putExtra("sat", sat);
        i_hsv.putExtra("val", val);
        startActivity(i_hsv);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hexNavClick(View view) {
        Intent i_hex = new Intent(this, HexActivity.class);
        i_hex.putExtra("red", red_value);
        i_hex.putExtra("green", green_value);
        i_hex.putExtra("blue", blue_value);
        startActivity(i_hex);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}