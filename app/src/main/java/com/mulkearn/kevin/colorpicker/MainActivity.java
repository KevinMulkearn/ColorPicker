package com.mulkearn.kevin.colorpicker;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void rgbActivityStart(View view) {
        Intent i_rgb = new Intent(this, RGBActivity.class);
        startActivity(i_rgb);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hsvActivityStart(View view) {
        Intent i_hsv = new Intent(this, hsvActivity.class);
        startActivity(i_hsv);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void hexActivityStart(View view) {
        Intent i_hex = new Intent(this, HexActivity.class);
        startActivity(i_hex);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void savedActivityStart(View view) {
        Intent i_saved = new Intent(this, SavedColorActivity.class);
        startActivity(i_saved);
        overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    public void aboutActivityStart(View view) {
        Intent i_about = new Intent(this, AboutActivity.class);
        startActivity(i_about);
    }

    public void aboutActivityTest(View view) {
        Intent i_test = new Intent(this, TestActivity.class);
        startActivity(i_test);
    }
}
