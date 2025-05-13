package ui;

import dao.UserDAO;
import dao.impl.UserDAOImpl;
import model.User;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

import com.github.lgooddatepicker.components.DatePicker;

public class RegisterFrame extends JFrame {

    private JTextField txtUsername, txtFullName, txtEmail, txtPhone;
    private JPasswordField txtPassword, txtConfirmPassword;
    private JButton btnRegister, btnBack;

    private DatePicker datePickerDob;

    private UserDAO userDAO = new UserDAOImpl();

    public RegisterFrame() {
        setTitle("Đăng ký người dùng");
        setSize(450, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        initUI();
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridLayout(9, 2, 10, 10));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        txtUsername = new JTextField();
        txtPassword = new JPasswordField();
        txtConfirmPassword = new JPasswordField();
        txtFullName = new JTextField();
        txtEmail = new JTextField();
        txtPhone = new JTextField();
        datePickerDob = new DatePicker();

        btnRegister = new JButton("Đăng ký");
        btnBack = new JButton("Quay lại");

        panel.add(new JLabel("Tên đăng nhập:"));
        panel.add(txtUsername);
        panel.add(new JLabel("Mật khẩu:"));
        panel.add(txtPassword);
        panel.add(new JLabel("Xác nhận mật khẩu:"));
        panel.add(txtConfirmPassword);
        panel.add(new JLabel("Họ tên:"));
        panel.add(txtFullName);
        panel.add(new JLabel("Email:"));
        panel.add(txtEmail);
        panel.add(new JLabel("Số điện thoại:"));
        panel.add(txtPhone);
        panel.add(new JLabel("Ngày sinh:"));
        panel.add(datePickerDob);

        panel.add(btnRegister);
        panel.add(btnBack);

        add(panel);

        btnRegister.addActionListener(e -> handleRegister());
        btnBack.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void handleRegister() {
        String username = txtUsername.getText().trim();
        String password = new String(txtPassword.getPassword());
        String confirmPassword = new String(txtConfirmPassword.getPassword());
        String fullName = txtFullName.getText().trim();
        String email = txtEmail.getText().trim();
        String phone = txtPhone.getText().trim();
        java.time.LocalDate dobLocal = datePickerDob.getDate();

        if (username.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()
                || email.isEmpty() || dobLocal == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập đầy đủ thông tin bắt buộc.");
            return;
        }

        if (!password.equals(confirmPassword)) {
            JOptionPane.showMessageDialog(this, "Mật khẩu xác nhận không khớp.");
            return;
        }

        if (userDAO.getUserByUsername(username) != null) {
            JOptionPane.showMessageDialog(this, "Tên đăng nhập đã tồn tại.");
            return;
        }

        Date dob = Date.valueOf(dobLocal);

        User user = new User();
        user.setUsername(username);
        user.setPassword(password);
        user.setFullName(fullName);
        user.setEmail(email);
        user.setPhone(phone);
        user.setDob(dob);

        boolean success = userDAO.addUser(user);

        if (success) {
            JOptionPane.showMessageDialog(this, "Đăng ký thành công!");
            new LoginFrame().setVisible(true);
            dispose();
        } else {
            JOptionPane.showMessageDialog(this, "Đăng ký thất bại. Kiểm tra lại dữ liệu.");
        }
    }
}
