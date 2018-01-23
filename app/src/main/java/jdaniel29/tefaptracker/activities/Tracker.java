package jdaniel29.tefaptracker.activities;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.content.pm.ActivityInfo;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.FileManager;
import jdaniel29.tefaptracker.fragments.AllCommodityDisplayFragment;
import jdaniel29.tefaptracker.util.ActivityConstants;

import java.io.File;
import java.util.ArrayList;

public class Tracker extends AppCompatActivity {
    Button changeFileButton, shareFileButton, add1CommodityButton, add23CommodityButton,
            add45CommodityButton, add6PlusCommodityButton;

    TextView totalOnesTextView, totalTwoThreesTextView, totalFourFivesTextView, totalSixPlusTextView;

    ToggleButton toggleButton;

    ListView commodityListView;

    int multiplier;



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
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.add(R.id.trackerFragmentLayout, allCommodityDisplayFragment, ActivityConstants.allCommodityDisplayFragmentTag);
        transaction.commit();
        getSupportFragmentManager().executePendingTransactions();

        changeFileButton           = findViewById(R.id.changeFileButton);
        shareFileButton            = findViewById(R.id.shareFileButton);
        toggleButton               = findViewById(R.id.countingToggle);
        add1CommodityButton        = findViewById(R.id.add1CommodityButton);
        add23CommodityButton       = findViewById(R.id.add23CommodityButton);
        add45CommodityButton       = findViewById(R.id.add45CommodityButton);
        add6PlusCommodityButton    = findViewById(R.id.add6PlusCommodityButton);

        totalOnesTextView          = findViewById(R.id.totalOneCommodityTextView);
        totalTwoThreesTextView     = findViewById(R.id.total23CommodityTextView);
        totalFourFivesTextView     = findViewById(R.id.total45CommodityTextView);
        totalSixPlusTextView       = findViewById(R.id.total6PlusCommodityTextView);

        updateTextView(true, null);


        multiplier = 1;
        toggleButton.setChecked(true);
        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplier = toggleButton.isChecked() ? 1 : -1;
            }
        });

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
        });

        setupButtonBarForMultiple();

    }

    public void setupButtonBarForMultiple(){

        add1CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Commodity commodity : FileManager.currentCommodities){
                    if(commodity.getLargeFamilyThreshold() == FileManager.Size.ONE && Boolean.valueOf(commodity.getCurrentlyCounting())){
                        commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + commodity.getDistributionPerBox()*multiplier);
                        commodity.updateDistributionTotal();
                    }
                }

                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(true, null);
            }
        });

        add23CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Commodity commodity : FileManager.currentCommodities){
                    if((commodity.getLargeFamilyThreshold() == FileManager.Size.TWOTOTHREE || commodity.getLargeFamilyThreshold() == FileManager.Size.ONE)
                            && Boolean.valueOf(commodity.getCurrentlyCounting())){
                        commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + commodity.getDistributionPerBox()*multiplier);
                        commodity.updateDistributionTotal();
                    }
                }

                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(true, null);
            }
        });

        add45CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Commodity commodity : FileManager.currentCommodities){
                    if(commodity.getLargeFamilyThreshold() != FileManager.Size.SIXPLUS && Boolean.valueOf(commodity.getCurrentlyCounting())){
                        commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + commodity.getDistributionPerBox()*multiplier);
                        commodity.updateDistributionTotal();
                    }
                }

                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(true, null);
            }
        });

        add6PlusCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for(Commodity commodity : FileManager.currentCommodities){
                    if(Boolean.valueOf(commodity.getCurrentlyCounting())){
                        commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() + commodity.getDistributionPerBox()*multiplier);
                        commodity.updateDistributionTotal();
                    }
                }

                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(true, null);
            }
        });

        updateTextView(true, null);
    }

    public void setupButtonBarForSingle(final int index){
        final Commodity commodity = FileManager.currentCommodities.get(index);

        add1CommodityButton     = findViewById(R.id.add1CommodityButton);
        add23CommodityButton    = findViewById(R.id.add23CommodityButton);
        add45CommodityButton    = findViewById(R.id.add45CommodityButton);
        add6PlusCommodityButton = findViewById(R.id.add6PlusCommodityButton);

        add1CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commodity.getLargeFamilyThreshold() == FileManager.Size.ONE && Boolean.valueOf(commodity.getCurrentlyCounting())){
                    commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + commodity.getDistributionPerBox()*multiplier);
                    commodity.updateDistributionTotal();
                    FileManager.currentCommodities.set(index, commodity);
                }

                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(false, index);
            }
        });

        add23CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if((commodity.getLargeFamilyThreshold() == FileManager.Size.TWOTOTHREE || commodity.getLargeFamilyThreshold() == FileManager.Size.ONE)
                        && Boolean.valueOf(commodity.getCurrentlyCounting())){
                    commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + commodity.getDistributionPerBox()*multiplier);
                    commodity.updateDistributionTotal();
                    FileManager.currentCommodities.set(index, commodity);
                }
                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(false, index);
            }
        });

        add45CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(commodity.getLargeFamilyThreshold() != FileManager.Size.SIXPLUS && Boolean.valueOf(commodity.getCurrentlyCounting())){
                    commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + commodity.getDistributionPerBox()*multiplier);
                    FileManager.currentCommodities.set(index, commodity);
                    commodity.updateDistributionTotal();
                }
                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(false, index);
            }
        });

        add6PlusCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(Boolean.valueOf(commodity.getCurrentlyCounting())){
                    commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() + commodity.getDistributionPerBox()*multiplier);
                    FileManager.currentCommodities.set(index, commodity);
                    commodity.updateDistributionTotal();
                }
                allCommodityDisplayFragment.getAdapter().notifyDataSetChanged();
                updateTextView(false, index);
            }
        });

        updateTextView(false, index);

    }

    private void updateTextView(boolean multiple, @Nullable Integer index){
        int totalOnes = 0,
                totalTwoThrees = 0,
                totalFourFives = 0,
                totalSixPlus   = 0;

        if(multiple){
            for(Commodity commodity : FileManager.currentCommodities){
                totalOnes      += commodity.getDistributionSizeOne();
                totalTwoThrees += commodity.getDistributionSizeTwoToThree();
                totalFourFives += commodity.getDistributionSizeFourToFive();
                totalSixPlus   += commodity.getDistributionSizeSixToSeven();
            }
        } else {
            Commodity commodity = FileManager.currentCommodities.get(index);

            totalOnes = commodity.getDistributionSizeOne();
            totalTwoThrees = commodity.getDistributionSizeTwoToThree();
            totalFourFives = commodity.getDistributionSizeFourToFive();
            totalSixPlus = commodity.getDistributionSizeSixToSeven();
        }

        totalOnesTextView     .setText(String.valueOf(totalOnes));
        totalTwoThreesTextView.setText(String.valueOf(totalTwoThrees));
        totalFourFivesTextView.setText(String.valueOf(totalFourFives));
        totalSixPlusTextView  .setText(String.valueOf(totalSixPlus));
    }



}
