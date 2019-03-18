/**
 * @author Han Zhang  hanzhan2
 */
package hw3;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Map;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;

public class CSVFiler extends DataFiler {

    @Override
    public void writeFile(String filename) {
        //append all data to stringbuilder and use it to write
        StringBuilder sb = new StringBuilder();
        if(NutriByte.person instanceof Female) {
            sb.append("Female").append(",");
        } else {
            sb.append("Male").append(",");
        }
        sb.append(NutriByte.person.age).append(",");
        sb.append(NutriByte.person.weight).append(",");
        sb.append(NutriByte.person.height).append(",");
        sb.append(NutriByte.person.physicalActivityLevel).append(",");
        sb.append(NutriByte.person.ingredientsToWatch);
        sb.append("\n");
        if(NutriByte.person.dietProductsList.size() > 0) {
            for(Product prod: NutriByte.person.dietProductsList) {
                sb.append(prod.getNdbNumber()).append(",");
                sb.append(prod.getServingSize()).append(",");
                sb.append(prod.getHouseholdSize());
                sb.append("\n");
            }
        }
        //write to file with bufferwriter
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename));) {
            bw.write(sb.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean readFile(String filename) {
        //read file and add product
        try (BufferedReader br = new BufferedReader(new FileReader(filename))){
            StringBuilder sb = new StringBuilder();
            String row = null;
            while((row = br.readLine()) != null) {
                sb.append(row).append("\n");
            }
            String[] datas = sb.toString().split("\n");
            if (validatePersonData(datas[0]) == null){
                return false;
            }
            //set person up
            NutriByte.person = validatePersonData(datas[0]);
            NutriProfiler.createNutriProfile(NutriByte.person);

            for (int i = 1; i < datas.length; i++){
                //validate product
                Product tmpProduct = validateProductData(datas[i]);
                if (tmpProduct != null) {
                    Product tmp = Model.productsMap.get(tmpProduct.getNdbNumber());
                    tmpProduct.setProductName(tmp.getProductName());
                    tmpProduct.setHouseholdUom(tmp.getHouseholdUom());
                    tmpProduct.setServingUom(tmp.getServingUom());
                    tmpProduct.setManufacturer(tmp.getManufacturer());
                    tmpProduct.setIngredients(tmp.getIngredients());
                    ObservableMap<String, Product.ProductNutrient> pNtmp = FXCollections.observableHashMap();
                    for(Map.Entry<String, Product.ProductNutrient> a: tmp.getProductNutrients().entrySet()) {
                        pNtmp.put(a.getKey(),  tmpProduct.new ProductNutrient(a.getValue().getNutrientCode(), a.getValue().getNutrientQuantity()));
                    }
                    tmpProduct.setProductNutrients(pNtmp);
                    NutriByte.person.dietProductsList.add(tmpProduct);
                    NutriByte.model.searchResult.add(tmpProduct);
                }
            }
            //generate dietnutrient map and set up the chart and table
            NutriByte.person.populateDietNutrientsMap();
            NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
            NutriByte.view.nutriChart.updateChart();

            //only set up the data when get valid input for the first time
            if(NutriByte.model.searchResult.size() > 0) {
                NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResult);
                NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResult.size() + " product(s) found");
                NutriByte.view.productsComboBox.getSelectionModel().select(0);
                Product p = NutriByte.view.productsComboBox.getValue();
                ObservableList<Product.ProductNutrient> pn = FXCollections.observableArrayList();
                for(Map.Entry<String, Product.ProductNutrient> tmp : p.getProductNutrients().entrySet()) {
                    pn.add(tmp.getValue());
                }
                NutriByte.view.productNutrientsTableView.setItems(pn);

                NutriByte.view.servingSizeLabel.textProperty().setValue(String.format("%.2f, %s", Model.productsMap.get(p.getNdbNumber()).getServingSize(), p.getServingUom()));
                NutriByte.view.householdSizeLabel.textProperty().setValue(String.format("%.2f, %s", Model.productsMap.get(p.getNdbNumber()).getHouseholdSize(), p.getHouseholdUom()));
                NutriByte.view.dietServingUomLabel.textProperty().setValue(Model.productsMap.get(p.getNdbNumber()).getServingUom());
                NutriByte.view.dietHouseholdUomLabel.textProperty().setValue(Model.productsMap.get(p.getNdbNumber()).getHouseholdUom());
                NutriByte.view.productIngredientsTextArea.textProperty().setValue("Product Ingredients: " + p.getIngredients());

            }
            return true;
        }
        catch (NumberFormatException | IOException e) {
        }
        return false;
    }

    Person validatePersonData(String data) {
        try {
            String error = "";
            try{
                String[] p = data.split(",");
                if (!(p[0].trim().equalsIgnoreCase("female") || p[0].trim().equalsIgnoreCase("male"))){
                    throw new InvalidProfileException(String.format("The profile must have gender. %nFemale or Male as the first word"));
                }
                error = String.format("Invalid data for age: %s %nAge must be a number", p[1].trim());
                float age = Float.parseFloat(p[1].trim());
                if(age < 0){
                    throw new InvalidProfileException("Invalid age number. Age should be positive number.");
                }

                error = String.format("Invalid data for weight: %s %nWeight must be a number", p[2].trim());
                float weight = Float.parseFloat(p[2].trim());
                if(weight < 0){
                    throw new InvalidProfileException("Invalid weight number. Weight should be positive number.");
                }

                error = String.format("Invalid data for height: %s %nHeight must be a number", p[3].trim());
                float height = Float.parseFloat(p[3].trim());
                if(height < 0){
                    throw new InvalidProfileException("Invalid height number. Height should be positive number.");
                }

                float physicalActivityLevel = Float.parseFloat(p[4].trim());
                if(physicalActivityLevel != 1.0f && physicalActivityLevel != 1.1f && physicalActivityLevel != 1.25f && physicalActivityLevel != 1.48f){
                    throw new InvalidProfileException(String.format("Invalid physical activity level: %s%nMust be 1.0, 1.1, 1.25 or 1.48", p[4]));
                }

                StringBuilder sb = new StringBuilder(); //ingredients to watch not necessary
                int i = 5;
                while (i < p.length) {
                    if(i == p.length) {
                        sb.append(p[i]).append(", ");
                    }else {
                        sb.append(p[i]);
                    }
                    i++;
                }
                if(p[0].trim().equalsIgnoreCase("female")) {
                    return new Female(age, weight, height, physicalActivityLevel, sb.toString());
                } else {
                    return new Male(age, weight, height, physicalActivityLevel, sb.toString());
                }

            } catch(InvalidProfileException e) {
                throw new InvalidProfileException("Could not read profile data");
            } catch(NumberFormatException e){
                try {
                    throw new InvalidProfileException(error);
                }catch(InvalidProfileException e2){
                    throw new InvalidProfileException("Could not read profile data");
                }
            }
        }catch(InvalidProfileException e3) {
        }
        return null;
    }

    Product validateProductData(String data) {
        String[] p = data.split(",");
        try{
            if(p.length < 3) {
                throw new InvalidProfileException(String.format("Cannot read %s%nThe data must be - "
                        + "String, number, number - for ndbNumber, serving size, household size", data));
            }
            String ndbNumber = p[0];
            if (!Model.productsMap.containsKey(ndbNumber)){
                throw new InvalidProfileException(String.format("No product found with this code: %s", ndbNumber));
            }
            float servingSize = Float.parseFloat(p[1]);
            float houseHoldSize = Float.parseFloat(p[2]);
            if(servingSize < 0 || houseHoldSize < 0){
                return null;
            }
            Product result = new Product();
            result.setNdbNumber(ndbNumber);
            result.setServingSize(servingSize);
            result.setHouseholdSize(houseHoldSize);
            return result;

        }catch(InvalidProfileException e) {
        } catch(NumberFormatException e2){
            try {
                throw new InvalidProfileException(String.format("Cannot read %s%nThe data must be - "
                        + "String, number, number - for ndbNumber, serving size, household size", data));
            }catch(InvalidProfileException e) {
            }
        }
        return null;
    }
}
