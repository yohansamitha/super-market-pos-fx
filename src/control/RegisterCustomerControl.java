package control;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import db.DBconnection;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import model.CustomerDTO;

import javax.swing.*;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

public class RegisterCustomerControl {
    public AnchorPane root;
    public JFXComboBox comboTitle;
    public JFXTextField txtCusName;
    public JFXTextField txtCusAddress;
    public JFXTextField txtCity;
    public JFXTextField txtProvince;
    public JFXTextField txtPostalCode;

    public void btnCreate(ActionEvent actionEvent) {

        if (comboTitle.getSelectionModel().isEmpty()){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error Detected");
            alert.setHeaderText(null);
            alert.setContentText("Fill all the details!");
            alert.initStyle(StageStyle.UTILITY);
            alert.show();
        }else{
            CustomerDTO customerDTO = new CustomerDTO(comboTitle.getValue().toString(),txtCusName.getText(),txtCusAddress.getText()
                    ,txtCity.getText(),txtProvince.getText(),txtPostalCode.getText());

                PreparedStatement pat = null;
                int res = 0;
            try {
                pat = DBconnection.getInstance().getConnection().prepareStatement("insert into customer values(?,?,?,?,?,?,?)");
                pat.setObject(1, getCustomerID());
                pat.setObject(2, customerDTO.getTitle());
                pat.setObject(3, customerDTO.getCusName());
                pat.setObject(4, customerDTO.getAddress());
                pat.setObject(5, customerDTO.getCity());
                pat.setObject(6, customerDTO.getProvince());
                pat.setObject(7, customerDTO.getPostalCode());
                res = pat.executeUpdate();

            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error Detected");
                alert.setHeaderText(null);
                alert.setContentText(throwables.getMessage());
                alert.initStyle(StageStyle.UTILITY);
                alert.show();
            }
            if (res>0){
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText("Customer registered successfully!");
                alert.initStyle(StageStyle.UTILITY);
                Optional<ButtonType> result = alert.showAndWait();
                if (result.get() == ButtonType.OK){
                    // ... user chose OK
                    comboTitle.getSelectionModel().selectFirst();
                    txtCusName.setText("");
                    txtCusAddress.setText("");
                    txtCity.setText("");
                    txtProvince.setText("");
                    txtPostalCode.setText("");
                }
            }
        }
    }

    public void btnCancel(ActionEvent actionEvent) {
        ((Stage)(((Button)actionEvent.getSource()).getScene().getWindow())).close();
    }

     public String getCustomerID() throws SQLException, ClassNotFoundException {
        String tempCustomerID = "";
        PreparedStatement pat = DBconnection.getInstance().getConnection().prepareStatement("select custID from customer order by custID desc limit 1");
        ResultSet rst = pat.executeQuery();
        while (rst.next()){
            tempCustomerID = rst.getString("custID");
        }
         String[] arrOfStr = tempCustomerID.split("C");
         String xr = "";
         for (int i = 0; i < arrOfStr.length ; i++){
             xr+= arrOfStr[i];
         }
         int custID = Integer.parseInt(xr);
         if(custID<=9) {
             return  "C00" + (custID+1);
         }
         else if(custID < 99) {
             return  "C0" + (custID+1);
         }else {
             return  "C" + (custID+1);
         }
    }
}
