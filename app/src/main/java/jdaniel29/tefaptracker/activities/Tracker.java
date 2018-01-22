package jdaniel29.tefaptracker.activities;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.fragments.AllCommodityDisplayFragment;
import jdaniel29.tefaptracker.fragments.ButtonBarFragment;
import jdaniel29.tefaptracker.fragments.CommodityDetailsFragment;

public class Tracker extends AppCompatActivity {
    Button changeFileButton, shareFileButton;

    ListView commodityListView;
    ButtonBarFragment buttonBarFragment;

    AllCommodityDisplayFragment allCommodityDisplayFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tracker);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setupXMLVariables();

    }


    @Override
    protected void onResume() {
        super.onResume();
        allCommodityDisplayFragment.setupListView();

        buttonBarFragment.setupMultipleMode(allCommodityDisplayFragment.getAdapter());
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
           */
    private void setupXMLVariables(){
        final Activity activity = this;

        allCommodityDisplayFragment = new AllCommodityDisplayFragment();

        buttonBarFragment = (ButtonBarFragment)getSupportFragmentManager().findFragmentById(R.id.buttonBarFragmentHolder);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.trackerFragmentLayout, allCommodityDisplayFragment);
        transaction.commit();


        //changeFileButton           = (Button)findViewById(R.id.changeFileButton);
        //shareFileButton            = (Button)findViewById(R.id.shareFileButton);

        /*
        changeFileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HomeScreen.class);
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
        });*/


    }








}
