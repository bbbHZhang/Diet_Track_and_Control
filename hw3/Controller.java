/**
 * @author Han Zhang  hanzhan2
 */
package hw3;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class Controller {

    class RecommendNutrientsButtonHandler implements EventHandler<ActionEvent> {
        //read data, calculate all nutrients and update results
        @Override
        public void handle(ActionEvent evclent) {
            //String error is used to save error message
            String error = "";
            //set default value of age, weight, height as 0, read data
            try {
                //The start of invalid data handling
                if(NutriByte.view.genderComboBox.getValue() == null || NutriByte.view.genderComboBox.getValue().length() == 0) {
                    throw new InvalidProfileException("Missing Gender Information");
                }
                error = "Incorrect age input. Must be a number";
                float age = 0;
                if (NutriByte.view.ageTextField.getText() != "" && NutriByte.view.ageTextField.getText().length() != 0) {
                    age = Float.parseFloat(NutriByte.view.ageTextField.getText());
                    if(age < 0){
                        throw new InvalidProfileException("Age must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing age information");
                }
                error = "Incorrect weight input. Must be a number";
                float weight = 0;
                if (NutriByte.view.weightTextField.getText() != "" && NutriByte.view.weightTextField.getText().length() != 0) {
                    weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
                    if(weight < 0){
                        throw new InvalidProfileException("Weight must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing weight information");
                }
                error = "Incorrect height input. Must be a number";
                float height = 0;
                if (NutriByte.view.heightTextField.getText() != "" && NutriByte.view.heightTextField.getText().length() != 0) {
                    height = Float.parseFloat(NutriByte.view.heightTextField.getText());
                    if(height < 0){
                        throw new InvalidProfileException("Height must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing Height information");
                }
                //set default value physical activity as 1
                float physical = 1;
                if (NutriByte.view.physicalActivityComboBox.getValue() != null && NutriByte.view.physicalActivityComboBox.getValue().length() != 0) {
                    for (NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()) {
                        if (e.getName().equalsIgnoreCase(NutriByte.view.physicalActivityComboBox.getValue())) {
                            physical = e.getPhysicalActivityLevel();
                        }
                    }
                }
                String ingredientsToWatch =  NutriByte.view.ingredientsToWatchTextArea.getText();
                //throw excpetion if gender is not selected
                if (NutriByte.view.genderComboBox.getValue() != null && NutriByte.view.genderComboBox.getValue().length() != 0) {
                    if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("female")) {
                        NutriByte.person = new Female(age, weight, height, physical, ingredientsToWatch);
                    } else if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("male")) {
                        NutriByte.person = new Male(age, weight, height, physical, ingredientsToWatch);
                    }
                } else {
                    throw new InvalidProfileException("Missing Gender Information");
                }
                //The end of invalid data handling

                //load data: create Profile & populate dietNutrients
                NutriProfiler.createNutriProfile(NutriByte.person);
                NutriByte.person.populateDietNutrientsMap();
                NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
            } catch(NumberFormatException e1) {
                //throw two info windows
                try {
                    throw new InvalidProfileException(error);
                } catch (InvalidProfileException e) {
                }
            } catch (InvalidProfileException e2){
            }


        }
    }

    class OpenMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            try{
                //read file
                FileChooser fileChooser = new FileChooser();
                fileChooser.setTitle("Select file");
                fileChooser.getExtensionFilters().addAll(
                        new ExtensionFilter("CSV Files (*.csv)", "*.csv"),
                        new ExtensionFilter("XML Files (*.xml)", "*.xml"),
                        new ExtensionFilter("All Files (*.*)", "*.*"));

                File file = null;
                //validate file
                if ((file = fileChooser.showOpenDialog(NutriByte.view.root.getScene().getWindow())) != null){
                    //call helper method to clean data
                    cleanData();
                    //if file could be read
                    if(NutriByte.model.readProfiles(file.getAbsolutePath())) {
                        //load data in personal info windows
                        float f = NutriByte.person.physicalActivityLevel;
                        NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
                        NutriByte.view.ageTextField.setText(String.valueOf(NutriByte.person.age));
                        NutriByte.view.weightTextField.setText(String.valueOf(NutriByte.person.weight));
                        NutriByte.view.heightTextField.setText(String.valueOf(NutriByte.person.height));
                        for(NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()) {
                            if(f == e.getPhysicalActivityLevel()) {
                                NutriByte.view.physicalActivityComboBox.setValue(e.getName());
                            }
                        }
                        NutriByte.person.physicalActivityLevel = f;

                        if(NutriByte.person instanceof Female) {
                            NutriByte.view.genderComboBox.setValue("Female");
                        } else if(NutriByte.person instanceof Male) {
                            NutriByte.view.genderComboBox.setValue("Male");
                        }

                        //create nutriprofile for the input person and update chart
                        NutriProfiler.createNutriProfile(NutriByte.person);
                        NutriByte.person.populateDietNutrientsMap();
                        NutriByte.view.nutriChart.updateChart();

                        NutriByte.view.recommendedNutrientsTableView.setItems(NutriByte.person.recommendedNutrientsList);
                        NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResult);

                        //set windows view
                        NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
                    } else {
                        throw new FileNotFoundException();
                    }
                }
            }catch (FileNotFoundException e){
            }
        }
    }

    class NewMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //clean current data
            cleanData();
            NutriByte.view.searchResultSizeLabel.setText("");
            NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResult);

            //create a new window and clear all values
            NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
            NutriByte.view.initializePrompts();
        }
    }

    //Provided by Prof
    class AboutMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            Alert alert = new Alert(AlertType.INFORMATION);
            alert.setTitle("About");
            alert.setHeaderText("NutriByte");
            alert.setContentText("Version 2.0 \nRelease 1.0\nCopyleft Java Nerds\nThis software is designed purely for educational purposes.\nNo commercial use intended");
            Image image = new Image(getClass().getClassLoader().getResource(NutriByte.NUTRIBYTE_IMAGE_FILE).toString());
            ImageView imageView = new ImageView();
            imageView.setImage(image);
            imageView.setFitWidth(300);
            imageView.setPreserveRatio(true);
            imageView.setSmooth(true);
            alert.setGraphic(imageView);
            alert.showAndWait();
        }
    }

    class SaveMenuItemHandler implements  EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            Person p = null;
            //The start of validating input data
            String error = "";
            try {
                if(NutriByte.view.genderComboBox.getValue() == null || NutriByte.view.genderComboBox.getValue().length() == 0) {
                    throw new InvalidProfileException("Missing Gender Information");
                }
                error = "Incorrect age input. Must be a number";
                float age = 0;
                if (NutriByte.view.ageTextField.getText() != "" && NutriByte.view.ageTextField.getText().length() != 0) {
                    age = Float.parseFloat(NutriByte.view.ageTextField.getText());
                    if(age < 0){
                        throw new InvalidProfileException("Age must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing age information");
                }
                error = "Incorrect weight input. Must be a number";
                float weight = 0;
                if (NutriByte.view.weightTextField.getText() != "" && NutriByte.view.weightTextField.getText().length() != 0) {
                    weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
                    if(weight < 0){
                        throw new InvalidProfileException("Weight must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing weight information");
                }
                error = "Incorrect height input. Must be a number";
                float height = 0;
                if (NutriByte.view.heightTextField.getText() != "" && NutriByte.view.heightTextField.getText().length() != 0) {
                    height = Float.parseFloat(NutriByte.view.heightTextField.getText());
                    if(height < 0){
                        throw new InvalidProfileException("Height must be a positive number");
                    }
                } else {
                    throw new InvalidProfileException("Missing Height information");
                }
                //set default value physical activity as 1
                float physical = 1;
                if (NutriByte.view.physicalActivityComboBox.getValue() != null && NutriByte.view.physicalActivityComboBox.getValue().length() != 0) {
                    for (NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()) {
                        if (e.getName().equalsIgnoreCase(NutriByte.view.physicalActivityComboBox.getValue())) {
                            physical = e.getPhysicalActivityLevel();
                        }
                    }
                }
                String ingredientsToWatch =  NutriByte.view.ingredientsToWatchTextArea.getText();
                //throw exception if gender is not selected
                if (NutriByte.view.genderComboBox.getValue() != null && NutriByte.view.genderComboBox.getValue().length() != 0) {
                    if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("female")) {
                        p = new Female(age, weight, height, physical, ingredientsToWatch);
                    } else if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("male")) {
                        p = new Male(age, weight, height, physical, ingredientsToWatch);
                    }
                } else {
                    throw new InvalidProfileException("Missing Gender Information");
                }
            } catch (InvalidProfileException e) {
            } catch(NumberFormatException e1) {
                try {
                    throw new InvalidProfileException(error);
                } catch (InvalidProfileException e) {
                }
            }
            //if fail to create person, return
            if (p == null) {
                return;
            }
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("CSV Files", "*.csv"),
                    new ExtensionFilter("XML Files", "*.xml"),
                    new ExtensionFilter("All Files", "*.*"));

            File file = fileChooser.showSaveDialog(null);
            //if file not found, return
            if(file == null) {
                return;
            }
            String filename = file.getAbsolutePath();
            //call writeProfile to write file
            NutriByte.model.writeProfile(filename);
        }
    }

    class CloseMenuItemHandler implements  EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent arg0) {
            //clean data and close windows
            cleanData();
            NutriByte.view.root.setCenter(NutriByte.view.setupWelcomeScene());
        }
    }


    class SearchButtonHandle implements EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            NutriByte.model.searchResult = FXCollections.observableArrayList(new ArrayList<Product>());
            //get all strings from the textField
            String productToSearch = NutriByte.view.productSearchTextField.getText().toLowerCase();
            String nutrientToSearch = NutriByte.view.nutrientSearchTextField.getText().toLowerCase();
            String ingredientToSearch = NutriByte.view.ingredientSearchTextField.getText().toLowerCase();
            //if all strings are empty, put all of products map to search result
            if((ingredientToSearch == null||ingredientToSearch.length() == 0) && (productToSearch == null||productToSearch.length() == 0) 
                    && (nutrientToSearch == null || nutrientToSearch.length() == 0)) {
                System.out.println("in!!!!");
                for(Product p: Model.productsMap.values()) {
                    NutriByte.model.searchResult.add(p);
                }
                System.out.println(Model.productsMap.size());
            } else {
                //only actually search when at least one string is not null
                if (ingredientToSearch == null) {
                    ingredientToSearch = "";
                }
                if (productToSearch == null) {
                    productToSearch = "";
                }

                //Key is nutrient code
                //Go through the whole nutrient map and Store all nutrients contains nutrientToSearch
                Map<String, Nutrient> nutrientResult = new HashMap<String, Nutrient>();
                if (nutrientToSearch != null) {
                    for (Map.Entry<String, Nutrient> nutriKey: Model.nutrientsMap.entrySet()) {
                        if (nutriKey.getValue().getNutrientName().toLowerCase().contains(nutrientToSearch)) {
                            nutrientResult.put(nutriKey.getKey(), nutriKey.getValue());

                        }
                    }
                }
                //go through the whole products map
                for (Map.Entry<String, Product> key: Model.productsMap.entrySet()) {
                    //check whether this prodcut contain the wanted ingredients or product name
                    if (key.getValue().getProductName().toLowerCase().contains(productToSearch) 
                            && key.getValue().getIngredients().toLowerCase().contains(ingredientToSearch) ) {
                        //only check the nutrient search result when the nutrientToSearch is not null
                        if (nutrientToSearch != null) {
                            for (String nutriCode: nutrientResult.keySet()) {
                                if(key.getValue().getProductNutrients().containsKey(nutriCode)) {
                                    NutriByte.model.searchResult.add(key.getValue());
                                    break;
                                }
                            } 
                        } else {
                            //add all necessary value only when 
                            NutriByte.model.searchResult.add(key.getValue());
                        }
                    }
                }
            }

            //set up the view with search result
            NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResult);
            NutriByte.view.searchResultSizeLabel.setText(NutriByte.model.searchResult.size() + " product(s) found");
            //only set up the combobox, all labess and tableview when search result is provided
            if(NutriByte.model.searchResult.size() > 0){
                NutriByte.view.productsComboBox.getSelectionModel().select(0);
                Product p = NutriByte.view.productsComboBox.getValue();
                ObservableList<Product.ProductNutrient> pn = FXCollections.observableArrayList();
                for(Map.Entry<String, Product.ProductNutrient> tmp : p.getProductNutrients().entrySet()) {
                    pn.add(tmp.getValue());
                }
                NutriByte.view.productNutrientsTableView.setItems(pn);
                NutriByte.view.servingSizeLabel.textProperty().setValue(String.format("%.2f %s", p.getServingSize(), p.getServingUom()));
                NutriByte.view.householdSizeLabel.textProperty().setValue(String.format("%.2f %s", p.getHouseholdSize(), p.getHouseholdUom()));
                NutriByte.view.dietServingUomLabel.textProperty().setValue(p.getServingUom());
                NutriByte.view.dietHouseholdUomLabel.textProperty().setValue(p.getHouseholdUom());
                NutriByte.view.productIngredientsTextArea.textProperty().setValue("Product Ingredients: " + p.getIngredients());
            }
            //clean all data if search result is not provided
            if(NutriByte.model.searchResult.size() == 0) {
                NutriByte.view.productNutrientsTableView.getItems().clear();
                NutriByte.view.servingSizeLabel.textProperty().setValue("");
                NutriByte.view.householdSizeLabel.textProperty().setValue("");
                NutriByte.view.dietServingUomLabel.textProperty().setValue("");
                NutriByte.view.dietHouseholdUomLabel.textProperty().setValue("");
                NutriByte.view.productIngredientsTextArea.textProperty().setValue("");
            }

            //add listener to productsCombobox
            NutriByte.view.productsComboBox.getSelectionModel().selectedItemProperty().addListener(new ProductComboBoxListener());
        }
    }

    class ProductComboBoxListener implements ChangeListener<Product> {
        @Override
        public void changed(ObservableValue<? extends Product> observalbe, Product oldValue, Product newValue) {
            //only change when products comboBox is selected
            if (NutriByte.view.productsComboBox.getSelectionModel().getSelectedIndex() >= 0) {
                Product p = NutriByte.view.productsComboBox.getValue();
                ObservableList<Product.ProductNutrient> pn = FXCollections.observableArrayList();
                for(Map.Entry<String, Product.ProductNutrient> tmp : p.getProductNutrients().entrySet()) {
                    pn.add(tmp.getValue());
                }
                //set up the table view
                NutriByte.view.productNutrientsTableView.setItems(pn);
                //set up all the labels
                NutriByte.view.servingSizeLabel.textProperty().setValue(String.format("%.2f, %s", p.getServingSize(), p.getServingUom()));
                NutriByte.view.householdSizeLabel.textProperty().setValue(String.format("%.2f, %s", p.getHouseholdSize(), p.getHouseholdUom()));
                NutriByte.view.dietServingUomLabel.textProperty().setValue(p.getServingUom());
                NutriByte.view.dietHouseholdUomLabel.textProperty().setValue(p.getHouseholdUom());
                NutriByte.view.productIngredientsTextArea.textProperty().setValue("Product Ingredients: " + p.getIngredients());
            }
        }

    }


    class ClearButtonHandler implements  EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            //clean all things related to the search result
            NutriByte.view.productSearchTextField.setText("");
            NutriByte.view.nutrientSearchTextField.setText("");
            NutriByte.view.ingredientSearchTextField.setText("");
            NutriByte.model.searchResult = FXCollections.observableArrayList(new ArrayList<Product>());
            NutriByte.view.servingSizeLabel.textProperty().setValue("");
            NutriByte.view.householdSizeLabel.textProperty().setValue("");
            NutriByte.view.dietServingUomLabel.textProperty().setValue("");
            NutriByte.view.dietHouseholdUomLabel.textProperty().setValue("");
            NutriByte.view.productIngredientsTextArea.textProperty().setValue("");
            NutriByte.view.searchResultSizeLabel.setText("");
            NutriByte.view.productsComboBox.setItems(NutriByte.model.searchResult);
            NutriByte.view.dietServingSizeTextField.setText("");
            NutriByte.view.dietHouseholdSizeTextField.setText("");
            NutriByte.view.productNutrientsTableView.getItems().clear();

        }
    }

    class AddDietButtonHandler implements  EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            //use Prodcut tmp to copy all the value from selected product
            Product tmp = NutriByte.view.productsComboBox.getSelectionModel().getSelectedItem();
            Product p = new Product(tmp.getNdbNumber(), tmp.getProductName(),tmp.getManufacturer(), tmp.getIngredients());
            p.setHouseholdSize(tmp.getHouseholdSize());
            p.setHouseholdUom(tmp.getHouseholdUom());
            p.setServingSize(tmp.getServingSize());
            p.setServingUom(tmp.getServingUom());
            ObservableMap<String, Product.ProductNutrient> pNtmp = FXCollections.observableHashMap();
            for(Map.Entry<String, Product.ProductNutrient> a: tmp.getProductNutrients().entrySet()) {
                pNtmp.put(a.getKey(),  p.new ProductNutrient(a.getValue().getNutrientCode(), a.getValue().getNutrientQuantity()));
            }
            p.setProductNutrients(pNtmp);

            //get the input form two textfiel
            String servingSizeString = NutriByte.view.dietServingSizeTextField.getText();
            String householdSizeString = NutriByte.view.dietHouseholdSizeTextField.getText();

            //both not empty, use servingsize and calculate household size
            if(!servingSizeString.isEmpty() && !householdSizeString.isEmpty()) {
                try {
                    float servingSize = Float.parseFloat(servingSizeString);
                    p.setHouseholdSize((servingSize * p.getHouseholdSize() /p.getServingSize()) );
                    p.setServingSize(servingSize);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if (servingSizeString.isEmpty() && !householdSizeString.isEmpty()){
                //use the given one when only one is provided
                try {
                    float householdSize = Float.parseFloat(householdSizeString);
                    p.setServingSize(householdSize * p.getServingSize() / p.getHouseholdSize());
                    p.setHouseholdSize(householdSize);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } else if (!servingSizeString.isEmpty() && householdSizeString.isEmpty()) {
                //use the given one when only one is provided
                try {
                    float servingSize = Float.parseFloat(servingSizeString);
                    p.setHouseholdSize((servingSize/p.getServingSize()) * p.getHouseholdSize());
                    p.setServingSize(servingSize);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            } //both  empty, use servingsize and calculate household size

            //add product to person's dietProductList
            NutriByte.person.dietProductsList.add(p);
            NutriByte.person.populateDietNutrientsMap();
            NutriByte.view.dietProductsTableView.setItems(NutriByte.person.dietProductsList);
            NutriByte.view.nutriChart.updateChart();
        }
    }

    class RemoveDietButtonHandler implements  EventHandler<ActionEvent>{
        @Override
        public void handle(ActionEvent event) {
            int index = NutriByte.view.dietProductsTableView.getSelectionModel().getSelectedIndex();
            NutriByte.person.dietProductsList.remove(index);
            NutriByte.person.populateDietNutrientsMap();
            NutriByte.view.nutriChart.updateChart();
        }
    }
    //helper method to clean data
    static void cleanData() {
        //clean up all data
        NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
        //clean all textfields
        NutriByte.view.ageTextField.setText("");
        NutriByte.view.weightTextField.setText("");
        NutriByte.view.heightTextField.setText("");
        NutriByte.view.physicalActivityComboBox.getSelectionModel().clearSelection();
        NutriByte.view.ingredientsToWatchTextArea.setText("");
        NutriByte.view.productSearchTextField.setText("");
        NutriByte.view.nutrientSearchTextField.setText("");
        NutriByte.view.ingredientSearchTextField.setText("");
        //clean all labels
        NutriByte.view.servingSizeLabel.textProperty().setValue("");
        NutriByte.view.householdSizeLabel.textProperty().setValue("");
        NutriByte.view.dietServingUomLabel.textProperty().setValue("");
        NutriByte.view.dietHouseholdUomLabel.textProperty().setValue("");
        NutriByte.view.productIngredientsTextArea.textProperty().setValue("");
        NutriByte.view.searchResultSizeLabel.setText("");
        NutriByte.view.recommendedNutrientsTableView.getItems().clear();
        NutriByte.view.dietProductsTableView.getItems().clear();
        NutriByte.view.productNutrientsTableView.getItems().clear();
        NutriByte.view.dietServingSizeTextField.setText("");
        NutriByte.view.dietHouseholdSizeTextField.setText("");
        NutriByte.view.genderComboBox.getSelectionModel().clearSelection();
        //clean all tableview
        NutriByte.view.recommendedNutrientsTableView.setItems(FXCollections.observableArrayList());
        NutriByte.view.dietProductsTableView.setItems(FXCollections.observableArrayList());
        NutriByte.view.productNutrientsTableView.setItems(FXCollections.observableArrayList());
        NutriByte.model.searchResult = FXCollections.observableArrayList();
        NutriByte.view.nutriChart.clearChart();
    }

}
