package jdaniel29.tefaptracker.data;

import org.supercsv.cellprocessor.ConvertNullTo;
import org.supercsv.cellprocessor.ParseEnum;
import org.supercsv.cellprocessor.ParseInt;
import org.supercsv.cellprocessor.ift.CellProcessor;

public class oldVarsAndProcessors {


    public static final String[] oldVars1 = {"sku", "productName", "distributionSizeOne", "distributionSizeTwoToThree",
            "distributionSizeFourToFive", "distributionSizeSixToSeven", "distributionTotal", "distributionPerBox"};

    public static final String[] oldHeaders1 = {"SKU Number", "Product Name", "Size 1", "Size 2-3", "Size 4-5", "Size 6-7", "Total", "Per Box"};

    public static final CellProcessor[] oldProcessors1 = {new ConvertNullTo(""), new ConvertNullTo(""), new ParseInt(), new ParseInt(),
            new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt(), new ParseEnum(FileManager.Size.class)};

    public static final String[] oldVars2 = {"sku", "productName", "distributionSizeOne", "distributionSizeTwoToThree",
            "distributionSizeFourToFive", "distributionSizeSixToSeven", "distributionTotal", "distributionPerBox", "largeFamilyThreshold"};

    /*
     * The headers for readability by normal people. This is displayed in the CSV file.
     */
    public static final String[] oldHeaders2 = {"SKU Number", "Product Name", "Size 1", "Size 2-3", "Size 4-5", "Size 6-7", "Total", "Per Box", "Large Family Threshold"};

    /*
     * This is how each piece of data in the csv file will be interpreted. The ConvertNullTos
     * are used to avoid null errors when parsing the data.
     */
    public static final CellProcessor[] oldProcessors2 = {new ConvertNullTo(""), new ConvertNullTo(""), new ParseInt(), new ParseInt(),
            new ParseInt(), new ParseInt(), new ParseInt(), new ParseInt(),
            new ParseEnum(FileManager.Size.class)};
}
