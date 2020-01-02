package com.mulkearn.kevin.colorpicker;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class HSVActivity extends AppCompatActivity{

    RelativeLayout hsvLayout;
    SeekBar hueSeeker, satSeeker, valSeeker;
    TextView hueValue, satValue,  valValue, hexValue, redValue, greenValue, blueValue, sat_s, sat_minus, sat_plus, val_plus;
    DatabaseHelper mDatabaseHelper;

    int hue_value = 0, sat_value = 0, val_value = 0;
    int red = 0, green = 0, blue = 0;
    String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsv);

        hsvLayout = findViewById(R.id.hsvLayout);
        hueValue = findViewById(R.id.hueValue);
        satValue = findViewById(R.id.satValue);
        valValue = findViewById(R.id.valValue);
        hexValue = findViewById(R.id.hexValue);
        redValue = findViewById(R.id.redValue);
        greenValue = findViewById(R.id.greenValue);
        blueValue = findViewById(R.id.blueValue);
        sat_s = findViewById(R.id.sat_s);
        hueSeeker = findViewById(R.id.hueSeeker);
        satSeeker = findViewById(R.id.satSeeker);
        valSeeker = findViewById(R.id.valSeeker);
        sat_minus = findViewById(R.id.sat_minus);
        sat_plus = findViewById(R.id.sat_plus);
        val_plus = findViewById(R.id.val_plus);

        mDatabaseHelper = new DatabaseHelper(this);

        float mainHue = getIntent().getFloatExtra("hue", 0);
        float mainSat = getIntent().getFloatExtra("sat", 0);
        float mainVal = getIntent().getFloatExtra("val", 0);
        hue_value = Math.round(mainHue);
        sat_value = Math.round(mainSat);
        val_value = Math.round(mainVal);
        hueValue.setText("H: " + hue_value + "\u00b0");
        satValue.setText("S: " + sat_value + "%");
        valValue.setText("V: " + val_value + "%");
        hueSeeker.setProgress(hue_value);
        satSeeker.setProgress(sat_value);
        valSeeker.setProgress(val_value);
        hsvToRGB();
        hsvToHex();
        getBackColor();
        setSliderGrads();

        hueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue_value = hueSeeker.getProgress();
                sat_value = satSeeker.getProgress();
                val_value = valSeeker.getProgress();
                hueValue.setText("H: " + progress + "\u00b0");
                hsvToRGB();
                hsvToHex();
                getBackColor();
                setSliderGrads();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        satSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue_value = hueSeeker.getProgress();
                sat_value = satSeeker.getProgress();
                val_value = valSeeker.getProgress();
                satValue.setText("S: " + progress + "%");
                hsvToRGB();
                hsvToHex();
                getBackColor();
                setSliderGrads();
            }
            public void onStartTrackingTouch(SeekBar seekBar) {}
            public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        valSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue_value = hueSeeker.getProgress();
                sat_value = satSeeker.getProgress();
                val_value = valSeeker.getProgress();
                valValue.setText("V: " + progress + "%");
                hsvToRGB();
                hsvToHex();
                getBackColor();
                setSliderGrads();
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
                AddData(hex);
                return true;
            case R.id.random:
                Random r_hue = new Random();
                Random r_sat = new Random();
                Random r_val = new Random();
                int randHue = r_hue.nextInt(361);
                int randSat = r_sat.nextInt(101);
                int randVal = r_val.nextInt(101);
                hueSeeker.setProgress(randHue);
                satSeeker.setProgress(randSat);
                valSeeker.setProgress(randVal);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void AddData(String newEntry) {
        boolean insertData = mDatabaseHelper.addColor(newEntry);
        if (insertData) {
            Toast.makeText(HSVActivity.this, newEntry + " " + getString(R.string.saved), Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(HSVActivity.this, "Error Saving Data!", Toast.LENGTH_SHORT).show();
        }
    }

    public void hsvToRGB(){
        float[] hsv = new float[3];
        hsv[0] = (float) hue_value;
        hsv[1] = (float) sat_value/100;
        hsv[2] = (float) val_value/100;
        int col = Color.HSVToColor(hsv);
        red = Color.red(col);
        green = Color.green(col);
        blue = Color.blue(col);
        redValue.setText("R: " + red);
        greenValue.setText("G: " + green);
        blueValue.setText("B: " + blue);
    }

    public void hsvToHex(){
        hex = String.format("#%02X%02X%02X", red, green, blue);
        hexValue.setText("Hex: " + hex);
    }

    public void getBackColor(){
        hsvLayout.setBackgroundColor(0xff000000 + red * 0x10000 + green * 0x100 + blue);
        redValue.setBackgroundColor(0xff000000 + red * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue);
    }

    private int[] buildHueColorArray(){
        int[] hueArr = new int[361];
        int count = 0;
        for (int i = hueArr.length - 1; i >= 0; i--, count++) {
            hueArr[count] = Color.HSVToColor(new float[]{i, 1f, 1f});
        }
        return hueArr;
    }

    public void setSliderGrads(){
        float[] temp = {0, 0, 0};
        float[] temp1 = {0, 0, 0};
        float[] temp2 = {0, 0, 0};
        // Hue SeekBar
        GradientDrawable hueGrad = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, buildHueColorArray());
        hueGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        hueSeeker.setBackground(hueGrad);

        // Val SeekBar
        temp[0] = (float) hueSeeker.getProgress();
        temp[1] = 1;
        temp[2] = 1;
        int[] valGradValues = {Color.rgb(0,0,0), Color.HSVToColor(temp)};  // Start color to end color
        GradientDrawable valGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, valGradValues);
        valGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        valSeeker.setBackground(valGrad);

        // Sat SeekBar
        temp1[0] = temp[0];
        temp1[1] = 1;
        temp1[2] = (float) valSeeker.getProgress()/100;
        temp2[0] = temp[0];
        temp2[1] = 0;
        temp2[2] = (float) valSeeker.getProgress()/100;
        int[] satGradValues = {Color.HSVToColor(temp2), Color.HSVToColor(temp1)};  // Start color to end color
        GradientDrawable satGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, satGradValues);
        satGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        satSeeker.setBackground(satGrad);

        sat_minus.setBackgroundColor(Color.HSVToColor(temp2));
        sat_plus.setBackgroundColor(Color.HSVToColor(temp1));
        val_plus.setBackgroundColor(Color.HSVToColor(temp));

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            sat_s.setBackgroundColor(Color.HSVToColor(temp2));
        }

    }

    public void rgbNavClick(View view) {
        Intent i_rgb = new Intent(this, RGBActivity.class);
        i_rgb.putExtra("red", red);
        i_rgb.putExtra("green", green);
        i_rgb.putExtra("blue", blue);
        startActivity(i_rgb);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hexNavClick(View view) {
        Intent i_hex = new Intent(this, HexActivity.class);
        i_hex.putExtra("red", red);
        i_hex.putExtra("green", green);
        i_hex.putExtra("blue", blue);
        startActivity(i_hex);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hueMinus(View view) {
        hueSeeker.setProgress(hue_value-1);
    }

    public void huePlus(View view) {
        hueSeeker.setProgress(hue_value+1);
    }

    public void satMinus(View view) {
        satSeeker.setProgress(sat_value-1);
    }

    public void satPlus(View view) {
        satSeeker.setProgress(sat_value+1);
    }

    public void valMinus(View view) {
        valSeeker.setProgress(val_value-1);
    }

    public void valPlus(View view) {
        valSeeker.setProgress(val_value+1);
    }
}
