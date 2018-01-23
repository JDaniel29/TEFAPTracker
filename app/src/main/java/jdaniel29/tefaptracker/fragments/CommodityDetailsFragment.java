package jdaniel29.tefaptracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.activities.Tracker;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.CommodityAdapter;
import jdaniel29.tefaptracker.data.FileManager;
import jdaniel29.tefaptracker.util.ActivityConstants;

public class CommodityDetailsFragment extends Fragment {
    EditText skuEditText, productNameEditText, perBoxEditText;
    Spinner minimumFamilySizeSpinner;
    CheckBox currentlyCountingCheckBox;

    Button submitButton, cancelButton;

    LinearLayout buttonHolder;

    CommodityAdapter adapter;

    Integer currentCommodityIndex;

    public CommodityDetailsFragment(){
        super();
    }

    public void setCurrentCommodityIndex(int newIndex){
        currentCommodityIndex = newIndex;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_commodity_details, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        submitButton = view.findViewById(R.id.confirmChangesButton);
        cancelButton = view.findViewById(R.id.cancelChangesButton);

        skuEditText = view.findViewById(R.id.editskuTextBox);
        productNameEditText = view.findViewById(R.id.editNameTextBox);
        perBoxEditText = view.findViewById(R.id.editPerBoxTextBox);

        minimumFamilySizeSpinner = view.findViewById(R.id.editLargeFamilyMinimumSpinner);
        currentlyCountingCheckBox = view.findViewById(R.id.currentlyCountingCheckBox);

        if(currentCommodityIndex != null) {
            skuEditText.setText(FileManager.currentCommodities.get(currentCommodityIndex).getSku());
            productNameEditText.setText(FileManager.currentCommodities.get(currentCommodityIndex).getProductName());
            perBoxEditText.setText(String.valueOf(FileManager.currentCommodities.get(currentCommodityIndex).getDistributionPerBox()));

            switch (FileManager.currentCommodities.get(currentCommodityIndex).getLargeFamilyThreshold()) {
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

            currentlyCountingCheckBox.setChecked(Boolean.parseBoolean(FileManager.currentCommodities.get(currentCommodityIndex).getCurrentlyCounting()));
        }

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Commodity commodity = new Commodity();

                commodity.setSku(skuEditText.getText().toString());
                commodity.setProductName(productNameEditText.getText().toString());
                commodity.setDistributionPerBox(Integer.valueOf(perBoxEditText.getText().toString()));

                commodity.setCurrentlyCounting(currentlyCountingCheckBox.isChecked());

                switch (minimumFamilySizeSpinner.getSelectedItemPosition()){
                    case 0:
                        commodity.setLargeFamilyThreshold(FileManager.Size.ONE);
                        break;

                    case 1:
                        commodity.setLargeFamilyThreshold(FileManager.Size.TWOTOTHREE);
                        break;

                    case 2:
                        commodity.setLargeFamilyThreshold(FileManager.Size.FOURTOFIVE);
                        break;

                    case 3:
                        commodity.setLargeFamilyThreshold(FileManager.Size.SIXPLUS);
                        break;
                }

                if(currentCommodityIndex != null){
                    FileManager.currentCommodities.set(currentCommodityIndex, commodity);
                } else {
                    FileManager.currentCommodities.add(commodity);
                }

                adapter.notifyDataSetChanged();
                replaceFragment();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((Tracker)getActivity()).setupButtonBarForMultiple();
                replaceFragment();
            }
        });
    }

    private void replaceFragment(){
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.trackerFragmentLayout, new AllCommodityDisplayFragment());
        transaction.commit();
    }

    public void setAdapter(CommodityAdapter commodityAdapter){
        adapter = commodityAdapter;
    }



}
