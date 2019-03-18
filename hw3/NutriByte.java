/**
 * @author Han Zhang  hanzhan2
 */
package hw3;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleFloatProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.stage.Stage;
import javafx.util.Callback;

public class NutriByte extends Application{
    static Model model = new Model();  	//made static to make accessible in the controller
    static View view = new View();		//made static to make accessible in the controller
    static Person person;				//made static to make accessible in the controller

    Controller controller = new Controller();	//all event handlers 

    /**Uncomment the following three lines if you want to try out the full-size data files */
    static final String PRODUCT_FILE = "data/Products.csv";
    static final String NUTRIENT_FILE = "data/Nutrients.csv";
    static final String SERVING_SIZE_FILE = "data/ServingSize.csv";

    /**The following constants refer to the data files to be used for this application */
    //    static final String PRODUCT_FILE = "data/Nutri2Products.csv";
    //    static final String NUTRIENT_FILE = "data/Nutri2Nutrients.csv";
    //    static final String SERVING_SIZE_FILE = "data/Nutri2ServingSize.csv";

    static final String NUTRIBYTE_IMAGE_FILE = "NutriByteLogo.png"; 

    static final String NUTRIBYTE_PROFILE_PATH = "profiles";  //folder that has profile data files

    static final int NUTRIBYTE_SCREEN_WIDTH = 1015;
    static final int NUTRIBYTE_SCREEN_HEIGHT = 675;

    public boolean processInput() {
        view.ageTextField.setStyle("-fx-text-inner-color: black;");
        view.weightTextField.setStyle("-fx-text-inner-color: black;");
        view.heightTextField.setStyle("-fx-text-inner-color: black;");
        String gender = NutriByte.view.genderComboBox.getValue();
        float age = 0f;
        float weight = 0f;
        float height = 0f;
        if(gender == null) {
            return false;
        }
        try {
            age =  Float.parseFloat(NutriByte.view.ageTextField.getText());
            if(age < 0) { throw new NumberFormatException();}
        }catch(NumberFormatException e) {
            view.ageTextField.setStyle("-fx-text-inner-color: red;");
            return false;
        }
        try { 
            weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
            if(weight < 0) { throw new NumberFormatException();}
        }catch(NumberFormatException e) {
            view.weightTextField.setStyle("-fx-text-inner-color: red;");
            return false;
        }
        try { 
            height = Float.parseFloat(NutriByte.view.heightTextField.getText());
            if(height < 0) { throw new NumberFormatException();}
        }catch(NumberFormatException e) {
            view.heightTextField.setStyle("-fx-text-inner-color: red;");
            return false;
        }
        return true;
    }

    ObjectBinding<Person> personObjectBinding = new ObjectBinding<Person>() {
        {
            super.bind(view.genderComboBox.valueProperty(), view.ageTextField.textProperty(), view.weightTextField.textProperty(), 
                    view.heightTextField.textProperty(), view.physicalActivityComboBox.valueProperty());
        }
        @Override
        protected Person computeValue() {

            if(processInput()) {
                String gender = NutriByte.view.genderComboBox.getValue();
                float age = 0f;
                float weight = 0f;
                float height = 0f;
                float physicalActivityLevel = 1;
                age =  Float.parseFloat(NutriByte.view.ageTextField.getText());
                weight = Float.parseFloat(NutriByte.view.weightTextField.getText());
                height = Float.parseFloat(NutriByte.view.heightTextField.getText());
                String ingredientsToWatch = view.ingredientsToWatchTextArea.getText();

                String physicalString = NutriByte.view.physicalActivityComboBox.getValue();

                if(physicalString != null && !physicalString.isEmpty()) {
                    for (NutriProfiler.PhysicalActivityEnum p : NutriProfiler.PhysicalActivityEnum.values()) {
                        if (p.getName().equals(NutriByte.view.physicalActivityComboBox.getSelectionModel().getSelectedItem())){
                            physicalActivityLevel = p.getPhysicalActivityLevel();
                        }
                    }
                }
                //creating person
                Person p = null;
                if (gender.equalsIgnoreCase("female")) {
                    p =  new Female(age, weight, height, physicalActivityLevel, ingredientsToWatch);
                } else if (gender.equalsIgnoreCase("male")) {
                    p =  new Male(age, weight, height, physicalActivityLevel, ingredientsToWatch);
                }

                if(person != null && person.dietProductsList.size() > 0) {
                    p.dietProductsList = person.dietProductsList;
                }
                person = p;
            }

            return person;
        }
    };


    @Override
    public void start(Stage stage) throws Exception {
        model.readProducts(PRODUCT_FILE);
        model.readNutrients(NUTRIENT_FILE);
        model.readServingSizes(SERVING_SIZE_FILE );
        view.setupMenus();
        view.setupNutriTrackerGrid();
        view.root.setCenter(view.setupWelcomeScene());
        Background b = new Background(new BackgroundFill(Color.WHITE, CornerRadii.EMPTY, Insets.EMPTY));
        view.root.setBackground(b);
        Scene scene = new Scene (view.root, NUTRIBYTE_SCREEN_WIDTH, NUTRIBYTE_SCREEN_HEIGHT);
        view.root.requestFocus();  //this keeps focus on entire window and allows the textfield-prompt to be visible
        setupBindings();
        stage.setTitle("NutriByte 3.0");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    void setupBindings() {
        view.newNutriProfileMenuItem.setOnAction(controller.new NewMenuItemHandler());
        view.openNutriProfileMenuItem.setOnAction(controller.new OpenMenuItemHandler());
        view.closeNutriProfileMenuItem.setOnAction(controller.new CloseMenuItemHandler());
        view.saveNutriProfileMenuItem.setOnAction(controller.new SaveMenuItemHandler());
        view.exitNutriProfileMenuItem.setOnAction(event -> Platform.exit());
        view.aboutMenuItem.setOnAction(controller.new AboutMenuItemHandler());
        view.addDietButton.setOnAction(controller.new AddDietButtonHandler());
        view.removeDietButton.setOnAction(controller.new RemoveDietButtonHandler());

        view.recommendedNutrientNameColumn.setCellValueFactory(recommendedNutrientNameCallback);
        view.recommendedNutrientQuantityColumn.setCellValueFactory(recommendedNutrientQuantityCallback);
        view.recommendedNutrientUomColumn.setCellValueFactory(recommendedNutrientUomCallback);

        view.productNutrientNameColumn.setCellValueFactory(productNutrientNameCallback);
        view.productNutrientQuantityColumn.setCellValueFactory(productNutrientQuantityCallback);
        view.productNutrientUomColumn.setCellValueFactory(productNutrientUomCallback);

        view.dietProductNameColumn.setCellValueFactory(dietProductNutrientNameCallback);
        view.dietServingSizeColumn.setCellValueFactory(dietServingSizeCallback);
        view.dietServingUomColumn.setCellValueFactory(dietServingUomCallback);
        view.dietHouseholdSizeColumn.setCellValueFactory(dietHouseholdSizeCallback);
        view.dietHouseholdUomColumn.setCellValueFactory(dietHouseholdUomCallback);

        view.createProfileButton.setOnAction(controller.new RecommendNutrientsButtonHandler());
        view.searchButton.setOnAction(controller.new SearchButtonHandle());
        view.clearButton.setOnAction(controller.new ClearButtonHandler());

        personObjectBinding.addListener((observable, oldValue, newValue) -> {
            if(newValue != null) {
                NutriProfiler.createNutriProfile(person);
                NutriByte.person.populateDietNutrientsMap();
                view.recommendedNutrientsTableView.setItems(person.recommendedNutrientsList);
            }
        });

    }
    //5 callbacks for dietProductsTable
    Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietProductNutrientNameCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
            return new SimpleStringProperty(arg0.getValue().getProductName());
        }
    };

    Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietServingSizeCallback = new Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>>() {
        @Override
        public ObservableValue<Float> call(CellDataFeatures<Product, Float> arg0) {
            return (new SimpleFloatProperty(arg0.getValue().getServingSize())).asObject();
        }
    };

    Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietServingUomCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
            return new SimpleStringProperty(arg0.getValue().getServingUom());
        }
    };

    Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>> dietHouseholdSizeCallback = new Callback<CellDataFeatures<Product, Float>, ObservableValue<Float>>() {
        @Override
        public ObservableValue<Float> call(CellDataFeatures<Product, Float> arg0) {
            return (new SimpleFloatProperty(arg0.getValue().getHouseholdSize())).asObject();
        }
    };

    Callback<CellDataFeatures<Product, String>, ObservableValue<String>> dietHouseholdUomCallback = new Callback<CellDataFeatures<Product, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product, String> arg0) {
            return new SimpleStringProperty(arg0.getValue().getHouseholdUom());
        }
    };



    //3 callbacks for productNutrientsTable
    Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientNameCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
            return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientNameProperty();
        }
    };

    Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientQuantityCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
            return new SimpleStringProperty(String.format("%.2f",arg0.getValue().getNutrientQuantity()));

        }
    };

    Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>> productNutrientUomCallback = new Callback<CellDataFeatures<Product.ProductNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<Product.ProductNutrient, String> arg0) {
            return Model.nutrientsMap.get(arg0.getValue().getNutrientCode()).nutrientUomProperty();
        }
    };

    //the old callbacks

    Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientNameCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
            Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
            return nutrient.nutrientNameProperty();
        }
    };

    //return nutrient quantity
    Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientQuantityCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
            //write your code here
            for(int i = 0; i < NutriProfiler.RECOMMENDED_NUTRI_COUNT + 1; i++) {
                RecommendedNutrient rn = NutriByte.person.recommendedNutrientsList.get(i);
                if(rn.getNutrientCode().equals(arg0.getValue().getNutrientCode())) {
                    return new SimpleStringProperty(String.format("%.2f", rn.getNutrientQuantity()));
                } else { continue;}
            }
            return null;
        }
    };

    //return nutrient uom
    Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>> recommendedNutrientUomCallback = new Callback<CellDataFeatures<RecommendedNutrient, String>, ObservableValue<String>>() {
        @Override
        public ObservableValue<String> call(CellDataFeatures<RecommendedNutrient, String> arg0) {
            //write your code here
            Nutrient nutrient = Model.nutrientsMap.get(arg0.getValue().getNutrientCode());
            return nutrient.nutrientUomProperty();
        }
    };
}

