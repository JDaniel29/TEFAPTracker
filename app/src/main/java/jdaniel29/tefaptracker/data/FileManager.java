package jdaniel29.tefaptracker.data;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
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
import jdaniel29.tefaptracker.Tracker;
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
import java.security.Permission;
import java.util.ArrayList;

public class FileManager {

    public static File currentFileDir = new File(Environment.getExternalStorageDirectory() +
                                                    File.separator + "Food Tracker");
    public static File currentFile;

    private static final String[] vars = {"sku", "productName", "distributionSizeOneToTwo", "distributionSizeThreeToFour",
            "distributionSizeFiveToSix", "distributionSizeSevenToEight", "distributionTotal", "distributionPerBox"};

    private static final String[] headers = {"SKU Number", "Product Name", "Size 1-2", "Size 3-4", "Size 5-6", "Size 7-8", "Total", "Per Box"};

    private static final CellProcessor[] processors = {new NotNull(), new NotNull(), new ParseInt(), new ParseInt(),
                                                       new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt()};

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

        if(currentFile.exists()) {
            writer = new CsvBeanWriter(new FileWriter(currentFile, true), CsvPreference.STANDARD_PREFERENCE);
        } else {
            writer = new CsvBeanWriter(new FileWriter(currentFile), CsvPreference.STANDARD_PREFERENCE);
            writer.writeHeader(headers);
        }

        if(commodities == null){
            writer.close();
            return;
        }

        for(Commodity commodity : commodities){
            writer.write(commodity, vars, processors);
        }

        writer.close();
    }

    public static void writeFile(Commodity commodity) throws Exception{
        Commodity[] commodities = {commodity};
        writeFile(commodities);
    }

    public static void createFile() throws Exception{
        ICsvBeanWriter writer;
        writer = new CsvBeanWriter(new FileWriter(currentFile), CsvPreference.STANDARD_PREFERENCE);
        writer.writeHeader(headers);
        writer.close();
    }

    public static Commodity[] readFile() throws Exception{
        ICsvBeanReader reader = new CsvBeanReader(new FileReader(currentFile), CsvPreference.STANDARD_PREFERENCE);

        Commodity currentCommodity;

        ArrayList<Commodity> commodityArrayList = new ArrayList<>();

        reader.getHeader(true);
        while ((currentCommodity = reader.read(Commodity.class, vars, processors)) != null){
            commodityArrayList.add(currentCommodity);
        }

        Commodity[] returnArray = new Commodity[commodityArrayList.size()];
        return commodityArrayList.toArray(returnArray);
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

    private static void launchFilePicker(Activity activity){
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
                    createFile();
                } catch (Exception e){
                    System.out.println(e.getMessage());
                }
                dialogInterface.dismiss();
            }
        });
        alert.create().show();

    }

    public static void listFiles(final Activity activity){
        AlertDialog.Builder alert = new AlertDialog.Builder(activity);
        final File[] createdFiles = currentFileDir.listFiles();
        CharSequence[] fileNames = new CharSequence[createdFiles.length+1];

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
                } else {
                    launchFilePicker(activity);
                }
                listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
            }
        });
        alert.create().show();

    }

    private static void listCommodities(final Activity activity, ListView listView){
        final Commodity[] commodities;

        try {
            commodities = FileManager.readFile();
        } catch (Exception e){
            Toast.makeText(activity, "Error Reading File", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
            return;
        }

        if(commodities.length == 0){
            Toast.makeText(activity, "No Commodities in File", Toast.LENGTH_SHORT).show();
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, layout.simple_list_item_1, new String[0]);
            listView.setAdapter(adapter);
            return;
        }

        String[] commodityNames = new String[commodities.length];
        for(int i = 0; i < commodities.length; i++){
            commodityNames[i] = commodities[i].getProductName();
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, layout.simple_list_item_1, commodityNames);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                showCommodityAlert(activity, commodities[(int)l]);
            }
        });


    }

    private static void showCommodityAlert(Activity activity, Commodity commodity){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
        builder.setTitle("Details about " + commodity.getProductName());
        builder.setMessage(commodity.toString());
        builder.setNeutralButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        builder.create().show();
    }

    public static void showAddProductDialog(final Activity activity){
        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(activity);
        alert.setTitle("Add Commodity");


        LayoutInflater inflater = activity.getLayoutInflater();
        final View view = inflater.inflate(R.layout.layout_add_commodity, null);
        alert.setView(view);

        alert.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
                view.findViewById(R.id.skuTextBox);

                String SKU = ((EditText)(view.findViewById(R.id.skuTextBox))).getText().toString();
                String commodityName = ((EditText)(view.findViewById(R.id.nameTextBox))).getText().toString();
                Integer perBox = Integer.valueOf(((EditText)(view.findViewById(R.id.perBoxTextBox))).getText().toString());

                Commodity commodity = new Commodity(SKU, commodityName, perBox);
                addProduct(activity, commodity);
                listCommodities(activity, (ListView)activity.findViewById(R.id.screenListView));
            }
        });

        alert.create().show();

    }

    private static void addProduct(Activity activity, Commodity commodity){
        try {
            FileManager.writeFile(commodity);
        } catch (Exception e){
            Toast.makeText(activity, "File Writing Exception Thrown", Toast.LENGTH_SHORT).show();
            System.out.println(e.getMessage());
        }
    }
}
