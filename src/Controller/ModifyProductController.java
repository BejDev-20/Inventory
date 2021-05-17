package Controller;

import Model.*;
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
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * This is a main controller that is responsible for the main menu of the app. Includes tables of all the parts and
 * products, includes search by id/name functionality for each. Provides functionality to add, modify, delete parts
 * and products.
 *
 * @author Iulia Bejsovec
 * @version 12/2020
 */
public class ModifyProductController implements Initializable {

    private Stage stage;
    private Parent scene;
    @FXML
    private Label addProductLabel;
    @FXML
    private TableView<Part> allPartsTable;
    @FXML
    private TableColumn<Part, Integer> allPartsIdColumn;
    @FXML
    private TableColumn<Part, String> allPartsNameColumn;
    @FXML
    private TableColumn<Part, Integer> allPartsInvColumn;
    @FXML
    private TableColumn<Part, Double> allPartsPriceColumn;
    @FXML
    private Button saveBttn;
    @FXML
    private Button addAssociatedPartBttn;
    @FXML
    private TableView<Part> associatedPartTable;
    @FXML
    private TableColumn<Part, Integer> productPartIdColumn;
    @FXML
    private TableColumn<Part, String> productPartNameColumn;
    @FXML
    private TableColumn<Part, Integer> productPartInvColumn;
    @FXML
    private TableColumn<Part, Double> productPartPriceColumn;
    @FXML
    private Button removeAssociatedPartBttn;
    @FXML
    private Button cancelBttn;
    @FXML
    private TextField allPartsSearchTxtArea;
    @FXML
    private TextField productNameTxt;
    @FXML
    private TextField productStockTxt;
    @FXML
    private TextField productPriceCostTxt;
    @FXML
    private TextField productMaxTxt;
    @FXML
    private TextField productMinTxt;
    @FXML
    private TextField productIdTxt;

    /**
     * Uses the attributes of the provided part to set the fields of the window
     * @param product product whose attributes are populated in the modify part form
     */
    public void saveProductData(Product product) {
        productIdTxt.setText(String.valueOf(product.getId()));
        productNameTxt.setText(product.getName());
        productStockTxt.setText(String.valueOf(product.getStock()));
        productPriceCostTxt.setText(String.valueOf(product.getPrice()));
        productMaxTxt.setText(String.valueOf(product.getMax()));
        productMinTxt.setText(String.valueOf(product.getMin()));
        ObservableList<Part> associatedParts = product.getAssociatedParts();
        fillAssociatedPartTable(associatedParts);
    }

    /**
     * Cancels the creation of the product and returns to the main menu. Prompts a confirmation alert by the user to
     * confirm the cancellation
     * @param event event to prompt the cancellation
     */
    @FXML
    private void onActionCancel(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "This will discard all the information you've entered"
                + ", do you want to continue?");
        alert.setTitle("Confirmation");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            stage = getStage("../View/MainMenu.fxml", event);
            stage.show();
        }
    }

    /**
     * Adds a selected part from the all parts table to the associated parts table
     * @param event event that triggers the addition of the part to the associated table
     */
    @FXML
    private void onActionAddAssociatedPart(ActionEvent event) {
        if (!(allPartsTable.getSelectionModel().isEmpty())) {
            associatedPartTable.getItems().add(allPartsTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Removes a selected part from the all parts table
     * @param event event that triggers the deletion of the part to the associated table
     */
    @FXML
    private void onActionRemovePart(ActionEvent event) {
        if (!(associatedPartTable.getSelectionModel().isEmpty())) {
            associatedPartTable.getItems().remove(associatedPartTable.getSelectionModel().getSelectedItem());
        }
    }

    /**
     * Retrieves the stage from the given path and event
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
     * Adds listener to the Save button, prompts an Alert if there is an issue with any of the values in the text
     * fields and adds a new product with the values given for each product's attribute (id, name, stock, price, min,
     * max) and associated parts (if any) to the Inventory.
     */
    private void setUpSaveBttnListener() {
        saveBttn.setOnAction(event -> {
            Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "This will save the changes to "
                    + "the part, do you want to continue?");
            saveConfirmation.setTitle("Save Changes");
            Optional<ButtonType> result = saveConfirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int id = Integer.parseInt(productIdTxt.getText());
                    String name = productNameTxt.getText();
                    int stock = Integer.parseInt(productStockTxt.getText());
                    double price = Double.parseDouble(productPriceCostTxt.getText());
                    int max = Integer.parseInt(productMaxTxt.getText());
                    int min = Integer.parseInt(productMinTxt.getText());
                    Product newProduct = new Product(id, name, price, stock, min, max);
                    if (!associatedPartTable.getItems().isEmpty()) {
                        associateParts(newProduct);
                    }
                    Inventory.updateProduct(newProduct.getId(), newProduct);
                    stage = getStage("../View/MainMenu.fxml", event);
                    stage.show();
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect Input");
                    alert.setContentText("Please enter valid values for all fields. No field can be empty:\n"
                            + "-name of the product cannot be only numbers\n"
                            + "-min should be less than max");
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * Adds all the associated parts to the given product
     * @param product to add all the parts to
     */
    private void associateParts(Product product) {
        ObservableList<Part> associatedParts = associatedPartTable.getItems();
        for (Part part : associatedParts) {
            product.addAssociatedPart(part);
        }
    }

    /**
     * Adds a listener to the search field that updates the table with every new key entered in the field. If the
     * field is empty, the table is populated with all the parts that are in the inventory.
     */
    private void setAllPartsFilterListener() {
        allPartsSearchTxtArea.textProperty().addListener((obj, oldVal, newVal) -> {
            if (!newVal.trim().isEmpty()) {
                String text = allPartsSearchTxtArea.getText();
                filterPart(text);
            } else {
                fillPartTable(Inventory.getAllParts());
            }
        });
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
     * Fills the part table's rows and columns with the given list data
     * @param list list to populate the data from
     */
    private void fillPartTable(ObservableList<Part> list) {
        allPartsTable.setItems(list);
        if (list.size() == 1) {
            allPartsTable.getSelectionModel().select(list.get(0));
        }
        allPartsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        allPartsNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        allPartsInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        allPartsPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Fills the associated part table's rows and columns with the given list data
     * @param list list to populate the data from
     */
    private void fillAssociatedPartTable(ObservableList<Part> list) {
        associatedPartTable.setItems(list);
        if (list.size() == 1) {
            associatedPartTable.getSelectionModel().select(list.get(0));
        }
        productPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    @FXML
    @Override
    public void initialize(URL url, ResourceBundle resources) {
        fillPartTable(Inventory.getAllParts());
        productPartIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        productPartNameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        productPartInvColumn.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPartPriceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        Pattern patternOnlyNumbers = Pattern.compile("\\d*");
        TextFormatter formatMaxTxt = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternOnlyNumbers.matcher(change.getControlNewText()).matches() ? change : null;
        });
        TextFormatter formatMinTxt = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternOnlyNumbers.matcher(change.getControlNewText()).matches() ? change : null;
        });
        productMaxTxt.setTextFormatter(formatMaxTxt);
        productMinTxt.setTextFormatter(formatMinTxt);

        Pattern patternDouble = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatPrice = new TextFormatter((UnaryOperator<TextFormatter.Change>) change -> {
            return patternDouble.matcher(change.getControlNewText()).matches() ? change : null;
        });
        productPriceCostTxt.setTextFormatter(formatPrice);
        setUpSaveBttnListener();
        setAllPartsFilterListener();
    }


}