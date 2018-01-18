package com.mulkearn.kevin.colorpicker;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.SeekBar;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RelativeLayout myLayout = (RelativeLayout) findViewById(R.id.mainView);
        myLayout.setBackgroundColor(Color.WHITE);

        SeekBar redSeeker = (SeekBar) findViewById(R.id.redSeeker);
        SeekBar greenSeeker = (SeekBar) findViewById(R.id.greenSeeker);
        SeekBar blueSeeker = (SeekBar) findViewById(R.id.blueSeeker);

        int redMax = redSeeker.getMax();
        int greenMax = greenSeeker.getMax();
        int blueMax = blueSeeker.getMax();

        TextView redValue = (TextView) findViewById(R.id.redValue);
        TextView greenValue = (TextView) findViewById(R.id.greenValue);
        TextView blueValue = (TextView) findViewById(R.id.blueValue);

        redValue.setText(Integer.toString(redMax));
        greenValue.setText(Integer.toString(greenMax));
        blueValue.setText(Integer.toString(blueMax));

    }
}
