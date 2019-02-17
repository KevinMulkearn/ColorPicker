package com.mulkearn.kevin.colorpicker;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.os.StrictMode;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import android.content.Context;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import android.os.Handler;
import android.os.Message;

public class SearchActivity extends AppCompatActivity {

    EditText addressBar;
    Button searchButton;
    ListView colorList;
    TextView addressName;
    Toast t1, t2;
    ProgressBar loadingBar;
    DatabaseHelper mDatabaseHelper;

    String[] colorArray;
    String siteAddress;
    String message;
    int urlColor;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            addressName.setText(siteAddress);
            addressName.setTextColor(urlColor);

            // Create the list adapter and set the adapter
            CustomAdapter colorAdapter = new CustomAdapter(SearchActivity.this, colorArray);
            colorList.setAdapter(colorAdapter);  // Set list colors

            loadingBar.setVisibility(View.INVISIBLE);
            t1.cancel();
            Toast.makeText(SearchActivity.this, message, Toast.LENGTH_SHORT).show();
            invalidateOptionsMenu();  // Recall menu create function
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        addressBar = findViewById(R.id.addressBar);
        searchButton = findViewById(R.id.searchButton);
        colorList = findViewById(R.id.listView);
        addressName = findViewById(R.id.addressName);
        loadingBar = findViewById(R.id.loadingBar);

        mDatabaseHelper = new DatabaseHelper(this);

        addressBar.setSelection(addressBar.getText().length()); // Set cursor position to end

        // On Keyboard Enter Click
        addressBar.setOnKeyListener(
                new View.OnKeyListener() {
                    @Override
                    public boolean onKey(View v, int keyCode, KeyEvent event) {
                        if (event.getAction() == KeyEvent.ACTION_DOWN) {
                            switch (keyCode) {
                                case KeyEvent.KEYCODE_DPAD_CENTER:
                                case KeyEvent.KEYCODE_ENTER:
                                    onSearch(v);
                                    InputMethodManager imm = (InputMethodManager) v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                                    assert imm != null;  // Avoid Null Point Exception
                                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                                    return true;
                                default:
                                    break;
                            }
                        }
                        return false;
                    }
                }
        );

        colorList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(t2 != null){
                    t2.cancel();
                }
                String item = colorList.getItemAtPosition(position).toString();

                boolean insertData = mDatabaseHelper.addColor(item);
                if (insertData) {
                    t2 = Toast.makeText(SearchActivity.this, item + " " + getString(R.string.saved), Toast.LENGTH_SHORT);
                } else {
                    t2 = Toast.makeText(SearchActivity.this, "Error Saving Data!", Toast.LENGTH_SHORT);
                }
                t2.show();
            }
        });

    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_search, menu);
        String address = formatAddress(siteAddress);
        if (!pingUrl(address)){
            menu.getItem(0).setVisible(false);
        } else {
            menu.getItem(0).setVisible(true);
        }
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.pageView:
                siteAddress = addressBar.getText().toString();
                String address = formatAddress(siteAddress); //replaced siteAddress
                if(pingUrl(address)){
                    Intent i_pageView = new Intent(this, WebPageActivity.class);
                    i_pageView.putExtra("url", address);
                    startActivity(i_pageView);
                    overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                } else {
                    Toast.makeText(SearchActivity.this, getString(R.string.enter_valid_url), Toast.LENGTH_LONG).show();
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void onSearch(View view){
        loadingBar.setVisibility(View.VISIBLE);

        Runnable r = new Runnable() {
            @Override
            public void run() {

                siteAddress = addressBar.getText().toString();
                String formattedUrl = formatAddress(siteAddress);  // Replaced siteAddress

                if (pingUrl(formattedUrl)){
                    urlColor = Color.parseColor("#00CC00");
                    String colors = getWebsite(formattedUrl);
                    if (colors.length() > 5) {
                        colorArray = colors.split(", ");
                        message = getString(R.string.colors_found);  // Colors Found
                    } else {
                        colorArray = new String[]{};  // Empty array
                        message = getString(R.string.no_colors_found);  // No Colors Found
                    }
                } else {
                    urlColor = Color.parseColor("#FF0000");
                    colorArray = new String[]{};  // Empty array
                    message = getString(R.string.enter_valid_url);  // Invalid URL
                }
                handler.sendEmptyMessage(0);
            }
        };

        InputMethodManager imm = (InputMethodManager) view.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        assert imm != null;  // Avoid Null Point Exception
        imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        t1 = Toast.makeText(SearchActivity.this, getString(R.string.please_wait), Toast.LENGTH_LONG);
        t1.show();

        Thread myThread = new Thread(r);
        myThread.start();
    }


    public String getWebsite(String website){
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(website);

        ArrayList<String> allColors = new ArrayList<>();
        int index;
        int lineLength;
        String hexTest;
        String sorted = "";

        try{
            HttpResponse response;
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            InputStream instream = entity.getContent();
            BufferedReader br = new BufferedReader(new InputStreamReader(instream, "windows-1251"),8);
            String inputLine;

            while((inputLine = br.readLine()) != null){
                index = inputLine.indexOf("#");

                while (index >= 0) {
                    lineLength = inputLine.length();
                    if (index < lineLength-7){
                        hexTest = inputLine.substring(index+1, index+7);
                        hexTest = hexTest.toUpperCase();

                        if(hexTest.matches("[0-9a-fA-F]+")){
                            allColors.add("#" + hexTest);
                        }
                    }
                    index = inputLine.indexOf("#", index + 1);
                }
            }
            instream.close();

        } catch (Exception e){
            e.printStackTrace();
            Toast.makeText(SearchActivity.this, "Error", Toast.LENGTH_SHORT).show();
        }

        if (!allColors.isEmpty()) {
            Set<String> hs = new HashSet<>(allColors);  // Add all elements to hash set (removes duplicates)

            allColors.clear();  // Clear array
            allColors.addAll(hs);  // Add all elements of hash set back to array

            Collections.sort(allColors);  // Sort array

            sorted = allColors.toString();
            sorted = sorted.substring(1,sorted.length()-1);  // Remove [] brackets
        }
        return sorted;
    }

    public static boolean pingUrl(String testUrl) {
        try{
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpClient httpclient = new DefaultHttpClient();
            HttpGet httpget = new HttpGet(testUrl);
            HttpResponse response;
            response = httpclient.execute(httpget);
            response.getEntity();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public static String formatAddress(String inputUrl) {
        String fullUrl = "";
        if (pingUrl(inputUrl)){
            fullUrl = inputUrl;
        } else if (pingUrl("https://" + inputUrl)){
            fullUrl = "https://" + inputUrl;
        } else if (pingUrl("http://" + inputUrl)){
            fullUrl = "http://" + inputUrl;
        }
        return fullUrl;
    }
}
