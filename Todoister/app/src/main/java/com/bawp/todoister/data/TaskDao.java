package com.bawp.todoister.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.bawp.todoister.model.Task;

import java.util.List;

@Dao
public interface TaskDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    public void insert(Task task);

    @Query("DELETE FROM task_table")
    public void deleteAll();

    @Query("SELECT * FROM task_table")
    public LiveData<List<Task>> getAllTasks();

    @Query("SELECT * FROM task_table WHERE task_table.task_id==:id")
    public LiveData<Task> getById(long id);

    @Update
    public void update(Task task);

    @Delete
    public void delete(Task task);

}
