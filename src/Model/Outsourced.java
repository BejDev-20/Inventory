package Model;

/**
 * Represents a part that is outsourced. Part has a unique ID, name, price, stock availability, minimum and maximum
 * values, company name.
 * @author Iulia Bejsovec
 * @version 12/2020
 */
public class Outsourced extends Part {
    /* Name of the company the part has been outsourced from */
    private String companyName;

    /**
     * Constructor to create an Outsourced part
     * @param id          unique id of the part
     * @param name        name of the part
     * @param price       price per one unit (part)
     * @param stock       number of parts of this kind available
     * @param min         minimum value for the number of parts
     * @param max         maximum value for the number of parts
     * @param companyName name of the company the part has been outsourced to
     */
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        checkArguments(name, stock, min, max, companyName);
        this.companyName = companyName;
    }

    /**
     * Checks that all values of the arguments are allowed. Name and company name cannot consists only of numbers,
     * min and max cannot equal to 0 and min cannot be bigger than max. Stock must be between min and max
     * @param name        name of the part
     * @param min         minimum value for the number of parts
     * @param max         maximum value for the number of parts
     * @param stock       inventory availability of the part
     * @param companyName name of the company the part has been outsourced to
     */
    private void checkArguments(String name, int stock, int min, int max, String companyName) {
        checkName(name);
        checkMinMaxStock(min, max, stock);
        checkCompanyName(companyName);
    }

    /**
     * Checks that given arguments are correct. Company name cannot consist only numbers
     * @param companyName name of the company supplying the part
     * @throws IllegalArgumentException if the company name consists only of numbers
     */
    private void checkCompanyName(String companyName) {
        try {
            Integer.parseInt(companyName);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException();
        }
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
     * Sets or changes the company name the part has been outsourced to
     * @param companyName Name of the company to change the current to
     */
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    /**
     * Retrieves the company name the part has been outsourced to
     * @return Company name the part has been outsourced to
     */
    public String getCompanyName() {
        return this.companyName;
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
            return this.getId() == ((Outsourced) object).getId();
        }
        return false;
    }
}
