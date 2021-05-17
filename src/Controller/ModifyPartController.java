package Controller;

import Model.InHouse;
import Model.Inventory;
import Model.Outsourced;
import Model.Part;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;
import java.util.regex.Pattern;

/**
 * This controller is responsible for the window which is used to modify a part of the Inventory and saving the changes.
 * The window allows to modify name, stock, price, max and min, MachineID/Company name of the part. Some text fields are
 * limited to digit-only inputs to prevent undesirable input values.
 *
 * @author Iulia Bejsovec StudentID: 001248083
 */
public class ModifyPartController implements Initializable {


    private Stage stage;
    private Parent scene;

    @FXML
    private ToggleGroup sourceTG;
    @FXML
    private Label formNameLbl;
    @FXML
    private Button saveBttn;
    @FXML
    private Button cancelBttn;
    @FXML
    private RadioButton partIn;
    @FXML
    private RadioButton partOut;
    @FXML
    private Label partMachineIdLbl;
    @FXML
    private TextField partIdTxt;
    @FXML
    private TextField partNameTxt;
    @FXML
    private TextField partStockTxt;
    @FXML
    private TextField partPriceCostTxt;
    @FXML
    private TextField partMaxTxt;
    @FXML
    private TextField partMachineIdTxt;
    @FXML
    private TextField partMinTxt;
    @FXML
    private Label emptyFieldsWarningLbl;

    /**
     * Cancels the modification of the part and returns to the main menu. Prompts a confirmation alert by the user to
     * confirm the cancellation
     *
     * @param event event to prompt the cancellation
     */
    @FXML
    private void OnActionCancelMod(ActionEvent event) {
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
     * Changes the label to the "MachineID" when the radio button is toggled to the In-House
     *
     * @param event event that triggers the change in the label, toggling the radio button
     */
    @FXML
    private void onActionInHouse(ActionEvent event) {
        partMachineIdLbl.setText("MachineID");
    }

    /**
     * Changes the label to the "Company Name" when the radio button is toggled to the Outsourced
     *
     * @param event event that triggers the change in the label, toggling the radio button
     */
    @FXML
    private void onActionOutsourced(ActionEvent event) {
        partMachineIdLbl.setText("Company Name");
    }

    /**
     * Retrieves the stage from the given path and event
     *
     * @param FXMLPath path of the FXML document to set up the next scene
     * @param event    that triggers the action
     * @return the stage from the given path and event
     */
    private Stage getStage(String FXMLPath, ActionEvent event) {
        stage = (Stage) ((Button) event.getSource()).getScene().getWindow();
        try {
            scene = FXMLLoader.load(getClass().getResource(FXMLPath));
            stage.setScene(new Scene(scene));
            // catches an IOException for the .load() method if it is not possible to load the hierarchy from the FXML doc
        } catch (IOException e) {
            e.printStackTrace();
        }
        return stage;
    }

    /**
     * Uses the attributes of the provided part to set the fields of the window
     *
     * @param part part whose attributes are populated in the modify part form
     */
    public void savePartData(Part part) {
        partIdTxt.setText(String.valueOf(part.getId()));
        partNameTxt.setText(part.getName());
        partStockTxt.setText(String.valueOf(part.getStock()));
        partPriceCostTxt.setText(String.valueOf(part.getPrice()));
        partMaxTxt.setText(String.valueOf(part.getMax()));
        partMinTxt.setText(String.valueOf(part.getMin()));
        if (part instanceof InHouse) {
            partMachineIdTxt.setText(String.valueOf(((InHouse) part).getMachineId()));
            partIn.setSelected(true);
        } else {
            partMachineIdTxt.setText(((Outsourced) part).getCompanyName());
            partMachineIdLbl.setText("Company Name");
            partOut.setSelected(true);
        }

    }

    /**
     * Adds listener to the Save button, prompts an Alert if there is an issue with any of the values in the text
     * fields and adds a new part with the values given for each part's attribute (id, name, stock, price, min, max,
     * MachineID/CompanyName) to the Inventory.
     */
    private void setUpSaveBttnListener() {
        saveBttn.setOnAction(event -> {
            Alert saveConfirmation = new Alert(Alert.AlertType.CONFIRMATION, "This will save the changes to "
                    + "the part, do you want to continue?");
            saveConfirmation.setTitle("Save Changes");
            Optional<ButtonType> result = saveConfirmation.showAndWait();
            if (result.isPresent() && result.get() == ButtonType.OK) {
                try {
                    int partId = Integer.parseInt(partIdTxt.getText());
                    String name = partNameTxt.getText();
                    int stock = Integer.parseInt(partStockTxt.getText());
                    double price = Double.parseDouble(partPriceCostTxt.getText());
                    int max = Integer.parseInt(partMaxTxt.getText());
                    int min = Integer.parseInt(partMinTxt.getText());
                    int machineId;
                    String companyName;
                    Part newPart;
                    if (partIn.isSelected()) {
                        machineId = Integer.parseInt(partMachineIdTxt.getText());
                        newPart = new InHouse(partId, name, price, stock, min, max, machineId);
                    } else {
                        companyName = partMachineIdTxt.getText();
                        newPart = new Outsourced(partId, name, price, stock, min, max, companyName);
                    }
                    Inventory.updatePart(newPart.getId(), newPart);
                    stage = getStage("../View/MainMenu.fxml", event);
                    stage.show();
                    // catches IllegalArgumentException that is thrown if any of the input text fields have improper values
                    // brings up an alert reminder of the format of the fields and its values
                } catch (IllegalArgumentException e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Incorrect Input");
                    alert.setContentText("Please enter valid values for all fields. No field can be empty:\n"
                            + "-name of the part cannot be only numbers\n"
                            + "-min should be less than max\n"
                            + "-machine id must be only numbers"
                            + "\n-company name cannot be only numbers");
                    alert.showAndWait();
                }
            }
        });
    }

    /**
     * Called to initialize a controller after its root element has been completely processed
     * Sets up input limitations for the min, max, price text fields to be digits (and period) only, adds listeners to
     * the save button.
     *
     * @param url            location used to resolve relative paths for the root object, or null if the location is not known
     * @param resourceBundle resources used to localize the root object, or null if the root object was not localized
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        Pattern patternOnlyNumbers = Pattern.compile("\\d*");
        TextFormatter formatMaxTxt = new TextFormatter(change -> {
            return patternOnlyNumbers.matcher(change.getControlNewText()).matches() ? change : null;
        });
        TextFormatter formatMinTxt = new TextFormatter(change -> {
            return patternOnlyNumbers.matcher(change.getControlNewText()).matches() ? change : null;
        });
        partMinTxt.setTextFormatter(formatMaxTxt);
        partMaxTxt.setTextFormatter(formatMinTxt);

        Pattern patternDouble = Pattern.compile("\\d*|\\d+\\.\\d*");
        TextFormatter formatPrice = new TextFormatter(change -> {
            return patternDouble.matcher(change.getControlNewText()).matches() ? change : null;
        });
        partPriceCostTxt.setTextFormatter(formatPrice);
        emptyFieldsWarningLbl.setVisible(false);
        setUpSaveBttnListener();
    }
}
