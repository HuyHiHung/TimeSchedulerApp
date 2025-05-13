package myapp;

import db.DBConnection;
import model.User;
import ui.LoginFrame;
import ui.calendar.TaskCalendarPanel;

import javax.swing.*;
import java.awt.*;

public class Main {
    public static void main(String[] args) {
        // Thiết lập giao diện
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

        // Chạy trên luồng sự kiện
        SwingUtilities.invokeLater(() -> {
            showLoginAndLaunchCalendar();
        });
    }

    private static void showLoginAndLaunchCalendar() {
        LoginFrame loginFrame = new LoginFrame();

        // Giả định bạn có phương thức getLoggedInUser() để lấy user sau khi đăng nhập
        loginFrame.setVisible(true);

        // Chờ người dùng đăng nhập xong
        loginFrame.addWindowListener(new java.awt.event.WindowAdapter() {
            @Override
            public void windowClosed(java.awt.event.WindowEvent e) {
                User user = loginFrame.getLoggedInUser();
                if (user != null) {
                    showCalendarUI(user);
                } else {
                    System.exit(0); // không đăng nhập → thoát
                }
            }
        });
    }

    public static void showCalendarUI(User user) {
        JFrame frame = new JFrame("Lịch công việc - Xin chào " + user.getFullName());
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(1200, 800);
        frame.setLocationRelativeTo(null);
        frame.add(new TaskCalendarPanel(user.getId()));
        frame.setVisible(true);
    }
}
