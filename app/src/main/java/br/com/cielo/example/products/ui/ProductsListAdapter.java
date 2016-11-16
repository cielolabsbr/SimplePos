package br.com.cielo.example.products.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import br.com.cielo.example.R;
import br.com.cielo.example.domain.model.Product;
import br.com.cielo.example.utils.CurrencyStringFormatter;

public class ProductsListAdapter extends ArrayAdapter<Product> {

    private List<Product> productsList;

    public ProductsListAdapter(Context context) {
        super(context, R.layout.order_item);
        productsList = new ArrayList<>(0);
    }

    @Override
    public int getCount() {
        return productsList.size();
    }

    @Nullable
    @Override
    public Product getItem(int position) {
        return productsList.get(position);
    }

    @Override
    public void addAll(Collection<? extends Product> collection) {
        productsList.addAll(collection);
        notifyDataSetChanged();
    }

    @Override
    public void add(Product product) {
        productsList.add(product);
        notifyDataSetChanged();
    }

    @Override
    public void remove(Product product) {
        productsList.remove(product);
        notifyDataSetChanged();
    }

    @Override
    public void clear() {
        productsList.clear();
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(getContext());

        View view = convertView;

        if (view == null) {
            view = layoutInflater.inflate(R.layout.product_item, parent, false);
        }

        Product product = productsList.get(position);

        ViewHolder viewHolder = new ViewHolder(view);
        viewHolder.textViewProduct.setText(product.getName());
        viewHolder.textViewPriceProduct.setText(
                CurrencyStringFormatter.fromCentsToUnitsString(getContext().getResources(),
                        product.getUnitPrice()));

        return view;
    }

    private class ViewHolder {
        private final TextView textViewProduct;
        private final TextView textViewPriceProduct;

        public ViewHolder(View view) {
            this.textViewProduct = (TextView) view.findViewById(R.id.textView_product_name);
            this.textViewPriceProduct = (TextView) view.findViewById(R.id.textView_product_price);
        }
    }
}
