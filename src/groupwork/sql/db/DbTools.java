package groupwork.sql.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DbTools {
    private Connection connection;

    public Connection getConnection() throws ClassNotFoundException, SQLException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        String url = "jdbc:sqlserver://127.0.0.1:1433;DatabaseName=LibrarySystem";
        String user = "sa";
        String password = "password";
        connection = DriverManager.getConnection(url, user, password);
        return connection;
    }
}
