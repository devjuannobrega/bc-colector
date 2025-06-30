package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {
    private static final String url = "jdbc:mysql://127.0.0.1:3306/bc?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String user = "root";
    private static final String pass = "25250416";


    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}
