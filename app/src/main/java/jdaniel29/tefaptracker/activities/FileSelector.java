package jdaniel29.tefaptracker.activities;

import android.Manifest;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;

import java.io.File;


public class FileSelector extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_selector);

        getSupportActionBar().setTitle("Select a File");

        requestPermissions();
        assignXMLVariables();
    }

    private void assignXMLVariables(){
        final Activity activity = this;

        Button addFileButton = (Button)findViewById(R.id.addFileButton);
        addFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFileNamer(activity);
            }
        });

    }

    private void requestPermissions(){
         Activity activity = this;
        String[] storagePermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(this, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, storagePermissions, 1);
        } else {
            System.out.println("WE HAVE THE PERMISSIONS!");
            FileManager.setupDirectory();
            listFiles();
        }

    }

    private void launchFileNamer(final Activity activity){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText editText = new EditText(activity);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alert.setView(editText);
        alert.setTitle("Enter a File Name");
        alert.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(editText.getText().toString().contains(".csv")){
                    FileManager.currentFile = new File(FileManager.currentFileDir, editText.getText().toString());
                } else {
                    FileManager.currentFile = new File(FileManager.currentFileDir, editText.getText().toString() + ".csv");
                }

                System.out.println(FileManager.currentFile.getPath() + " Succesfully Created");

                try {
                    if(FileManager.currentFile == null) {
                        FileManager.writeFile((Commodity) null);
                    } else {
                        Commodity[] writeArray = new Commodity[FileManager.currentCommodities.size()];
                        FileManager.writeFile(FileManager.currentCommodities.toArray(writeArray));
                    }
                    FileManager.readFile();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

                Intent intent = new Intent(activity, Tracker.class);
                dialogInterface.dismiss();
                startActivity(intent);
            }
        });
        alert.create().show();

    }

    private void listFiles(){

        final Activity activity = this;
        ListView fileListView = (ListView)findViewById(R.id.fileListView);


        final File[] createdFiles = FileManager.currentFileDir.listFiles();
        String[] fileNames = new String[createdFiles.length];

        if(FileManager.currentFile != null){
            Commodity[] commodities = new Commodity[FileManager.currentCommodities.size()];
            try {
                FileManager.writeFile(FileManager.currentCommodities.toArray(commodities));
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        for(int i = 0; i < createdFiles.length; i++){
            fileNames[i] = createdFiles[i].getName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, fileNames);
        fileListView.setAdapter(adapter);
        fileListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                FileManager.currentFile = FileManager.currentFileDir.listFiles()[i];
                System.out.println(FileManager.currentFile.getPath());
                try {
                    FileManager.readFile();
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

                Intent intent = new Intent(view.getContext(), Tracker.class);
                startActivity(intent);
            }
        });

        if(fileNames.length == 0){
            launchFileNamer(activity);
        }

    }

    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        FileManager.setupDirectory();
        listFiles();

        // other 'case' lines to check for other
        // permissions this app might request

    }
}
