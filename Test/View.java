package lesson06.end;

import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;

public class View {
	ComboBox<Item> itemsComboBox = new ComboBox<>();  //the drop down will show item-names from itemsMasterList
	Label unitValueLabel = new Label();
	Label unitPriceValueLabel = new Label();

	//new for chapter4lesson3
	Slider quantitySlider = new Slider(0, 10, 0);  //min, max, current
	Label purchasedUnitsValueLabel = new Label("0");

	//new for chapter5lesson3
	Button addButton = new Button("Add to Cart");
	Button removeButton = new Button("Remove from Cart");
	Label totalValueLabel = new Label();
	TableView<ItemInCart> itemsTableView = new TableView<>();
	TableColumn<ItemInCart, Double> rateColumn = new TableColumn<>("Purchase price");
	
	//for chapter6lesson2
	StackPane imageStackPane = new StackPane();  //holds image or media views
	Node node;

	@SuppressWarnings("unchecked")
	BorderPane setupScene() {
		BorderPane root = new BorderPane();
		GridPane topGrid = new GridPane();
		root.setTop(topGrid);
		
		/**setup topGrid */
		topGrid.setVgap(10);
		topGrid.setHgap(10);

		//create fixed labels
		Label unitLabel = new Label("Unit");
		Label pricePerUnitLabel = new Label("Price/unit");

		//add all controls to topGrid
		topGrid.add(unitLabel, 0, 1);
		topGrid.add(pricePerUnitLabel, 0, 2);
		topGrid.add(itemsComboBox, 0, 0, 2, 1);
		topGrid.add(unitValueLabel, 1, 1);
		topGrid.add(unitPriceValueLabel, 1, 2);

		// set all columns' width 
		for (int i = 0; i < 5; i++)
			topGrid.getColumnConstraints().add(new ColumnConstraints(120)); 

		//set up look and feel, fonts, alignment, etc.
		itemsComboBox.setPromptText("Select item");
		unitValueLabel.setTextFill(Color.TEAL);
		unitPriceValueLabel.setTextFill(Color.TEAL);
		unitLabel.setFont(Font.font(15));
		pricePerUnitLabel.setFont(Font.font(15));
		unitValueLabel.setFont(Font.font(15));
		unitPriceValueLabel.setFont(Font.font(15));

		//new for chapter4lesson3

		quantitySlider.setMinorTickCount(1);
		quantitySlider.setMajorTickUnit(2);
		quantitySlider.setPrefWidth(300);
		quantitySlider.setSnapToTicks(true);
		quantitySlider.setShowTickLabels(true);
		quantitySlider.setShowTickMarks(true);

		Label qtySliderLabel = new Label("Select units");
		Label purchasedUnitsLabel = new Label("Purchased units");

		topGrid.add(qtySliderLabel, 2, 0);
		topGrid.add(quantitySlider, 3, 0, 2, 1);
		topGrid.add(purchasedUnitsLabel, 2, 1);
		topGrid.add(purchasedUnitsValueLabel, 3, 1);

		qtySliderLabel.setFont(Font.font(15));
		purchasedUnitsLabel.setFont(Font.font(15));
		purchasedUnitsValueLabel.setFont(Font.font(15));
		purchasedUnitsValueLabel.setTextFill(Color.TEAL);
		//new for chapter5lesson3

		//setup bottomGrid
		GridPane bottomGrid = new GridPane();
		root.setBottom(bottomGrid);
		bottomGrid.setVgap(10);
		bottomGrid.setHgap(10);

		//setup Add button and Total amount

		Label totalLabel = new Label("Total amount:");
		addButton.setPrefWidth(150);
		addButton.setFont(Font.font(15));
		addButton.setPrefWidth(150);
		removeButton.setFont(Font.font(15));
		totalLabel.setFont(Font.font(15));
		totalValueLabel.setFont(Font.font(15));

		//create table view
		TableColumn<ItemInCart, String> nameColumn = new TableColumn<>("Item name");
		nameColumn.setCellValueFactory(new PropertyValueFactory<ItemInCart, String>("name"));
		TableColumn<ItemInCart, Double> qtyColumn = new TableColumn<>("Purchased units");
		qtyColumn.setCellValueFactory(new PropertyValueFactory<ItemInCart, Double>("quantity"));

		itemsTableView.getColumns().setAll(nameColumn, qtyColumn, rateColumn);
		itemsTableView.setPrefSize(350, 200);
		itemsTableView.getColumns().get(0).setPrefWidth(100);
		itemsTableView.getColumns().get(1).setPrefWidth(100);
		itemsTableView.getColumns().get(2).setPrefWidth(125);
		
		bottomGrid.add(addButton, 0, 0);
		bottomGrid.add(removeButton, 1, 0);
		bottomGrid.add(itemsTableView, 0, 1, 2, 1);
		bottomGrid.add(totalLabel, 0, 2);
		bottomGrid.add(totalValueLabel, 1, 2);
		
		//for chapter6lesson2
		imageStackPane.setPrefSize(325, 350);
		bottomGrid.add(imageStackPane, 2, 0, 2, 2);
		imageStackPane.setStyle("-fx-border-color:lightgray");

		topGrid.setPrefSize(700, 125);
		bottomGrid.setPrefSize(700, 425);
		root.setPrefSize(700, 550);
		BorderPane.setMargin(topGrid, new Insets(10,10,10,10));
		BorderPane.setMargin(bottomGrid, new Insets(10,10,10,10));
		
		return root;
	}
}
