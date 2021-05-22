package control;

import com.jfoenix.controls.JFXTextField;
import db.DBconnection;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import model.itemDTO;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class ItemsControl implements Initializable {

    public AnchorPane root;
    public JFXTextField txtSearch;
    public TableView itemTable;
    public TableColumn itemCode;
    public TableColumn Description;
    public TableColumn packSize;
    public TableColumn unitPrice;
    public TableColumn qtyOnHand;


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        Description.setCellValueFactory(new PropertyValueFactory<>("description"));
        packSize.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        unitPrice.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyOnHand.setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));
        try {
            getAllData();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }

    }
    public void btnSearch(ActionEvent actionEvent) {
        try {
            getAllData();
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }


    public void realTimeSearch(KeyEvent keyEvent) {
        try {
            getDataInRealTime(txtSearch.getText());
        } catch (SQLException | ClassNotFoundException throwables) {
            throwables.printStackTrace();
        }
    }

    public void getAllData() throws ClassNotFoundException, SQLException {
        ArrayList<itemDTO> allItems = new ArrayList<>();
        ResultSet rst = DBconnection.getInstance().getConnection().createStatement().executeQuery("select * from item");
        while(rst.next()){
            itemDTO record  = new itemDTO(rst.getString("itemCode"), rst.getString("Description"), rst.getString("packSize"), rst.getDouble("unitPrice"), rst.getInt("QtyOnHand"));
            allItems.add(record);
        }
        itemTable.setItems(FXCollections.observableArrayList(allItems));
    }

    public void getDataInRealTime(String description) throws SQLException, ClassNotFoundException {

        ArrayList<itemDTO> allItems = new ArrayList<>();
        ResultSet rst;
        Connection con = DBconnection.getInstance().getConnection();
        PreparedStatement pat = con.prepareStatement("select * from item where Description like ? '%'");
        pat.setObject(1, description);
        rst = pat.executeQuery();
        while(rst.next()){
            itemDTO record  = new itemDTO(rst.getString("itemCode"), rst.getString("Description"), rst.getString("packSize"), rst.getDouble("unitPrice"), rst.getInt("QtyOnHand"));
            allItems.add(record);
        }
        itemTable.setItems(FXCollections.observableArrayList(allItems));
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
