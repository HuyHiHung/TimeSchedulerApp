package dao.impl;

import dao.TaskDAO;
import db.DBConnection;
import model.Task;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TaskDAOImpl implements TaskDAO {

    private Connection conn = DBConnection.getConnection();

    @Override
    public boolean addTask(Task task) {
        String sql = "INSERT INTO tasks (title, description, start_time, end_time, priority, status, user_id) VALUES (?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setTimestamp(3, task.getStartTime());
            ps.setTimestamp(4, task.getEndTime());
            ps.setString(5, task.getPriority());
            ps.setString(6, task.getStatus());
            ps.setInt(7, task.getUserId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean updateTask(Task task) {
        String sql = "UPDATE tasks SET title=?, description=?, start_time=?, end_time=?, priority=?, status=? WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, task.getTitle());
            ps.setString(2, task.getDescription());
            ps.setTimestamp(3, task.getStartTime());
            ps.setTimestamp(4, task.getEndTime());
            ps.setString(5, task.getPriority());
            ps.setString(6, task.getStatus());
            ps.setInt(7, task.getId());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean deleteTask(int taskId) {
        String sql = "DELETE FROM tasks WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Task getTaskById(int taskId) {
        String sql = "SELECT * FROM tasks WHERE id=?";
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, taskId);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractTask(rs);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public List<Task> getTasksByUserId(int userId) {
        String sql = "SELECT * FROM tasks WHERE user_id=?";
        List<Task> list = new ArrayList<>();
        try (PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    @Override
    public List<Task> getAllTasks() {
        String sql = "SELECT * FROM tasks";
        List<Task> list = new ArrayList<>();
        try (Statement st = conn.createStatement();
             ResultSet rs = st.executeQuery(sql)) {
            while (rs.next()) {
                list.add(extractTask(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    private Task extractTask(ResultSet rs) throws SQLException {
        return new Task(
            rs.getInt("id"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getTimestamp("start_time"),
            rs.getTimestamp("end_time"),
            rs.getString("priority"),
            rs.getString("status"),
            rs.getInt("user_id")
        );
    }
}
