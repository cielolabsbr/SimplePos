package br.com.cielo.example.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class OrderItem implements Parcelable {

    private Product product;
    private int quantity;

    public OrderItem(Product product, int quantity) {
        this.product = product;
        this.quantity = quantity;
    }

    public Product getProduct() {
        return product;
    }

    public int getQuantity() {
        return quantity;
    }

    public long getOrderItemTotal() {
        return quantity * product.getUnitPrice();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeParcelable(this.product, flags);
        dest.writeInt(this.quantity);
    }

    protected OrderItem(Parcel in) {
        this.product = in.readParcelable(Product.class.getClassLoader());
        this.quantity = in.readInt();
    }

    public static final Parcelable.Creator<OrderItem> CREATOR = new Parcelable.Creator<OrderItem>() {
        @Override
        public OrderItem createFromParcel(Parcel source) {
            return new OrderItem(source);
        }

        @Override
        public OrderItem[] newArray(int size) {
            return new OrderItem[size];
        }
    };
}