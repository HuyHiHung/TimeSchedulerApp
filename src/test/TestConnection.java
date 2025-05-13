package test;

import db.DBConnection;
import java.sql.Connection;

public class TestConnection {
    public static void main(String[] args) {
        Connection conn = DBConnection.getConnection();

        if (conn != null) {
            System.out.println("✅ Kết nối CSDL thành công!");
        } else {
            System.out.println("❌ Kết nối CSDL thất bại.");
        }
    }
}
