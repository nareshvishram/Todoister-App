package com.bawp.todoister.data;

import android.app.Application;

import androidx.lifecycle.LiveData;

import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskRepository {

    private TaskDao taskDao;
    private LiveData<List<Task>> allTasks;

    public TaskRepository(Application application) {
        TaskRoomDatabase database=TaskRoomDatabase.getDatabase(application);
        taskDao=database.taskDao();
        allTasks=taskDao.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasks(){
        return allTasks;
    }
    public LiveData<Task> getTaskById(long id){
        return taskDao.getById(id);
    }

    public void insert(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()->{
            taskDao.insert(task);
        });
    }
    public void update(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()->taskDao.update(task));
    }
    public void delete(Task task){
        TaskRoomDatabase.databaseWriterExecutor.execute(()->taskDao.delete(task));
    }

}
