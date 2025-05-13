package dao;

import model.Task;
import java.time.LocalDate;
import java.util.List;

public interface TaskDAO {
    boolean addTask(Task task);
    boolean updateTask(Task task);
    boolean deleteTask(int taskId);
    Task getTaskById(int taskId);
    List<Task> getTasksByUserId(int userId);
    List<Task> getAllTasks();
    List<Task> getTasksByUserIdAndDate(int userId, LocalDate date);

}
