package ui.calendar;

import javax.swing.*;

import ui.TaskDialog;

import java.awt.*;
import java.time.LocalDate;

/**
 * Giao diện chính của lịch quản lý công việc:
 * - Hiển thị tháng/năm hiện tại
 * - Nút chuyển tháng trước/sau
 * - Gắn TaskDatePanel bên dưới
 */
public class TaskCalendarPanel extends JPanel {

    private int currentMonth;
    private int currentYear;
    private final int userId;

    private final JLabel lbMonthYear;
    private final JPanel contentPanel;

    public TaskCalendarPanel(int userId) {
        this.userId = userId;

        // Lấy thời gian hiện tại
        LocalDate now = LocalDate.now();
        currentMonth = now.getMonthValue();
        currentYear = now.getYear();

        setLayout(new BorderLayout());
        setBackground(Color.WHITE);

        // ===== Header =====
        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(230, 230, 250)); // màu nhẹ

        JButton btnPrev = new JButton("<");
        JButton btnNext = new JButton(">");
        lbMonthYear = new JLabel("", SwingConstants.CENTER);
        lbMonthYear.setFont(new Font("SansSerif", Font.BOLD, 18));

        header.add(btnPrev, BorderLayout.WEST);
        header.add(lbMonthYear, BorderLayout.CENTER);
        header.add(btnNext, BorderLayout.EAST);

        btnPrev.addActionListener(e -> changeMonth(-1));
        btnNext.addActionListener(e -> changeMonth(1));

        // ===== Content =====
        contentPanel = new JPanel(new BorderLayout());

        // Gắn vào layout chính
        add(header, BorderLayout.NORTH);
        add(contentPanel, BorderLayout.CENTER);
        
        // Thêm nút "Thêm công việc"
        JButton btnAddTask = new JButton("+ Thêm công việc");
        btnAddTask.setFocusPainted(false);
        btnAddTask.setBackground(new Color(97, 173, 255));
        btnAddTask.setForeground(Color.BLACK);

        btnAddTask.addActionListener(e -> openAddTaskDialog());

        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        bottomPanel.add(btnAddTask);

        add(bottomPanel, BorderLayout.SOUTH);

        // Load ban đầu
        updateCalendar();
    }

    private void changeMonth(int delta) {
        currentMonth += delta;
        if (currentMonth < 1) {
            currentMonth = 12;
            currentYear--;
        } else if (currentMonth > 12) {
            currentMonth = 1;
            currentYear++;
        }
        updateCalendar();
    }

    private void updateCalendar() {
        lbMonthYear.setText(getMonthYearString(currentMonth, currentYear));
        contentPanel.removeAll();
        contentPanel.add(new TaskDatePanel(currentMonth, currentYear, userId), BorderLayout.CENTER);
        revalidate();
        repaint();
    }

    private String getMonthYearString(int month, int year) {
        String[] months = {
                "Tháng 1", "Tháng 2", "Tháng 3", "Tháng 4",
                "Tháng 5", "Tháng 6", "Tháng 7", "Tháng 8",
                "Tháng 9", "Tháng 10", "Tháng 11", "Tháng 12"
        };
        return months[month - 1] + " - " + year;
    }
    private void openAddTaskDialog() {
        TaskDialog dialog = new TaskDialog(
        	(Frame) SwingUtilities.getWindowAncestor(this),
            null, // task = null → thêm mới
            userId
        );
        dialog.setVisible(true);

        if (dialog.isSaved()) {
            updateCalendar(); // làm mới giao diện nếu có thay đổi
        }
    }

}
