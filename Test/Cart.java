package lesson06.end;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
import javafx.beans.binding.DoubleBinding;
import javafx.beans.binding.ObjectBinding;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.value.ObservableValue;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn.CellDataFeatures;
import javafx.stage.Stage;
import javafx.util.Callback;

public class Cart extends Application {
	Model model = new Model();
	View view = new View();

	public static void main(String[] args) {
		launch(args);
	}
	@Override
	public void start(Stage primaryStage) throws Exception {
		model.loadData();		//this will load itemsObservableList from csv file
		Scene scene = new Scene(view.setupScene(), 700, 550);  
		setupActions();
		primaryStage.setScene(scene);
		primaryStage.setTitle("Shopping Cart");
		primaryStage.show();
	}

	ObjectBinding<Item> itemBinding = new ObjectBinding<>() {
		{
			super.bind(view.itemsComboBox.valueProperty());
		}
		@Override
		protected Item computeValue() {
			Item item;
			if (view.itemsComboBox.getSelectionModel().getSelectedIndex() >= 0) 
				item = view.itemsComboBox.getSelectionModel().selectedItemProperty().get();
			else item = new Item();
			view.quantitySlider.setValue(0);
			return item;
		}
	};


	void setupActions() {
		//bind itemsTableView with data
		view.itemsTableView.setItems(model.cartObservableList);
		view.itemsComboBox.setItems(model.itemsObservableList); 

		//bind purchase units label with slider value using Fluent API
		view.purchasedUnitsValueLabel.textProperty().bind(Bindings.format("%.0f", view.quantitySlider.valueProperty()));

		//selecting item in itemsComboBox updates unit quantity and unit price labels

		view.unitValueLabel.textProperty().bind(Bindings.format("%.2f %s", Bindings.select(itemBinding, "unitQuantity"), Bindings.select(itemBinding, "unit")));
		view.unitPriceValueLabel.textProperty().bind(Bindings.format("$ %.2f", Bindings.select(itemBinding, "unitPrice")));

		//provide cell value factory for Price column in itemsTableView
		Callback<CellDataFeatures<ItemInCart, Double>, ObservableValue<Double>> callback = new Callback<CellDataFeatures<ItemInCart, Double>, ObservableValue<Double>>() {
			@Override
			public ObservableValue<Double> call(CellDataFeatures<ItemInCart, Double> param) {
				for(Item item : model.itemsObservableList) {
					if (item.getName().equalsIgnoreCase(param.getValue().getName())) {
						return (new SimpleDoubleProperty(param.getValue().getQuantity() * item.getUnitPrice())).asObject();
					}
				}
				return null;
			}
		};

		//set rate columnn's call-back for factory method
		view.rateColumn.setCellValueFactory(callback);

		//event handler for add button creates new ItemInCart with currently selected item'name and purchase qty 
		//and adds it to cartObservableList
		//it also moves the table selection to the last item just added

		view.addButton.setOnAction(event -> {
			Item selectedItem = view.itemsComboBox.getSelectionModel().getSelectedItem(); 
			ItemInCart shoppingCartItem = new ItemInCart(selectedItem.getName(), view.quantitySlider.getValue());
			model.cartObservableList.add(shoppingCartItem);
			view.itemsTableView.getSelectionModel().selectLast();
		});

		//totalBinding to add all cell values in the price column of table view
		
		DoubleBinding totalBinding = new DoubleBinding() {
			{
				super.bind(model.cartObservableList);
			}

			@Override
			protected double computeValue() {
				double total = 0;
				if (model.cartObservableList.size() > 0)
					for (int row = 0; row < model.cartObservableList.size(); row++)
						total += (double)view.itemsTableView.getColumns().get(2).getCellObservableValue(row).getValue();
				return total;
			}
			
		};

		//bind totalValueLabel's textProperty to totalBinding
		view.totalValueLabel.textProperty().bind(Bindings.format("%.2f", totalBinding));


		//listener for item-selection in itemsTableView updates the image, item in combo-box, and qty in quantitySlider
		//it looks for the selected item in the tableView in itemsObservableList, sets itemComboBox to that index, and
		//quantitySlider to the newValue's quantity.
		view.itemsTableView.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
			if (newValue != null) {
				int index = 0;
				for (Item item : model.itemsObservableList) {
					if (item.getName().equals(newValue.getName())) { 
						view.itemsComboBox.getSelectionModel().select(index);
						view.quantitySlider.setValue(newValue.getQuantity());
						break;
					}
					index++;
				}
			}
		});
		
		//event handler for remove button 
		//checks the current selected index 
		//if it is >= 0, it removes the item at that index from cartObservableList
		view.removeButton.setOnAction(event -> {
			int index = view.itemsTableView.getSelectionModel().getFocusedIndex();
			if (model.cartObservableList.size() > index) model.cartObservableList.remove(index);
			if (model.cartObservableList.size() == 0) view.itemsComboBox.getSelectionModel().clearSelection();
		});
	}
}
