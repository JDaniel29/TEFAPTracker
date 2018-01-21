package jdaniel29.tefaptracker.fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;


public class CommodityDetailsFragment extends DialogFragment {
    EditText skuEditText, productNameEditText, perBoxEditText;
    Spinner minimumFamilySizeSpinner;
    CheckBox currentlyCountingCheckBox;

    public CommodityDetailsFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        /*View inflatedView = inflater.inflate(R.layout.fragment_commodity_details, container, false);

        skuEditText         = (EditText) inflatedView.findViewById(R.id.editskuTextBox);
        productNameEditText = (EditText) inflatedView.findViewById(R.id.editNameTextBox);
        perBoxEditText      = (EditText) inflatedView.findViewById(R.id.editPerBoxTextBox);

        minimumFamilySizeSpinner = (Spinner) inflatedView.findViewById(R.id.editLargeFamilyMinimumSpinner);

        currentlyCountingCheckBox = (CheckBox) inflatedView.findViewById(R.id.currentlyCountingCheckBox);

        return inflatedView;*/
        return null;
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }

    public void updateCommodity(Commodity commodity){
        skuEditText.setText(commodity.getSku());
        productNameEditText.setText(commodity.getProductName());
        perBoxEditText.setText(commodity.getDistributionPerBox());

        switch (commodity.getLargeFamilyThreshold()){
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

        currentlyCountingCheckBox.setChecked(Boolean.parseBoolean(commodity.getCurrentlyCounting()));

    }
}
