//Han Zhang; hanzhan2
package hw1;

public class Product {
	String ndbNumber;
	String productName; 
	String manufacturer;
	String ingredients;
	float servingSize;
	String servingUom;
	float householdSize;
	String househouldUom;
	//Constructor
	Product(String ndb, String productN, String manf, String ingred){
		ndbNumber = ndb;
		productName = productN;
		manufacturer = manf;
		ingredients = ingred;
	}
}
