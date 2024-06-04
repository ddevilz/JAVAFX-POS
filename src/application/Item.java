package application;

import javafx.beans.property.*;

public class Item {
    private final StringProperty serviceName;
    private final IntegerProperty quantity;
    private final DoubleProperty price;
    private final DoubleProperty totalPrice;

    public Item(String serviceName, int quantity, double price, double totalPrice) {
        this.serviceName = new SimpleStringProperty(serviceName);
        this.quantity = new SimpleIntegerProperty(quantity);
        this.price = new SimpleDoubleProperty(price);
        this.totalPrice = new SimpleDoubleProperty(totalPrice);
    }

    public Item(int index, String serviceName, double price) {
        this(serviceName, 0, price, 0.0);
    }

    public String getServiceName() {
        return serviceName.get();
    }

    public StringProperty serviceNameProperty() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName.set(serviceName);
    }

    public int getQuantity() {
        return quantity.get();
    }

    public IntegerProperty quantityProperty() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity.set(quantity);
    }

    public double getPrice() {
        return price.get();
    }

    public DoubleProperty priceProperty() {
        return price;
    }

    public void setPrice(double price) {
        this.price.set(price);
    }

    public double getTotalPrice() {
        return totalPrice.get();
    }

    public DoubleProperty totalPriceProperty() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice.set(totalPrice);
    }

    public char[] getIndex() {
        String indexString = "1";
        return indexString.toCharArray();
    }
}
