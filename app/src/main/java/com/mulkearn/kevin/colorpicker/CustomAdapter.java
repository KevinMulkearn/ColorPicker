package com.mulkearn.kevin.colorpicker;

import android.content.Context;
import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class CustomAdapter extends ArrayAdapter<String>{

    CustomAdapter(Context context, String[] cols) {
        super(context,R.layout.custom_row , cols);
    }

    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {
        LayoutInflater colorInflator = LayoutInflater.from(getContext());
        View customView = colorInflator.inflate(R.layout.custom_row, parent, false);

        String singleColor = getItem(position);
        TextView hexText = (TextView) customView.findViewById(R.id.hexText);
        TextView rgbText = (TextView) customView.findViewById(R.id.rgbText);
        TextView hsvText = (TextView) customView.findViewById(R.id.hsvText);

        int color = Color.parseColor(singleColor);
        int red = Color.red(color);
        int green = Color.green(color);
        int blue = Color.blue(color);
        String rgb = "RGB (" + red + ", " + green + ", " + blue + ")";

        float[] hsv = new float[3];
        Color.RGBToHSV(red, green, blue, hsv);
        float hue = hsv[0];
        float sat = hsv[1] * 100;
        float val = hsv[2] * 100;
        String hsvString = "HSV (" + Math.round(hue) + "\u00b0, " + Math.round(sat) + "%, " + Math.round(val) + "%)";

        hexText.setText(singleColor);
        rgbText.setText(rgb);
        hsvText.setText(hsvString);

        hexText.setBackgroundColor(Color.parseColor(singleColor));
        rgbText.setBackgroundColor(Color.parseColor(singleColor));
        hsvText.setBackgroundColor(Color.parseColor(singleColor));

        return customView;

    }

}
