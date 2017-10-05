package jdaniel29.tefaptracker.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;

public class Tracker extends AppCompatActivity {
    Button pickFileButton, incrementCommodities, decrementCommodities;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupXMLVariables();

        FileManager.requestPermissions(this);
        FileManager.listFiles(this);

    }

    @Override
    protected void onPause() {
        super.onPause();
        Commodity[] writeArray = new Commodity[FileManager.currentCommodities.size()];
        try {
            FileManager.writeFile(FileManager.currentCommodities.toArray(writeArray));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setupXMLVariables(){
        final Activity activity = this;

        pickFileButton = (Button)findViewById(R.id.pickFileButton);
        incrementCommodities = (Button)findViewById(R.id.incrementAllButton);
        decrementCommodities = (Button) findViewById(R.id.decrementAllButton);

        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.listFiles(activity);
            }
        });
        incrementCommodities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.incrementAllProducts(activity);
            }
        });
        decrementCommodities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.decrementAllProducts(activity);
            }
        });
    }






    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 1: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    FileManager.setupDirectory();
                    System.out.println("PERMISSIONS GRANTED2");
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }
}
