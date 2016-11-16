package br.com.cielo.example.order;

import android.os.Bundle;

import br.com.cielo.example.base.mvp.BasePresenter;
import br.com.cielo.example.base.mvp.BaseView;
import br.com.cielo.example.domain.model.OrderItem;

public interface OrderContract {
    interface Presenter extends BasePresenter {
        void startCheckoutProcess();

        void resetOrder();

        void addNewItem(OrderItem orderItem);

        void saveInstanceState(Bundle outState);

        void restoreSavedInstanceState(Bundle savedInstanceState);
    }

    interface View extends BaseView<Presenter> {
        void cleanOrderItemsList();

        void updateOrderTotal(long orderTotal);

        void addNewOrderItem(OrderItem orderItem);

        void disableCheckoutButton();

        void enableCheckoutButton();

        void showPaymentSuccessDialog();

        void showPaymentErrorDialog(String errorMessage);

        void showPaymentCancelledDialog();

        void showPaymentProcessingView();

        void hidePaymentProcessingView();

        void showToastMessage(String message);
    }
}
