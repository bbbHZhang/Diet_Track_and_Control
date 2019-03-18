package lesson06.end;

import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class ItemInCart {

	private StringProperty name = new SimpleStringProperty();
	private DoubleProperty quantity = new SimpleDoubleProperty();

	ItemInCart() {
		name.set("");
		quantity.set(0);
	}

	ItemInCart(String name, double quantity) {
		this.name.set(name);
		this.quantity.set(quantity);

	}

	public final String getName() { return name.get(); }
	public final double getQuantity() { return quantity.get(); }

	public final void setName(String name) { this.name.set(name); }
	public final void setQuantity(double unitQuantity) { this.quantity.set(unitQuantity); }

	public StringProperty nameProperty() { return name; }
	public DoubleProperty quantityProperty() { return quantity; }

}


