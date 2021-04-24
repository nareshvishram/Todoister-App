package com.bawp.todoister.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.bawp.todoister.data.TaskRepository;
import com.bawp.todoister.util.TaskRoomDatabase;

import java.util.List;

public class TaskViewModel extends AndroidViewModel {

    public  static  TaskRepository repository ;
    private LiveData<List<Task>> allTasks;

    public TaskViewModel(@NonNull Application application) {
        super(application);
        repository=new TaskRepository(application);
        allTasks=repository.getAllTasks();
    }
    public LiveData<List<Task>> getAllTasks(){return allTasks;}

    public static void insert(Task task){
        repository.insert(task);
    }
    public static LiveData<Task> getTaskById(long id){
        return repository.getTaskById(id);
    }
    public static void update(Task task){
        repository.update(task);
    }
    public static void delete(Task task){
        repository.delete(task);
    }
}
