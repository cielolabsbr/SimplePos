package br.com.cielo.example.products.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.NumberPicker;

import java.util.List;

import br.com.cielo.example.R;
import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.Product;
import br.com.cielo.example.order.ui.OrderActivity;
import br.com.cielo.example.products.ProductsContract;
import br.com.cielo.example.products.presenter.ProductsPresenter;

public class ProductsActivity extends AppCompatActivity implements ProductsContract.View {

    public static final String EXTRA_ORDER_ITEM = "extra_order_item";

    private ProductsContract.Presenter presenter;
    private ListView listViewProducts;
    private ProductsListAdapter productsListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);
        initializeViews();
        initializePresenter();
    }

    private void initializePresenter() {
        presenter = new ProductsPresenter(this);
    }

    private void initializeViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        productsListAdapter = new ProductsListAdapter(this);

        listViewProducts = (ListView) findViewById(R.id.listview_products);
        listViewProducts.setAdapter(productsListAdapter);
        listViewProducts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                presenter.productSelected(i);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            setResult(RESULT_CANCELED);
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void renderProductsList(List<Product> productsList) {
        productsListAdapter.clear();
        productsListAdapter.addAll(productsList);
    }

    @Override
    public void returnOrderItemToOrderActivity(OrderItem orderItem) {
        Intent intent = new Intent(this, OrderActivity.class);
        intent.putExtra(EXTRA_ORDER_ITEM, orderItem);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public void setPresenter(ProductsContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showProductQuantitySelectionDialog() {
        buildQuantitySelectionDialog().show();
    }

    private AlertDialog buildQuantitySelectionDialog() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        View quantityView = getLayoutInflater().inflate(R.layout.number_picker, null);

        final NumberPicker numberPickerProductQuantity = (NumberPicker) quantityView.findViewById(R.id.numberPicker_productQuantity);
        numberPickerProductQuantity.setMinValue(1);
        numberPickerProductQuantity.setMaxValue(10);

        alertDialogBuilder.setTitle(R.string.dialog_quantity_title);
        alertDialogBuilder.setView(quantityView);

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    int quantity = numberPickerProductQuantity.getValue();
                    presenter.quantitySelected(quantity);
                }
            }
        };

        alertDialogBuilder.setPositiveButton(R.string.dialog_quantity_positive_button, onClickListener);
        alertDialogBuilder.setNegativeButton(R.string.dialog_quantity_negative_button, onClickListener);

        return alertDialogBuilder.create();
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.resume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.destroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        presenter.pause();
    }
}