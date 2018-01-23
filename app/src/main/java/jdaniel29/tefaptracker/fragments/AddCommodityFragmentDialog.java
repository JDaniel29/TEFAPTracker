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


public class AddCommodityFragmentDialog extends DialogFragment {
    EditText skuEditText, productNameEditText, perBoxEditText;
    Spinner minimumFamilySizeSpinner;
    CheckBox currentlyCountingCheckBox;

    LinearLayout buttonHolder;

    CommodityAdapter adapter;

    Commodity currentCommodity = null;

    View inflatedView;
    public AddCommodityFragmentDialog(){

    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*
        inflatedView = inflater.inflate(R.layout.fragment_add_commodity_dialog, container, false);

        skuEditText         = (EditText) inflatedView.findViewById(R.id.editskuTextBox);
        productNameEditText = (EditText) inflatedView.findViewById(R.id.editNameTextBox);
        perBoxEditText      = (EditText) inflatedView.findViewById(R.id.editPerBoxTextBox);

        minimumFamilySizeSpinner = (Spinner) inflatedView.findViewById(R.id.editLargeFamilyMinimumSpinner);

        currentlyCountingCheckBox = (CheckBox) inflatedView.findViewById(R.id.currentlyCountingCheckBox);

        return inflatedView;*/
        return inflater.inflate(R.layout.fragment_commodity_details, container, false);
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
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.fragment_commodity_details, null);
        builder.setView(v);


        //productNameEditText.setText("UFDBUFSB");
        //System.out.println(productNameEditText.getText().toString());

        builder.setTitle("Add a New Commodity");
        builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                skuEditText         = (EditText) ((AlertDialog)dialog).findViewById(R.id.editskuTextBox);
                productNameEditText = (EditText) ((AlertDialog)dialog).findViewById(R.id.editNameTextBox);
                perBoxEditText      = (EditText) ((AlertDialog)dialog).findViewById(R.id.editPerBoxTextBox);

                minimumFamilySizeSpinner = (Spinner) ((AlertDialog)dialog).findViewById(R.id.editLargeFamilyMinimumSpinner);

                currentlyCountingCheckBox = (CheckBox) ((AlertDialog)dialog).findViewById(R.id.currentlyCountingCheckBox);

                Commodity commodity = new Commodity(skuEditText.getText().toString(), productNameEditText.getText().toString(),
                                                    Integer.valueOf(perBoxEditText.getText().toString()), currentlyCountingCheckBox.isChecked());

                FileManager.currentCommodities.add(commodity);

                adapter.notifyDataSetChanged();
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
