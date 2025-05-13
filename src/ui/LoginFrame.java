package ui;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    private UserDAO userDAO = new UserDAOImpl();

    public LoginFrame() {
        setTitle("Đăng nhập");
        setSize(400, 250);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        JLabel lblUsername = new JLabel("Tên đăng nhập:");
        JLabel lblPassword = new JLabel("Mật khẩu:");

        txtUsername = new JTextField(20);
        txtPassword = new JPasswordField(20);

        btnLogin = new JButton("Đăng nhập");
        btnRegister = new JButton("Đăng ký");

        gbc.insets = new Insets(10, 10, 10, 10);

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblUsername, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtUsername, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblPassword, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtPassword, gbc);

        gbc.gridx = 0; gbc.gridy = 2;
        panel.add(btnLogin, gbc);
        gbc.gridx = 1;
        panel.add(btnRegister, gbc);

        add(panel);

        // Xử lý đăng nhập
        btnLogin.addActionListener(e -> handleLogin());

        // Mở form đăng ký
        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            dispose(); // đóng frame hiện tại
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Nếu có dùng PasswordUtils thì hash mật khẩu ở đây
        // password = PasswordUtils.hash(password);

        boolean result = userDAO.checkLogin(username, password);

        if (result) {
            User user = userDAO.getUserByUsername(username);
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");

            // Mở giao diện quản lý task
            new TaskManagerFrame(user).setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }
}
