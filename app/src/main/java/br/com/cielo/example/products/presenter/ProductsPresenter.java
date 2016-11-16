package br.com.cielo.example.products.presenter;

import java.util.ArrayList;
import java.util.List;

import br.com.cielo.example.domain.model.OrderItem;
import br.com.cielo.example.domain.model.Product;
import br.com.cielo.example.products.ProductsContract;

public class ProductsPresenter implements ProductsContract.Presenter {

    private ProductsContract.View view;
    private List<Product> productsList;
    private int selectedProductIndex = -1;

    public ProductsPresenter(ProductsContract.View view) {
        this.view = view;
        this.view.setPresenter(this);
    }

    @Override
    public void productSelected(int productIndex) {
        this.selectedProductIndex = productIndex;
        view.showProductQuantitySelectionDialog();
    }

    @Override
    public void quantitySelected(int quantity) {
        Product selectedProduct = productsList.get(selectedProductIndex);
        OrderItem orderItem = new OrderItem(selectedProduct, quantity);
        view.returnOrderItemToOrderActivity(orderItem);
    }

    @Override
    public void resume() {
        loadProductsList();
    }

    private void loadProductsList() {
        if (productsList == null || productsList.isEmpty()) {
            generateProductList();
        }
        displayProductsList();
    }

    private void displayProductsList() {
        view.renderProductsList(productsList);
    }

    private void generateProductList() {
        productsList = new ArrayList<>(50);
        for (int i = 1; i <= 50; i++) {
            Product product = new Product(String.format("%08d", i),
                    String.format("Produto %d", i), i * 100);
            productsList.add(product);
        }
    }

    @Override
    public void pause() {
    }

    @Override
    public void destroy() {
    }
}
