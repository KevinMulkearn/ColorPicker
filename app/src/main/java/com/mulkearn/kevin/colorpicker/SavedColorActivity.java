package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

public class SavedColorActivity extends AppCompatActivity{

    DatabaseHelper mDatabaseHelper;
    ListView savedColorsList;
    Toast t;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_color);
        savedColorsList = findViewById(R.id.savedColorsList);

        mDatabaseHelper = new DatabaseHelper(this);

        // Copy hex value to clipboard
        savedColorsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long id) {
                if(t != null){
                    t.cancel();
                }
                String hex_value = adapterView.getItemAtPosition(position).toString();

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value", hex_value);
                assert clipboard != null;
                clipboard.setPrimaryClip(clip);
                t = Toast.makeText(SavedColorActivity.this, hex_value + " " + getString(R.string.copied), Toast.LENGTH_SHORT);
                t.show();
            }
        });

        // Remove color from list
        savedColorsList.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int position, long id) {
                String hex_value = adapterView.getItemAtPosition(position).toString();
                Cursor data = mDatabaseHelper.getItemID(hex_value);  // Get the id associated with that name

                int itemID = -1;
                while(data.moveToNext()){
                    itemID = data.getInt(0);
                }
                if(itemID > -1){
                    mDatabaseHelper.deleteColor(itemID, hex_value);
                    Toast.makeText(SavedColorActivity.this, hex_value + " " + getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                }
                else{
                    Toast.makeText(SavedColorActivity.this, "Already Deleted", Toast.LENGTH_SHORT).show();
                }
                populateListView();
                return true;
            }
        });

        populateListView();
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_saved_color, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                NavUtils.navigateUpFromSameTask(this);
                overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
                return true;
            case R.id.clear:
                AlertDialog diaBox = AskOption();
                diaBox.getWindow().setBackgroundDrawable(new ColorDrawable(Color.argb(180,255,255,255)));
                diaBox.show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void populateListView() {
        // Get the data and append to a list
        Cursor data = mDatabaseHelper.getColors();
        ArrayList<String> listData = new ArrayList<>();
        while(data.moveToNext()){
            // Get the value from the database in column 1, then add it to the ArrayList
            listData.add(data.getString(1));
        }
        String[] savedColorsArray  = listData.toArray(new String[0]);
        // Create the list adapter and set the adapter
        CustomAdapter colorAdapter = new CustomAdapter(SavedColorActivity.this, savedColorsArray);
        savedColorsList.setAdapter(colorAdapter);  // Set list colors
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // (your deleting code here)
                        mDatabaseHelper.clearColors();
                        populateListView();
                        dialog.dismiss();
                        Toast.makeText(SavedColorActivity.this, getString(R.string.all_colors_deleted), Toast.LENGTH_SHORT).show();
                    }
                })
                .setNegativeButton(getString(R.string.cancel), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                }).create();
        return myQuittingDialogBox;
    }

}

