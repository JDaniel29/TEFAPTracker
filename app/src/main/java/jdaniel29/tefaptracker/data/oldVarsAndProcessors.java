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
}
