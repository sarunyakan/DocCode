/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Project.data.database;
import Project.data.preparation.Configuration;
/**
 *
 * @author fang
 */
/*
 * Make a database connection to postgres
 */
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fang
 */
public class DatabaseConnect {

    private boolean result_driver = false;
    private boolean result_connect = false;
    private boolean result_DB = false;
    private Connection connection = null;
    
    public DatabaseConnect() {
        boolean res1 = DriverRegister();
        boolean res2 = ConnectDB();
        setResult_DB(res1 && res2);
        
        if (isResult_DB() == false) {
            System.exit(0);
        }
    }

    public boolean DriverRegister() {
        System.out.println("-------- PostgreSQL " + "JDBC Connection Testing ------------");

        try {
            Class.forName("org.postgresql.Driver");
            setResult_driver(true);
            System.out.println("PostgreSQL JDBC Driver Registered!");
        } catch (ClassNotFoundException e) {
            System.out.println("Where is your PostgreSQL JDBC Driver? " + "Include in your library path!");
            e.printStackTrace();
            setResult_driver(false);
        }
        return isResult_driver();
    }

    public boolean ConnectDB() {
        setConnection(null);

        try {
            setConnection(DriverManager.getConnection("jdbc:postgresql://localhost:5432/" + Configuration.DANAME, Configuration.USERNAME, Configuration.PASSWORD));

        } catch (SQLException e) {
            System.out.println("Connection Failed! Check output console");
            e.printStackTrace();

        }

        if (getConnection() != null) {
            setResult_connect(true);
            System.out.println("You made it, take control your database now!");
        } else {
            System.out.println("Failed to make connection!");
        }
        return isResult_connect();
    }
    //+++++++++++++++++++ Encupsulation ++++++++++++++++++++++++++++++++++//

    /**
     * @return the result_driver
     */
    public boolean isResult_driver() {
        return result_driver;
    }

    /**
     * @param result_driver the result_driver to set
     */
    public void setResult_driver(boolean result_driver) {
        this.result_driver = result_driver;
    }

    /**
     * @return the result_connect
     */
    public boolean isResult_connect() {
        return result_connect;
    }

    /**
     * @param result_connect the result_connect to set
     */
    public void setResult_connect(boolean result_connect) {
        this.result_connect = result_connect;
    }

    /**
     * @return the result_DB
     */
    public boolean isResult_DB() {
        return result_DB;
    }

    /**
     * @param result_DB the result_DB to set
     */
    public void setResult_DB(boolean result_DB) {
        this.result_DB = result_DB;
    }

    /**
     * @return the connection
     */
    public Connection getConnection() {
        return connection;
    }

    /**
     * @param connection the connection to set
     */
    public void setConnection(Connection connection) {
        this.connection = connection;
    }
}
