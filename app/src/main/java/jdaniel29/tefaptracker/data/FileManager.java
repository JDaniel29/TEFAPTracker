package jdaniel29.tefaptracker.data;

import android.Manifest;
import android.app.Activity;
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
import java.util.ArrayList;

public class FileManager {

    public static File currentFileDir = new File(Environment.getExternalStorageDirectory() +
                                                    File.separator + "Food Tracker");
    public static File currentFile;

    private static final String[] vars = {"sku", "productName", "distributionSizeOne", "distributionSizeTwoToThree",
            "distributionSizeFourToFive", "distributionSizeSixToSeven", "distributionTotal", "distributionPerBox"};

    private static final String[] headers = {"SKU Number", "Product Name", "Size 1", "Size 2-3", "Size 4-5", "Size 6-7", "Total", "Per Box"};

    private static final CellProcessor[] processors = {new NotNull(), new NotNull(), new ParseInt(), new ParseInt(),
                                                       new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt()};

    public static ArrayList<Commodity> currentCommodities = new ArrayList<>();
    private FileManager(){

    }

    public static void setupDirectory(){

        if(!currentFileDir.exists()){

            if(currentFileDir.mkdir()){
                System.out.println(currentFileDir.getPath() + " succesfully created.");
            } else {
                System.out.println(currentFileDir.getPath() + " creation failed.");
            }

        }


    }

    public static void writeFile(Commodity[] commodities) throws Exception{
        ICsvBeanWriter writer;
        System.out.println(currentFile.exists());

        writer = new CsvBeanWriter(new FileWriter(currentFile, false), CsvPreference.STANDARD_PREFERENCE);
        writer.writeHeader(headers);


        if(commodities == null){
            writer.close();
            return;
        }

        for(Commodity commodity : commodities){
            writer.write(commodity, vars, processors);
        }

        writer.close();
    }

    private static void writeFile(Commodity commodity) throws Exception{
        Commodity[] commodities = {commodity};
        writeFile(commodities);
    }

    private static void readFile() throws Exception{
        ICsvBeanReader reader = new CsvBeanReader(new FileReader(currentFile), CsvPreference.STANDARD_PREFERENCE);

        Commodity currentCommodity;

        currentCommodities = new ArrayList<>();
        reader.getHeader(true);
        while ((currentCommodity = reader.read(Commodity.class, vars, processors)) != null){
            System.out.println(currentCommodity.toString());
            currentCommodities.add(currentCommodity);
        }


    }

    public static void requestPermissions(Activity activity){
        String[] storagePermissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};

        if(ContextCompat.checkSelfPermission(activity, storagePermissions[0]) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(activity, storagePermissions[1]) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(activity, storagePermissions, 1);
        } else {
            System.out.println("WE HAVE THE PERMISSIONS!");
            FileManager.setupDirectory();
        }

    }

    private static void launchFileNamer(final Activity activity){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final EditText editText = new EditText(activity);

        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);

        editText.setLayoutParams(lp);
        alert.setView(editText);
        alert.setTitle("Enter a File Name");
        alert.setNeutralButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(editText.getText().toString().contains(".csv")){
                    currentFile = new File(currentFileDir, editText.getText().toString());
                } else {
                    currentFile = new File(currentFileDir, editText.getText().toString() + ".csv");
                }

                System.out.println(currentFile.getPath() + " Succesfully Created");

                try {
                    writeFile((Commodity)null);
                    readFile();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }

                listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
                listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
                dialogInterface.dismiss();
            }
        });
        alert.create().show();

    }

    public static void listFiles(final Activity activity){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final File[] createdFiles = currentFileDir.listFiles();
        CharSequence[] fileNames = new CharSequence[createdFiles.length+1];

        if(currentFile != null){
            Commodity[] commodities = new Commodity[currentCommodities.size()];
            try {
                writeFile(currentCommodities.toArray(commodities));
            } catch (Exception e){
                System.out.println(e.getMessage());
            }
        }

        for(int i = 0; i < createdFiles.length; i++){
            fileNames[i] = createdFiles[i].getName();
        }

        fileNames[createdFiles.length] = "Create a New File";

        alert.setItems(fileNames, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if(i < createdFiles.length) {
                    currentFile = currentFileDir.listFiles()[i];
                    System.out.println(currentFile.getPath());
                    dialogInterface.dismiss();
                    try {
                        FileManager.readFile();
                    }catch (Exception e){
                        System.out.println(e.getMessage());
                    }
                    listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
                } else {
                    launchFileNamer(activity);
                }
            }
        });
        alert.create().show();

    }

    private static void listCommodities(final Activity activity, ListView listView){
        /*
        try {
            FileManager.readFile();
        } catch (Exception e){
            Toast.makeText(activity, "Error Reading File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            return;
        }*/
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

        if(currentCommodities.size() == 0 || currentCommodities == null){
            Toast.makeText(activity, "No Commodities in File", Toast.LENGTH_SHORT).show();
        }

        String[] commodityNames = new String[currentCommodities.size()+1];
        for(int i = 0; i < currentCommodities.size(); i++){
            commodityNames[i] = currentCommodities.get(i).getProductName() + "   -   " + currentCommodities.get(i).getDistributionTotal() + " Distributed.";
        }
        commodityNames[currentCommodities.size()] = "Add New Product";

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
            }
        });

        Button add1Button = (Button)dialog.findViewById(R.id.add1Commodity),
                add23Button = (Button)dialog.findViewById(R.id.add23Commodity),
                add45Button = (Button)dialog.findViewById(R.id.add45Commodity),
                add67Button = (Button)dialog.findViewById(R.id.add67Commodity);

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

    public static void showAddProductDialog(final Activity activity){
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setTitle("Add Commodity");

        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_add_commodity, null);
        alert.setView(view);

        final AlertDialog dialog = alert.create();
        dialog.show();

        Button submitButton = (Button)dialog.findViewById(R.id.addProductConfirmation);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
                dialog.findViewById(R.id.skuTextBox);

                String SKU = ((EditText)(dialog.findViewById(R.id.skuTextBox))).getText().toString();
                String commodityName = ((EditText)(dialog.findViewById(R.id.nameTextBox))).getText().toString();
                Integer perBox = Integer.valueOf(((EditText)(dialog.findViewById(R.id.perBoxTextBox))).getText().toString());

                Commodity commodity = new Commodity(SKU, commodityName, perBox);
                addProduct(activity, commodity);
            }
        });
    }

    private static void addProduct(Activity activity, Commodity commodity){
        currentCommodities.add(commodity);
        System.out.println(currentCommodities.size());
        listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
    }

    public static void incrementAllProducts(Activity activity, int category){
        for(Commodity commodity : currentCommodities){
            //System.out.println(commodity.toString());
            switch(category){
                case 1:
                    commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() + commodity.getDistributionPerBox());
                    break;
                case 23:
                    commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() + commodity.getDistributionPerBox());
                    break;
                case 45:
                    commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() + commodity.getDistributionPerBox());
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
            listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
        }catch (Exception e){
            Toast.makeText(activity, "Error Writing File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

    public static void decrementAllProducts(Activity activity, int category){
        for(Commodity commodity : currentCommodities){
            switch(category){
                case 1:
                    commodity.setDistributionSizeOne(commodity.getDistributionSizeOne() - commodity.getDistributionPerBox());
                    break;
                case 23:
                    commodity.setDistributionSizeTwoToThree(commodity.getDistributionSizeTwoToThree() - commodity.getDistributionPerBox());
                    break;
                case 45:
                    commodity.setDistributionSizeFourToFive(commodity.getDistributionSizeFourToFive() - commodity.getDistributionPerBox());
                    break;
                case 67:
                    commodity.setDistributionSizeSixToSeven(commodity.getDistributionSizeSixToSeven() - commodity.getDistributionPerBox());
                    break;
            }
            commodity.updateDistributionTotal();
            //commodity.setDistributionTotal(commodity.getDistributionTotal() - commodity.getDistributionPerBox());
        }


        try {
            listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
        }catch (Exception e){
            Toast.makeText(activity, "Error Writing File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }

}
