package jdaniel29.tefaptracker.data;

import android.app.Activity;
import android.os.Environment;
import android.view.View;
import android.widget.*;
import android.R.layout;
import jdaniel29.tefaptracker.fragments.AddCommodityFragmentDialog;
import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseBool;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseInt;
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
    public static CommodityAdapter adapter;

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
    private static final String[] localCommodityVars = {"sku", "productName", "distributionSizeOne", "distributionSizeTwoToThree",
            "distributionSizeFourToFive", "distributionSizeSixPlus", "distributionTotal", "distributionPerBox",
            "largeFamilyThreshold", "currentlyCounting"};

    /*
     * The localCommodityHeaders for readability by normal people. This is displayed in the CSV file.
     */
    private static final String[] localCommodityHeaders = {"SKU Number", "Product Name", "Size 1", "Size 2-3", "Size 4-5",
            "Size 6+", "Total", "Per Box", "Large Family Threshold", "Currently Counting"};


    /*
     * This is how each piece of data in the csv file will be interpreted. The ConvertNullTos
     * are used to avoid null errors when parsing the data.
     */
    private static final CellProcessor[] localCommodityProcessors = {new ConvertNullTo(""), new ConvertNullTo(""),
                                                        new ParseInt(), new ParseInt(),
                                                       new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt(),
                                                        new ParseEnum(Size.class), new ParseBool()};

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

    public static int currentIndex;
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
     * This takes an array of commodities and writes them with the localCommodityProcessors. If there are no commodities to write
     * only the header is written.
     *
     *
     * @throws Exception
     * A file IO Exception may occur just cause we are dealing with file I/O.
     */
    public static void writeFile() throws Exception{
        ICsvBeanWriter writer; //The writer we are going to use

        //Append is set to false because its easier to start from scratch rather than try to compare what exists
        //and what doesn't.
        writer = new CsvBeanWriter(new FileWriter(currentFile, false), CsvPreference.STANDARD_PREFERENCE);
        writer.writeHeader(localCommodityHeaders);


        if(currentCommodities.size() == 0){
            writer.close();
            return;
        }

        for(Commodity commodity : currentCommodities){
            writer.write(commodity, localCommodityVars, localCommodityProcessors);
        }

        writer.close(); //THIS MUST BE CALLED OR NOTHING WILL BE SAVED
    }

    /**
     * This method will read the file and store the current commodities in the arrayList. If there are no commodities,
     * then we will simply have an empty arraylist.
     *
     * @throws Exception
     * A file IO Exception may occur just cause we are dealing with file I/O. (Seem to notice a trend here)
     */
    public static void readDistributionFile() throws Exception{
        ICsvBeanReader reader = new CsvBeanReader(new FileReader(currentFile), CsvPreference.STANDARD_PREFERENCE);

        Commodity currentCommodity;

        currentCommodities = new ArrayList<>();
        String[] headers = reader.getHeader(true); //This moves the reader by one line

        while ((currentCommodity = reader.read(Commodity.class, localCommodityVars, localCommodityProcessors)) != null) {
                System.out.println(currentCommodity.toString());
                currentCommodities.add(currentCommodity);

        }

    }



}
