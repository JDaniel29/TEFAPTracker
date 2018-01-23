package jdaniel29.tefaptracker.fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.CommodityAdapter;
import jdaniel29.tefaptracker.data.FileManager;

import static jdaniel29.tefaptracker.data.FileManager.Size.*;


public class AddCommodityFragmentDialog extends DialogFragment {
    EditText skuEditText, productNameEditText, perBoxEditText;
    Spinner minimumFamilySizeSpinner;
    CheckBox currentlyCountingCheckBox;

    Button confirmChangesButton, cancelChangesButton;

    LinearLayout buttonHolder;

    CommodityAdapter adapter;

    Commodity currentCommodity = null;

    View inflatedView;
    public AddCommodityFragmentDialog(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        inflatedView = inflater.inflate(R.layout.fragment_add_commodity_dialog, container, false);

        skuEditText         = (EditText) inflatedView.findViewById(R.id.editskuTextBox);
        productNameEditText = (EditText) inflatedView.findViewById(R.id.editNameTextBox);
        perBoxEditText      = (EditText) inflatedView.findViewById(R.id.editPerBoxTextBox);

        minimumFamilySizeSpinner = (Spinner) inflatedView.findViewById(R.id.editLargeFamilyMinimumSpinner);


        currentlyCountingCheckBox = (CheckBox) inflatedView.findViewById(R.id.currentlyCountingCheckBox);

        return inflatedView;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        /*
        if(currentCommodity != null) {
            skuEditText.setText(currentCommodity.getSku());
            productNameEditText.setText(currentCommodity.getProductName());
            perBoxEditText.setText(String.valueOf(currentCommodity.getDistributionPerBox()));

            switch (currentCommodity.getLargeFamilyThreshold()) {
                case ONE:
                    minimumFamilySizeSpinner.setSelection(0);
                    break;

                case TWOTOTHREE:
                    minimumFamilySizeSpinner.setSelection(1);
                    break;

                case FOURTOFIVE:
                    minimumFamilySizeSpinner.setSelection(2);
                    break;

                case SIXPLUS:
                    minimumFamilySizeSpinner.setSelection(3);
                    break;
            }

            currentlyCountingCheckBox.setChecked(Boolean.parseBoolean(currentCommodity.getCurrentlyCounting()));

        }*/

    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_add_commodity_dialog, null);
        builder.setView(v);

        minimumFamilySizeSpinner = v.findViewById(R.id.editLargeFamilyMinimumSpinner);

        String[] array = {"1", "2-3", "4-5", "6+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_list_item_1, array);
        minimumFamilySizeSpinner.setAdapter(adapter);


        //productNameEditText.setText("UFDBUFSB");
        //System.out.println(productNameEditText.getText().toString());

        builder.setTitle("Add a New Commodity");
        builder.setPositiveButton("Add Commodity", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                skuEditText = ((AlertDialog)dialog).findViewById(R.id.editskuTextBox);
                productNameEditText = ((AlertDialog)dialog).findViewById(R.id.editNameTextBox);
                perBoxEditText = ((AlertDialog)dialog).findViewById(R.id.editPerBoxTextBox);
                minimumFamilySizeSpinner = ((AlertDialog)dialog).findViewById(R.id.editLargeFamilyMinimumSpinner);
                currentlyCountingCheckBox = ((AlertDialog)dialog).findViewById(R.id.currentlyCountingCheckBox);

                Commodity commodity = new Commodity();

                commodity.setSku(skuEditText.getText().toString());
                commodity.setProductName(productNameEditText.getText().toString());
                commodity.setDistributionPerBox(Integer.valueOf(perBoxEditText.getText().toString()));
                commodity.setCurrentlyCounting(currentlyCountingCheckBox.isChecked());

                switch (minimumFamilySizeSpinner.getSelectedItemPosition()){
                    case 0:
                        commodity.setLargeFamilyThreshold(ONE);
                        break;

                    case 1:
                        commodity.setLargeFamilyThreshold(TWOTOTHREE);
                        break;

                    case 2:
                        commodity.setLargeFamilyThreshold(FOURTOFIVE);
                        break;

                    case 3:
                        commodity.setLargeFamilyThreshold(SIXPLUS);
                        break;
                }

                FileManager.currentCommodities.add(commodity);
                FileManager.adapter.notifyDataSetChanged();
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });




        return builder.create();
    }

    public Commodity getCommodity(){
        return new Commodity(skuEditText.getText().toString(), productNameEditText.getText().toString(),
                Integer.valueOf(perBoxEditText.getText().toString()), currentlyCountingCheckBox.isChecked());
    }

    public void updateCommodity(@Nullable Commodity commodity){
        currentCommodity = commodity;
    }
}
