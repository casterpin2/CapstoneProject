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

    public List<BrandEntities> listBrand(int page) throws SQLException;
    public List<BrandEntities> listBrandByType(int typeId) throws SQLException;
    public List<BrandEntities> listBrandTop5() throws SQLException;
    public List<ProductAddEntites> listProductWithBrand(int brandId, int page) throws SQLException;
    public List<ProductAddEntites> listProductWithBrandType(int brandId, int typeId, int page) throws SQLException;
    public List<BrandEntities> getBrandsByName(String query, int page) throws SQLException;
    public List<BrandEntities> listBrandByCategory(int categoryId) throws SQLException;
    public List<ProductAddEntites> listProductWithBrandCategory(int brandId, int categoryId, int page) throws SQLException;
}
