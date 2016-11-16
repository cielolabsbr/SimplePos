package br.com.cielo.example.domain.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PosOrder implements Parcelable {

    private String id;
    private Date date;
    private List<OrderItem> items;

    public PosOrder(String id, Date date, List<OrderItem> items) {
        this.id = id;
        this.date = date;
        this.items = items;
    }

    public PosOrder(String id, Date date) {
        this(id, date, new ArrayList<OrderItem>(0));
    }

    public String getId() {
        return id;
    }

    public List<OrderItem> getItems() {
        return items;
    }

    public Date getDate() {
        return date;
    }

    public long getOrderTotal() {
        long orderTotal = 0;
        for (OrderItem item : items) {
            orderTotal += item.getOrderItemTotal();
        }
        return orderTotal;
    }

    public PosOrder(Parcel parcel) {
        this.id = parcel.readString();
        this.date = new Date(parcel.readLong());
        this.items = new ArrayList<>();
        parcel.readTypedList(items, OrderItem.CREATOR);
    }

    public static final Creator<PosOrder> CREATOR = new Creator<PosOrder>() {
        @Override
        public PosOrder createFromParcel(Parcel in) {
            return new PosOrder(in);
        }

        @Override
        public PosOrder[] newArray(int size) {
            return new PosOrder[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeLong(date.getTime());
        dest.writeList(items);
    }
}
