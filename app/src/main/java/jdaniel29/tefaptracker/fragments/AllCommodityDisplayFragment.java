package jdaniel29.tefaptracker.fragments;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;
import jdaniel29.tefaptracker.R;
import jdaniel29.tefaptracker.activities.Tracker;
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


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        final AllCommodityDisplayFragment allCommodityDisplayFragment = this;

        adapter = new CommodityAdapter(getContext(), FileManager.currentCommodities);
        commodityListView.setAdapter(adapter);

        FileManager.adapter = adapter;

        commodityListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();

                CommodityDetailsFragment commodityDetailsFragment = new CommodityDetailsFragment();
                commodityDetailsFragment.setCurrentCommodityIndex((int)(id));
                commodityDetailsFragment.setAdapter(adapter);

                transaction.replace(R.id.trackerFragmentLayout, commodityDetailsFragment);
                transaction.commit();

                ((Tracker)getActivity()).setupButtonBarForSingle((int)(id));
                FileManager.currentIndex = (int)(id);



            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();

        System.out.println("onResume() ran");
    }

    public CommodityAdapter getAdapter(){
        return adapter;
    }


    private void showAddCommodityDialog(){


        final AddCommodityFragmentDialog addCommodityFragmentDialog = new AddCommodityFragmentDialog();
        addCommodityFragmentDialog.show(getActivity().getSupportFragmentManager(), "Commodity Maker");


        /*
        builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Commodity commodity = new Commodity();

                System.out.println(addCommodityFragmentDialog.getCommodity().getProductName());
            }
        });
        builder.create().show();*/
    }
}
