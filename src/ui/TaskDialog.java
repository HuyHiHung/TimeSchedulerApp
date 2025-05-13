package ui;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Task;
import utils.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

public class TaskDialog extends JDialog {

    private JTextField txtTitle, txtStart, txtEnd;
    private JTextArea txtDescription;
    private JComboBox<String> cbPriority, cbStatus;
    private JButton btnSave, btnCancel;
    private boolean saved = false;

    private TaskDAO taskDAO = new TaskDAOImpl();
    private Task task;
    private int userId;

    public TaskDialog(Frame owner, Task task, int userId) {
        super(owner, true);
        this.task = task;
        this.userId = userId;

        setTitle(task == null ? "Thêm công việc" : "Chỉnh sửa công việc");
        setSize(500, 400);
        setLocationRelativeTo(owner);
        initUI();

        if (task != null) {
            fillForm(task);
        }
    }

    private void initUI() {
        JPanel panel = new JPanel(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);

        JLabel lblTitle = new JLabel("Tiêu đề:");
        JLabel lblDescription = new JLabel("Mô tả:");
        JLabel lblStart = new JLabel("Bắt đầu (yyyy-MM-dd HH:mm):");
        JLabel lblEnd = new JLabel("Kết thúc (yyyy-MM-dd HH:mm):");
        JLabel lblPriority = new JLabel("Ưu tiên:");
        JLabel lblStatus = new JLabel("Trạng thái:");

        txtTitle = new JTextField(25);
        txtDescription = new JTextArea(4, 25);
        txtStart = new JTextField(20);
        txtEnd = new JTextField(20);
        cbPriority = new JComboBox<>(new String[]{"High", "Medium", "Low"});
        cbStatus = new JComboBox<>(new String[]{"Pending", "Done"});

        btnSave = new JButton("Lưu");
        btnCancel = new JButton("Hủy");

        gbc.gridx = 0; gbc.gridy = 0; gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblTitle, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(txtTitle, gbc);

        gbc.gridx = 0; gbc.gridy = 1; gbc.anchor = GridBagConstraints.NORTHEAST;
        panel.add(lblDescription, gbc);
        gbc.gridx = 1; gbc.anchor = GridBagConstraints.WEST;
        panel.add(new JScrollPane(txtDescription), gbc);

        gbc.gridx = 0; gbc.gridy = 2; gbc.anchor = GridBagConstraints.EAST;
        panel.add(lblStart, gbc);
        gbc.gridx = 1; panel.add(txtStart, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblEnd, gbc);
        gbc.gridx = 1; panel.add(txtEnd, gbc);

        gbc.gridx = 0; gbc.gridy = 4; panel.add(lblPriority, gbc);
        gbc.gridx = 1; panel.add(cbPriority, gbc);

        gbc.gridx = 0; gbc.gridy = 5; panel.add(lblStatus, gbc);
        gbc.gridx = 1; panel.add(cbStatus, gbc);

        JPanel btnPanel = new JPanel();
        btnPanel.add(btnSave);
        btnPanel.add(btnCancel);

        gbc.gridx = 0; gbc.gridy = 6; gbc.gridwidth = 2;
        panel.add(btnPanel, gbc);

        add(panel);

        btnSave.addActionListener(e -> handleSave());
        btnCancel.addActionListener(e -> dispose());
    }

    private void fillForm(Task task) {
        txtTitle.setText(task.getTitle());
        txtDescription.setText(task.getDescription());
        txtStart.setText(DateUtils.formatTimestamp(task.getStartTime()));
        txtEnd.setText(DateUtils.formatTimestamp(task.getEndTime()));
        cbPriority.setSelectedItem(task.getPriority());
        cbStatus.setSelectedItem(task.getStatus());
    }

    private void handleSave() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String startStr = txtStart.getText().trim();
        String endStr = txtEnd.getText().trim();
        String priority = cbPriority.getSelectedItem().toString();
        String status = cbStatus.getSelectedItem().toString();

        if (title.isEmpty() || startStr.isEmpty() || endStr.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Tiêu đề, thời gian bắt đầu và kết thúc là bắt buộc.");
            return;
        }

        Timestamp startTime = DateUtils.toTimestamp(startStr);
        Timestamp endTime = DateUtils.toTimestamp(endStr);

        if (startTime == null || endTime == null) {
            JOptionPane.showMessageDialog(this, "Định dạng ngày/giờ không hợp lệ. Dạng đúng: yyyy-MM-dd HH:mm");
            return;
        }

        if (!DateUtils.isStartBeforeEnd(startTime, endTime)) {
            JOptionPane.showMessageDialog(this, "Thời gian kết thúc phải sau thời gian bắt đầu.");
            return;
        }

        if (task == null) {
            task = new Task(0, title, description, startTime, endTime, priority, status, userId);
            taskDAO.addTask(task);
        } else {
            task.setTitle(title);
            task.setDescription(description);
            task.setStartTime(startTime);
            task.setEndTime(endTime);
            task.setPriority(priority);
            task.setStatus(status);
            taskDAO.updateTask(task);
        }

        saved = true;
        dispose();
    }

    public boolean isSaved() {
        return saved;
    }
}
