package jdaniel29.tefaptracker.data;

public class Commodity {
    private String sku, productName;
    private int distributionSizeOneToTwo,
                distributionSizeThreeToFour,
                distributionSizeFiveToSix,
                distributionSizeSevenToEight,
                distributionTotal,
                distributionPerBox;

    public Commodity(){
        sku                          = "UNASSIGNED";
        productName                  = "UNASSIGNED";
        distributionSizeOneToTwo     = 0;
        distributionSizeThreeToFour  = 0;
        distributionSizeFiveToSix    = 0;
        distributionSizeSevenToEight = 0;
        distributionTotal            = 0;
        distributionPerBox           = 0;
    }

    public Commodity(String sku, String productName, int distributionPerBox) {
        this.sku = sku;
        this.productName = productName;
        this.distributionPerBox = distributionPerBox;
    }

    @Override
    public String toString() {
        return "SKU: " + this.sku +
                "\nName: " + this.productName +
                "\nDistribution 1-2: " + this.distributionSizeOneToTwo +
                "\nDistribution 3-4: " + this.distributionSizeThreeToFour +
                "\nDistribution 5-6: " + this.distributionSizeFiveToSix +
                "\nDistribution 7-8: " + this.distributionSizeSevenToEight +
                "\nDistribution Total: " + this.distributionTotal;
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

    public int getDistributionSizeOneToTwo() {
        return distributionSizeOneToTwo;
    }

    public void setDistributionSizeOneToTwo(int distributionSizeOneToTwo) {
        this.distributionSizeOneToTwo = distributionSizeOneToTwo;
    }

    public int getDistributionSizeThreeToFour() {
        return distributionSizeThreeToFour;
    }

    public void setDistributionSizeThreeToFour(int distributionSizeThreeToFour) {
        this.distributionSizeThreeToFour = distributionSizeThreeToFour;
    }

    public int getDistributionSizeFiveToSix() {
        return distributionSizeFiveToSix;
    }

    public void setDistributionSizeFiveToSix(int distributionSizeFiveToSix) {
        this.distributionSizeFiveToSix = distributionSizeFiveToSix;
    }

    public int getDistributionSizeSevenToEight() {
        return distributionSizeSevenToEight;
    }

    public void setDistributionSizeSevenToEight(int distributionSizeSevenToEight) {
        this.distributionSizeSevenToEight = distributionSizeSevenToEight;
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

    public void setDistributionTotal(int distributionTotal) {
        this.distributionTotal = distributionTotal;
    }
}