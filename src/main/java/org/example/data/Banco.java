package org.example.data;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class Banco {
    //    private static final String pass = "CVL9zl~|hHE?K599";
//    private static final String url = "jdbc:mysql://localhost/bcdata";

    private static final String url = "jdbc:mysql://35.225.31.229:3306/bcdata?useSSL=false";
    private static final String user = "root";
    private static final String pass = "25250416";

    public static Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }
}