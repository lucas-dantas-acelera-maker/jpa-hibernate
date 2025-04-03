package br.com.aceleramaker.application;

import br.com.aceleramaker.model.Product;
import br.com.aceleramaker.repository.DAO;

public class Main {
    public static void main(String[] args) {
        Product p = new Product("Pen", 7.45);

        DAO< Product> productDAO = new DAO<>(Product.class);
        productDAO.addEntity(p).close();
    }
}