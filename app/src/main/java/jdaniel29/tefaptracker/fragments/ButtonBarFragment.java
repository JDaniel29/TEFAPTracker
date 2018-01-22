package jdaniel29.tefaptracker.fragments;

import android.content.Context;
import android.icu.text.SymbolTable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.ToggleButton;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.CommodityAdapter;
import jdaniel29.tefaptracker.data.FileManager;

import java.io.File;
import java.util.ArrayList;


public class ButtonBarFragment extends Fragment {
    Button add1CommodityButton, add23CommodityButton, add45CommodityButton, add6PlusCommodityButton;
    TextView totalOneCommodityTextView, total23CommodityTextView, total45CommodityTextView, total6PlusCommodityTextView;
    ToggleButton incrementAndDecrementToggle;

    CommodityAdapter adapter;

    Mode mode;
    int multiplier, currentIndex;

    public enum Mode{
        SINGLE, MULTIPLE;
    }

    public ButtonBarFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_button_bar, container, false);

        add1CommodityButton     = inflatedView.findViewById(R.id.add1CommodityButton);
        add23CommodityButton    = inflatedView.findViewById(R.id.add23CommodityButton);
        add45CommodityButton    = inflatedView.findViewById(R.id.add45CommodityButton);
        add6PlusCommodityButton = inflatedView.findViewById(R.id.add6PlusCommodityButton);

        totalOneCommodityTextView   = inflatedView.findViewById(R.id.totalOneCommodityTextView);
        total23CommodityTextView    = inflatedView.findViewById(R.id.total23CommodityTextView);
        total45CommodityTextView    = inflatedView.findViewById(R.id.total45CommodityTextView);
        total6PlusCommodityTextView = inflatedView.findViewById(R.id.total6PlusCommodityTextView);

        incrementAndDecrementToggle = inflatedView.findViewById(R.id.countingToggle);
        incrementAndDecrementToggle.setChecked(true);

        multiplier = 1;
        currentIndex = -1;

        return inflatedView;
    }


    public void setupMultipleMode(CommodityAdapter commodityAdapter){
        mode = Mode.MULTIPLE;

        adapter = commodityAdapter;
        updateButtons();
        updateTextViews();

        System.out.println("MULTIPLE MODE SET");
    }

    public void setupSingleMode(int index){
        mode = Mode.SINGLE;
        currentIndex = index;
    }

    private void updateButtons(){

        add1CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == Mode.MULTIPLE) {
                    for (Commodity commodity : FileManager.currentCommodities) {
                        if(commodity.getLargeFamilyThreshold() == FileManager.Size.ONE && Boolean.valueOf(commodity.getCurrentlyCounting())) {
                            commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + commodity.getDistributionPerBox() * multiplier);
                            commodity.updateDistributionTotal();
                        }
                    }
                } else if(mode == Mode.SINGLE){
                    FileManager.Size currentThreshold = FileManager.currentCommodities.get(currentIndex).getLargeFamilyThreshold();

                    if(currentThreshold == FileManager.Size.ONE && Boolean.valueOf(FileManager.currentCommodities.get(currentIndex).getCurrentlyCounting())) {
                        FileManager.currentCommodities.get(currentIndex).setDistributionSizeOne(FileManager.currentCommodities.get(currentIndex).getDistributionSizeOne() +
                                FileManager.currentCommodities.get(currentIndex).getDistributionPerBox() * multiplier);
                        FileManager.currentCommodities.get(currentIndex).updateDistributionTotal();
                    }
                }
                updateTextViews();


            }
        });

        add23CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == Mode.MULTIPLE) {
                    for (Commodity commodity : FileManager.currentCommodities) {
                        FileManager.Size currentThreshold = commodity.getLargeFamilyThreshold();
                        if((currentThreshold == FileManager.Size.ONE || currentThreshold == FileManager.Size.TWOTOTHREE) && Boolean.valueOf(commodity.getCurrentlyCounting())) {

                            commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + commodity.getDistributionPerBox() * multiplier);
                            commodity.updateDistributionTotal();

                        }
                    }
                } else if(mode == Mode.SINGLE){
                    FileManager.Size currentThreshold = FileManager.currentCommodities.get(currentIndex).getLargeFamilyThreshold();
                    if((currentThreshold == FileManager.Size.ONE || currentThreshold == FileManager.Size.TWOTOTHREE) && Boolean.valueOf(FileManager.currentCommodities.get(currentIndex).getCurrentlyCounting())) {
                        FileManager.currentCommodities.get(currentIndex).setDistributionSizeTwoToThree(FileManager.currentCommodities.get(currentIndex).getDistributionSizeTwoToThree() +
                                FileManager.currentCommodities.get(currentIndex).getDistributionPerBox() * multiplier);
                        FileManager.currentCommodities.get(currentIndex).updateDistributionTotal();
                    }
                }
                updateTextViews();

            }
        });

        add45CommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == Mode.MULTIPLE) {
                    for (Commodity commodity : FileManager.currentCommodities) {
                        FileManager.Size currentThreshold = commodity.getLargeFamilyThreshold();

                        if(currentThreshold != FileManager.Size.SIXPLUS && Boolean.valueOf(commodity.getCurrentlyCounting())) {
                            commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + commodity.getDistributionPerBox() * multiplier);
                            commodity.updateDistributionTotal();
                        }
                    }
                } else if(mode == Mode.SINGLE){
                    FileManager.Size currentThreshold = FileManager.currentCommodities.get(currentIndex).getLargeFamilyThreshold();
                    if(currentThreshold != FileManager.Size.FOURTOFIVE && Boolean.valueOf(FileManager.currentCommodities.get(currentIndex).getCurrentlyCounting())) {
                        FileManager.currentCommodities.get(currentIndex).setDistributionSizeFourToFive(FileManager.currentCommodities.get(currentIndex).getDistributionSizeFourToFive() +
                                FileManager.currentCommodities.get(currentIndex).getDistributionPerBox() * multiplier);
                        FileManager.currentCommodities.get(currentIndex).updateDistributionTotal();
                    }
                }
                updateTextViews();

            }
        });

        add6PlusCommodityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mode == Mode.MULTIPLE) {
                    for (Commodity commodity : FileManager.currentCommodities) {
                        if (Boolean.valueOf(commodity.getCurrentlyCounting())) {
                            commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() + commodity.getDistributionPerBox() * multiplier);
                            commodity.updateDistributionTotal();
                        }

                    }
                } else if(mode == Mode.SINGLE){
                    if(Boolean.valueOf(FileManager.currentCommodities.get(currentIndex).getCurrentlyCounting())) {
                        FileManager.currentCommodities.get(currentIndex).setDistributionSizeSixToSeven(FileManager.currentCommodities.get(currentIndex).getDistributionSizeSixToSeven() +
                                FileManager.currentCommodities.get(currentIndex).getDistributionPerBox() * multiplier);
                        FileManager.currentCommodities.get(currentIndex).updateDistributionTotal();
                    }
                }
                updateTextViews();
            }
        });

        incrementAndDecrementToggle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                multiplier = (incrementAndDecrementToggle.isChecked()) ?  1 : -1;
            }
        });
    }

    private void updateTextViews(){
        System.out.println(mode + " MODE");
        int totalOnes = 0, total23s = 0, total45s = 0, total6Pluses = 0;
        if(mode == Mode.MULTIPLE) {
            ArrayList<Commodity> oldCommodity = FileManager.currentCommodities;
            for (Commodity commodity : FileManager.currentCommodities) {
                totalOnes    += commodity.getDistributionSizeOne();
                total23s     += commodity.getDistributionSizeTwoToThree();
                total45s     += commodity.getDistributionSizeFourToFive();
                total6Pluses += commodity.getDistributionSizeSixToSeven();
            }
            adapter.notifyDataSetChanged();


        } else {
            totalOnes    = FileManager.currentCommodities.get(currentIndex).getDistributionSizeOne();
            total23s     = FileManager.currentCommodities.get(currentIndex).getDistributionSizeTwoToThree();
            total45s     = FileManager.currentCommodities.get(currentIndex).getDistributionSizeFourToFive();
            total6Pluses = FileManager.currentCommodities.get(currentIndex).getDistributionSizeSixToSeven();
        }

        totalOneCommodityTextView  .setText(String.valueOf(totalOnes));
        total23CommodityTextView   .setText(String.valueOf(total23s));
        total45CommodityTextView   .setText(String.valueOf(total45s));
        total6PlusCommodityTextView.setText(String.valueOf(total6Pluses));
    }
}

