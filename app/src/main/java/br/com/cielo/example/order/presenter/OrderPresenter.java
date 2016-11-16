package br.com.cielo.example.order.presenter;

import android.os.Bundle;
import android.util.Log;

import org.jetbrains.annotations.NotNull;

import java.util.Date;
import java.util.UUID;

import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.PosOrder;
import br.com.cielo.example.lio.PaymentManager;
import br.com.cielo.example.order.OrderContract;
import cielo.sdk.order.Order;
import cielo.sdk.order.payment.PaymentError;
import cielo.sdk.order.payment.PaymentListener;

public class OrderPresenter implements OrderContract.Presenter {

    private static final String CURRENT_ORDER = "currentOrder";

    private OrderContract.View view;
    private PosOrder order;
    private PaymentManager paymentManager;
    private String LOG_TAG = OrderPresenter.class.getName();

    public OrderPresenter(OrderContract.View view, PaymentManager paymentManager) {
        this.view = view;
        this.view.setPresenter(this);
        this.paymentManager = paymentManager;
    }

    @Override
    public void resume() {
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
        paymentManager.unbindOrderManager();
    }

    @Override
    public void startCheckoutProcess() {
        paymentManager.startPaymentProcess(order, paymentListener);
        view.showPaymentProcessingView();
    }

    @Override
    public void resetOrder() {
        order = new PosOrder(UUID.randomUUID().toString(), new Date(System.currentTimeMillis()));
        view.cleanOrderItemsList();
        updateOrderTotal();
        updateCheckoutButtonStatus();
    }

    private void updateCheckoutButtonStatus() {
        view.disableCheckoutButton();
        if (!order.getItems().isEmpty()) {
            view.enableCheckoutButton();
        }
    }

    private void updateOrderTotal() {
        view.updateOrderTotal(order.getOrderTotal());
    }

    @Override
    public void addNewItem(OrderItem orderItem) {
        order.getItems().add(orderItem);
        view.addNewOrderItem(orderItem);
        updateOrderTotal();
        updateCheckoutButtonStatus();
    }

    @Override
    public void saveInstanceState(Bundle outState) {
        if (!order.getItems().isEmpty()) {
            outState.putParcelable(CURRENT_ORDER, order);
        }
    }

    @Override
    public void restoreSavedInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState != null && savedInstanceState.containsKey(CURRENT_ORDER)) {
            order = savedInstanceState.getParcelable(CURRENT_ORDER);
            reloadOrderItems();
            updateOrderTotal();
            updateCheckoutButtonStatus();
        }
    }

    private void reloadOrderItems() {
        view.cleanOrderItemsList();
        for (OrderItem orderItem : order.getItems()) {
            view.addNewOrderItem(orderItem);
        }
    }

    private void onPaymentError(String errorMessage) {
        view.hidePaymentProcessingView();
        view.showPaymentErrorDialog(errorMessage);
    }

    private void onPaymentSucceeded() {
        view.hidePaymentProcessingView();
        view.showPaymentSuccessDialog();
    }

    private void onPaymentCancelled() {
        view.hidePaymentProcessingView();
        view.showPaymentCancelledDialog();
    }

    private PaymentListener paymentListener = new PaymentListener() {
        @Override
        public void onStart() {
            Log.d(LOG_TAG, "The payment has started.");
            view.showToastMessage("The payment has started");
        }

        @Override
        public void onPayment(@NotNull Order order) {
            Log.d(LOG_TAG, "One payment was received.");
            view.showToastMessage("One payment was received");
        }

        @Override
        public void onCancel() {
            OrderPresenter.this.onPaymentCancelled();
            view.showToastMessage("The payment was cancelled.");
        }

        @Override
        public void onSuccess() {
            OrderPresenter.this.onPaymentSucceeded();
            view.showToastMessage("Payment success!");
        }

        @Override
        public void onError(@NotNull PaymentError paymentError) {
            OrderPresenter.this.onPaymentError(paymentError.toString());
            view.showToastMessage("Payment error.");
        }
    };
}
