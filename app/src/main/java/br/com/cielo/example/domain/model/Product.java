package br.com.cielo.example.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {

    private String sku;
    private String name;
    private long unitPrice;

    public Product(String sku, String name, long unitPrice) {
        this.sku = sku;
        this.name = name;
        this.unitPrice = unitPrice;
    }

    public String getSku() {
        return sku;
    }

    public String getName() {
        return name;
    }

    public long getUnitPrice() {
        return unitPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.sku);
        dest.writeString(this.name);
        dest.writeLong(this.unitPrice);
    }

    protected Product(Parcel in) {
        this.sku = in.readString();
        this.name = in.readString();
        this.unitPrice = in.readLong();
    }

    public static final Parcelable.Creator<Product> CREATOR = new Parcelable.Creator<Product>() {
        @Override
        public Product createFromParcel(Parcel source) {
            return new Product(source);
        }

        @Override
        public Product[] newArray(int size) {
            return new Product[size];
        }
    };
}
