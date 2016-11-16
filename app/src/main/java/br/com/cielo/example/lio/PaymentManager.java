package br.com.cielo.example.lio;

import android.app.Activity;

import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.PosOrder;
import br.com.cielo.example.domain.model.Product;
import cielo.sdk.order.Order;
import cielo.sdk.order.OrderManager;
import cielo.sdk.order.ServiceBindListener;
import cielo.sdk.order.payment.PaymentListener;

public class PaymentManager {

    private final OrderManager orderManager;
    private boolean isServiceBound = true;

    public PaymentManager(Activity activity, OrderManager orderManager) {
        this.orderManager = orderManager;
        bindOrderManager(activity);
    }

    private void bindOrderManager(Activity activity) {
        orderManager.bind(activity, new ServiceBindListener() {
            @Override
            public void onServiceBound() {
                isServiceBound = true;
            }

            @Override
            public void onServiceUnbound() {
                isServiceBound = false;
            }
        });
    }

    public void unbindOrderManager() {
        orderManager.unbind();
    }

    public void startPaymentProcess(PosOrder posOrder, PaymentListener paymentListener) {
        if (isServiceBound) {
            Order order = createLioOrderFromPosOrder(posOrder);
            orderManager.placeOrder(order);
            orderManager.checkoutOrder(order.getId(), paymentListener);
        }
    }

    private Order createLioOrderFromPosOrder(PosOrder posOrder) {
        Order order = orderManager.createDraftOrder(posOrder.getId());

        for (OrderItem orderItem : posOrder.getItems()) {
            Product itemProduct = orderItem.getProduct();

            String sku = itemProduct.getSku();
            String name = itemProduct.getName();
            long unitPrice = itemProduct.getUnitPrice();
            int quantity = orderItem.getQuantity();
            String unitOfMeasure = "EACH"; // Using EACH just to illustrate the concept

            order.addItem(sku, name, unitPrice, quantity, unitOfMeasure);
        }

        orderManager.updateOrder(order);

        return order;
    }
}
