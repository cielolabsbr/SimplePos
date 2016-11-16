package br.com.cielo.example.products;


import java.util.List;

import br.com.cielo.example.base.mvp.BasePresenter;
import br.com.cielo.example.base.mvp.BaseView;
import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.Product;

public interface ProductsContract {

    interface Presenter extends BasePresenter {
        void productSelected(int productIndex);

        void quantitySelected(int quantity);
    }

    interface View extends BaseView<Presenter> {
        void showProductQuantitySelectionDialog();

        void renderProductsList(List<Product> productsList);

        void returnOrderItemToOrderActivity(OrderItem orderItem);
    }
}
