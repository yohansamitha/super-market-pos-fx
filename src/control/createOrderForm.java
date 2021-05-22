package control;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTextField;
import db.DBconnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;
import model.PlaceOrderDTO;
import model.itemDTO;
import model.orderItemTableDTO;
import org.controlsfx.control.textfield.TextFields;

import javax.swing.*;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.ResourceBundle;

public class createOrderForm implements Initializable {
    static PlaceOrderDTO placeOrderDTO;
    public AnchorPane root;
    public Label lblOrderDate;
    public Label lblOrderID;
    public Label lblCusID;
    public Label lblCusTitle;
    public Label lblCusAddress;
    public Label lblCusName;
    public Label lblCity;
    public Label lblPostalCode;
    public Label lblProvince;
    public Label lblItemCode;
    public TableView itemTable;
    public TableColumn itemCode;
    public TableColumn description;
    public TableColumn packSize;
    public TableColumn unitPrice;
    public TableColumn discount;
    public TableColumn qty;
    public TableColumn price;
    public TableColumn orderID;
    public JFXTextField txtCusName;
    public JFXTextField txtItemName;
    public JFXTextField txtDiscount;
    public JFXTextField txtQTY;
    public JFXButton btnSItem;
    public JFXButton btnPOrder;
    public JFXButton btnEditOrder;
    ArrayList<String> allCustomerName = new ArrayList<>();
    ArrayList<String> allItemName = new ArrayList<>();
    ArrayList<orderItemTableDTO> tableDetails = new ArrayList<>();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        packSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        try {
            getCusname();
            getItemName();
            System.out.println("inizilize check");
            getOrderID();
            itemTable.setItems(FXCollections.observableArrayList(tableDetails));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        TextFields.bindAutoCompletion(txtCusName, allCustomerName);
        TextFields.bindAutoCompletion(txtItemName, allItemName);

    }

    public void btnCustomerSearch(ActionEvent actionEvent) {
        String cName = txtCusName.getText();
        if (txtCusName.getText().isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Warning!");
            alert.setHeaderText(null);
            alert.setContentText("Enter Customer Name and Search!");
            alert.showAndWait();
        } else {
            try {
                PreparedStatement pat = DBconnection.getInstance().getConnection().prepareStatement("select * from customer where custName = ?");
                pat.setObject(1, cName);
                ResultSet rst = pat.executeQuery();
                CustomerDTO dto = null;
                while (rst.next()) {
                    dto = new CustomerDTO(
                            rst.getString("custID"),
                            rst.getString("custTital"),
                            rst.getString("custName"),
                            rst.getString("custAddress"),
                            rst.getString("city"),
                            rst.getString("province"),
                            rst.getString("postalcode"));
                }
                Date thisDate = new Date();
                SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
                lblOrderDate.setText(f.format(thisDate));
                lblCusID.setText(dto.getCustID());
                lblCusTitle.setText(dto.getTitle());
                lblCusName.setText(dto.getCusName());
                lblCusAddress.setText(dto.getAddress());
                lblCity.setText(dto.getCity());
                lblProvince.setText(dto.getProvince());
                ;
                lblPostalCode.setText(dto.getPostalCode());
                btnSItem.setDisable(false);
                txtItemName.setDisable(false);
                txtDiscount.setDisable(false);
                txtQTY.setDisable(false);
                btnPOrder.setDisable(false);
                btnEditOrder.setDisable(false);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
    }

    public void btnAddItem(ActionEvent actionEvent) {
        int discount;
        int qty;
        double price;
        double total;
        if (txtDiscount.getText().isEmpty()) {
            discount = 0;
        } else {
            discount = Integer.parseInt(txtDiscount.getText());
        }
        if (txtQTY.getText().isEmpty()) {
            qty = 0;
        } else {
            qty = Integer.parseInt(txtQTY.getText());
        }
        ResultSet rst;
        Connection con = null;
        try {
            getItemCode(txtItemName.getText());
            int index = isAllReadyAdd(lblItemCode.getText());
            if (index == -1) {
                con = DBconnection.getInstance().getConnection();
                PreparedStatement pat = con.prepareStatement("select * from item where itemCode = ?");
                pat.setObject(1, lblItemCode.getText());
                rst = pat.executeQuery();
                while (rst.next()) {
                    price = rst.getDouble("unitPrice");
                    orderItemTableDTO record = new orderItemTableDTO(
                            lblOrderID.getText(),
                            rst.getString("itemCode")
                            , rst.getString("Description")
                            , rst.getString("packSize"), rst.getDouble("unitPrice"), discount, qty, getTotal(price, qty, discount));
                    System.out.println(record.toString());
                    tableDetails.add(record);
                }
            } else {
                price = tableDetails.get(index).getPrice();
                double tempPrice = getTotal(price, qty, discount) + tableDetails.get(index).getPrice();
                tableDetails.set(index, new orderItemTableDTO(
                        lblOrderID.getText(),
                        tableDetails.get(index).getItemCode(),
                        tableDetails.get(index).getDescription(),
                        tableDetails.get(index).getPackSize(),
                        tableDetails.get(index).getUnitPrice(),
                        tableDetails.get(index).getDiscount() + discount,
                        tableDetails.get(index).getQty() + qty,
                        tempPrice));
            }
            itemTable.setItems(FXCollections.observableArrayList(tableDetails));

        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
        lblItemCode.setText("");
        txtItemName.setText("");
        txtQTY.setText("");
        txtDiscount.setText("");
    }

    private int isAllReadyAdd(String itemCode) {
        System.out.println("isAllreadyADD");
        System.out.println(tableDetails.size());
        for (int i = 0; i < tableDetails.size(); i++) {
            orderItemTableDTO orderItemTabledto = tableDetails.get(i);
            String tableItemCode = orderItemTabledto.getItemCode();
            System.out.println(tableItemCode);
            System.out.println(itemCode);
            System.out.println("loop");
            if (tableItemCode.equals(itemCode)) {
                System.out.println(i + "index");
                return i;
            }
        }
//        System.out.println("-1");
        return -1;
    }

    private double getTotal(double price, int qty, int discount) {
        int tempPrice = (int) price;
        if (discount == 0) {
//            System.out.println((tempPrice*qty)+"  1");
//            System.out.println(((tempPrice*qty)*10/100)+"  2");
            return price * qty;
        } else {
            return (price * qty - ((price * discount) / 100));
        }
    }

    public void btnPlaceOrder(ActionEvent actionEvent) {
        placeOrderDTO = new PlaceOrderDTO(lblOrderID.getText(), lblCusID.getText(), lblOrderDate.getText(), tableDetails);
        try {
            if (tableDetails.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Please Enter Details");
            } else {
                System.out.println("placing order");
                boolean isAdded = PlaceOrder(placeOrderDTO);
                System.out.println("place order boolean" + isAdded);
                if (isAdded) {
//                    JOptionPane.showMessageDialog(null,"Order Add SuccessFully");

                    Parent root = FXMLLoader.load(this.getClass().getResource("/view/paymentForm.fxml"));
                    Scene scene = new Scene(root);
                    Stage primaryStage = new Stage();
                    primaryStage.setScene(scene);
                    primaryStage.centerOnScreen();
                    primaryStage.show();
                    primaryStage.setTitle("Payment");

                    System.out.println("get new orderID");
                    getOrderID();
                    System.out.println("clening table");
                    tableDetails.clear();
                    itemTable.setItems(FXCollections.observableArrayList(tableDetails));
                    cancelOrder();
                } else {
                    JOptionPane.showMessageDialog(null, "Something went Wrong!");
                }
            }
        } catch (SQLException | ClassNotFoundException | IOException throwables) {
            throwables.printStackTrace();
        }
    }

    private boolean PlaceOrder(PlaceOrderDTO placeOrderDTO) throws SQLException, ClassNotFoundException {
        Connection connection = DBconnection.getInstance().getConnection();
        try {
            connection.setAutoCommit(false);
            PreparedStatement stm = connection.prepareStatement("Insert into Orders values(?,?,?)");
            stm.setObject(1, placeOrderDTO.getOrderID());
            stm.setObject(2, placeOrderDTO.getOrderDate());
            stm.setObject(3, placeOrderDTO.getCustID());
            boolean isAddedOrder = stm.executeUpdate() > 0;
            System.out.println("add order " + isAddedOrder);
            if (isAddedOrder) {
                boolean isAddOrderDetails = addOrderDetail(placeOrderDTO.getOrderItems());
                System.out.println("is add order details " + isAddOrderDetails);
                if (isAddOrderDetails) {
                    boolean isAddPayment = addPayment(placeOrderDTO);
                    System.out.println("is add payment " + isAddPayment);
                    if (isAddPayment) {
                        boolean isUpdate = updateStock(placeOrderDTO.getOrderItems());
                        System.out.println("is stock update " + isUpdate);
                        if (isUpdate) {
                            connection.commit();
                            System.out.println("place order returning true");
                            return true;
                        }
                    }
                }
            } else {
                connection.rollback();
            }

        } finally {
            connection.setAutoCommit(true);
        }
        System.out.println("place order returning false");
        return false;
    }

    private boolean updateStock(ArrayList<orderItemTableDTO> orderItems) throws SQLException, ClassNotFoundException {
        for (orderItemTableDTO orderDetail : orderItems) {
            boolean isUpdateStock = updateStock(orderDetail);
            if (!isUpdateStock) {
                return false;
            }
        }
        return true;
    }

    public boolean updateStock(orderItemTableDTO orderDetail) throws ClassNotFoundException, SQLException {
        PreparedStatement stm = DBconnection.getInstance().getConnection().prepareStatement("Update item set qtyOnHand=qtyOnHand-? where itemCode=?");
        stm.setObject(1, orderDetail.getQty());
        stm.setObject(2, orderDetail.getItemCode());
        return stm.executeUpdate() > 0;
    }

    private boolean addPayment(PlaceOrderDTO placeOrderDTO) throws SQLException, ClassNotFoundException {
//        PreparedStatement orderDetailStm = DBconnection.getInstance().getConnection().prepareStatement("Insert into payment values(?,?,?,?)");
        PreparedStatement orderDetailStm = DBconnection.getInstance().getConnection().prepareStatement("Insert into payment values(?,?,?,?)");
        orderDetailStm.setObject(1, null);
        orderDetailStm.setObject(2, placeOrderDTO.getOrderID());
        orderDetailStm.setObject(3, "paid");
        orderDetailStm.setObject(4, getTotalCost(placeOrderDTO.getOrderItems()));
        boolean isAddedOrderDetails = orderDetailStm.executeUpdate() > 0;
        if (isAddedOrderDetails) {
            return true;
        } else {
            return false;
        }
    }

    private int getTotalCost(ArrayList<orderItemTableDTO> orderItems) {
        int total = 0;
        for (orderItemTableDTO dto : orderItems) {
            total += dto.getPrice();
        }
        return total;
    }

    private boolean addOrderDetail(ArrayList<orderItemTableDTO> orderItems) throws SQLException, ClassNotFoundException {
        for (orderItemTableDTO orderitemtabledto : orderItems) {
            System.out.println("");
            boolean isAddedOrderDetails = addOrderDetail(orderitemtabledto);
            if (!isAddedOrderDetails) {
                return false;
            }
        }
        return true;
    }

    private boolean addOrderDetail(orderItemTableDTO orderitemtableDTO) throws SQLException, ClassNotFoundException {
        PreparedStatement orderDetailStm = DBconnection.getInstance().getConnection().prepareStatement("Insert into orderDetail values(?,?,?,?,?)");
        orderDetailStm.setObject(1, orderitemtableDTO.getOrderID());
        orderDetailStm.setObject(2, orderitemtableDTO.getItemCode());
        orderDetailStm.setObject(3, orderitemtableDTO.getQty());
        orderDetailStm.setObject(4, orderitemtableDTO.getDiscount());
        orderDetailStm.setObject(5, orderitemtableDTO.getUnitPrice());
        boolean isAddedOrderDetails = orderDetailStm.executeUpdate() > 0;
        System.out.println("add order detail 2 method " + isAddedOrderDetails);
        if (isAddedOrderDetails) {
            System.out.println("true");
            return true;
        } else {
            System.out.println("false");
            return false;
        }
    }

    public void btnEditOrder(ActionEvent actionEvent) {
    }

    public void btnCancelOrder(ActionEvent actionEvent) {
        cancelOrder();
    }

    private void cancelOrder() {
        lblCusID.setText("");
        lblOrderDate.setText("");
        lblCusTitle.setText("");
        lblCusName.setText("");
        lblCusAddress.setText("");
        lblCity.setText("");
        lblProvince.setText("");
        ;
        lblPostalCode.setText("");
        lblItemCode.setText("");
        txtCusName.setText("");
        txtItemName.setText("");
        txtQTY.setText("");
        txtDiscount.setText("");
        txtItemName.setDisable(true);
        txtDiscount.setDisable(true);
        txtQTY.setDisable(true);
        btnSItem.setDisable(true);
        btnPOrder.setDisable(true);
        btnEditOrder.setDisable(true);
    }

    private void getItemName() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBconnection.getInstance().getConnection().createStatement().executeQuery("select Description from item");
        while (rst.next()) {
            allItemName.add(rst.getString("Description"));
//            System.out.println(rst.getString("Description"));
        }
    }

    private void getCusname() throws SQLException, ClassNotFoundException {
        ResultSet rst = DBconnection.getInstance().getConnection().createStatement().executeQuery("select custName from customer");
        while (rst.next()) {
            allCustomerName.add(rst.getString("custName"));
//            System.out.println(rst.getString("custName"));
        }
    }

    public void btnNewOrder(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/createOrderForm.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("New Order");
    }

    public void btnOrderHistory(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/orderHistoryForm.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Order History");
    }

    public void btnRegCustomer(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/RegisterCustomerForm.fxml"));
        Scene scene = new Scene(root);
        Stage primaryStage = new Stage();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.show();
        primaryStage.setTitle("Register Customer");
    }

    public void btnItems(ActionEvent actionEvent) throws IOException {
        Scene scene = new Scene(FXMLLoader.load(this.getClass().getResource("/view/itemsForm.fxml")));
        Stage primaryStage = (Stage) root.getScene().getWindow();
        primaryStage.setScene(scene);
        primaryStage.centerOnScreen();
        primaryStage.setTitle("Items");
    }

    public void getOrderID() throws SQLException, ClassNotFoundException {
        String tempCustomerID = "";
        PreparedStatement pat = DBconnection.getInstance().getConnection().prepareStatement("select orderID from orders order by orderID desc limit 1");
        ResultSet rst = pat.executeQuery();
        if (rst.next()) {
            tempCustomerID = rst.getString("orderID");
        }
        String[] arrOfStr = tempCustomerID.split("O");
        String xr = "";
        for (int i = 0; i < arrOfStr.length; i++) {
            xr += arrOfStr[i];
        }
        try {
            int custID = Integer.parseInt(xr);
            if (custID < 9) {
                lblOrderID.setText("O00" + (custID + 1));
            } else if (custID < 99) {
                lblOrderID.setText("O0" + (custID + 1));
            } else {
                lblOrderID.setText("O" + (custID + 1));
            }
        } catch (NullPointerException | NumberFormatException e) {
            lblOrderID.setText("O001");
        }
    }

    public void tblEdit(MouseEvent mouseEvent) {
        System.out.println("mouse clicked");
    }


    public void itemNameSearch(ActionEvent actionEvent) {
        System.out.println("item enter event");
        try {
            getItemCode(txtItemName.getText());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        txtQTY.requestFocus();
    }

    public void getItemCode(String itemName) throws SQLException, ClassNotFoundException {
        System.out.println("set item code");
        Connection con = DBconnection.getInstance().getConnection();
        PreparedStatement pat = con.prepareStatement("select itemCode from item where Description = ?");
        pat.setObject(1, itemName);
        ResultSet rst = pat.executeQuery();
        while (rst.next()) {
            itemDTO record = new itemDTO(rst.getString("itemCode"));
            lblItemCode.setText(record.getItemCode());
        }
    }

}
