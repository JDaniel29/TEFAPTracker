package jdaniel29.tefaptracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;
import jdaniel29.tefaptracker.fragments.ButtonBarFragment;

public class Tracker extends AppCompatActivity {
    Button changeFileButton, shareFileButton;

    ListView commodityListView;
    ButtonBarFragment buttonBarFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_tracker);
        //setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        //setupXMLVariables();

    }
    /*

    @Override
    protected void onPause() {
        super.onPause();
        Commodity[] writeArray = new Commodity[FileManager.currentCommodities.size()];
        try {
            FileManager.saveDistributionFile(FileManager.currentCommodities.toArray(writeArray));
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    private void setupXMLVariables(){
        final Activity activity = this;

        buttonBarFragment = (ButtonBarFragment)getSupportFragmentManager().findFragmentById(R.id.buttonBarFragmentHolder);
        buttonBarFragment.changeCommodity(FileManager.currentCommodities.toArray(new Commodity[FileManager.currentCommodities.size()]));



        commodityListView = new ListView(this);
        commodityListView.setAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1));

        changeFileButton           = (Button)findViewById(R.id.changeFileButton);
        shareFileButton            = (Button)findViewById(R.id.shareFileButton);

        changeFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), FileSelector.class);
                startActivity(intent);
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


    }*/






}
