package ui;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Task;
import model.User;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.Timestamp;
import java.util.List;

public class TaskManagerFrame extends JFrame {

    private User user;
    private TaskDAO taskDAO = new TaskDAOImpl();
    private JTable taskTable;
    private DefaultTableModel tableModel;

    public TaskManagerFrame(User user) {
        this.user = user;

        setTitle("Quản lý công việc - Xin chào, " + user.getFullName());
        setSize(800, 400);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        initUI();
        loadTasks();
    }

    private void initUI() {
        tableModel = new DefaultTableModel(new Object[]{"ID", "Tiêu đề", "Bắt đầu", "Kết thúc", "Ưu tiên", "Trạng thái"}, 0);
        taskTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(taskTable);

        JButton btnAdd = new JButton("Thêm");
        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JButton btnLogout = new JButton("Đăng xuất");

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(btnAdd);
        buttonPanel.add(btnEdit);
        buttonPanel.add(btnDelete);
        buttonPanel.add(btnLogout);

        add(scrollPane, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);

        btnAdd.addActionListener(e -> handleAddTask());
        btnEdit.addActionListener(e -> handleEditTask());
        btnDelete.addActionListener(e -> handleDeleteTask());
        btnLogout.addActionListener(e -> {
            new LoginFrame().setVisible(true);
            dispose();
        });
    }

    private void loadTasks() {
        tableModel.setRowCount(0);
        List<Task> tasks = taskDAO.getTasksByUserId(user.getId());
        for (Task task : tasks) {
            tableModel.addRow(new Object[]{
                    task.getId(),
                    task.getTitle(),
                    task.getStartTime(),
                    task.getEndTime(),
                    task.getPriority(),
                    task.getStatus()
            });
        }
    }

    private void handleAddTask() {
        TaskDialog dialog = new TaskDialog(this, null, user.getId());
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadTasks();
        }
    }

    private void handleEditTask() {
        int selected = taskTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc để sửa.");
            return;
        }
        int taskId = (int) tableModel.getValueAt(selected, 0);
        Task task = taskDAO.getTaskById(taskId);
        TaskDialog dialog = new TaskDialog(this, task, user.getId());
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            loadTasks();
        }
    }

    private void handleDeleteTask() {
        int selected = taskTable.getSelectedRow();
        if (selected == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc để xóa.");
            return;
        }
        int taskId = (int) tableModel.getValueAt(selected, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa công việc này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            taskDAO.deleteTask(taskId);
            loadTasks();
        }
    }
}
