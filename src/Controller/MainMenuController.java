package Controller;

import Model.Inventory;
import Model.Part;
import Model.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * This is a main controller that is responsible for the main menu of the app. Includes tables of all the parts and
 * products, includes search by id/name functionality for each. Provides functionality to add, modify, delete parts
 * and products.
 * @author Iulia Bejsovec
 * @version 12/2020
 */
public class MainMenuController implements Initializable {
    public Button exitBttn;
    @FXML
    private TableView<Part> partTable;
    @FXML
    private TableColumn<Part, Integer> partIdColumn;
    @FXML
    private TableColumn<Part, String> partNameColumn;
    @FXML
    private TableColumn<Part, Integer> partInvColumn;
    @FXML
    private TableColumn<Part, Double> partPriceColumn;
    @FXML
    private Button partAddBttn;
    @FXML
    private Button productAddBttn;
    @FXML
    private TableView<Product> productTable;
    @FXML
    private TableColumn<Product, Integer> productIdColumn;
    @FXML
    private TableColumn<Product, String> productNameColumn;
    @FXML
    private TableColumn<Product, Integer> productInvColumn;
    @FXML
    private TableColumn<Product, Double> productPriceColumn;
    @FXML
    private Button partModifyBttn;
    @FXML
    private Button partDeleteBttn;
    @FXML
    private Button productModifyBttn;
    @FXML
    private Button productDeleteBttn;
    @FXML
    private TextField partSearchTxtArea;
    @FXML
    private TextField productSearchTxtArea;

    private Stage stage;
    private Parent scene;

    /**
     * Opens a window with text fields to fill out to add a part to the inventory
     * @param event event that prompted the change of the scene to Add Part
     */
    @FXML
    private void onActionAddPart(ActionEvent event) {
        stage = getStage("../View/AddPart.fxml", event);
        stage.show();
        stage.setResizable(false);
    }

    /**
     * Opens a window with text fields to fill out to add a product to the inventory
     * @param event event that prompted the change of the scene to Add Product
     */
    @FXML
    private void onActionAddProduct(ActionEvent event) {
        stage = getStage("../View/AddProduct.fxml", event);
        stage.show();
        stage.setResizable(false);
    }

    /**
     * Deletes the selected part in the table after confirmation through an Alert. The scene changes to the main
     * menu after a successful delete. Nothing happens if no part is selected.
     * @param event event that triggers the deletion of the part
     */
    @FXML
    private void onActionDeletePart(ActionEvent event) {
        if (!(partTable.getSelectionModel().isEmpty())) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the part that you've selected"
                    + ", do you want to continue?");
            alert.setTitle("Confirmation");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Inventory.deletePart(partTable.getSelectionModel().getSelectedItem());
                stage = getStage("../View/MainMenu.fxml", event);
                stage.show();
            }
        }
    }

    /**
     * Deletes the selected product in the table after confirmation through an Alert. The scene changes to the main
     * menu after a successful delete. Nothing happens if no product is
     * selected.
     * @param event event that triggers the deletion of the product
     */
    @FXML
    private void onActionDeleteProduct(ActionEvent event) {
        if (!(productTable.getSelectionModel().isEmpty())) {
            Alert deleteConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "This will delete the product that you've selected"
                    + ", do you want to continue?");
            deleteConfirmation.setTitle("Confirmation");
            Optional<ButtonType> result = deleteConfirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                Product product = productTable.getSelectionModel().getSelectedItem();
                if (!product.getAssociatedParts().isEmpty()) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Delete Failed");
                    alert.setContentText("Please make sure product you are trying to delete doesn't have any parts"
                            + " associated with it.");
                    alert.show();
                } else {
                    Inventory.deleteProduct(product);
                }
                stage = getStage("../View/MainMenu.fxml", event);
                stage.show();
            }
        }
    }

    /**
     * Exits app after the action is triggered
     * @param event event that triggers the closure of the program
     */
    @FXML
    private void onActionExitApp(ActionEvent event) {
        System.exit(0);
    }

    /**
     * Opens a window with text fields filled out with part's information (id, name, stock, price, min and max,
     * machineId/company name) to modify. Allows to save changes or discard them
     * @param event event that triggers the modify window to open up
     */
    @FXML
    private void onActionModifyPart(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/ModifyPart.fxml"));
            loader.load();
            ModifyPartController modifyPartController = loader.getController();
            if (!(partTable.getSelectionModel().isEmpty())) {
                modifyPartController.savePartData(partTable.getSelectionModel().getSelectedItem());
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Opens a window with text fields filled out with part's information (id, name, stock, price, min and max,
     * machineId/company name) to modify. Allows to save changes or discard them
     * @param event event that triggers the modify window to open up
     */
    @FXML
    private void onActionModifyProduct(ActionEvent event) {
        try {
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("../View/ModifyProduct.fxml"));
            loader.load();
            ModifyProductController modifyProductController = loader.getController();
            if (!(productTable.getSelectionModel().isEmpty())) {
                modifyProductController.saveProductData(productTable.getSelectionModel().getSelectedItem());
                stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
                Parent scene = loader.getRoot();
                stage.setScene(new Scene(scene));
                stage.show();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Filters the part table by the part ID or name provided
     * @param text text (id or name) to filter the part table by
     */
    private void filterPart(String text) {
        try {
            int id = Integer.parseInt(text);
            ObservableList<Part> tempList = searchPartById(id, Inventory.getAllParts());
            fillPartTable(tempList);
        } catch (NumberFormatException e) {
            ObservableList<Part> tempList = searchPartByName(text, Inventory.getAllParts());
            fillPartTable(tempList);
        }
    }

    /**
     * Filters the product table by the part ID or name provided
     * @param text text (id or name) to filter the product table by
     */
    public void filterProduct(String text) {
        try {
            int id = Integer.parseInt(text);
            ObservableList<Product> tempList = searchProductById(id, Inventory.getAllProducts());
            fillProductTable(tempList);
        } catch (NumberFormatException e) {
            ObservableList<Product> tempList = searchProductByName(text, Inventory.getAllProducts());
            fillProductTable(tempList);
        }
    }

    /**
     * Searches all parts in the inventory and returns a list of the parts whose id partially or fully match the id
     * provided
     * @param id   to match all the parts' ids to
     * @param list list of all the parts to search through
     * @return a list of all the parts whose ids partially or fully match the id provided
     */
    private ObservableList<Part> searchPartById(int id, ObservableList<Part> list) {
        ObservableList<Part> newList = FXCollections.observableArrayList();
        for (Object obj : list) {
            if (Integer.toString(id).contains(Integer.toString(((Part) obj).getId()))) {
                newList.add((Part) obj);
            }
        }
        return newList;
    }

    /**
     * Searches all parts in the inventory and returns a list of the parts whose name partially or fully match the name
     * provided
     * @param name name to match all the parts' names to
     * @param list list of all the parts to search through
     * @return a list of all the parts whose names partially or fully match the name provided
     */
    private ObservableList<Part> searchPartByName(String name, ObservableList<Part> list) {
        ObservableList<Part> newList = FXCollections.observableArrayList();
        for (Object obj : list) {
            if ((((Part) obj).getName().toLowerCase()).contains(name.toLowerCase())) {
                newList.add((Part) obj);
            }
        }
        return newList;
    }

    /**
     * Searches all products in the inventory and returns a list of the products whose ids partially or fully match the
     * id provided
     * @param id   to match all the products' ids to
     * @param list list of all the products to search through
     * @return a list of all the products whose ids partially or fully match the id provided
     */
    private ObservableList<Product> searchProductById(int id, ObservableList<Product> list) {
        ObservableList<Product> newList = FXCollections.observableArrayList();
        for (Object obj : list) {
            if ((Integer.toString(((Product) obj).getId())).contains(Integer.toString(id))) {
                newList.add((Product) obj);
            }
        }
        return newList;
    }

    /**
     * Searches all parts in the inventory and returns a list of the products whose name partially or fully match the
     * name provided
     * @param name name to match all the products' names to
     * @param list list of all the products to search through
     * @return a list of all the products whose names partially or fully match the name provided
     */
    private ObservableList<Product> searchProductByName(String name, ObservableList<Product> list) {
        ObservableList<Product> newList = FXCollections.observableArrayList();
        for (Object obj : list) {
            if ((((Product) obj).getName().toLowerCase()).contains(name.toLowerCase())) {
                newList.add((Product) obj);
            }
        }
        return newList;
    }

    /**
     * Fills the part table's rows and columns with the given list data
     * @param list list to populate the data from
     */
    private void fillPartTable(ObservableList<Part> list) {
        partTable.setItems(list);
        if (list.size() == 1) {
            partTable.getSelectionModel().select(list.get(0));
        }
        partIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Fills the product table's rows and columns with the given list data
     * @param list list to populate the data from
     */
    private void fillProductTable(ObservableList<Product> list) {
        productTable.setItems(list);
        if (list.size() == 1) {
            productTable.getSelectionModel().select(list.get(0));
        }
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Retrieves the stage from the given path from the given event
     * @param FXMLPath path of the FXML document to set up the next scene
     * @param event    that triggers the action
     * @return the stage from the given path and event
     */
    private Stage getStage(String FXMLPath, ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getResource(FXMLPath));
            stage.setScene(new Scene(scene));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Fills the parts and products tables with the data and sets up listeners for the search text areas for parts and
     * product tables.
     * @param url       the location used to resolve relative paths for the root object, or null if the location is not known
     * @param resources the resources used to localize the root object, or null if the root object was not localized
     */
    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        fillPartTable(Inventory.getAllParts());
        fillProductTable(Inventory.getAllProducts());

        partSearchTxtArea.textProperty().addListener((obj, oldVal, newVal) -> {
            if (!newVal.trim().isEmpty()) {
                String text = partSearchTxtArea.getText();
                filterPart(text);
            } else {
                fillPartTable(Inventory.getAllParts());
            }
        });

        productSearchTxtArea.textProperty().addListener((obj, oldVal, newVal) -> {
            if (!newVal.trim().isEmpty()) {
                String text = productSearchTxtArea.getText();
                filterProduct(text);
            } else {
                fillProductTable(Inventory.getAllProducts());
            }
        });
    }
}
