package jdaniel29.tefaptracker.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.FileManager;

import java.io.File;

public class HomeScreen extends AppCompatActivity {

    Button createNewFilebutton, selectExistingFileButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_screen);

        setupXMLVariables();
    }

    private void setupXMLVariables() {
        createNewFilebutton = (Button) findViewById(R.id.createNewFileButton);
        selectExistingFileButton = (Button) findViewById(R.id.selectExistingFileButton);

        createNewFilebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileCreateDialog();
            }
        });

        selectExistingFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showFileSelectDialog();
            }
        });
    }

    private void showFileSelectDialog(){
        //Setting up array of file names
        String[] fileNames = new String[FileManager.currentFileDir.listFiles().length];
        for(int i = 0; i < fileNames.length; i++){
            fileNames[i] = (FileManager.currentFileDir.listFiles())[i].getName();
        }

        if(fileNames.length == 0){
            Toast.makeText(this, "There are no files. Please create a new file." , Toast.LENGTH_SHORT).show();

            return;
        }

        //Create Alert
        final Context workingContext = this;

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Select a File");

        builder.setItems(fileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                FileManager.currentFile = FileManager.currentFileDir.listFiles()[which];

                Intent intent = new Intent(workingContext, Tracker.class);
                startActivity(intent);
            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.show();

    }

    private void showFileCreateDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Create a New File");

        final EditText editText = new EditText(this);
        editText.setHint("File Title Here");

        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(layoutParams);
        builder.setView(editText);

        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if(editText.getText().toString().contains(".csv")){
                    FileManager.currentFile = new File(FileManager.currentFileDir, editText.getText().toString() + ".csv");
                } else {
                    FileManager.currentFile = new File(FileManager.currentFileDir, editText.getText().toString());
                }

            }
        });

        builder.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
    }
}
