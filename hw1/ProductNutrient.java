//Han Zhang; hanzhan2
package hw1;

public class ProductNutrient {
	String ndbNumber;
	String nutrientCode;
	String nutrientName;
	float quantity;
	String nutrientUom;
	//Constructor
	ProductNutrient(String ndb, String nCode, String nName, float quant, String nUom){
		ndbNumber = ndb;
		nutrientCode = nCode;
		nutrientName = nName;
		quantity = quant;
		nutrientUom = nUom;
	}

}
