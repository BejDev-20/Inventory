package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Represents one product that consists of parts (in-house and outsourced). Product also has a unique id, non-unique
 * name, price of the product, stock availability, minimum and maximum values.
 *
 * @author Iulia Bejsovec StudentID: 001248083
 */

public class Product {
    /* All the parts the product needs/has */
    private ObservableList<Part> associatedParts;
    /* Unique ID of the product */
    private int id;
    /* Non-unique name of the product */
    private String name;
    /* Product's price */
    private double price;
    /* Stock availability for the product */
    private int stock;
    /* Minimum value for the product */
    private int min;
    /* Maximum value for the product */
    private int max;

    /**
     * Constructor for the product
     *
     * @param id    Unique ID of the product
     * @param name  Non-unique name of the product
     * @param price Product's price
     * @param stock Stock availability for the product
     * @param min   Minimum value for the product
     * @param max   Maximum value for the product
     */
    public Product(int id, String name, double price, int stock, int min, int max) {
        checkArguments(name, min, max, stock);
        this.id = id;
        this.name = name;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
        associatedParts = FXCollections.observableArrayList();
    }

    /**
     * Checks that given arguments are correct. Name cannot consist only of numbers, min must be less than max,
     * stock must be between min and max
     *
     * @param name  name of the product
     * @param min   minimum number of products
     * @param max   maximum number of products
     * @param stock inventory availability of the part
     */
    private void checkArguments(String name, int min, int max, int stock) {
        checkName(name);
        checkMinMaxStock(min, max, stock);
    }


    /**
     * Checks that given arguments are correct. Min must be less than max, stock must be between min and max
     *
     * @param min   minimum number of products
     * @param max   maximum number of products
     * @param stock inventory availability of the part
     * @throws IllegalArgumentException if either min or max equals to 0 or min > max
     */
    private void checkMinMaxStock(int min, int max, int stock) {
        if (min == 0 || max == 0 || min > max || stock < min || stock > max) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Checks that given argument is correct. Name cannot consist only of numbers
     *
     * @param name name of the product to check
     * @throws IllegalArgumentException if name consists only of numbers
     */
    private void checkName(String name) {
        try {
            Integer.parseInt(name);
            // catches NumberFormatException if parsing to integer is unsuccessful
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets the given id for the product
     *
     * @param id New unique ID of the product
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Updates the name of the product to the given one
     *
     * @param name Non-unique name of the product
     */
    public void setName(String name) {
        checkName(name);
        this.name = name;
    }

    /**
     * Updates the product's price to the given one
     *
     * @param price Product's price
     */
    public void setPrice(double price) {
        this.price = price;
    }

    /**
     * Updates the product's availability to the given one
     *
     * @param stock Number of units of the product available
     */
    public void setStock(int stock) {
        checkMinMaxStock(this.min, this.max, stock);
        this.stock = stock;
    }

    /**
     * Updates the product's minimum value to the given one
     *
     * @param min Minimum value for the product
     */
    public void setMin(int min) {
        checkMinMaxStock(min, this.max, this.stock);
        this.min = min;
    }

    /**
     * Updates the product's maximum value to the given one
     *
     * @param max Maximum value for the product
     */
    public void setMax(int max) {
        checkMinMaxStock(this.min, max, this.stock);
        this.max = max;
    }

    /**
     * Sets the associated parts to the given list
     *
     * @param newList list to set the associated parts to
     */
    public void setAssociatedParts(ObservableList<Part> newList) {
        associatedParts = newList;
    }

    /**
     * Retrieves product's ID
     *
     * @return ID of the product
     */
    public int getId() {
        return this.id;
    }

    /**
     * Retrieves product's name
     *
     * @return Name of the product
     */
    public String getName() {
        return this.name;
    }

    /**
     * Retrieves product's price per unit
     *
     * @return Price of the product
     */
    public double getPrice() {
        return this.price;
    }

    /**
     * Retrieves product's stock availability
     *
     * @return Stock of the product
     */
    public int getStock() {
        return this.stock;
    }

    /**
     * Retrieves product's minimum value
     *
     * @return Minimum value for the product
     */
    public int getMin() {
        return this.min;
    }

    /**
     * Retrieves product's maximum value
     *
     * @return Maximum value for the product
     */
    public int getMax() {
        return this.max;
    }

    /**
     * Adds the given part to the product
     *
     * @param part part to add to the product
     */
    public void addAssociatedPart(Part part) {
        associatedParts.add(part);

    }

    /**
     * Deletes the given part from the product
     *
     * @param selectedAssociatedPart part to be deleted
     * @return true if the deletion is successful
     */
    public boolean deleteAssociatedPart(Part selectedAssociatedPart) {
        associatedParts.remove(selectedAssociatedPart);
        return true;
    }

    /**
     * Retrieves the list of all associated with the product parts
     *
     * @return Retrieves the list of all associated with the product parts
     */
    public ObservableList<Part> getAssociatedParts() {
        return this.associatedParts;
    }

    /**
     * Compares two objects if they belong to the same class and returns true if the objects are equal, otherwise
     * false
     *
     * @param object Object to compare this product to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this.getClass() == object.getClass()) {
            return this.getId() == ((Product) object).getId();
        }
        return false;
    }
}
