/**
 * @author Han Zhang  hanzhan2
 */
package hw2;

import java.io.File;

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
        //read data, calculate all nutrients and output results
        @Override
        public void handle(ActionEvent event) {
            Person person = null;
            //set default value of age, weight, height as 0, read data
            float age = 0;
            if (NutriByte.view.ageTextField.getText() != "" && NutriByte.view.ageTextField.getText().length() != 0){
                age = Float.parseFloat(NutriByte.view.ageTextField.getText());
            }
            float weight = 0;
            if (NutriByte.view.weightTextField.getText() != "" && NutriByte.view.weightTextField.getText().length() != 0){
                weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
            }
            float height = 0;
            if (NutriByte.view.heightTextField.getText() != "" && NutriByte.view.heightTextField.getText().length() != 0){
                height = Float.parseFloat(NutriByte.view.heightTextField.getText());
            }

            //set default value physical activity as 1
            float physical = 1;
            if (NutriByte.view.physicalActivityComboBox.getValue() != null && NutriByte.view.physicalActivityComboBox.getValue().length() != 0) {
                for(NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()){
                    if (e.getName().equalsIgnoreCase(NutriByte.view.physicalActivityComboBox.getValue())){
                        physical = e.getPhysicalActivityLevel();
                    }
                }
            }
            //do nothing if gender is not selected
            if (NutriByte.view.genderComboBox.getValue() != null && NutriByte.view.genderComboBox.getValue().length() != 0) {
                if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("female")){
                    person = new Female(age, weight, height, physical, NutriByte.view.ingredientsToWatchTextArea.getText());
                } else if (NutriByte.view.genderComboBox.getValue().equalsIgnoreCase("male")){
                    person = new Male(age, weight, height, physical, NutriByte.view.ingredientsToWatchTextArea.getText());
                } 
            } else { return;}
            //load data
            NutriProfiler.createNutriProfile(person);
            NutriByte.view.recommendedNutrientsTableView.setItems(NutriProfiler.recommendedNutrientsList);
        }			
    }

    class OpenMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //read files
            FileChooser fileChooser = new FileChooser();
            fileChooser.setTitle("Select file");
            fileChooser.getExtensionFilters().addAll(
                    new ExtensionFilter("CSV Files", "*.csv"),
                    new ExtensionFilter("XML Files", "*.xml"),
                    new ExtensionFilter("All Files", "*.*"));
            File file = null;
            if ((file = fileChooser.showOpenDialog(NutriByte.view.root.getScene().getWindow())) != null){
                //validate file
                if(NutriByte.model.readProfiles(file.getAbsolutePath())) {
                    if(NutriByte.person instanceof Female) {
                        NutriByte.view.genderComboBox.setValue("Female");
                    } else if(NutriByte.person instanceof Male) {
                        NutriByte.view.genderComboBox.setValue("Male");
                    }
                    //load data in windows
                    NutriByte.view.ageTextField.setText(String.valueOf(NutriByte.person.age));
                    NutriByte.view.weightTextField.setText(String.valueOf(NutriByte.person.weight));
                    NutriByte.view.heightTextField.setText(String.valueOf(NutriByte.person.height));
                    for(NutriProfiler.PhysicalActivityEnum e : NutriProfiler.PhysicalActivityEnum.values()) {
                        if(NutriByte.person.physicalActivityLevel == e.getPhysicalActivityLevel()) {
                            NutriByte.view.physicalActivityComboBox.setValue(e.getName());
                        }
                    }
                    NutriByte.view.ingredientsToWatchTextArea.setText(NutriByte.person.ingredientsToWatch);
                    NutriProfiler.createNutriProfile(NutriByte.person);
                    NutriByte.view.recommendedNutrientsTableView.setItems(NutriProfiler.recommendedNutrientsList);
                    
                    //set windows view
                    NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
                } else {
                    //output message is fail is invalid
                    System.out.println("Invalid file!");
                }

            } 

        }
    }

    class NewMenuItemHandler implements EventHandler<ActionEvent> {
        @Override
        public void handle(ActionEvent event) {
            //create a new window and clear all values
            NutriByte.view.root.setCenter(NutriByte.view.nutriTrackerPane);
            NutriByte.view.initializePrompts();
            NutriByte.view.recommendedNutrientsTableView.getItems().clear();
        }
    }

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
}
