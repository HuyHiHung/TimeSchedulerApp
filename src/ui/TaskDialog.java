package ui;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Task;
import utils.DateUtils;

import javax.swing.*;
import java.awt.*;
import java.sql.Timestamp;

import com.github.lgooddatepicker.components.DateTimePicker;
import com.github.lgooddatepicker.components.DatePickerSettings;
import com.github.lgooddatepicker.components.TimePickerSettings;

public class TaskDialog extends JDialog {

    private JTextField txtTitle;
    private JTextArea txtDescription;
    private JComboBox<String> cbPriority, cbStatus;
    private JButton btnSave, btnCancel;
    private boolean saved = false;

    private DateTimePicker dateTimeStart;
    private DateTimePicker dateTimeEnd;

    private TaskDAO taskDAO = new TaskDAOImpl();
    private Task task;
    private int userId;

    public TaskDialog(Frame owner, Task task, int userId) {
        super(owner, true);
        this.task = task;
        this.userId = userId;

        setTitle(task == null ? "Thêm công việc" : "Chỉnh sửa công việc");
        setSize(500, 420);
        setLocationRelativeTo(owner);
        setResizable(false);
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
        JLabel lblStart = new JLabel("Bắt đầu:");
        JLabel lblEnd = new JLabel("Kết thúc:");
        JLabel lblPriority = new JLabel("Ưu tiên:");
        JLabel lblStatus = new JLabel("Trạng thái:");

        txtTitle = new JTextField(25);
        txtDescription = new JTextArea(4, 25);

        // Tạo DateTimePicker đúng cách
        DatePickerSettings dateSettingsStart = new DatePickerSettings();
        TimePickerSettings timeSettingsStart = new TimePickerSettings();
        dateTimeStart = new DateTimePicker(dateSettingsStart, timeSettingsStart);

        DatePickerSettings dateSettingsEnd = new DatePickerSettings();
        TimePickerSettings timeSettingsEnd = new TimePickerSettings();
        dateTimeEnd = new DateTimePicker(dateSettingsEnd, timeSettingsEnd);

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

        gbc.gridx = 0; gbc.gridy = 2; panel.add(lblStart, gbc);
        gbc.gridx = 1; panel.add(dateTimeStart, gbc);

        gbc.gridx = 0; gbc.gridy = 3; panel.add(lblEnd, gbc);
        gbc.gridx = 1; panel.add(dateTimeEnd, gbc);

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

        dateTimeStart.setDateTimePermissive(task.getStartTime().toLocalDateTime());
        dateTimeEnd.setDateTimePermissive(task.getEndTime().toLocalDateTime());

        cbPriority.setSelectedItem(task.getPriority());
        cbStatus.setSelectedItem(task.getStatus());
    }

    private void handleSave() {
        String title = txtTitle.getText().trim();
        String description = txtDescription.getText().trim();
        String priority = cbPriority.getSelectedItem().toString();
        String status = cbStatus.getSelectedItem().toString();

        if (title.isEmpty() || dateTimeStart.getDateTimeStrict() == null || dateTimeEnd.getDateTimeStrict() == null) {
            JOptionPane.showMessageDialog(this, "Vui lòng nhập tiêu đề và thời gian đầy đủ.");
            return;
        }

        Timestamp startTime = Timestamp.valueOf(dateTimeStart.getDateTimeStrict());
        Timestamp endTime = Timestamp.valueOf(dateTimeEnd.getDateTimeStrict());

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
