/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.BrandDao;
import com.entites.BrandEntities;
import com.entites.ProductAddEntites;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

/**
 *
 * @author TUYEN
 */
@Repository("brandService")
public class BrandServiceImpl implements BrandService {

    @Autowired
    BrandDao dao;

    @Override
    public List<BrandEntities> listBrand() throws SQLException {
        return dao.listBrand();
    }

    @Override
    public List<BrandEntities> listBrandTop10() throws SQLException {
       return dao.listBrandTop10();
    }

    @Override
    public List<ProductAddEntites> listProductWithBrand(int brandId) throws SQLException {
       return  dao.listProductWithBrand(brandId);
    }
}
