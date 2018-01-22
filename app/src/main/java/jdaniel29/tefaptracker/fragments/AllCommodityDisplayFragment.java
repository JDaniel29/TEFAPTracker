package jdaniel29.tefaptracker.fragments;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.data.Commodity;
import jdaniel29.tefaptracker.data.CommodityAdapter;
import jdaniel29.tefaptracker.data.FileManager;

public class AllCommodityDisplayFragment extends Fragment {
    ListView commodityListView;
    Button createNewCommodity;



    CommodityAdapter adapter;

    public AllCommodityDisplayFragment(){

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View inflatedView = inflater.inflate(R.layout.fragment_all_commodity_display, container, false);


        commodityListView  = inflatedView.findViewById(R.id.allCommodityListView);
        createNewCommodity = inflatedView.findViewById(R.id.createNewCommodityButton);

        createNewCommodity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(inflater.getContext(), "LAUNCHING CREATE NEW DIALOG", Toast.LENGTH_SHORT).show();
                showAddCommodityDialog();
            }
        });

        return inflatedView;
    }

    public void setupListView(){
        adapter = new CommodityAdapter(getContext(), FileManager.currentCommodities);
        commodityListView.setAdapter(adapter);
    }

    public CommodityAdapter getAdapter(){
        return adapter;
    }

    private void showAddCommodityDialog(){


        final CommodityDetailsFragment commodityDetailsFragment = new CommodityDetailsFragment(getAdapter());

        commodityDetailsFragment.show(getActivity().getSupportFragmentManager(), "Commodity Maker");


        /*
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Commodity commodity = new Commodity();

                System.out.println(commodityDetailsFragment.getCommodity().getProductName());
            }
        });
        builder.create().show();*/
    }
}
