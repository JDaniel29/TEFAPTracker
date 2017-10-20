package jdaniel29.tefaptracker.data;

public class Commodity {
    private String sku, productName;
    private int distributionSizeOne,
                distributionSizeTwoToThree,
                distributionSizeFourToFive,
                distributionSizeSixToSeven,
                distributionTotal,
                distributionPerBox;

    private FileManager.Size largeFamilyThreshold;


    public Commodity(){
        sku                          = "UNASSIGNED";
        productName                  = "UNASSIGNED";
        distributionSizeOne = 0;
        distributionSizeTwoToThree = 0;
        distributionSizeFourToFive = 0;
        distributionSizeSixToSeven = 0;
        distributionTotal            = 0;
        distributionPerBox           = 0;
    }

    public Commodity(String sku, String productName, int distributionPerBox) {
        //this.sku = (sku1 == null) ? sku1 : "";
        //this.productName = (productName1 == null) ? productName1 : "";

        this.sku = (sku.length() == 0) ? " " : productName;
        this.productName = (productName.length() == 0) ? " " : productName;
        this.distributionPerBox = distributionPerBox;

    }

    public Commodity(String sku, String productName, int distributionPerBox, FileManager.Size largeFamilyThreshold) {
        this.sku = sku;
        this.productName = productName;
        this.distributionPerBox = distributionPerBox;
        this.largeFamilyThreshold = largeFamilyThreshold;
    }

    @Override
    public String toString() {
        return "SKU: " + this.sku +
                "\nName: " + this.productName +
                "\nDistribution 1: " + this.distributionSizeOne +
                "\nDistribution 2-3: " + this.distributionSizeTwoToThree +
                "\nDistribution 4-5: " + this.distributionSizeFourToFive +
                "\nDistribution 6-7: " + this.distributionSizeSixToSeven +
                "\nDistribution Per Box: " + this.distributionPerBox +
                "\nDistribution Total: " + this.distributionTotal +
                "\nMinimum Distribution Size: " + this.largeFamilyThreshold;
    }

    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public int getDistributionSizeOne() {
        return distributionSizeOne;
    }

    public void setDistributionSizeOne(int distributionSizeOne) {
        this.distributionSizeOne = distributionSizeOne;
    }

    public int getDistributionSizeTwoToThree() {
        return distributionSizeTwoToThree;
    }

    public void setDistributionSizeTwoToThree(int distributionSizeTwoToThree) {
        this.distributionSizeTwoToThree = distributionSizeTwoToThree;
    }

    public int getDistributionSizeFourToFive() {
        return distributionSizeFourToFive;
    }

    public void setDistributionSizeFourToFive(int distributionSizeFourToFive) {
        this.distributionSizeFourToFive = distributionSizeFourToFive;
    }

    public int getDistributionSizeSixToSeven() {
        return distributionSizeSixToSeven;
    }

    public void setDistributionSizeSixToSeven(int distributionSizeSixToSeven) {
        this.distributionSizeSixToSeven = distributionSizeSixToSeven;
    }

    public int getDistributionPerBox() {
        return distributionPerBox;
    }

    public void setDistributionPerBox(int distributionPerBox) {
        this.distributionPerBox = distributionPerBox;
    }

    public int getDistributionTotal() {
        return distributionTotal;
    }

    public void updateDistributionTotal(){
        this.distributionTotal = this.distributionSizeOne + this.distributionSizeTwoToThree + this.distributionSizeFourToFive + this.distributionSizeSixToSeven;
    }

    public void setDistributionTotal(int distributionTotal) {
        this.distributionTotal = distributionTotal;
    }

    public FileManager.Size getLargeFamilyThreshold() {
        return largeFamilyThreshold;
    }

    public void setLargeFamilyThreshold(FileManager.Size largeFamilyThreshold) {
        this.largeFamilyThreshold = largeFamilyThreshold;
    }

}