package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class hsvActivity extends AppCompatActivity{

    RelativeLayout hsvLayout;
    SeekBar hueSeeker, satSeeker, valSeeker;
    TextView hueValue, satValue,  valValue, hexValue, redValue, greenValue, blueValue;
    int hue_value, sat_value, val_value;
    int red = 0, green = 0, blue = 0;
    String hex = "#000000";
    float[] temp = {0, 0, 0};
    float[] temp1 = {0, 0, 0};
    float[] temp2 = {0, 0, 0};

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

        hueSeeker = (SeekBar) findViewById(R.id.hueSeeker);
        satSeeker = (SeekBar) findViewById(R.id.satSeeker);
        valSeeker = (SeekBar) findViewById(R.id.valSeeker);

        setSliderGrads();

        hueSeeker.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                hueValue.setText("H: " + Integer.toString(progress) + "\u00b0");
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
                satValue.setText("S: " + Integer.toString(progress) + "%");
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
                valValue.setText("V: " + Integer.toString(progress) + "%");
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
        inflater.inflate(R.menu.menu_hsv, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.rgb:
                Intent i_rgb = new Intent(this, MainActivity.class);
                startActivity(i_rgb);
                return true;
            case R.id.about:
                Intent i_about = new Intent(this, AboutActivity.class);
                startActivity(i_about);
                return true;
            case R.id.randomHSV:
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
            case R.id.saveHex:
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value","#" + hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(hsvActivity.this, "#" + hex + " Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void hsvToRGB(){
        hue_value = hueSeeker.getProgress();
        sat_value = satSeeker.getProgress();
        val_value = valSeeker.getProgress();
        float[] hsv = new float[3];
        hsv[0] = (float) hue_value;
        hsv[1] = (float) sat_value/100;
        hsv[2] = (float) val_value/100;
        int col = Color.HSVToColor(hsv);
        red = Color.red(col);
        green = Color.green(col);
        blue = Color.blue(col);
        redValue.setText("R: " + Integer.toString(red));
        greenValue.setText("G: " + Integer.toString(green));
        blueValue.setText("B: " + Integer.toString(blue));
    }

    public void hsvTohex(){
        hex = String.format("%02X%02X%02X", red, green, blue);
        hexValue.setText("Hex: #" + hex);
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
        //Hue seeker
        GradientDrawable hueGrad = new GradientDrawable(GradientDrawable.Orientation.RIGHT_LEFT, buildHueColorArray());
        hueGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        hueSeeker.setBackgroundDrawable(hueGrad);

        //Sat seeker
        temp[0] = (float) hueSeeker.getProgress();
        temp[1] = (float) 1;
        temp[2] = (float) 1;
        int[] valGradValues = {Color.rgb(0,0,0), Color.HSVToColor(temp)};
        GradientDrawable valGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, valGradValues);
        valGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        valSeeker.setBackgroundDrawable(valGrad);

        //Val seeker
        temp1[0] = temp[0];
        temp1[1] = (float) 1;
        temp1[2] = (float) valSeeker.getProgress()/100;
        temp2[0] = temp[0];
        temp2[1] = (float) 0;
        temp2[2] = (float) valSeeker.getProgress()/100;
        int[] satGradValues = {Color.HSVToColor(temp2), Color.HSVToColor(temp1)};
        GradientDrawable satGrad = new GradientDrawable(GradientDrawable.Orientation.LEFT_RIGHT, satGradValues);
        satGrad.setGradientType(GradientDrawable.LINEAR_GRADIENT);
        satSeeker.setBackgroundDrawable(satGrad);
    }


}
