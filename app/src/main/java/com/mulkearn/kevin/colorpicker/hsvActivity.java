package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
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

public class hsvActivity extends AppCompatActivity{

    RelativeLayout hsvLayout;
    SeekBar hueSeeker, satSeeker, valSeeker;
    TextView hueValue, satValue,  valValue, hexValue, redValue, greenValue, blueValue, sat_s;
    DBHandler dbHandler;

    int hue_value = 0, sat_value = 0, val_value = 0;
    int red = 0, green = 0, blue = 0;
    String hex = "#000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hsv);

        hsvLayout = (RelativeLayout) findViewById(R.id.hsvLayout);
        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);
        hexValue = (TextView) findViewById(R.id.hexValue);
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);
        sat_s = (TextView) findViewById(R.id.sat_s);
        hueSeeker = (SeekBar) findViewById(R.id.hueSeeker);
        satSeeker = (SeekBar) findViewById(R.id.satSeeker);
        valSeeker = (SeekBar) findViewById(R.id.valSeeker);

        dbHandler = new DBHandler(this, null, null, 1);

        //Color data from main activity
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
        hsvTohex();
        getBackColor();
        setSliderGrads();

        hueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hue_value = hueSeeker.getProgress();
                sat_value = satSeeker.getProgress();
                val_value = valSeeker.getProgress();
                hueValue.setText("H: " + progress + "\u00b0");
                hsvToRGB();
                hsvTohex();
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
                hsvTohex();
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
                hsvTohex();
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
            case R.id.saveHex:
                Colors color = new Colors(hex);
                dbHandler.addColor(color);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value", hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(hsvActivity.this, hex + " Copied to Clipboard", Toast.LENGTH_SHORT).show();
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
            case R.id.saved:
                Intent i_saved = new Intent(this, SavedColorActivity.class);
                startActivity(i_saved);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.about:
                Intent i_about = new Intent(this, AboutActivity.class);
                startActivity(i_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void hsvTohex(){
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
        //Hue seeker
        GradientDrawable hueGrad = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, buildHueColorArray());
        hueGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        hueSeeker.setBackgroundDrawable(hueGrad);

        //Val seeker
        temp[0] = (float) hueSeeker.getProgress();
        temp[1] = 1;
        temp[2] = 1;
        int[] valGradValues = {Color.rgb(0,0,0), Color.HSVToColor(temp)}; //start color to end color
        GradientDrawable valGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, valGradValues);
        valGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        valSeeker.setBackgroundDrawable(valGrad);

        //Sat seeker
        temp1[0] = temp[0];
        temp1[1] = 1;
        temp1[2] = (float) valSeeker.getProgress()/100;
        temp2[0] = temp[0];
        temp2[1] = 0;
        temp2[2] = (float) valSeeker.getProgress()/100;
        int[] satGradValues = {Color.HSVToColor(temp2), Color.HSVToColor(temp1)}; //start color to end color
        GradientDrawable satGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, satGradValues);
        satGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        satSeeker.setBackgroundDrawable(satGrad);

        int orientation = this.getResources().getConfiguration().orientation;
        if (orientation == Configuration.ORIENTATION_PORTRAIT) {
            sat_s.setBackgroundColor(Color.HSVToColor(temp2));
        }

    }

    public void rgbNavClick(View view) {
        Intent i_rgb = new Intent(this, MainActivity.class);
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
}
