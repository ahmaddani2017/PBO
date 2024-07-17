package library.controller;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbKoneksi {
    private static final String URL = "jdbc:sqlite:library.db";
    private static Connection instance;
    
    public static Connection getConnection()
    {
        try {
            if (instance == null || instance.isClosed())
            {
                instance = DriverManager.getConnection(URL);
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return instance;
    }
}
