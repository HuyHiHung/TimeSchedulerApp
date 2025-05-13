package ui.calendar;

import model.Task;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

public class TaskCell extends JButton {

    private int day;
    private boolean isInCurrentMonth = true;
    private List<Task> tasks;

    public TaskCell(int day) {
        this.day = day;
        setContentAreaFilled(false);
        setFocusPainted(false);
        setHorizontalAlignment(SwingConstants.CENTER);
        setVerticalAlignment(SwingConstants.TOP);
        setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
    }

    public void setInCurrentMonth(boolean inMonth) {
        this.isInCurrentMonth = inMonth;
        repaint();
    }

    public void setTasks(List<Task> tasks) {
        this.tasks = tasks;
        repaint();
    }

    public List<Task> getTasks() {
        return tasks;
    }

    public int getDay() {
        return day;
    }

    public Map<String, Integer> getPriorityStats() {
        Map<String, Integer> stats = new HashMap<>();
        stats.put("High", 0);
        stats.put("Medium", 0);
        stats.put("Low", 0);

        if (tasks != null) {
            for (Task task : tasks) {
                String priority = task.getPriority();
                if (stats.containsKey(priority)) {
                    stats.put(priority, stats.get(priority) + 1);
                }
            }
        }

        return stats;
    }

    @Override
    protected void paintComponent(Graphics g) {
        // ======= Fill background =======
        if (!isInCurrentMonth) {
            g.setColor(new Color(245, 245, 245)); // ngoài tháng → xám
        } else if (tasks != null && !tasks.isEmpty()) {
            g.setColor(new Color(220, 240, 255)); // trong tháng có task → xanh nhạt
        } else {
            g.setColor(Color.WHITE); // trong tháng không có task → trắng
        }
        g.fillRect(0, 0, getWidth(), getHeight());

        // ======= Vẽ viền =======
        g.setColor(Color.LIGHT_GRAY);
        g.drawRect(0, 0, getWidth() - 1, getHeight() - 1);

        // ======= Vẽ số ngày =======
        Color textColor = isInCurrentMonth ? Color.BLACK : Color.GRAY;
        g.setColor(textColor);
        g.setFont(new Font("SansSerif", Font.BOLD, 14));
        String dayStr = String.valueOf(day);
        int textWidth = g.getFontMetrics().stringWidth(dayStr);
        g.drawString(dayStr, (getWidth() - textWidth) / 2, 18);

        // ======= Vẽ tổng số công việc =======
        int totalTasks = tasks != null ? tasks.size() : 0;
        if (tasks != null && !tasks.isEmpty()) {
            // Vẽ dòng: "Số việc trong ngày: X"
            String summary = "Số việc trong ngày: " + tasks.size();
            g.setFont(new Font("SansSerif", Font.BOLD, 11));
            g.setColor(Color.BLACK);
            int summaryWidth = g.getFontMetrics().stringWidth(summary);
            g.drawString(summary, (getWidth() - summaryWidth) / 2, 32);
        }


        // ======= Vẽ thống kê task chi tiết =======
        if (tasks != null && !tasks.isEmpty()) {
            Map<String, Integer> stats = getPriorityStats();
            g.setFont(new Font("SansSerif", Font.PLAIN, 11));
            int y = 48;

            if (stats.get("High") > 0) {
                g.setColor(isInCurrentMonth ? Color.RED : Color.GRAY);
                String line = "Ưu tiên Cao: " + stats.get("High");
                int w = g.getFontMetrics().stringWidth(line);
                g.drawString(line, (getWidth() - w) / 2, y);
                y += 13;
            }
            if (stats.get("Medium") > 0) {
                g.setColor(isInCurrentMonth ? new Color(255, 165, 0) : Color.GRAY);
                String line = "Ưu tiên TB: " + stats.get("Medium");
                int w = g.getFontMetrics().stringWidth(line);
                g.drawString(line, (getWidth() - w) / 2, y);
                y += 13;
            }
            if (stats.get("Low") > 0) {
                g.setColor(isInCurrentMonth ? new Color(0, 128, 0) : Color.GRAY);
                String line = "Ưu tiên Thấp: " + stats.get("Low");
                int w = g.getFontMetrics().stringWidth(line);
                g.drawString(line, (getWidth() - w) / 2, y);
            }
        }
    }



}
