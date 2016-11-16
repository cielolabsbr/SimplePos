package br.com.cielo.example.order.ui;

import android.content.Context;
import android.content.res.Resources;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import br.com.cielo.example.R;
import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.Product;
import br.com.cielo.example.utils.CurrencyStringFormatter;

public class OrderItemsAdapter extends ArrayAdapter<OrderItem> {
    private ArrayList<OrderItem> orderItemsList;

    public OrderItemsAdapter(Context context) {
        super(context, R.layout.order_item);
        orderItemsList = new ArrayList<>(0);
    }

    @Override
    public int getCount() {
        return orderItemsList.size();
    }

    @Nullable
    @Override
    public OrderItem getItem(int position) {
        return orderItemsList.get(position);
    }

    @Override
    public void add(OrderItem product) {
        orderItemsList.add(product);
        notifyDataSetChanged();
    }

    @Override
    public void remove(OrderItem product) {
        orderItemsList.remove(product);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        orderItemsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.order_item, parent, false);
        }

        ViewHolder viewHolder = new ViewHolder(view);
        Resources resources = getContext().getResources();

        OrderItem orderItem = getItem(position);
        Product product = orderItem.getProduct();

        viewHolder.textViewProductName.setText(product.getName());

        String quantityText = String.format(resources.getString(R.string.product_quantity), orderItem.getQuantity());
        viewHolder.textViewItemQuantity.setText(quantityText);

        String totalItemTotalText = CurrencyStringFormatter.fromCentsToUnitsString(resources, orderItem.getOrderItemTotal());
        viewHolder.textViewItemTotal.setText(resources.getText(R.string.total) + " " + totalItemTotalText);

        String unitPriceText = CurrencyStringFormatter.fromCentsToUnitsString(resources, product.getUnitPrice());
        viewHolder.textViewProductUnitPrice.setText(unitPriceText + " " + resources.getText(R.string.per_unit));

        return view;
    }

    private class ViewHolder {
        private final TextView textViewProductName;
        private final TextView textViewItemQuantity;
        private final TextView textViewItemTotal;
        private final TextView textViewProductUnitPrice;

        public ViewHolder(View view) {
            textViewProductName = (TextView) view.findViewById(R.id.textView_product_name);
            textViewItemQuantity = (TextView) view.findViewById(R.id.textView_product_quantity);
            textViewItemTotal = (TextView) view.findViewById(R.id.textView_product_total);
            textViewProductUnitPrice = (TextView) view.findViewById(R.id.textView_product_unit_price);
        }
    }
}
