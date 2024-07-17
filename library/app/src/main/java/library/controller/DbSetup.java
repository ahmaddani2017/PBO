package library.controller;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class DbSetup {
    public static void createTable() {
        ArrayList<String> sql = new ArrayList<>();

        sql.add("""
                CREATE TABLE IF NOT EXISTS books (id TEXT PRIMARY KEY, title TEXT, author TEXT, isAvailable INTEGER);
                """);

        sql.add("""
                CREATE TABLE IF NOT EXISTS members (id TEXT PRIMARY KEY, name TEXT, email TEXT, phoneNumber TEXT);
                """);
        
        sql.add("""
                CREATE TABLE IF NOT EXISTS loans
                    (id TEXT PRIMARY KEY, book_id TEXT, member_id TEXT, loan_date DATE, return_date DATE, actual_return_date DATE, fine INTEGER,
                        FOREIGN KEY(book_id) REFERENCES books(id), FOREIGN KEY(member_id) REFERENCES members(id));
                """);
        
        sql.add("""
                CREATE TABLE IF NOT EXISTS loan_history 
                    (loan_id TEXT PRIMARY KEY, book_id TEXT, member_id TEXT, loan_date DATE, return_date DATE, actual_return_date DATE, fine INTEGER,
                    FOREIGN KEY(book_id) REFERENCES books(id)    
                    FOREIGN KEY(member_id) REFERENCES members(id));
                """);
        try (Connection conn = DbKoneksi.getConnection(); Statement stmt = conn.createStatement())
        {
            for (String i: sql)
            {
                stmt.execute(i);
            }

        }
        catch (SQLException e)
        {
            System.out.println(e.getMessage());
        }
    }
}