package jdaniel29.tefaptracker.data;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.*;
import android.R.layout;
import jdaniel29.tefaptracker.R;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.CsvBeanWriter;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.io.ICsvBeanWriter;
import org.supercsv.prefs.CsvPreference;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

import static jdaniel29.tefaptracker.data.FileManager.Size.*;

public class FileManager {

    //External Public Directory Home for the Files
    public static File currentFileDir = new File(Environment.getExternalStorageDirectory() +
                                                    File.separator + "Food Tracker");

    /**
     * Link to the current file we are working with. When the app starts, it is null because the user will
     * not have selected a file yet. When that occurs, it is assigned a value.
     *
     * Notice how I said link to the file. A file is not technically created until we write a value.
     */
    public static File currentFile;

    /*
     * Strings of the variable names that will be used so we know how to arrange our table values.
     */
    private static final String[] vars = {"sku", "productName", "distributionSizeOne", "distributionSizeTwoToThree",
            "distributionSizeFourToFive", "distributionSizeSixToSeven", "distributionTotal", "distributionPerBox", "largeFamilyThreshold"};

    /*
     * The headers for readability by normal people. This is displayed in the CSV file.
     */
    private static final String[] headers = {"SKU Number", "Product Name", "Size 1", "Size 2-3", "Size 4-5", "Size 6-7", "Total", "Per Box", "Large Family Threshold"};

    /*
     * This is how each piece of data in the csv file will be interpreted. The ConvertNullTos
     * are used to avoid null errors when parsing the data.
     */
    private static final CellProcessor[] processors = {new ConvertNullTo(""), new ConvertNullTo(""), new ParseInt(), new ParseInt(),
                                                       new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt(),
                                                        new ParseEnum(Size.class)};

    /*
     * This ArrayList will hold the commodities that the user is working with. They will be pulled
     * when the app starts and cleared for each new file pulled.
     */
    public static ArrayList<Commodity> currentCommodities = new ArrayList<>();

    /*
     * The different size thresholds that can be used for the commodities.
     */
    public enum Size {
        ONE, TWOTOTHREE, FOURTOFIVE, SIXPLUS;
    }

    /*
     * A private constructor is used here because no instances of FileManager need to be created. All
     * commands need to be called with FileManager.command().
     */
    private FileManager(){

    }

    /**
     * This creates the folder if it does not already exist in the file UI.
     */
    public static void setupDirectory(){

        if(!currentFileDir.exists()){

            if(currentFileDir.mkdir()){
                System.out.println(currentFileDir.getPath() + " succesfully created.");
            } else {
                System.out.println(currentFileDir.getPath() + " creation failed.");
            }

        }


    }

    /**
     * This takes an array of commodities and writes them with the processors. If there are no commodities to write
     * only the header is written.
     *
     * @param commodities
     * The commodities to be written to the file. Can be null if there is nothing to write.
     *
     * @throws Exception
     * A file IO Exception may occur just cause we are dealing with file I/O.
     */
    public static void writeFile(@Nullable Commodity[] commodities) throws Exception{
        ICsvBeanWriter writer; //The writer we are going to use

        //Append is set to false because its easier to start from scratch rather than try to compare what exists
        //and what doesn't.
        writer = new CsvBeanWriter(new FileWriter(currentFile, false), CsvPreference.STANDARD_PREFERENCE);
        writer.writeHeader(headers);


        if(commodities == null){
            writer.close();
            return;
        }

        for(Commodity commodity : commodities){
            writer.write(commodity, vars, processors);
        }

        writer.close(); //THIS MUST BE CALLED OR NOTHING WILL BE SAVED
    }

    /**
     * This method is pretty much the same as the one above just it will only write one commodity
     * instead of multiple commodities.
     * @param commodity
     * The comoodity to be written to the file.
     *
     * @throws Exception
     * A file IO Exception may occur just cause we are dealing with file I/O.
     */
    public static void writeFile(Commodity commodity) throws Exception{
        Commodity[] commodities = {commodity};
        writeFile(commodities);
    }

    /**
     * This method will read the file and store the current commodities in the arrayList. If there are no commodities,
     * then we will simply have an empty arraylist.
     *
     * @throws Exception
     * A file IO Exception may occur just cause we are dealing with file I/O. (Seem to notice a trend here)
     */
    public static void readFile() throws Exception{
        ICsvBeanReader reader = new CsvBeanReader(new FileReader(currentFile), CsvPreference.STANDARD_PREFERENCE);

        Commodity currentCommodity;

        currentCommodities = new ArrayList<>();
        String[] headers = reader.getHeader(true); //This moves the reader by one line


        /*
        *
        *So, this while loop is going to seem a little weird. What's happening is that we save the line we just read
        * to a variable then check to see if there is an actual value. While we could do this with 2 lines (one for
        * assigning and another for checking, I just ripped this from the tutorial cause I'm lazy
        */
        if(headers == oldVarsAndProcessors.oldHeaders1){ //Just to see if we have an old version of our commodities
            while ((currentCommodity = reader.read(Commodity.class, oldVarsAndProcessors.oldVars1, oldVarsAndProcessors.oldProcessors1)) != null){
                System.out.println(currentCommodity.toString());
                currentCommodities.add(currentCommodity);
            }
        } else {
            while ((currentCommodity = reader.read(Commodity.class, vars, processors)) != null) {
                System.out.println(currentCommodity.toString());
                currentCommodities.add(currentCommodity);
            }
        }

    }


    /**
     * This method takes the commodities that are in the arraylist and lists them in the
     * listView. Simple, right?
     *
     * @param activity
     * The current instance of the Tracker Activity.
     *
     * @param listView
     * The listview we are working with.
     */
    public static void listCommodities(final Activity activity, ListView listView){
        /*
        try {
            FileManager.readFile();
        } catch (Exception e){
            Toast.makeText(activity, "Error Reading File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            return;
        }*/
        //If there are no commodities, we only need to have one listing saying to add a new product
        if(currentCommodities == null){
            String[] commodityNames = {"Add New Product"};
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, layout.simple_list_item_1, commodityNames);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    showAddProductDialog(activity);
                }
            });
            return;
        }

        //If there aren't any commodities to be working with, then we just need to alert the user's there's not jack.
        if(currentCommodities.size() == 0 || currentCommodities == null){
            Toast.makeText(activity, "No Commodities in File", Toast.LENGTH_SHORT).show();
        }

        //We have an array of +1 elements cause the last listing will be to add a new product
        String[] commodityNames = new String[currentCommodities.size()+1];
        for(int i = 0; i < currentCommodities.size(); i++){
            commodityNames[i] = currentCommodities.get(i).getProductName() + "   -   " + currentCommodities.get(i).getDistributionTotal() + " Distributed.";
        }
        commodityNames[currentCommodities.size()] = "Add New Product";

        //The adapter to display the stuff in the listview
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, layout.simple_list_item_1, commodityNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if(i < currentCommodities.size()) {
                    showCommodityAlert(activity, currentCommodities.get((int) l));
                } else {
                    showAddProductDialog(activity);
                }
            }
        });


    }

    private static void showCommodityAlert(final Activity activity, final Commodity commodity){
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setTitle("Details for " + commodity.getProductName());

        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_commodity_details, null);
        alert.setView(view);

        final AlertDialog dialog = alert.create();
        dialog.show();

        dialog.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialogInterface) {
                listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
                updateProductCounts(activity);
            }
        });

        Button add1Button = (Button)dialog.findViewById(R.id.add1Commodity),
                add23Button = (Button)dialog.findViewById(R.id.add23Commodity),
                add45Button = (Button)dialog.findViewById(R.id.add45Commodity),
                add67Button = (Button)dialog.findViewById(R.id.add67Commodity),
                removeButton = (Button)dialog.findViewById(R.id.removeCommodity);

        final ToggleButton toggle = (ToggleButton)dialog.findViewById(R.id.switchIncrementAndDecrementToggle);

        final TextView textView = (TextView)dialog.findViewById(R.id.commodityDescriptionTextView);
        textView.setText(commodity.toString());

        add1Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int multiplier = (toggle.isChecked()) ? 1 : -1;
                commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + multiplier*commodity.getDistributionPerBox());
                commodity.updateDistributionTotal();
                textView.setText(commodity.toString());
            }
        });

        add23Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int multiplier = (toggle.isChecked()) ? 1 : -1;
                commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + multiplier*commodity.getDistributionPerBox());
                commodity.updateDistributionTotal();
                textView.setText(commodity.toString());
            }
        });

        add45Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int multiplier = (toggle.isChecked()) ? 1 : -1;
                commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + multiplier*commodity.getDistributionPerBox());
                commodity.updateDistributionTotal();
                textView.setText(commodity.toString());
            }
        });

        add67Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int multiplier = (toggle.isChecked()) ? 1 : -1;
                commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() + multiplier*commodity.getDistributionPerBox());
                commodity.updateDistributionTotal();
                textView.setText(commodity.toString());
            }
        });

        removeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                removeProduct(activity, commodity);
                dialog.dismiss();
            }
        });

        /*
        builder.setTitle("Details about " + commodity.getProductName());
        builder.setMessage(commodity.toString());
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });*/

    }

    private static void showAddProductDialog(final Activity activity){
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setTitle("Add Commodity");

        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_add_commodity, null);
        alert.setView(view);



        final AlertDialog dialog = alert.create();
        dialog.show();

        String[] array = {"1", "2-3", "4-5", "6+"};
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(view.getContext(), layout.simple_list_item_1, array);
        final Spinner spinner = (Spinner)dialog.findViewById(R.id.largeFamilyMinimumSpinner);
        spinner.setAdapter(adapter);

        Button submitButton = (Button)dialog.findViewById(R.id.addProductConfirmation);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.findViewById(R.id.skuTextBox);

                String SKU = ((EditText)(dialog.findViewById(R.id.skuTextBox))).getText().toString();
                String commodityName = ((EditText)(dialog.findViewById(R.id.nameTextBox))).getText().toString();
                Integer perBox = Integer.valueOf(((EditText)(dialog.findViewById(R.id.perBoxTextBox))).getText().toString());

                Commodity commodity = new Commodity();

                //spinner.setSelection(0);
                System.out.println(spinner.getSelectedItem().toString());
                switch (spinner.getSelectedItem().toString()){
                    case "1":
                        commodity = new Commodity(SKU, commodityName, perBox, ONE);
                        break;
                    case "2-3":
                        commodity = new Commodity(SKU, commodityName, perBox, TWOTOTHREE);
                        break;
                    case "4-5":
                        commodity = new Commodity(SKU, commodityName, perBox, FOURTOFIVE);
                        break;
                    case "6+":
                        commodity = new Commodity(SKU, commodityName, perBox, SIXPLUS);
                        break;
                }

                System.out.println(commodity.toString());
                addProduct(activity, commodity);
            }
        });
    }

    private static void addProduct(Activity activity, Commodity commodity){
        currentCommodities.add(commodity);
        System.out.println(currentCommodities.size());
        listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
    }

    private static void removeProduct(Activity activity, Commodity commodity){
        currentCommodities.remove(commodity);
        System.out.println(currentCommodities.size());
        listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
    }

    public static void incrementAllProducts(Activity activity, int category){


        for(Commodity commodity : currentCommodities){
            System.out.println("Category Increment" + category);
            switch(category){
                case 1:
                    if(commodity.getLargeFamilyThreshold() == ONE) {
                        commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + commodity.getDistributionPerBox());
                    }
                    break;
                case 23:
                    if(commodity.getLargeFamilyThreshold() == ONE || commodity.getLargeFamilyThreshold() == TWOTOTHREE){
                        commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + commodity.getDistributionPerBox());
                    }
                    break;
                case 45:
                    if(commodity.getLargeFamilyThreshold() == ONE || commodity.getLargeFamilyThreshold() == TWOTOTHREE || commodity.getLargeFamilyThreshold() == FOURTOFIVE ) {
                        commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + commodity.getDistributionPerBox());
                    }
                    break;
                case 67:
                    commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() + commodity.getDistributionPerBox());
                    break;
            }
            commodity.updateDistributionTotal();
            //System.out.println(commodity.toString());
            //commodity.setDistributionTotal(commodity.getDistributionTotal() + commodity.getDistributionPerBox());
        }


        try {
            updateProductCounts(activity);
            listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
        }catch (Exception e){
            Toast.makeText(activity, "Error Writing File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    public static void decrementAllProducts(Activity activity, int category){
        for(Commodity commodity : currentCommodities) {
            switch (category) {
                case 1:
                    if (commodity.getLargeFamilyThreshold() == ONE) {
                        commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() - commodity.getDistributionPerBox());
                    }
                    break;
                case 23:
                    if (commodity.getLargeFamilyThreshold() == ONE || commodity.getLargeFamilyThreshold() == TWOTOTHREE) {
                        commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() - commodity.getDistributionPerBox());
                    }
                    break;
                case 45:
                    if (commodity.getLargeFamilyThreshold() == ONE || commodity.getLargeFamilyThreshold() == TWOTOTHREE || commodity.getLargeFamilyThreshold() == FOURTOFIVE) {
                        commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() - commodity.getDistributionPerBox());
                    }
                    break;
                case 67:
                    commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() - commodity.getDistributionPerBox());
                    break;

            }

            commodity.updateDistributionTotal();
        }


        try {
            updateProductCounts(activity);
            listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
        }catch (Exception e){
            Toast.makeText(activity, "Error Writing File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    public static void updateProductCounts(Activity activity){
        TextView totalOneCommoditiesTextView      = (TextView)activity.findViewById(R.id.totalOneCommodities),
                 totalTwoThreeCommoditiesTextView = (TextView)activity.findViewById(R.id.totalTwoThreeCommodities),
                 totalFourFiveCommoditiesTextView = (TextView)activity.findViewById(R.id.totalFourFiveCommodities),
                 totalSixPlusCommoditiesTextView  = (TextView)activity.findViewById(R.id.totalSixPlusCommodities);

        int totalOneCommodities      = 0,
            totalTwoThreeCommodities = 0,
            totalFourFiveCommodities = 0,
            totalSixPlusCommodities  = 0;

        for(Commodity commodity : currentCommodities){
            totalOneCommodities      += commodity.getDistributionSizeOne();
            totalTwoThreeCommodities += commodity.getDistributionSizeTwoToThree();
            totalFourFiveCommodities += commodity.getDistributionSizeFourToFive();
            totalSixPlusCommodities  += commodity.getDistributionSizeSixToSeven();
        }

        totalOneCommoditiesTextView.setText(String.valueOf(totalOneCommodities));
        totalTwoThreeCommoditiesTextView.setText(String.valueOf(totalTwoThreeCommodities));
        totalFourFiveCommoditiesTextView.setText(String.valueOf(totalFourFiveCommodities));
        totalSixPlusCommoditiesTextView.setText(String.valueOf(totalSixPlusCommodities));
    }

}
