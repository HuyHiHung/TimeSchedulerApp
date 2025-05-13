package ui.calendar;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Task;
import ui.TaskDialog;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.time.LocalDate;
import java.util.List;

public class TaskPopupDialog extends JDialog {

    private final int userId;
    private final LocalDate date;
    private final TaskDAO taskDAO = new TaskDAOImpl();
    private final JTable table;
    private final DefaultTableModel tableModel;
    private final TaskUpdateListener listener;

    public TaskPopupDialog(Window owner, int userId, LocalDate date, List<Task> tasks, TaskUpdateListener listener) {
        super(owner, "Công việc ngày " + date, ModalityType.APPLICATION_MODAL);
        this.userId = userId;
        this.date = date;
        this.listener = listener;

        setSize(600, 300);
        setLocationRelativeTo(owner);

        tableModel = new DefaultTableModel(new Object[]{"ID", "Tiêu đề", "Bắt đầu", "Kết thúc", "Ưu tiên", "Trạng thái"}, 0);
        table = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(table);

        JButton btnEdit = new JButton("Sửa");
        JButton btnDelete = new JButton("Xóa");
        JPanel actionPanel = new JPanel();
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        add(scrollPane, BorderLayout.CENTER);
        add(actionPanel, BorderLayout.SOUTH);

        loadTasks(tasks);

        btnEdit.addActionListener(e -> handleEdit());
        btnDelete.addActionListener(e -> handleDelete());
    }

    private void loadTasks(List<Task> tasks) {
        tableModel.setRowCount(0);
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

    private void handleEdit() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vui lòng chọn công việc để sửa.");
            return;
        }

        int taskId = (int) tableModel.getValueAt(row, 0);
        Task task = taskDAO.getTaskById(taskId);
        TaskDialog dialog = new TaskDialog((Frame) SwingUtilities.getWindowAncestor(this), task, userId);
        dialog.setVisible(true);
        if (dialog.isSaved()) {
            reload();
            if (listener != null) listener.onTasksUpdated();
        }
    }

    private void handleDelete() {
        int row = table.getSelectedRow();
        if (row == -1) {
            JOptionPane.showMessageDialog(this, "Vuia lòng chọn công việc để xóa.");
            return;
        }

        int taskId = (int) tableModel.getValueAt(row, 0);
        int confirm = JOptionPane.showConfirmDialog(this, "Bạn có chắc muốn xóa công việc này?", "Xác nhận", JOptionPane.YES_NO_OPTION);
        if (confirm == JOptionPane.YES_OPTION) {
            taskDAO.deleteTask(taskId);
            reload();
            if (listener != null) listener.onTasksUpdated();
        }
    }

    private void reload() {
        List<Task> tasks = taskDAO.getTasksByUserIdAndDate(userId, date);
        loadTasks(tasks);
    }
}
