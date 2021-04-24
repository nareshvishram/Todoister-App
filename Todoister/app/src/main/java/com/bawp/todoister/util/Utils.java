package com.bawp.todoister.util;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.Task;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String formattedDate(Date date) {
        SimpleDateFormat simpleDateFormat = (SimpleDateFormat) SimpleDateFormat.getDateInstance();
        simpleDateFormat.applyPattern("EEE,MMM d");
        return simpleDateFormat.format(date);
    }

    public static void hideSoftKeyBoard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) view.getContext().getSystemService(
                Context.INPUT_METHOD_SERVICE
        );
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

    }

    public static int priorityColor(Task task) {
        Priority priority = task.getPriority();
        int color;
        if (priority == Priority.HIGH) {
            color = Color.argb(200, 201, 21, 23);
        } else if (priority == Priority.MEDIUM) {
            color = Color.argb(200, 155, 179, 0);
        } else {
            color = Color.argb(200, 51, 181, 129);
        }
        return color;
    }
}
