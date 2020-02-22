package Ass.Repository;

import Ass.Model.Products;

public interface ProductsRepository extends Repository<Products>{
    Products findByName(String name);
    void refresh(Products products);
}
