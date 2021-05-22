package control;

import com.jfoenix.controls.JFXTextField;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.PlaceOrderDTO;
import model.orderItemTableDTO;

import javax.swing.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.Optional;
import java.util.ResourceBundle;

public class PaymentControl implements Initializable {
    public TableColumn price;
    public TableColumn qty;
    public TableColumn item;
    public TableColumn itemCode;
    public Label lblDate;
    public TableView tblItem;
    public JFXTextField txtTotal;
    ArrayList<orderItemTableDTO> table = new ArrayList<>();
    private int total;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        itemCode.setCellValueFactory(new PropertyValueFactory<>("itemCode"));
        item.setCellValueFactory(new PropertyValueFactory<>("description"));
        qty.setCellValueFactory(new PropertyValueFactory<>("packSize"));
        price.setCellValueFactory(new PropertyValueFactory<>("price"));
        tableLoad();
    }

    private void tableLoad(){
        System.out.println("table load method in payment class");
        System.out.println(createOrderForm.placeOrderDTO.getOrderID());
        System.out.println(createOrderForm.placeOrderDTO.getOrderItems().size());
        for (orderItemTableDTO or: createOrderForm.placeOrderDTO.getOrderItems()) {
                orderItemTableDTO ors = new orderItemTableDTO(or.getItemCode(),or.getDescription(),or.getQty(),or.getPrice());
                total+= or.getPrice();
                table.add(ors);
        }
        tblItem.setItems(FXCollections.observableArrayList(table));
        txtTotal.setText(total+"");
    }

    public void btnPayMent(ActionEvent actionEvent) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("success");
        alert.setHeaderText(null);
        alert.setContentText("Order Placed Success!");
        alert.initStyle(StageStyle.UTILITY);
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            // ... user chose OK
            ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }
}
