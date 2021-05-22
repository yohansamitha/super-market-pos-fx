package model;

import java.sql.Date;
import java.util.ArrayList;

public class PlaceOrderDTO {
    private String orderID;
    private String custID;
    private Date orderDate;
    private ArrayList<orderItemTableDTO> orderItems = new ArrayList<>();

    public PlaceOrderDTO(String orderID, String custID, String orderDate, ArrayList<orderItemTableDTO> orderItems) {
        this.orderID = orderID;
        this.custID = custID;
        this.orderDate = Date.valueOf(orderDate);
        this.orderItems = orderItems;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getCustID() {
        return custID;
    }

    public void setCustID(String custID) {
        this.custID = custID;
    }

    public Date getOrderDate() {
        return orderDate;
    }

    public void setOrderDate(Date orderDate) {
        this.orderDate = orderDate;
    }

    public ArrayList<orderItemTableDTO> getOrderItems() {
        return orderItems;
    }

    public void setOrderItems(ArrayList<orderItemTableDTO> orderItems) {
        this.orderItems = orderItems;
    }
}
