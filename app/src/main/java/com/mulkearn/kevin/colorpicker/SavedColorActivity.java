package com.mulkearn.kevin.colorpicker;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
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
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Toast;

public class SavedColorActivity extends AppCompatActivity{

    DBHandler dbHandler;
    ListView savedColorsList;
    ListAdapter colorAdapter;
    Toast t;

    String dbString;
    String[] savedColorsArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_saved_color);

        savedColorsList = (ListView) findViewById(R.id.savedColorsList);

        dbHandler = new DBHandler(this, null, null, 1);

        savedColorsList.setOnItemClickListener(new android.widget.AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if(t != null){
                    t.cancel();
                }
                String item = (String) savedColorsList.getItemAtPosition(position);

                ClipboardManager clipboard = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clip = ClipData.newPlainText("Hex Value", item);
                clipboard.setPrimaryClip(clip);
                t = Toast.makeText(SavedColorActivity.this, item + " " + getString(R.string.copied), Toast.LENGTH_SHORT);
                t.show();
            }
        });

        savedColorsList.setOnItemLongClickListener(new android.widget.AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                String item = (String) savedColorsList.getItemAtPosition(position);
                dbHandler.deleteColor(item);
                Toast.makeText(SavedColorActivity.this, item + " " + getString(R.string.deleted), Toast.LENGTH_SHORT).show();
                printDatabase();

                return true;
            }
        });

        printDatabase();
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

    public void printDatabase(){
        if(dbHandler.databaseToString().length() > 4){ //At least one color
            dbString = dbHandler.databaseToString();
            dbString = dbString.substring(0,dbString.length()-1); //Remove , at end
        } else {
            dbString = "#000000"; ////Add Black list can't be empty
        }
        savedColorsArray = dbString.split(",");
        colorAdapter = new CustomAdapter(SavedColorActivity.this, savedColorsArray); //use my custom adapter
        savedColorsList.setAdapter(colorAdapter); //set list colors
    }

    private AlertDialog AskOption() {
        AlertDialog myQuittingDialogBox = new AlertDialog.Builder(this)
                .setTitle(getString(R.string.delete_all))
                .setMessage(getString(R.string.are_you_sure))
                .setPositiveButton(getString(R.string.delete), new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        //your deleting code
                        dbHandler.clearColors();
                        Colors color = new Colors("#000000");
                        dbHandler.addColor(color); //Add Black list can't be empty
                        printDatabase();
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

