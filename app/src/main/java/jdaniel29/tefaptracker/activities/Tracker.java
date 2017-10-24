package jdaniel29.tefaptracker.activities;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;

import java.io.File;

public class Tracker extends AppCompatActivity {
    Button pickFileButton, incrementCommodities, decrementCommodities;

    Button allOneCommodityButton, allTwoThreeCommodityButton, allFourFiveCommodityButton,
            allSixSevenCommodityButton, shareFileButton;
    ToggleButton allCommoditiesToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupXMLVariables();


        FileManager.listCommodities(this, (ListView)findViewById(R.id.screenListView));
        FileManager.updateProductCounts(this);




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

        allCommoditiesToggle = (ToggleButton)findViewById(R.id.AllCommoditiesIncrementAndDecrementToggle);


        pickFileButton             = (Button)findViewById(R.id.pickFileButton);
        shareFileButton            = (Button)findViewById(R.id.shareFileButton);
        allOneCommodityButton      = (Button)findViewById(R.id.add1AllCommodity);
        allTwoThreeCommodityButton = (Button)findViewById(R.id.add23AllCommodity);
        allFourFiveCommodityButton = (Button)findViewById(R.id.add45AllCommodity);
        allSixSevenCommodityButton = (Button)findViewById(R.id.add67AllCommodity);
        //incrementCommodities = (Button)findViewById(R.id.incrementAllButton);
        //decrementCommodities = (Button) findViewById(R.id.decrementAllButton);

        pickFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FileSelector.class);
                startActivity(intent);
            }
        });
        allOneCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allCommoditiesToggle.isChecked()){
                    FileManager.incrementAllProducts(activity, 1);
                } else {
                    FileManager.decrementAllProducts(activity, 1);
                }
            }
        });

        shareFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                Uri documentURI = FileProvider.getUriForFile(view.getContext(), "com.JDaniel29.fileprovider", FileManager.currentFile);
                shareIntent.putExtra(Intent.EXTRA_STREAM, documentURI);
                shareIntent.setType("text/csv");
                startActivity(shareIntent);
            }
        });
        allTwoThreeCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allCommoditiesToggle.isChecked()){
                    FileManager.incrementAllProducts(activity, 23);
                } else {
                    FileManager.decrementAllProducts(activity, 23);
                }
            }
        });
        allFourFiveCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allCommoditiesToggle.isChecked()){
                    FileManager.incrementAllProducts(activity, 45);
                } else {
                    FileManager.decrementAllProducts(activity, 45);
                }
            }
        });
        allSixSevenCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(allCommoditiesToggle.isChecked()){
                    FileManager.incrementAllProducts(activity, 67);
                } else {
                    FileManager.decrementAllProducts(activity, 67);
                }
            }
        });
        /*
        incrementCommodities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { FileManager.incrementAllProducts(activity);
            }
        });
        decrementCommodities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) { FileManager.decrementAllProducts(activity);
            }
        });
        */


    }






}
