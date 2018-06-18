/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.entites.BrandEntities;
import com.entites.ProductAddEntites;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author TUYEN
 */
public interface BrandService {

    public List<BrandEntities> listBrand() throws SQLException;

    public List<BrandEntities> listBrandTop10() throws SQLException;
    public List<ProductAddEntites> listProductWithBrand(int brandId) throws SQLException;
}
