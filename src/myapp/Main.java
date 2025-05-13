package myapp;

import db.DBConnection;
import ui.LoginFrame;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        // Thiết lập giao diện Swing (nếu muốn đẹp hơn)
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {
            System.out.println("Không thể thiết lập giao diện: " + e.getMessage());
        }

        // Kiểm tra kết nối CSDL
        if (DBConnection.getConnection() == null) {
            JOptionPane.showMessageDialog(null, "Không thể kết nối CSDL. Kiểm tra cấu hình!");
            System.exit(1);
        }

        // Hiển thị giao diện đăng nhập
        SwingUtilities.invokeLater(() -> {
            new LoginFrame().setVisible(true);
        });
    }
}
