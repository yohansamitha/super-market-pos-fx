package control;

import db.DBconnection;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.CustomerDTO;
import model.orderItemTableDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.ResourceBundle;

public class OrderHistoryControl implements Initializable {
    public AnchorPane root;
    public TableColumn itemCode;
    public TableColumn Description;
    public TableColumn packSize;
    public TableColumn unitPrice;
    public TableColumn discount;
    public TableColumn qty;
    public TableColumn price;
    public TableColumn orderID;
    public TableColumn description;
    public TableView orderTable;
    public TableColumn MainOrderID;
    public TableView tblOrderHistory;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        MainOrderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        orderID.setCellValueFactory(new PropertyValueFactory<>("orderID"));
        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        description.setCellValueFactory(new PropertyValueFactory<>("description"));
        packSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        discount.setCellValueFactory(new PropertyValueFactory<>("discount"));
        qty.setCellValueFactory(new PropertyValueFactory<>("qty"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        loadOrdertable();

    }
    ArrayList<orderItemTableDTO> tblOrderID = new ArrayList<>();
    ArrayList<orderItemTableDTO> tblOrderDetail = new ArrayList<>();

    private void loadOrdertable() {
        try {
            Connection connection = DBconnection.getInstance().getConnection();
            PreparedStatement pat = connection.prepareStatement("select orderID from orders");
            ResultSet rst = pat.executeQuery();
            while (rst.next()) {
                orderItemTableDTO ord = new orderItemTableDTO(rst.getString("orderID"));
                tblOrderID.add(ord);
            }
            orderTable.setItems(FXCollections.observableArrayList(tblOrderID));
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void fetchData() throws SQLException, ClassNotFoundException {
//        ObservableList<orderItemTableDTO> fetchFromDatatbl;
        ObservableList<orderItemTableDTO> fetchFromDatatbl;
        fetchFromDatatbl = orderTable.getSelectionModel().getSelectedItems();
//        tblOrderHistory.getSelectionModel().clearSelection();
        MainOrderID.setText(fetchFromDatatbl.get(0).getItemCode());
//        itemDescriptionTxt.setText(fetchFromDatatbl.get(0).getDescription());
//        itemPackTxt.setText(fetchFromDatatbl.get(0).getPackSize());
//        itemUnitPriceTxt.setText(fetchFromDatatbl.get(0).getUnitPrice() + "");
//        itemQtyTxt.setText(fetchFromDatatbl.get(0).getQtyOnHand() + "");
        System.out.println(fetchFromDatatbl.toString());
//        PreparedStatement pat = DBconnection.getInstance().getConnection().prepareStatement("select * from orderDetail where orderID = ?");
        PreparedStatement pat = DBconnection.getInstance().getConnection().prepareStatement(
//                "SELECT * FROM orderDetail where orderID = ?"
                "select o.orderID,i.itemCode,description,packsize,od.unitPrice,od.Discount,od.orderQTY from orders o,item i,orderdetail od\n" +
                        "where (o.orderID=od.orderID && i.itemCode=od.itemCode) && od.orderID = ?"
        );
        pat.setObject(1, fetchFromDatatbl.get(0).getOrderID());
        System.out.println(fetchFromDatatbl.get(0));
        ResultSet rst = pat.executeQuery();
        orderItemTableDTO dto = null;
        while (rst.next()){
            dto = new orderItemTableDTO(
                    rst.getString("orderID"),
                    rst.getString("itemCode"),
                    rst.getString("description"),
                    rst.getString("packSize"),
                    rst.getDouble("unitPrice"),
                    rst.getInt("Discount"),
                    rst.getInt("orderQTY"),
                    rst.getDouble("Discount"));
            tblOrderDetail.add(dto);
            System.out.println(dto.toString()+"toString dto");
        }
        tblOrderHistory.setItems(FXCollections.observableArrayList(tblOrderDetail));
    }

    public void btnUpdateOrder(ActionEvent actionEvent) {

    }

    public void selectUpdateItem(MouseEvent mouseEvent) {

    }

    public void getSelectedOrder(MouseEvent mouseEvent) {
        try {
            fetchData();
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }

    }

    public void btnCancel(ActionEvent actionEvent) {

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
}
