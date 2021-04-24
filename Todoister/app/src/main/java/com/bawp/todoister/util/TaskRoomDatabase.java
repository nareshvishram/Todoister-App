package com.bawp.todoister.util;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.bawp.todoister.data.TaskDao;
import com.bawp.todoister.model.Task;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Database(entities = {Task.class},version = 1,exportSchema = false)
@TypeConverters({Converter.class})
public abstract class TaskRoomDatabase extends RoomDatabase {
    public static final String NAME_OF_DATABASE="todoister_database";
    public static final int NUMBER_OF_THREADS=4;
    public abstract TaskDao taskDao();

    public static volatile TaskRoomDatabase INSTANCE;

    public static final ExecutorService databaseWriterExecutor= Executors.newFixedThreadPool(NUMBER_OF_THREADS);

    public static TaskRoomDatabase getDatabase(final Context context){
        if(INSTANCE==null){
            synchronized (TaskRoomDatabase.class){
                if(INSTANCE==null){
                    INSTANCE= Room.databaseBuilder(context.getApplicationContext(),
                            TaskRoomDatabase.class,NAME_OF_DATABASE)
                            .addCallback(sRoomDatabase)
                            .build();
                }
            }
        }
        return INSTANCE;
    }
    private static  final RoomDatabase.Callback sRoomDatabase=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            databaseWriterExecutor.execute(()->{
                //invoke dao and write
                TaskDao dao=INSTANCE.taskDao();
                //first clean the slate/database
                dao.deleteAll();
                //now write
                //dao.insert(new Task("Finish Todoister",));
            });
        }
    };



}
