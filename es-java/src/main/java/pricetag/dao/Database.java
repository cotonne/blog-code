package pricetag.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Database {
    static {
        try {
            System.out.println("Registering driver");
            Class.forName("com.mysql.jdbc.Driver").newInstance();
            System.out.println("Registering driver done!");
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public Database() {
    }

    public Connection getConnection() {
        final String myUrl = System.getenv("databaseUrl");
        final String user = System.getenv("databaseUser");
        final String password = System.getenv("databasePassword");
        System.out.println("getConnection : " + myUrl);
        Connection connection = null;
        try {
            connection = DriverManager.getConnection(myUrl, user, password);
        } catch (SQLException e) {
            System.out.println("Failure!");
            e.printStackTrace();
        }
        System.out.println("getConnection done");
        return connection;
    }
}