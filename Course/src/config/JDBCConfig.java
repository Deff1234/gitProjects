package config;

import java.sql.*;

public class JDBCConfig {

    private static final String url = "jdbc:mysql://localhost:3306/bsuir";
    private static final String user = "root";
    private static final String password = "revzKP";

    private static Connection con;
    private static Statement stmt;
    private static ResultSet rs;


    public static ResultSet executeQuery(String query) throws SQLException {
        try {
            con = DriverManager.getConnection(url, user, password);
            stmt = con.createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            return rs;
        }

    }

}
