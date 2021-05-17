package Model;

/**
 * Represents a part that is made in-house. Part has a unique ID, name, price, stock availability, minimum and maximum
 * values, machine ID.
 * @author Iulia Bejsovec
 * @version 12/2020
 */
public class InHouse extends Part {
    /* Machine ID of the part that is made in-house*/
    private int machineId;

    /**
     * Constructor to create an InHouse part
     * @param id        unique id of the part
     * @param name      name of the part
     * @param price     price per one unit (part)
     * @param stock     number of parts of this kind available
     * @param min       minimum value for the number of parts
     * @param max       maximum value for the number of parts
     * @param machineId in-house id of the part
     */
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, price, stock, min, max);
        checkArguments(name, min, max, stock);
        this.machineId = machineId;
    }

    /**
     * Checks that given arguments are correct. Name cannot consist only of numbers, min must be less than max,
     * stock must be between min and max
     * @param name  name of the part
     * @param min   minimum number of parts
     * @param max   maximum number of parts
     * @param stock inventory availability of the part
     */
    private void checkArguments(String name, int min, int max, int stock) {
        checkName(name);
        checkMinMaxStock(min, max, stock);
    }


    /**
     * Checks that given arguments are correct. Min must be less than max, stock must be between min and max
     * @param min   minimum number of parts
     * @param max   maximum number of parts
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
     * @param name name of the part to check
     * @throws IllegalArgumentException if name consists only of numbers
     */
    private void checkName(String name) {
        try {
            Integer.parseInt(name);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Sets or changes the Machine ID of the part
     * @param machineId Machine ID to change the current to
     */
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    /**
     * Retrieves the part's Machine ID
     * @return part's Machine ID
     */
    public int getMachineId() {
        return this.machineId;
    }

    /**
     * Compares two objects if they belong to the same class and returns true if the objects are equal, otherwise
     * false
     * @param object Object to compare this part to
     * @return true if the objects are equal, false otherwise
     */
    @Override
    public boolean equals(Object object) {
        if (this.getClass() == object.getClass()) {
            return this.getId() == ((InHouse) object).getId();
        }
        return false;
    }
}
