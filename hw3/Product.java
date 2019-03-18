/**
 * @author Han Zhang  hanzhan2
 */
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

public class Product {
    private StringProperty ndbNumber = new SimpleStringProperty();
    private StringProperty productName = new SimpleStringProperty();
    private StringProperty manufacturer = new SimpleStringProperty();
    private StringProperty ingredients = new SimpleStringProperty();
    private FloatProperty servingSize = new SimpleFloatProperty();
    private FloatProperty householdSize = new SimpleFloatProperty();
    private StringProperty servingUom = new SimpleStringProperty();
    private StringProperty householdUom = new SimpleStringProperty();
    //Nutrient
    private ObservableMap<String, ProductNutrient> productNutrients = FXCollections.observableHashMap();
    //default constructor
    public Product() {
        ndbNumber.setValue("");
        productName.setValue("");
        manufacturer.setValue("");
        ingredients.setValue("");
        servingUom.setValue("");
        householdUom.setValue("");
    }
    //constructor to set values of Product's characters
    public Product(String ndbNumber, String productName, String manufacturer, String ingredients) {
        this.ndbNumber.setValue(ndbNumber);
        this.productName.setValue(productName);
        this.manufacturer.setValue(manufacturer);
        this.ingredients.setValue(ingredients);

    }

    @Override
    public String toString(){
        return getProductName() + " by " + getManufacturer();
    }

    //all getters and setters
    //inner class is at the bottom
    public String getNdbNumber() {
        return ndbNumber.get();
    }

    public StringProperty ndbNumberProperty() {
        return ndbNumber;
    }

    public void setNdbNumber(String ndbNumber) {
        this.ndbNumber.set(ndbNumber);
    }

    public String getProductName() {
        return productName.get();
    }

    public StringProperty productNameProperty() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName.set(productName);
    }

    public String getManufacturer() {
        return manufacturer.get();
    }

    public StringProperty manufacturerProperty() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer.set(manufacturer);
    }

    public String getIngredients() {
        return ingredients.get();
    }

    public StringProperty ingredientsProperty() {
        return ingredients;
    }

    public void setIngredients(String ingredients) {
        this.ingredients.set(ingredients);
    }

    public float getServingSize() {
        return servingSize.get();
    }

    public FloatProperty servingSizeProperty() {
        return servingSize;
    }

    public void setServingSize(float servingSize) {
        this.servingSize.set(servingSize);
    }

    public float getHouseholdSize() {
        return householdSize.get();
    }

    public FloatProperty householdSizeProperty() {
        return householdSize;
    }

    public void setHouseholdSize(float householdSize) {
        this.householdSize.set(householdSize);
    }

    public String getServingUom() {
        return servingUom.get();
    }

    public StringProperty servingUomProperty() {
        return servingUom;
    }

    public void setServingUom(String servingUom) {
        this.servingUom.set(servingUom);
    }

    public String getHouseholdUom() {
        return householdUom.get();
    }

    public StringProperty householdUomProperty() {
        return householdUom;
    }

    public void setHouseholdUom(String householdUom) {
        this.householdUom.set(householdUom);
    }

    public ObservableMap<String, ProductNutrient> getProductNutrients() {
        return productNutrients;
    }

    public void setProductNutrients(ObservableMap<String, ProductNutrient> productNutrients) {
        this.productNutrients = productNutrients;
    }

    //InnerClass 
    public class ProductNutrient {
        private StringProperty nutrientCode = new SimpleStringProperty();
        private FloatProperty nutrientQuantity = new SimpleFloatProperty();
        public ProductNutrient() {
            nutrientCode.setValue("");
        }
        public ProductNutrient(String nutrientCode, Float nutrientQuantity) {
            this.nutrientCode.setValue(nutrientCode);
            this.nutrientQuantity.setValue(nutrientQuantity);
        }

        //all getters and setters
        public String getNutrientCode() {
            return nutrientCode.get();
        }

        public StringProperty nutrientCodeProperty() {
            return nutrientCode;
        }

        public void setNutrientCode(String nutrientCode) {
            this.nutrientCode.set(nutrientCode);
        }

        public float getNutrientQuantity() {
            return nutrientQuantity.get();
        }

        public FloatProperty nutrientQuantityProperty() {
            return nutrientQuantity;
        }

        public void setNutrientQuantity(float nutrientQuantity) {
            this.nutrientQuantity.set(nutrientQuantity);
        }
    }


}