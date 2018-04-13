package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseArray;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.Random;

public class HexActivity  extends AppCompatActivity implements View.OnClickListener, ViewSwitcher.ViewFactory {

    RelativeLayout testLayout;

    TextSwitcher hex_digit_0, hex_digit_1, hex_digit_2, hex_digit_3, hex_digit_4, hex_digit_5;
    TextView textView, redValue, greenValue, blueValue, hueValue, satValue,  valValue;
    Animation animation;
    DBHandler dbHandler;

    SparseArray<String> keyValues = new SparseArray<>();
    String val0 = "0", val1 = "0", val2 = "0", val3 = "0", val4 = "0", val5 = "0";
    String hex = "000000";
    int digitIndex = 0;
    int red = 0, green = 0, blue = 0;
    float hue = 0, sat = 0, val = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        testLayout = (RelativeLayout) findViewById(R.id.testLayout);
        Button button_0 = (Button) findViewById(R.id.button_0);
        Button button_1 = (Button) findViewById(R.id.button_1);
        Button button_2 = (Button) findViewById(R.id.button_2);
        Button button_3 = (Button) findViewById(R.id.button_3);
        Button button_4 = (Button) findViewById(R.id.button_4);
        Button button_5 = (Button) findViewById(R.id.button_5);
        Button button_6 = (Button) findViewById(R.id.button_6);
        Button button_7 = (Button) findViewById(R.id.button_7);
        Button button_8 = (Button) findViewById(R.id.button_8);
        Button button_9 = (Button) findViewById(R.id.button_9);
        Button button_A = (Button) findViewById(R.id.button_A);
        Button button_B = (Button) findViewById(R.id.button_B);
        Button button_C = (Button) findViewById(R.id.button_C);
        Button button_D = (Button) findViewById(R.id.button_D);
        Button button_E = (Button) findViewById(R.id.button_E);
        Button button_F = (Button) findViewById(R.id.button_F);

        hex_digit_0 = (TextSwitcher) findViewById(R.id.hex_digit_0);
        hex_digit_1 = (TextSwitcher) findViewById(R.id.hex_digit_1);
        hex_digit_2 = (TextSwitcher) findViewById(R.id.hex_digit_2);
        hex_digit_3 = (TextSwitcher) findViewById(R.id.hex_digit_3);
        hex_digit_4 = (TextSwitcher) findViewById(R.id.hex_digit_4);
        hex_digit_5 = (TextSwitcher) findViewById(R.id.hex_digit_5);

        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);

        dbHandler = new DBHandler(this, null, null, 1);

        // map buttons IDs to input strings
        keyValues.put(R.id.button_0, "0");
        keyValues.put(R.id.button_1, "1");
        keyValues.put(R.id.button_2, "2");
        keyValues.put(R.id.button_3, "3");
        keyValues.put(R.id.button_4, "4");
        keyValues.put(R.id.button_5, "5");
        keyValues.put(R.id.button_6, "6");
        keyValues.put(R.id.button_7, "7");
        keyValues.put(R.id.button_8, "8");
        keyValues.put(R.id.button_9, "9");
        keyValues.put(R.id.button_A, "A");
        keyValues.put(R.id.button_B, "B");
        keyValues.put(R.id.button_C, "C");
        keyValues.put(R.id.button_D, "D");
        keyValues.put(R.id.button_E, "E");
        keyValues.put(R.id.button_F, "F");

        button_0.setOnClickListener(this);
        button_1.setOnClickListener(this);
        button_2.setOnClickListener(this);
        button_3.setOnClickListener(this);
        button_4.setOnClickListener(this);
        button_5.setOnClickListener(this);
        button_6.setOnClickListener(this);
        button_7.setOnClickListener(this);
        button_8.setOnClickListener(this);
        button_9.setOnClickListener(this);
        button_A.setOnClickListener(this);
        button_B.setOnClickListener(this);
        button_C.setOnClickListener(this);
        button_D.setOnClickListener(this);
        button_E.setOnClickListener(this);
        button_F.setOnClickListener(this);

        hex_digit_0.setFactory(this);
        hex_digit_1.setFactory(this);
        hex_digit_2.setFactory(this);
        hex_digit_3.setFactory(this);
        hex_digit_4.setFactory(this);
        hex_digit_5.setFactory(this);

        red = getIntent().getIntExtra("red", 0);
        green = getIntent().getIntExtra("green", 0);
        blue = getIntent().getIntExtra("blue", 0);
        hex = String.format("%02X%02X%02X", red, green, blue);

        val0 = "" + hex.charAt(0);
        val1 = "" + hex.charAt(1);
        val2 = "" + hex.charAt(2);
        val3 = "" + hex.charAt(3);
        val4 = "" + hex.charAt(4);
        val5 = "" + hex.charAt(5);

        hex_digit_0.setText(val0);
        hex_digit_1.setText(val1);
        hex_digit_2.setText(val2);
        hex_digit_3.setText(val3);
        hex_digit_4.setText(val4);
        hex_digit_5.setText(val5);

        animation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.digit_select);
        hex_digit_0.startAnimation(animation);
        hex_digit_0.setBackgroundColor(Color.parseColor("#660086A1"));
        setBackground();
        setValues();
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
                if(hex.length() == 6){
                    Colors color = new Colors("#" + hex);
                    dbHandler.addColor(color);
                    ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                    ClipData clip = ClipData.newPlainText("Hex Value", "#" + hex);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(HexActivity.this, "#" + hex + " " + getString(R.string.copied), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HexActivity.this, "#" + hex + " " + getString(R.string.not_valid), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.random:
                Random rand = new Random();

                val0 = String.format("%01X", rand.nextInt(16));
                val1 = String.format("%01X", rand.nextInt(16));
                val2 = String.format("%01X", rand.nextInt(16));
                val3 = String.format("%01X", rand.nextInt(16));
                val4 = String.format("%01X", rand.nextInt(16));
                val5 = String.format("%01X", rand.nextInt(16));

                hex_digit_0.setText(val0);
                hex_digit_1.setText(val1);
                hex_digit_2.setText(val2);
                hex_digit_3.setText(val3);
                hex_digit_4.setText(val4);
                hex_digit_5.setText(val5);

                setBackground();
                setValues();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void onClick(View v) {
        String value = keyValues.get(v.getId());
        clearAllAnimations();

        if(digitIndex == 0){
            val0 = value;
            hex_digit_0.setText(value);
            hex_digit_1.startAnimation(animation);
            hex_digit_1.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 1;
        } else if (digitIndex == 1){
            val1 = value;
            hex_digit_1.setText(value);
            hex_digit_2.startAnimation(animation);
            hex_digit_2.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 2;
        } else if (digitIndex == 2){
            val2 = value;
            hex_digit_2.setText(value);
            hex_digit_3.startAnimation(animation);
            hex_digit_3.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 3;
        } else if (digitIndex == 3){
            val3 = value;
            hex_digit_3.setText(value);
            hex_digit_4.startAnimation(animation);
            hex_digit_4.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 4;
        } else if (digitIndex == 4){
            val4 = value;
            hex_digit_4.setText(value);
            hex_digit_5.startAnimation(animation);
            hex_digit_5.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 5;
        } else if (digitIndex == 5){
            val5 = value;
            hex_digit_5.setText(value);
            hex_digit_0.startAnimation(animation);
            hex_digit_0.setBackgroundColor(Color.parseColor("#660086A1"));
            digitIndex = 0;
        }
        setBackground();
        setValues();
    }

    @Override
    public View makeView() {
        textView = new TextView(HexActivity.this);
        textView.setTextColor(Color.WHITE);
        textView.setTextSize(30);
        textView.setGravity(Gravity.CENTER_HORIZONTAL);
        return textView;
    }


    public void digit0Clicked(View view) {
        digitIndex = 0;
        clearAllAnimations();
        hex_digit_0.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_0.startAnimation(animation);
    }

    public void digit1Clicked(View view) {
        digitIndex = 1;
        clearAllAnimations();
        hex_digit_1.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_1.startAnimation(animation);
    }

    public void digit2Clicked(View view) {
        digitIndex = 2;
        clearAllAnimations();
        hex_digit_2.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_2.startAnimation(animation);
    }

    public void digit3Clicked(View view) {
        digitIndex = 3;
        clearAllAnimations();
        hex_digit_3.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_3.startAnimation(animation);
    }

    public void digit4Clicked(View view) {
        digitIndex = 4;
        clearAllAnimations();
        hex_digit_4.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_4.startAnimation(animation);
    }

    public void digit5Clicked(View view) {
        digitIndex = 5;
        clearAllAnimations();
        hex_digit_5.setBackgroundColor(Color.parseColor("#660086A1"));
        hex_digit_5.startAnimation(animation);
    }

    public void clearAllAnimations(){
        hex_digit_0.clearAnimation();
        hex_digit_1.clearAnimation();
        hex_digit_2.clearAnimation();
        hex_digit_3.clearAnimation();
        hex_digit_4.clearAnimation();
        hex_digit_5.clearAnimation();

        hex_digit_0.setBackgroundColor(Color.TRANSPARENT);
        hex_digit_1.setBackgroundColor(Color.TRANSPARENT);
        hex_digit_2.setBackgroundColor(Color.TRANSPARENT);
        hex_digit_3.setBackgroundColor(Color.TRANSPARENT);
        hex_digit_4.setBackgroundColor(Color.TRANSPARENT);
        hex_digit_5.setBackgroundColor(Color.TRANSPARENT);
    }

    public void setBackground(){
        String col = "#" + val0 + val1 + val2 + val3 + val4 + val5;
        testLayout.setBackgroundColor(Color.parseColor(col));
    }

    public void setValues(){
        String col = "#" + val0 + val1 + val2 + val3 + val4 + val5;
        int color = Color.parseColor(col);
        red = Color.red(color);
        green = Color.green(color);
        blue = Color.blue(color);
        hex = String.format("%02X%02X%02X",red, green, blue);

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        hue = hsv[0];
        sat = hsv[1] * 100;
        val = hsv[2] * 100;

        hueValue.setText("H: " + Math.round(hue) + "\u00b0");
        satValue.setText("S: " + Math.round(sat)  + "%");
        valValue.setText("V: " + Math.round(val)  + "%");
        redValue.setText("R: " + red);
        greenValue.setText("G: " + green);
        blueValue.setText("B: " + blue);
    }

    public void rgbNavClick(View view) {
        Intent i_rgb = new Intent(this, RGBActivity.class);
        i_rgb.putExtra("red", red);
        i_rgb.putExtra("green", green);
        i_rgb.putExtra("blue", blue);
        startActivity(i_rgb);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hsvNavClick(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        i_hsv.putExtra("hue", hue);
        i_hsv.putExtra("sat", sat);
        i_hsv.putExtra("val", val);
        startActivity(i_hsv);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }
}