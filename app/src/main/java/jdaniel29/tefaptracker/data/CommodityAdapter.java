package jdaniel29.tefaptracker.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import jdaniel29.tefaptracker.R;

import java.util.ArrayList;

public class CommodityAdapter extends ArrayAdapter<Commodity> {

    public CommodityAdapter(Context context, ArrayList<Commodity> commodities){
        super(context, R.layout.item_commodity,  commodities);
    }

    public View getView(int position, View convertView, ViewGroup parent){
        Commodity commodity = getItem(position);

        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_commodity, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.commodityDetailsTextView);
        textView.setText(commodity.getProductName() + ": " + commodity.getDistributionTotal() + " item(s) dispensed");
        return convertView;
    }



    @Override
    public void notifyDataSetChanged() {
        super.notifyDataSetChanged();
        System.out.println("NOTIFY CHANGED");
    }
}
