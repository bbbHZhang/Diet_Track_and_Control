//Han Zhang; hanzhan2
package hw1;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Scanner;


public class Model {//

	Product[] products;
	Nutrient[] nutrients;
	ProductNutrient[] productNutrients;

	void readProducts(String fileName) {

		Scanner input = null;
		try {
			input = new Scanner(new FileReader(fileName));

		}catch(FileNotFoundException e) {
			System.out.println(e);
		}

		//read all data and store in productsSB
		StringBuilder productsSB = new StringBuilder();
		while(input.hasNextLine()) {
			productsSB.append(input.nextLine()).append("\n");
		}
		String[] rows = productsSB.toString().split("\n");	


		//count products' quantity, initialize products and read all products
		//get rid of the tile line
		products = new Product[rows.length - 1];
		for(int a = 0 ; a < (rows.length - 1); a++) {
			String[] row = rows[a + 1].split("\",\"");
			products[a] = new Product(row[0].replace("\"", ""), row[1], row[4], row[7].replace("\"", "")); 
			//I got String.replace method idea from https://stackoverflow.com/questions/2608665/how-can-i-trim-beginning-and-ending-double-quotes-from-a-string

		}

		input.close();
	}

	void readNutrients(String fileName) {
		Scanner input = null;
		try {
			input = new Scanner(new FileReader(fileName));

		}catch(FileNotFoundException e) {
			System.out.println(e);
		}

		//read all data and store in nutrientSB
		StringBuilder nutrientSB = new StringBuilder();
		while(input.hasNext()) {
			nutrientSB.append(input.nextLine()).append("\n");
		}

		//find unique nutrient and store all required columns in separated StringBuilders
		String[] rows = nutrientSB.toString().split("\n");
		StringBuilder uniqueCodeSB = new StringBuilder();
		StringBuilder uniqueNameSB = new StringBuilder();
		StringBuilder uniqueUomSB = new StringBuilder();

		//store all data from Nutrients.csv in productNutrients
		productNutrients = new ProductNutrient[(rows.length - 1)];
		for(int a = 0 ; a < (rows.length - 1 ); a++) {
			String[] row = rows[a + 1].split("\",\"");		//skip the first title line
			productNutrients[a] = new ProductNutrient(row[0].replace("\"", ""), row[1], row[2], Float.parseFloat(row[4]), row[5].replace("\"", "") );

			if(!uniqueCodeSB.toString().contains(row[1])) {
				uniqueCodeSB.append(row[1]).append(";");
				uniqueNameSB.append(row[2]).append(";");
				uniqueUomSB.append(row[5]).append(";");
			}else {
				continue;
			}

		}

		//initialize nutrients and transfer all StringBuilder values to it;
		String[] uniqueCode = uniqueCodeSB.toString().split(";");
		String[] uniqueName = uniqueNameSB.toString().split(";");
		String[] uniqueUom = uniqueUomSB.toString().split(";");
		nutrients = new Nutrient[uniqueCode.length];
		for(int b = 0; b < uniqueCode.length; b++) {
			nutrients[b] = new Nutrient(uniqueCode[b], uniqueName[b], uniqueUom[b]);
		}


		input.close();
	}

	void readServingSizes(String fileName) {
		Scanner input = null;
		try {
			input = new Scanner(new FileReader(fileName));

		}catch(FileNotFoundException e) {
			System.out.println(e);
		}

		//store all data in StringBuilder servingSizeSB
		StringBuilder servingSizeSB = new StringBuilder();
		while(input.hasNextLine()) {
			servingSizeSB.append(input.nextLine()).append("\n");
		}

		//match with products by ndbNumber and store all other available data in to products
		String[] allServingSize = servingSizeSB.toString().split("\"\n\"");
		for(int c = 0 ; c < (allServingSize.length - 1); c++) {
			String[] perServingSize = allServingSize[c + 1].split("\",\"");

			for(int d = 0; d < products.length; d++) {
				//float need to be careful about null value
				if(products[d].ndbNumber.equals(perServingSize[0].replace("\"", ""))) {
					if(perServingSize[1] == null || perServingSize[1].length() == 0) {
						products[d].servingSize = 0;
					}else {
						products[d].servingSize = Float.parseFloat(perServingSize[1]);
					}
					
					products[d].servingUom = perServingSize[2];
					if(perServingSize[3] == null || perServingSize[3].length() == 0) {
						products[d].householdSize = 0;
					}else {
						products[d].householdSize = Float.parseFloat(perServingSize[3]);
					}
					products[d].househouldUom = perServingSize[4].replace("\"", "");
				}
			}
		}

		input.close();

	}

}
