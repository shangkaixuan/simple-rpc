package com.simple.rpc.service.impl;

import com.simple.rpc.client.domain.Product;
import com.simple.rpc.client.service.IProductService;

/**
 * @author srh
 * @date 2019/10/23
 **/
public class ProductServiceImpl implements IProductService {

    public Product create() {
        return new Product(1, "橡皮擦", 1.5D);
    }
}
