/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view.sceneControllers;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

import business.AdminManager;
import business.CategoryManager;
import javafx.animation.TranslateTransition;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.util.Duration;
import view.utils.BackButton;
import view.utils.CustomGridPane;
import view.utils.LocatedImage;

import static javafx.scene.layout.BackgroundPosition.CENTER;
import static javafx.scene.layout.BackgroundRepeat.NO_REPEAT;
import static javafx.scene.layout.BackgroundRepeat.REPEAT;
import static javafx.scene.layout.BackgroundSize.DEFAULT;

/**
 * FXML Controller class
 *
 * @author Natalia
 */
public class CategoryPaneController extends BaseView implements Initializable {


    public BorderPane categoryContainer;
    public VBox mainPane;
    private CategoryManager categoryManager = new CategoryManager();
    public int permissionLevel = AdminManager.ROOT_PERMISSION_LEVEL;
    final int GRIDPANE_COLUMNS_NUMBER = 4;
    final int ANIMATION_DURATION = 275;
    final int ANIMATION_DISTANCE = 700;
    private Node productsPane = null;
    private CustomGridPane gridPane;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        commController.setCategoryPaneController(this);
        //storeMainPane.setBackground(new Background(new BackgroundImage(new LocatedImage(BACKGROUND_PATH), REPEAT, NO_REPEAT, CENTER, DEFAULT)));
        mainPane.setStyle("-fx-background-image: url(\"/view/style/img/background/grey.jpeg\");-fx-background-repeat: no-repeat;");
        gridPane = new CustomGridPane(this.GRIDPANE_COLUMNS_NUMBER);
        gridPane.setFinalColumnsNumber(2);
        gridPane.setBreakPoint(1200, commController.getStage());
        gridPane.setHgap(20);
        gridPane.setVgap(20);
        gridPane.setPadding(new Insets(5, 20, 20, 20));

        ArrayList<HashMap<String,Object>> categories =  this.categoryManager.getAll();
        HashMap<String, HashMap<String, Object>> categoriesInfo = this.categoryManager.getCategoriesBasicInfo();
        categories.forEach((category) -> {
            try{
                HashMap<String, Object> categoryInfo = categoriesInfo.get(category.get("name"));
                category.put("info", categoryInfo);
                this.addCategory(category);
            }catch (IOException ex){
                Logger.getLogger(CategoryPaneController.class.getName()).log(Level.SEVERE, null, ex);
            }
        });


        categoryContainer.setCenter(gridPane);
    }
    

    public void addCategory(HashMap<String,Object> category) throws IOException{
        FXMLLoader loader = new FXMLLoader(getClass().getResource(this.CATEGORY_COMPONENT_PATH));
        Node categoryNode = loader.load();
        CategoryController categoryContr = loader.getController();
        categoryContr.setCategoryInfo(category);
        gridPane.add(categoryNode);
        this.animate();
    }

    @FXML
    private void addCategoryButtonClicked(ActionEvent event) throws IOException {
        BorderPane dashboardBorderPane = (BorderPane) mainPane.getParent();
        dashboardBorderPane.setRight(FXMLLoader.load(getClass().getResource(this.ADD_CATEGORY_PANE_PATH)));
    }
    
    public void showProductsForCategory(HashMap<String, Object> category) throws IOException{
        if(this.productsPane == null){
            this.productsPane = FXMLLoader.load(getClass().getResource(this.PRODUCT_PANE_PATH));
        }
        ProductsPaneController productsPaneContr = commController.getProductsPaneController();
        productsPaneContr.emptyProductInfo();
        productsPaneContr.loadProductsByCategory(category);
        DashboardController dashboardController = commController.getDashboardController();
        BackButton backButton = this.makeBackButton(dashboardController);
        dashboardController.setCenterPane(productsPane, backButton);
        dashboardController.setRightPane(null);
    }

    /**
     * generate the back button to return to the category Pane scene
     * before the category Pane scene there is nothing so the back button generated by this method hasn't a previous back button
     * @return BackButton
     */
    private BackButton makeBackButton(DashboardController dc){
        BackButton backButton = new BackButton();
        backButton.setCenterScene(dc.getCenterPane());
        backButton.setRightScene(dc.getRightPane());
        backButton.setDashboardController(dc);
        return backButton;
    }

    public void animate(){
        List<Node> categories = gridPane.getChildren();
        for(Node category: categories){
            TranslateTransition t = new TranslateTransition(Duration.millis(this.ANIMATION_DURATION), category);
            t.setFromX(this.ANIMATION_DISTANCE);
            t.setToX(0);
            t.play();
        }
    }
    
}
