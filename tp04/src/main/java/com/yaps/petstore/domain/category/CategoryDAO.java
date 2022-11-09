package com.yaps.petstore.domain.category;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import com.yaps.utils.dao.AbstractDAO;
import com.yaps.utils.dao.exception.DataAccessException;
import com.yaps.utils.dao.exception.DuplicateKeyException;
import com.yaps.utils.dao.exception.ObjectNotFoundException;
import com.yaps.utils.model.CheckException;

/**
 * This class does all the database access for the class Category.
 *
 * @see Category
 */
public class CategoryDAO extends AbstractDAO<Category> {
    private static final String COLUMNS[] = { "name", "description" };

    public CategoryDAO(Connection connection) {
        super(connection, "category", COLUMNS);
    }

    @Override
    public Optional<Category> findById(String id) {
        if (id == null)
            throw new NullPointerException("id should not be null");
        String sql = "select * from " + getTableName() + " where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
            pst.setString(1, id);
            try (ResultSet resultSet = pst.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(extractData(resultSet));
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public List<Category> findAll() {
        String sql = "select * from " + getTableName();
        try (
                Statement st = getConnection().createStatement();
                ResultSet resultSet = st.executeQuery(sql);) {
            List<Category> result = new ArrayList<>();
            while (resultSet.next()) {
                result.add(extractData(resultSet));
            }
            return result;
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    @Override
    public void save(Category category) throws DuplicateKeyException, CheckException {
        category.checkData();
        if (findById(category.getId()).isEmpty()) {
            String sql = "insert into " + getTableName()
                    + " ( id, " + String.join(", ", getFieldsNames()) + ")"
                    + " values (?, ?, ?)";
            try (PreparedStatement pst = getConnection().prepareStatement(sql)) {
                pst.setString(1, category.getId());
                pst.setString(2, category.getName());
                pst.setString(3, category.getDescription());
                pst.executeUpdate();
            } catch (SQLException e) {
                throw new DataAccessException(e.getMessage(), e);
            }
        } else {
            throw new DuplicateKeyException();
        }
    }

    @Override
    public void update(Category category) throws ObjectNotFoundException, CheckException {
        category.checkData();
        if (findById(category.getId()).isEmpty()) {
            throw new ObjectNotFoundException();
        }
        String sql = "update " + getTableName() + " set name = ?, description = ? where id = ?";
        try (PreparedStatement pst = getConnection().prepareStatement(sql)) {            
            pst.setString(1, category.getName());
            pst.setString(2, category.getDescription());
            pst.setString(3, category.getId());
            pst.executeUpdate();
        } catch (SQLException e) {
            throw new DataAccessException(e.getMessage(), e);
        }
    }

    private Category extractData(ResultSet res) throws SQLException {
        String id = res.getString("id");
        String name = res.getString("name");
        String description = res.getString("description");
        return new Category(id, name, description);
    }

}
