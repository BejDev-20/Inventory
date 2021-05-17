package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 * Keeps track of all inventory, all parts and products. Allows to add, delete, update parts and products. Provides
 * functionality to retrieve all the parts and the products in the inventory individually and in bulk.
 * @author Iulia Bejsovec
 * @version 12/2020
 */
public class Inventory {
    /* List of all parts in the inventory */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    /* List of all products in the inventory  */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    /* Keeps track of the next part ID */
    private static int partId = 1;
    /* Keeps track of the next product ID  */
    private static int productId = 1;

    /**
     * Adds the part to the list of all parts and updates the part id for the next part
     * @param newPart Part to add to the inventory
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
        partId++;
    }

    /**
     * Adds the product to the list of all products and updates the product id for the next product
     * @param newProduct Product to add to the inventory
     */
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
        productId++;
    }

    /**
     * Retrieves the part associated with the given part ID
     * @param partId Part ID of the part to be retrieved
     * @return Part associated with the given part ID
     */
    public static Part lookupPart(int partId) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                return part;
            }
        }
        return null;
    }

    /**
     * Retrieves the product associated with the given product ID
     * @param productId Product ID of the part to be retrieved
     * @return Product associated with the given product ID
     */
    public static Product lookupProduct(int productId) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                return product;
            }
        }
        return null;
    }

    /**
     * Updates the part with the given part ID to the selected part
     * @param partId  Part ID of the part to be updated
     * @param newPart Part to update the part to
     */
    public static void updatePart(int partId, Part newPart) {
        for (Part part : allParts) {
            if (part.getId() == partId) {
                allParts.set(allParts.indexOf(part), newPart);
            }
        }
    }

    /**
     * Updates the product with the given product ID to the selected product
     * @param productId  Product ID of the product to be updated
     * @param newProduct Product to update the product to
     */
    public static void updateProduct(int productId, Product newProduct) {
        for (Product product : allProducts) {
            if (product.getId() == productId) {
                allProducts.set(allProducts.indexOf(product), newProduct);
            }
        }
    }

    /**
     * Deletes the provided part from the list of all parts and returns true
     * @param selectedPart part to be deleted
     * @return true if the action executed successfully
     */
    public static boolean deletePart(Part selectedPart) {
        allParts.remove(selectedPart);
        return true;
    }

    /**
     * Deletes the provided product if there are no parts associated with it from the list of all products and returns
     * true if the action was successful, false otherwise
     * @param selectedProduct product to be deleted
     * @return true if the action executed successfully, false otherwise
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if (Inventory.lookupProduct(selectedProduct.getId()).getAssociatedParts().size() == 0) {
            allProducts.remove(selectedProduct);
            return true;
        }
        return false;
    }

    /**
     * Retrieves a list of all the parts in the inventory
     * @return a list of all the parts in the inventory
     */
    public static ObservableList<Part> getAllParts() {
        return allParts;
    }

    /**
     * Retrieves a list of all the products in the inventory
     * @return a list of all the products in the inventory
     */
    public static ObservableList<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * Retrieves the part ID for the next part added
     * @return part ID for the next part added
     */
    public static int getPartId() {
        return partId;
    }

    /**
     * Retrieves the product ID for the next part added
     * @return product ID for the next part added
     */
    public static int getProductId() {
        return productId;
    }
}
