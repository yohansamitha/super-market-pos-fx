package model;

public class orderItemTableDTO {
    private String orderID;
    private String itemCode;
    private String description; //no from items table
    private String packSize; //no from items table
    private double unitPrice;
    private int Discount;
    private int qty;
    private double price;

    public orderItemTableDTO(String orderID, String itemCode, String description, String packSize, double unitPrice, int discount, int qty, double price) {
        this.orderID = orderID;
        this.itemCode = itemCode;
        this.description = description;
        this.packSize = packSize;
        this.unitPrice = unitPrice;
        Discount = discount;
        this.qty = qty;
        this.price = price;
    }

    public orderItemTableDTO(String orderID) {
        this.orderID = orderID;
    }

    public orderItemTableDTO(String itemCode, String description, int qty, double price) {
        this.itemCode = itemCode;
        this.description = description;
        this.qty = qty;
        this.price = price;
    }


    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getItemCode() {
        return itemCode;
    }

    public void setItemCode(String itemCode) {
        this.itemCode = itemCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPackSize() {
        return packSize;
    }

    public void setPackSize(String packSize) {
        this.packSize = packSize;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }

    public int getDiscount() {
        return Discount;
    }

    public void setDiscount(int discount) {
        Discount = discount;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    @Override
    public String toString() {
        return "orderItemTableDTO{" +
                "orderID='" + orderID + '\'' +
                ", itemCode='" + itemCode + '\'' +
                ", description='" + description + '\'' +
                ", packSize='" + packSize + '\'' +
                ", unitPrice=" + unitPrice +
                ", Discount=" + Discount +
                ", qty=" + qty +
                ", price=" + price +
                '}';
    }
}
