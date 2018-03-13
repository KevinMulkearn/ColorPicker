package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
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

    RelativeLayout mEntry_background;
    TextView redValue, greenValue,  blueValue, hueValue, satValue,  valValue;;
    EditText hexValue;
    DBHandler dbHandler;

    int red = 0, green = 0, blue = 0;
    float hue = 0, sat = 0, val = 0;
    String hex = "000000";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hex);

        mEntry_background = (RelativeLayout) findViewById(R.id.mEntry_background);

        hexValue = (EditText) findViewById(R.id.hexValue);

        hueValue = (TextView) findViewById(R.id.hueValue);
        satValue = (TextView) findViewById(R.id.satValue);
        valValue = (TextView) findViewById(R.id.valValue);
        redValue = (TextView) findViewById(R.id.redValue);
        greenValue = (TextView) findViewById(R.id.greenValue);
        blueValue = (TextView) findViewById(R.id.blueValue);

        dbHandler = new DBHandler(this, null, null, 1);

        hexValue.setText(hex);
        hueValue.setText("" + (int) hue);
        satValue.setText("" + (int) sat);
        valValue.setText("" + (int) val);

        red = getIntent().getIntExtra("red", 0);
        green = getIntent().getIntExtra("green", 0);
        blue = getIntent().getIntExtra("blue", 0);

        getBackColor(red, green, blue);
        setValues(red, green, blue);

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
                                        getBackColor(red, green, blue);
                                        setValues(red, green, blue);
                                        InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                        imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    } else {
                                        Toast.makeText(HexActivity.this, "Enter Valid Number", Toast.LENGTH_SHORT).show();
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

    public void getBackColor(int red, int green, int blue){
        mEntry_background.setBackgroundColor(0xff000000 + red * 0x10000 + green * 0x100 + blue);
        redValue.setBackgroundColor(0xff000000 + red * 0x10000);
        greenValue.setBackgroundColor(0xff000000 + green * 0x100);
        blueValue.setBackgroundColor(0xff000000 + blue);
    }

    public void setValues(int red, int green, int blue){
        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        hue = hsv[0];
        sat = hsv[1] * 100;
        val = hsv[2] * 100;
        hueValue.setText("H: " + Math.round(hue) + "\u00b0");
        satValue.setText("S: " + Math.round(sat)  + "%");
        valValue.setText("V: " + Math.round(val)  + "%");
        redValue.setText("R: " + Integer.toString(red));
        greenValue.setText("G: " + Integer.toString(green));
        blueValue.setText("B: " + Integer.toString(blue));

        hex = String.format("%02X%02X%02X",red, green, blue);
        hexValue.setText(hex);
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.saveHex:
                Colors color = new Colors("#" + hex);
                dbHandler.addColor(color);
                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value", hex);
                clipboard.setPrimaryClip(clip);
                Toast.makeText(HexActivity.this, "#" + hex + " Copied to Clipboard", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.random:
                Random r_red = new Random();
                Random r_green = new Random();
                Random r_blue = new Random();
                red = r_red.nextInt(255 - 0 + 1) + 0;
                green = r_green.nextInt(255 - 0 + 1) + 0;
                blue = r_blue.nextInt(255 - 0 + 1) + 0;
                getBackColor(red, green, blue);
                setValues(red, green, blue);
                return true;
            case R.id.saved:
                Intent i_saved = new Intent(this, SavedColorActivity.class);
                startActivity(i_saved);
                return true;
            case R.id.about:
                Intent i_about = new Intent(this, AboutActivity.class);
                startActivity(i_about);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    public void rgbNavClick(View view) {
        Intent i_rgb = new Intent(this, MainActivity.class);
        i_rgb.putExtra("red", red);
        i_rgb.putExtra("green", green);
        i_rgb.putExtra("blue", blue);
        startActivity(i_rgb);
    }

    public void hsvNavClick(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        i_hsv.putExtra("hue", hue);
        i_hsv.putExtra("sat", sat);
        i_hsv.putExtra("val", val);
        startActivity(i_hsv);
    }
}
