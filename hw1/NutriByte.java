//Han Zhang; hanzhan2
package hw1;

import java.util.Scanner;

public class NutriByte {
	Model model = new Model();  				//will handle all data read from the data files
	Scanner input = new Scanner(System.in);  	//to be used for all console i/o in this class

	static final String PRODUCT_FILE = "data/SampleProducts.csv";
	static final String NUTRIENT_FILE = "data/SampleNutrients.csv";
	static final String SERVING_SIZE_FILE = "data/SampleServingSize.csv";

	//do not change this method
	public static void main(String[] args) {
		NutriByte nutriByte = new NutriByte();
		nutriByte.model.readProducts(PRODUCT_FILE);
		nutriByte.model.readNutrients(NUTRIENT_FILE);
		nutriByte.model.readServingSizes(SERVING_SIZE_FILE);
		switch (nutriByte.getMenuChoice()) {
		case 1: {
			nutriByte.printSearchResults(nutriByte.searchProductsWithSelectedIngredients(nutriByte.getIngredientChoice()));
			break;
		}
		case 2: {
			int nutrientChoice = nutriByte.getNutrientChoice();
			nutriByte.printSearchResults(nutriByte.searchProductsWithSelectedNutrient(nutrientChoice), nutrientChoice);
			break;
		}
		case 3:  
		default: System.out.println("Good Bye!"); break;
		}
	}

	//do not change this method
	int getMenuChoice() {
		System.out.println("*** Welcome to NutriByte ***");
		System.out.println("--------------------------------------------------");
		System.out.println("1. Find ingredient(s)");
		System.out.println("2. Find a nutrient");
		System.out.println("3. Exit");
		input = new Scanner(System.in);
		return input.nextInt();
	}

	//do not change this method
	String getIngredientChoice() {
		input.nextLine();
		System.out.println("Type the keywords to search for the ingredients");
		System.out.println("--------------------------------------------------");
		return input.nextLine();
	}

	int getNutrientChoice() {
		//write your code here
		input.nextLine();

		System.out.println("Select the nutrient you are looking for");
		System.out.println("--------------------------------------------------");
		for(int a = 0; a < model.nutrients.length; a++) {
			System.out.printf("%2d. %-40s", a+1, model.nutrients[a].nutrientName);
			if((a + 1) % 3 ==0) { //for every three nutrients, start a new line 
				System.out.printf("%n");
			}
		}
		System.out.println("");
		System.out.println("--------------------------------------------------");
		int result = Integer.parseInt(input.nextLine());
		return result;
	}


	Product[] searchProductsWithSelectedIngredients(String searchString) {
		//use a boolean to deal with missing ingredients 
		String[] wanted = searchString.toLowerCase().split(" ");
		boolean isFound = false;
		StringBuilder wantedNDBsb = new StringBuilder();

		//find the unique product ndbNumber based on searchString
		for(String s: wanted) {
			for(int a = 0; a < model.products.length; a++) {
				if(model.products[a].ingredients.toLowerCase().contains(s)) {
					isFound = true;
					if(!wantedNDBsb.toString().contains(model.products[a].ndbNumber)) {
						wantedNDBsb.append(model.products[a].ndbNumber).append(";");
					}

				}

			}
		}	

		//find the all products based on the ndbNumber
		String[] wantedNDB = wantedNDBsb.toString().split(";");
		Product[] result = new Product[wantedNDB.length];
		for(int x = 0; x < wantedNDB.length; x++) {
			for(int y = 0 ; y < model.products.length; y++) {
				if(wantedNDB[x].equals(model.products[y].ndbNumber)) {
					result[x] = model.products[y];
				}
			}
		}

		if(isFound != false) {
			return result;
		}else {
			Product[] missResult = new Product[0];
			return missResult;
		}
	}


	Product[] searchProductsWithSelectedNutrient(int menuChoice) {

		String nCode = model.nutrients[menuChoice - 1].nutrientCode;

		//Based on the give nutrientCode, store all ndbNumber in StringBuilder and change to String[]
		StringBuilder sbAllNDB = new StringBuilder();
		for(int a = 0; a < model.productNutrients.length; a++) {
			if(model.productNutrients[a].nutrientCode.equals(nCode) && model.productNutrients[a].quantity != 0 ) {	//quantity can not equal to 0
				sbAllNDB.append(model.productNutrients[a].ndbNumber).append(";");
			}
		}
		String[] allNDB = sbAllNDB.toString().split(";");

		//Based on the given allNDB, find and store all
		Product[] result = new Product[allNDB.length];
		for(int a = 0; a < allNDB.length; a++) {
			for(int b = 0; b < model.products.length; b++) {
				if(model.products[b].ndbNumber.equals(allNDB[a])) {
					result[a] = model.products[b];
				}
			}

		}

		return result;
	}

	void printSearchResults(Product[] searchResults) {
		//write your code here
		System.out.printf("*** %d results found ***%n", searchResults.length);
		if(searchResults.length != 0) {
			for(int a = 0; a < searchResults.length; a++) {
				System.out.printf("%d. %s from %s%n", a+1 , searchResults[a].productName, searchResults[a].manufacturer);
			}
		}
	}

	void printSearchResults(Product[] searchResults, int nutrientChoice) {
		//write your code here
		System.out.printf("*** %d products found ***%n", searchResults.length);

		for(int a = 0; a < searchResults.length; a++) {
			String nCode = model.nutrients[nutrientChoice - 1].nutrientCode;
			float quant = 0;
			String nUom = null;
			//find the productNutrient with both ndbNumber and nutrientCode
			for(ProductNutrient pn: model.productNutrients) {
				if(pn.ndbNumber.equals(searchResults[a].ndbNumber) && pn.nutrientCode.equals(nCode)) {
					quant = pn.quantity;
					nUom = pn.nutrientUom;
					break;
				}
			}
			System.out.printf("%d. %.2f %s of %s: %s has %.2f %s of %s%n", a + 1, searchResults[a].householdSize, searchResults[a].househouldUom, 
					searchResults[a].ndbNumber, searchResults[a].productName,(searchResults[a].servingSize * quant / 100 ) , 
					nUom , model.nutrients[nutrientChoice - 1].nutrientName);
		}
	}
}
