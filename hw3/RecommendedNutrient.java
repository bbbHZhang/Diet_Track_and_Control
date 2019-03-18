/**
 * @author Han Zhang  hanzhan2
 */
package hw3;

import javafx.beans.property.FloatProperty;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class RecommendedNutrient {
    private StringProperty nutrientCode = new SimpleStringProperty();
    private FloatProperty nutrientQuantity = new SimpleFloatProperty();
    //default constructor
    RecommendedNutrient(){
        nutrientCode.setValue("");
    }
    //non-default constructor to set values
    RecommendedNutrient(String nutrientCode, Float nutrientQuantity){
        this.nutrientQuantity.setValue(nutrientQuantity);
        this.nutrientCode.setValue(nutrientCode);
    }
    //all setters and getters
    public StringProperty NutrientCodeProperty() {
        return nutrientCode;
    }
    
    public String getNutrientCode() {
        return nutrientCode.get();
    }

    public void setNutrientCode(StringProperty nutrientCode) {
        this.nutrientCode = nutrientCode;
    }

    public FloatProperty NutrientQuantityProperty() {
        return nutrientQuantity;
    }
    
    public float getNutrientQuantity() {
        return nutrientQuantity.get();
    }

    public void setNutrientQuantity(FloatProperty nutrientQuantity) {
        this.nutrientQuantity = nutrientQuantity;
    }

}
