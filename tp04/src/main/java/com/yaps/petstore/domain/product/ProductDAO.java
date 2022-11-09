package com.yaps.petstore.domain.product;

import com.yaps.petstore.domain.category.Category;
import com.yaps.petstore.domain.category.CategoryDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ProductDAO extends AbstractDAO<Product> {
    static final String[] COLUMNS = {"name", "description", "category_fk"};

    public ProductDAO(Connection connection){
        super(connection, "product", COLUMNS);
    }
    @Override
    public void save(Product obj) throws DuplicateKeyException, CheckException {
        obj.checkData();
        if(findById(obj.getId()).isEmpty()){
            String sql = "insert into " + getTableName()
                    + "(id, " + String.join(",", getFieldsNames()) + ")"
                    + " values (?, ?, ?, ?)";
            try(PreparedStatement pst = getConnection().prepareStatement(sql)){
                pst.setString(1, obj.getId());
                pst.setString(2, obj.getName());
                pst.setString(3, obj.getDescription());
                pst.setString(4, obj.getCategory().getId());
                pst.executeUpdate();
            }catch (SQLException exception){
                throw new DataAccessException(exception.getMessage(), exception);
            }
        }else{
            throw  new DuplicateKeyException();
        }
    }

    @Override
    public void update(Product obj) throws ObjectNotFoundException, CheckException {
        obj.checkData();
        if(findById(obj.getId()).isEmpty()){
            throw new ObjectNotFoundException();
        }else{
            String sql = "update " + getTableName() + " set name = ?, description = ?, category_fk = ? where id = ?";
            try(PreparedStatement pst = getConnection().prepareStatement(sql)){
                pst.setString(1, obj.getName());
                pst.setString(2, obj.getDescription());
                pst.setString(3, obj.getCategory().getId());
                pst.setString(4, obj.getId());
            }catch (SQLException ex){
                throw new DataAccessException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Optional<Product> findById(String id) {
        if(id == null){
            throw new NullPointerException("id should not be null");
        }else{
            String sql = "select * from " + getTableName() + " where id=?";
            try(PreparedStatement statement = getConnection().prepareStatement(sql)){
                statement.setString(1,id);
                try(ResultSet resultSet = statement.executeQuery()){
                    if(resultSet.next()){
                        return Optional.of(extractData(resultSet));
                    }else{
                        return Optional.empty();
                    }
                }
            }catch (SQLException ex){
                throw  new DataAccessException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public List<Product> findAll() {
        String sql = "select * from " + getTableName();
        try(Statement st = getConnection().createStatement();
            ResultSet resultSet = st.executeQuery(sql);){
            List<Product> liste = new ArrayList<>();
            while(resultSet.next()){
                liste.add(extractData(resultSet));
            }
            return liste;
        }catch (SQLException ex){
            throw new DataAccessException(ex.getMessage(), ex);
        }
    }

    private Product extractData(ResultSet resultSet) throws SQLException{
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String description = resultSet.getString("description");
        String cat_id = resultSet.getString("category_fk");
        CategoryDAO categoryDAO = new CategoryDAO(getConnection());
        Optional<Category> category = categoryDAO.findById(cat_id);
        Product product = new Product(id, name, description, category.get());
        return product;
    }
}
