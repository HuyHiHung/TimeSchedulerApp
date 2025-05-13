package ui.calendar;

import dao.TaskDAO;
import dao.impl.TaskDAOImpl;
import model.Task;

import javax.swing.*;
import java.awt.*;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TaskDatePanel extends JPanel {

    private final int month;
    private final int year;
    private final int userId;

    private final TaskDAO taskDAO = new TaskDAOImpl();

    public TaskDatePanel(int month, int year, int userId) {
        this.month = month;
        this.year = year;
        this.userId = userId;

        setLayout(new GridLayout(7, 7)); // 7x7 (Sun to Sat + max 6 weeks)
        setBackground(Color.WHITE);

        initCalendar();
    }

    private void initCalendar() {
        removeAll();

        // Add day headers
        String[] days = {"Sun", "Mon", "Tue", "Wed", "Thu", "Fri", "Sat"};
        for (String d : days) {
            JLabel label = new JLabel(d, SwingConstants.CENTER);
            label.setFont(new Font("SansSerif", Font.BOLD, 13));
            label.setOpaque(true);
            label.setBackground(new Color(240, 240, 240));
            add(label);
        }

        // Tạo map lưu danh sách task theo từng ngày
        Map<LocalDate, List<Task>> tasksByDate = new HashMap<>();

        // Lấy toàn bộ công việc của user
        List<Task> allTasks = taskDAO.getTasksByUserId(userId);
        for (Task task : allTasks) {
            LocalDate start = task.getStartTime().toLocalDateTime().toLocalDate();
            LocalDate end = task.getEndTime().toLocalDateTime().toLocalDate();
            long days1 = ChronoUnit.DAYS.between(start, end);

            for (int i = 0; i <= days1; i++) {
                LocalDate date = start.plusDays(i);
                tasksByDate.computeIfAbsent(date, k -> new ArrayList<>()).add(task);
            }
        }

        // Tính ngày bắt đầu hiển thị
        Calendar cal = Calendar.getInstance();
        cal.set(year, month - 1, 1);
        int startDayOfWeek = cal.get(Calendar.DAY_OF_WEEK) - 1;
        cal.add(Calendar.DATE, -startDayOfWeek);

        // Vẽ 42 ô ngày
        for (int i = 0; i < 42; i++) {
            int day = cal.get(Calendar.DATE);
            int displayMonth = cal.get(Calendar.MONTH) + 1;
            int displayYear = cal.get(Calendar.YEAR);

            LocalDate currentDate = LocalDate.of(displayYear, displayMonth, day);
            List<Task> tasks = tasksByDate.getOrDefault(currentDate, new ArrayList<>());

            TaskCell cell = new TaskCell(day);
            cell.setTasks(tasks);
            cell.setInCurrentMonth(displayMonth == this.month);
            cell.setToolTipText("Click để xem công việc");

            cell.addActionListener(e -> {
                TaskPopupDialog dialog = new TaskPopupDialog(
                    SwingUtilities.getWindowAncestor(this),
                    userId,
                    currentDate,
                    tasks,
                    this::initCalendar // gọi lại chính hàm này để cập nhật giao diện
                );
                dialog.setVisible(true);
            });

            add(cell);
            cal.add(Calendar.DATE, 1);
        }

        revalidate();
        repaint();
    }
}
