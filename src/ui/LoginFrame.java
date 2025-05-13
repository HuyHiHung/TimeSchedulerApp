package ui;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;
import myapp.Main;

import javax.swing.*;
import java.awt.*;

public class LoginFrame extends JFrame {

    private JTextField txtUsername;
    private JPasswordField txtPassword;
    private JButton btnLogin, btnRegister;

    private UserDAO userDAO = new UserDAOImpl();
    private User loggedInUser = null;  // ✅ thêm biến này để lưu user đã đăng nhập

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

        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 15, 0));
        buttonPanel.add(btnLogin);
        buttonPanel.add(btnRegister);

        gbc.gridx = 0; 
        gbc.gridy = 2; 
        gbc.gridwidth = 2; 
        gbc.anchor = GridBagConstraints.CENTER;
        panel.add(buttonPanel, gbc);


        add(panel);

        // Xử lý đăng nhập
        btnLogin.addActionListener(e -> handleLogin());

        // Mở form đăng ký
        btnRegister.addActionListener(e -> {
            new RegisterFrame().setVisible(true);
            setVisible(false);
        });
    }

    private void handleLogin() {
        String username = txtUsername.getText();
        String password = new String(txtPassword.getPassword());

        if (username.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin.");
            return;
        }

        // Nếu có PasswordUtils.hash() thì gọi ở đây
        // password = PasswordUtils.hash(password);

        boolean result = userDAO.checkLogin(username, password);

        if (result) {
            loggedInUser = userDAO.getUserByUsername(username);  // ✅ Lưu user lại
            JOptionPane.showMessageDialog(this, "Đăng nhập thành công!");
            Main.showCalendarUI(loggedInUser);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Sai tên đăng nhập hoặc mật khẩu.");
        }
    }

    // ✅ Trả về người dùng sau đăng nhập
    public User getLoggedInUser() {
        return this.loggedInUser;
    }
}
