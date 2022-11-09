package com.yaps.petstore;

import java.io.StringReader;
import java.sql.Connection;
import java.sql.SQLException;

import org.dbunit.IDatabaseTester;
import org.dbunit.JdbcDatabaseTester;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.ITable;
import org.dbunit.dataset.xml.FlatXmlDataSetBuilder;
import org.dbunit.operation.DatabaseOperation;

import com.yaps.petstore.config.PetstoreConnectionFactory;

/**
 * Basic class for the tests.
 * <p> the protected databaseTester field gives access to a IDatabaseTester instance.
 * <p> Gives methods to set up and clear up the DB Unit tests.
 * <p> The connection to the database can be easily accessed ;
 * <p> two methods, {@link #setup()} and {@link #tearDown()} <b>must</b>
 * be called by the actual test classes, respectively as <code>@BeforeEach</code>
 * and <code>@AfterEach</code> code.
 */
public abstract class AbstractDBUnitTestCase {

    PetstoreConnectionFactory accessConstants = PetstoreConnectionFactory.getInstance();

    protected IDatabaseTester databaseTester;

    /**
     * Gives access to the SQL connection for JDBC.
     * @return
     * @throws Exception
     */
    protected final Connection connection() throws Exception {
        return databaseTester.getConnection().getConnection();
    }

    /**
     * Call this method in your test in a method annotated with @BeforeEach.
     * @throws Exception
     */
    protected void setup() throws Exception {
        databaseTester = new JdbcDatabaseTester(
            accessConstants.getJDBCDriverName(),
                accessConstants.getTestURL(), 
                accessConstants.getUser(),
                accessConstants.getPassword());
       
        databaseTester.setSetUpOperation(DatabaseOperation.CLEAN_INSERT);       
        databaseTester.setDataSet(buildIDataSet(getStartDatasetContent()));
        databaseTester.onSetup();
    }

    /**
     * Call this method in your test in a method annotated with @AfterEach.
     * @throws Exception
     */
    protected void tearDown() throws Exception {
        databaseTester.onTearDown();
    }

    /**
     * Asserts that the current content of a table is the same as in a user-built dataset.
     * @param expectedDataSet expected values
     * @param tableName the table to compare.
     * @throws Exception
     * @throws SQLException
     */
    protected void assertCurrentTableContentCorrespondsFor(
        IDataSet expectedDataSet,
         String tableName
         ) throws SQLException, Exception {
        ITable expectedTable = expectedDataSet.getTable(tableName);
        IDataSet databaseDataSet = databaseTester.getConnection().createDataSet();
        ITable actualTable = databaseDataSet.getTable(tableName);
        org.dbunit.Assertion.assertEquals(expectedTable, actualTable);    
    }
    /**
     * Returns a String describing the <em>content</em> of the initial database.
     * The String should be something like :
     * <pre>
     *   <person id="3" name="Babbage"/>
     * </pre>
     * if some data is there, or 
     * <pre>
     *   <person/>
     * </pre>
     * if for instance table person is empty.
     * @return
     */
    protected abstract String getStartDatasetContent();

    /**
     * Builds a dataset using an XML description of its content.
     * The string should be something like :
     * <pre>
     *   <person id="3" name="Babbage"/>
     * </pre>
     * if some data is there, or 
     * <pre>
     *   <person/>
     * </pre>
     * @param innerXML
     * @return
     * @throws DataSetException
     */
    protected static IDataSet buildIDataSet(String innerXML) throws DataSetException {
        StringBuilder builder = new StringBuilder();
        builder.append("<?xml version='1.0' encoding='UTF-8'?>\n");
        builder.append("<dataset>\n");
        builder.append(innerXML);
        builder.append("\n</dataset>\n");
        String xml =  builder.toString();
        IDataSet dataset = new FlatXmlDataSetBuilder().build(new StringReader(xml));     
        return dataset;  
    }

    
}
