/**
 * @author Han Zhang  hanzhan2
 */
package hw3;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;



public class Model {
    static ObservableMap<String, Product> productsMap = FXCollections.observableMap(new HashMap<String, Product>());
    static ObservableMap<String, Nutrient> nutrientsMap = FXCollections.observableMap(new HashMap<String, Nutrient>());
    ObservableList<Product> searchResult = FXCollections.observableArrayList(new ArrayList<Product>());  

    public void writeProfile(String filename) {
        CSVFiler csvf = new CSVFiler();
        csvf.writeFile(filename);
        
    }

    public void readNutrients(String filename) {
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                //add all nutrients to nutrientsMap, HashMap deals with duplicates automatically
                Nutrient nutrient = new Nutrient(csvRecord.get(1), csvRecord.get(2), csvRecord.get(5));
                nutrientsMap.put(csvRecord.get(1), nutrient);

                //add all non-zero nutrients values and uom to specific product in productsMap
                if (Float.parseFloat(csvRecord.get(4)) != 0.0 ) {
                    productsMap.get(csvRecord.get(0)).getProductNutrients().put(csvRecord.get(1),
                            productsMap.get(csvRecord.get(0)).new ProductNutrient(csvRecord.get(1), Float.parseFloat(csvRecord.get(4))));
                }
            }
        }
        catch (FileNotFoundException e1) { e1.printStackTrace(); }
        catch (IOException e1) { e1.printStackTrace(); }
    }

    public void readProducts(String filename){
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                //add all products to productsMap
                Product product = new Product(csvRecord.get(0), csvRecord.get(1), csvRecord.get(4), csvRecord.get(7));
                productsMap.put(csvRecord.get(0), product);
            }
        }
        catch (FileNotFoundException e1) { e1.printStackTrace(); }
        catch (IOException e1) { e1.printStackTrace(); }
    }

    public void readServingSizes(String filename){
        CSVFormat csvFormat = CSVFormat.DEFAULT.withFirstRecordAsHeader();
        try {
            CSVParser csvParser = CSVParser.parse(new FileReader(filename), csvFormat);
            for (CSVRecord csvRecord : csvParser) {
                //test serving size is zero or not
                //then add all non-zero serving size to specific product in productsMap
                if (productsMap.get(csvRecord.get(0)).getNdbNumber().equals(csvRecord.get(0)) 
                        && csvRecord.get(1) != null && csvRecord.get(1).length() != 0
                        && csvRecord.get(3) != null && csvRecord.get(3).length() != 0) {
                    productsMap.get(csvRecord.get(0)).setServingSize(Float.parseFloat(csvRecord.get(1)));
                    productsMap.get(csvRecord.get(0)).setServingUom(csvRecord.get(2));
                    productsMap.get(csvRecord.get(0)).setHouseholdSize(Float.parseFloat(csvRecord.get(3)));
                    productsMap.get(csvRecord.get(0)).setHouseholdUom(csvRecord.get(4));
                }
            }
        }
        catch (FileNotFoundException e1) { e1.printStackTrace(); }
        catch (IOException e1) { e1.printStackTrace(); }
    }

    public boolean readProfiles(String filename){
        DataFiler df;
        String s = filename.substring(filename.lastIndexOf(".") + 1, filename.length());
        if (s.equals("csv")) {
            df = new CSVFiler();
        } else if (s.equals("xml")) {
            df = new XMLFiler();
        } else {return false;}//return false if readProfile fails
        return df.readFile(filename);
    }
}
