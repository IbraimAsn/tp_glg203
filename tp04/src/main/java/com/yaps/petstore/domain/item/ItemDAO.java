package com.yaps.petstore.domain.item;

import com.yaps.petstore.domain.product.Product;
import com.yaps.petstore.domain.product.ProductDAO;
import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class ItemDAO extends AbstractDAO<Item> {
    static final String[] COLUMNS = {"name", "unit_cost","product_fk"};
    public ItemDAO(Connection connection){
        super(connection, "item", COLUMNS);
    }
    @Override
    public void save(Item obj) throws DuplicateKeyException, CheckException {
        obj.checkData();
        if(findById(obj.getId()).isEmpty()){
            String sql = "insert into " + getTableName()
                    + " (id, " + String.join(",", getFieldsNames()) + ")"
                    + " values (?, ?, ?, ?)";
            try(PreparedStatement pst = getConnection().prepareStatement(sql)){
                pst.setString(1, obj.getId());
                pst.setString(2, obj.getName());
                pst.setString(3, String.valueOf(obj.getUnitCost()));
                pst.setString(4, obj.getProduct().getId());
            }catch(SQLException ex){
                throw new DataAccessException(ex.getMessage(), ex);
            }

        }else{
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(Item obj) throws ObjectNotFoundException, CheckException {
        obj.checkData();
        if(findById(obj.getId()).isEmpty()){
            throw new ObjectNotFoundException();
        }else{
            String sql = "update "+ getTableName() + " set name = ?, unit_cost = ?, product_fk = ? where id = ?";
            try(PreparedStatement pst = getConnection().prepareStatement(sql)){
                pst.setString(1, obj.getName());
                pst.setString(2, String.valueOf(obj.getUnitCost()));
                pst.setString(3, obj.getProduct().getId());
                pst.setString(4, obj.getId());
            }catch (SQLException ex){
                throw new DataAccessException(ex.getMessage(), ex);
            }
        }
    }

    @Override
    public Optional<Item> findById(String id) {
        if(id == null){
            throw new NullPointerException("id should not be null");
        }else{
            String sql = "select * from " + getTableName() + " where id = ?";
            try(PreparedStatement pst = getConnection().prepareStatement(sql)){
                pst.setString(1,id);
                try(ResultSet resultSet = pst.executeQuery()){
                    if(resultSet.next()){
                        return Optional.of(extractData(resultSet));
                    }else{
                        return Optional.empty();
                    }
                }
            }catch(SQLException ex){
                throw new DataAccessException(ex.getMessage(), ex);
            }
        }
    }


    private Item extractData(ResultSet resultSet) throws SQLException{
        String id = resultSet.getString("id");
        String name = resultSet.getString("name");
        String product_id = resultSet.getString("product_fk");
        String unitCost = resultSet.getString("unit_cost");


        ProductDAO productDAO = new ProductDAO(getConnection());
        Optional<Product> optionalProduct = productDAO.findById(product_id);
        return new Item(id, name, Double.parseDouble(unitCost), optionalProduct.get());


    }

    @Override
    public List<Item> findAll() {
        String sql = "select * from " + getTableName();
        try(Statement st = getConnection().createStatement();
        ResultSet resultSet = st.executeQuery(sql)){
            List<Item> liste = new ArrayList<>();
            while (resultSet.next()){
                liste.add(extractData(resultSet));
            }
            return liste;
        }catch (SQLException ex){
            throw new DataAccessException(ex.getMessage(), ex);

        }
    }
}
