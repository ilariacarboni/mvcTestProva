/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/javafx/FXMLController.java to edit this template
 */
package view.sceneControllers;

import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import view.utils.BackButton;
import view.utils.LocatedImage;

public class EmployeeItemController extends BaseView implements Initializable {

    @FXML
    private AnchorPane employeeAnchorPane;

    @FXML
    private ImageView iconEmployee;

    @FXML
    private Label nameLbl;

    @FXML
    private Label roleLbl;

    @FXML
    private Label surnameLbl;
    
    private HashMap<String, Object> employeeInfo;
    private Node employeeInfoPane = null;
    
    
    public void setEmployeeInfo( HashMap<String, Object> empInfo) {
        this.employeeInfo = empInfo;
        this.nameLbl.setText((String)empInfo.get("name"));
        this.surnameLbl.setText((String)empInfo.get("surname"));
        String role = (String)empInfo.get("role");
        this.roleLbl.setText((String)empInfo.get("role"));
        if(role.equals("cuoco")) {
            this.iconEmployee.setImage(new LocatedImage("view/style/img/employee-icons/chef.png"));
        }
        else{
          this.iconEmployee.setImage(new LocatedImage("view/style/img/employee-icons/waiter.png"));   
        }
        
       
    }
    public HashMap<String, Object> getEmployeeInfo() {
        return employeeInfo;
    }

    @FXML
    void itemHovered(MouseEvent event) {
        this.employeeAnchorPane.getStyleClass().add("product-hover");
    }

    @FXML
    void itemNotHovered(MouseEvent event) {
     if (this.employeeAnchorPane.getStyleClass().contains("product-hover")) {
            this.employeeAnchorPane.getStyleClass().remove("product-hover");
        }
    }

    @FXML
    void itemSelected(MouseEvent event) throws IOException {
         FXMLLoader loader = new FXMLLoader(getClass().getResource("/view/scene/employeeInfoPane.fxml"));
         Node employeeInfoPane = loader.load();
         commController.getDashboardController().setRightPane(employeeInfoPane);
         EmployeeInfoPaneController empInfoContr = loader.getController();
         empInfoContr.setChosenEmployee(this.employeeInfo);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        
    }

}
