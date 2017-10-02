package jdaniel29.tefaptracker;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;

public class Tracker extends AppCompatActivity {
    Button addProductButton, pickFileButton;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);

        setupXMLVariables();

        FileManager.requestPermissions(this);
        FileManager.listFiles(this);
    }

    private void setupXMLVariables(){
        final Activity activity = this;

        addProductButton = (Button)findViewById(R.id.addProductButton);
        pickFileButton = (Button)findViewById(R.id.pickFileButton);

        addProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.showAddProductDialog(activity);
            }
        });
        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FileManager.listFiles(activity);
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
