package db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;
import java.io.IOException;

public class DBConnection {
    private static Connection connection = null;

    static {
        try {
            // Đọc file config.properties
            Properties props = new Properties();
            InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("config.properties");

            if (input == null) {
                throw new IOException("Không tìm thấy file cấu hình config.properties");
            }

            props.load(input);

            String url = props.getProperty("db.url");
            String username = props.getProperty("db.username");
            String password = props.getProperty("db.password");

            // Kết nối CSDL
            connection = DriverManager.getConnection(url, username, password);

        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        return connection;
    }
}
