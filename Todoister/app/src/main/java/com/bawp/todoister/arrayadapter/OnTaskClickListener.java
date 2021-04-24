package com.bawp.todoister.arrayadapter;

import com.bawp.todoister.model.Task;

public interface OnTaskClickListener {
    void onTaskClick(int adapterPosition, Task task);
    void onRadioButtonClick(Task task);
}
