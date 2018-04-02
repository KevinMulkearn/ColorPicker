package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Random;

public class HexActivity extends AppCompatActivity{

    RelativeLayout hex_background;
    TextView redValue, greenValue, blueValue, hueValue, satValue,  valValue, cyanValue, magentaValue, yellowValue, blackValue;
    EditText hexValue;
    DBHandler dbHandler;

    int red = 0, green = 0, blue = 0;
    float hue = 0, sat = 0, val = 0;
    String hex = "000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        hex_background = (RelativeLayout) findViewById(R.id.hex_background);
        hexValue = (EditText) findViewById(R.id.hexValue);
        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);
        cyanValue = (TextView) findViewById(R.id.cyanValue);
        magentaValue = (TextView) findViewById(R.id.magentaValue);
        yellowValue = (TextView) findViewById(R.id.yellowValue);
        blackValue = (TextView) findViewById(R.id.blackValue);

        dbHandler = new DBHandler(this, null, null, 1);

        red = getIntent().getIntExtra("red", 0);
        green = getIntent().getIntExtra("green", 0);
        blue = getIntent().getIntExtra("blue", 0);

        getBackColor();
        setValues();
        rgbTocmyk();

        hexValue.setSelection(hexValue.getText().length()); //Set cursor position to end

        //On Keyboard Enter Click
        hexValue.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    hex = hexValue.getText().toString();
                                    if(hex.length() == 6){
                                        int color = Color.parseColor("#" + hex);
                                        red = Color.red(color);
                                        green = Color.green(color);
                                        blue = Color.blue(color);
                                        getBackColor();
                                        setValues();
                                        rgbTocmyk();
                                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    } else {
                                        Toast.makeText(HexActivity.this, getString(R.string.enter_valid), Toast.LENGTH_SHORT).show();
                                    }
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                }
        );

    }

    public void getBackColor(){
        hex_background.setBackgroundColor(0xff000000 + red * 0x10000 + green * 0x100 + blue);
    }

    public void setValues(){
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        hue = hsv[0];
        sat = hsv[1] * 100;
        val = hsv[2] * 100;
        hex = String.format("%02X%02X%02X",red, green, blue);

        hueValue.setText("H: " + Math.round(hue) + "\u00b0");
        satValue.setText("S: " + Math.round(sat)  + "%");
        valValue.setText("V: " + Math.round(val)  + "%");
        redValue.setText("R: " + red);
        greenValue.setText("G: " + green);
        blueValue.setText("B: " + blue);
        hexValue.setText(hex);
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
                    ClipData clip = ClipData.newPlainText("Hex Value", hex);
                    clipboard.setPrimaryClip(clip);
                    Toast.makeText(HexActivity.this, "#" + hex + " " + getString(R.string.copied), Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(HexActivity.this, "#" + hex + " " + getString(R.string.not_valid), Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.random:
                Random r_red = new Random();
                Random r_green = new Random();
                Random r_blue = new Random();
                red = r_red.nextInt(256);
                green = r_green.nextInt(256);
                blue = r_blue.nextInt(256);
                getBackColor();
                setValues();
                rgbTocmyk();
                return true;
            default:
                return super.onOptionsItemSelected(item);
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

    public void hsvNavClick(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        i_hsv.putExtra("hue", hue);
        i_hsv.putExtra("sat", sat);
        i_hsv.putExtra("val", val);
        startActivity(i_hsv);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void rgbTocmyk() {
        float r = (float)red/255;
        float g = (float)green/255;
        float b = (float)blue/255;

        float k = 1 - (Math.max(r,Math.max(g,b)));
        float c = (1-r-k)/(1-k);
        float m = (1-g-k)/(1-k);
        float y = (1-b-k)/(1-k);

        cyanValue.setText("C: " + Math.round(c*100) + "%");
        magentaValue.setText("M: " + Math.round(m*100)  + "%");
        yellowValue.setText("Y: " + Math.round(y*100)  + "%");
        blackValue.setText("K: " + Math.round(k*100)  + "%");
    }

}
