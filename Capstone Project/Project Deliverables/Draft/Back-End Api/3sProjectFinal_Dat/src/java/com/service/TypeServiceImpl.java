/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.service;

import com.dao.TypeDao;
import com.entites.TypeEntites;
import java.sql.SQLException;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author AHBP
 */
@Service("typeService")
public class TypeServiceImpl implements TypeService{
    @Autowired
    TypeDao dao;
    @Override
    public List<TypeEntites> getTypebyCategory(int categoryId) throws SQLException {
        return dao.getTypebyCategory(categoryId);
    }
    
}
