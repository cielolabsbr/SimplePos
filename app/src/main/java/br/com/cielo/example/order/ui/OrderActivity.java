package br.com.cielo.example.order.ui;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import br.com.cielo.example.BuildConfig;
import br.com.cielo.example.R;
import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.lio.PaymentManager;
import br.com.cielo.example.order.OrderContract;
import br.com.cielo.example.order.presenter.OrderPresenter;
import br.com.cielo.example.products.ui.ProductsActivity;
import br.com.cielo.example.utils.CurrencyStringFormatter;
import cielo.sdk.order.Credentials;
import cielo.sdk.order.Environment;
import cielo.sdk.order.OrderManager;

public class OrderActivity extends AppCompatActivity implements View.OnClickListener,
        OrderContract.View {

    private static int PRODUCTS_SCREEN = 2;

    private Button buttonAddProduct;
    private Button buttonCheckout;
    private Button buttonCleanList;
    private ListView listViewOrderItems;
    private TextView textViewOrderTotal;

    private OrderItemsAdapter orderItemsAdapter;
    private OrderContract.Presenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initializeViews();
        initializePresenter();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        presenter.saveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        presenter.restoreSavedInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void initializePresenter() {
        PaymentManager paymentManager = createPaymentManager();
        presenter = new OrderPresenter(this, paymentManager);
        presenter.resetOrder();
    }

    @Override
    public void setPresenter(OrderContract.Presenter presenter) {
        this.presenter = presenter;
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

    private void initializeViews() {
        buttonAddProduct = (Button) findViewById(R.id.button_add_product);
        buttonAddProduct.setOnClickListener(this);

        buttonCheckout = (Button) findViewById(R.id.button_checkout);
        buttonCheckout.setOnClickListener(this);

        buttonCleanList = (Button) findViewById(R.id.button_clean_list);
        buttonCleanList.setOnClickListener(this);

        listViewOrderItems = (ListView) findViewById(R.id.listView_orderItems);
        orderItemsAdapter = new OrderItemsAdapter(this);
        listViewOrderItems.setAdapter(orderItemsAdapter);

        TextView emptyView = (TextView) findViewById(android.R.id.empty);
        listViewOrderItems.setEmptyView(emptyView);

        textViewOrderTotal = (TextView) findViewById(R.id.textView_total_order);
    }

    @Override
    public void updateOrderTotal(long orderTotal) {
        String orderTotalText = CurrencyStringFormatter.fromCentsToUnitsString(getResources(), orderTotal);
        textViewOrderTotal.setText(orderTotalText);
    }

    @Override
    public void addNewOrderItem(OrderItem orderItem) {
        orderItemsAdapter.add(orderItem);
    }

    @Override
    public void disableCheckoutButton() {
        buttonCheckout.setEnabled(false);
    }

    @Override
    public void enableCheckoutButton() {
        buttonCheckout.setEnabled(true);
    }

    @Override
    public void showPaymentSuccessDialog() {
        String title = getString(R.string.payment_sucess_title);
        String message = getString(R.string.payment_sucess_message);
        String buttonText = getString(R.string.payment_success_button);

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                buttonCleanOnClick();
            }
        };

        showStatusDialog(title, message, buttonText, onClickListener);
    }

    @Override
    public void showPaymentErrorDialog(String errorMessage) {
        String title = getString(R.string.payment_error_title);
        String message = errorMessage;
        String buttonText = getString(R.string.payment_error_button);

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        showStatusDialog(title, message, buttonText, onClickListener);
    }

    private void showStatusDialog(String title, String message, String buttonText, DialogInterface.OnClickListener clickListener) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(title)
                .setMessage(message)
                .setPositiveButton(buttonText, clickListener);
        builder.create().show();
    }

    @Override
    public void showPaymentCancelledDialog() {
        String title = getString(R.string.payment_canceled_title);
        String message = getString(R.string.payment_canceled_message);
        String buttonText = getString(R.string.payment_canceled_button);

        DialogInterface.OnClickListener onClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        };

        showStatusDialog(title, message, buttonText, onClickListener);
    }

    @Override
    public void showPaymentProcessingView() {
    }

    @Override
    public void hidePaymentProcessingView() {
    }

    @Override
    public void showToastMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show();
    }

    private PaymentManager createPaymentManager() {
        Credentials credentials = new Credentials(BuildConfig.ACCESS_KEY_ID,
                BuildConfig.SECRET_ACCESS_KEY);
        OrderManager orderManager = new OrderManager(credentials, Environment.SANDBOX);
        PaymentManager paymentManager = new PaymentManager(this, orderManager);
        return paymentManager;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_add_product:
                buttonAddProductOnClick();
                break;
            case R.id.button_clean_list:
                buttonCleanOnClick();
                break;
            case R.id.button_checkout:
                buttonCheckoutOnClicked();
                break;
            default:
                break;
        }
    }

    private void buttonAddProductOnClick() {
        Intent intent = new Intent(this, ProductsActivity.class);
        this.startActivityForResult(intent, PRODUCTS_SCREEN);
    }

    private void buttonCleanOnClick() {
        presenter.resetOrder();
    }

    private void buttonCheckoutOnClicked() {
        presenter.startCheckoutProcess();
    }

    @Override
    public void cleanOrderItemsList() {
        orderItemsAdapter.clear();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PRODUCTS_SCREEN && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            OrderItem orderItem = bundle.getParcelable(ProductsActivity.EXTRA_ORDER_ITEM);
            presenter.addNewItem(orderItem);
        }
    }
}
