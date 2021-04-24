package com.bawp.todoister.arrayadapter;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.widget.AppCompatRadioButton;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.R;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.util.Utils;
import com.google.android.material.chip.Chip;

import java.util.List;
import java.util.Objects;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {
    private static OnTaskClickListener onTaskClickListener;
    private final List<Task> allTasks;
    private final Context context;

    public RecyclerViewAdapter(List<Task> allTasks, Context context, OnTaskClickListener onTaskClickListener) {
        this.allTasks = allTasks;
        this.context = context;
        RecyclerViewAdapter.onTaskClickListener = onTaskClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.todo_row, parent, false);
        return new ViewHolder(v);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Task task = allTasks.get(position);
        holder.task.setText(task.getTask().trim());
        holder.todayChip.setText(Utils.formattedDate(task.getDueDate()));

        ColorStateList colorStateList=new ColorStateList(new int[][]{
                new int[]{-android.R.attr.state_enabled},
                new int[]{android.R.attr.state_enabled}
        },new int[]{
                Color.LTGRAY,//disabling
                Utils.priorityColor(task)
        });
        holder.todayChip.setTextColor(Utils.priorityColor(task));
        holder.todayChip.setChipIconTint(colorStateList);
        holder.isDoneCheck.setButtonTintList(colorStateList);
    }

    @Override
    public int getItemCount() {
        return Objects.requireNonNull(allTasks.size());
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public AppCompatTextView task;
        public AppCompatRadioButton isDoneCheck;
        public Chip todayChip;
        OnTaskClickListener onTaskClickListener;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            task = itemView.findViewById(R.id.todo_row_todo);
            isDoneCheck = itemView.findViewById(R.id.todo_radio_button);
            todayChip = itemView.findViewById(R.id.todo_row_chip);
            this.onTaskClickListener = RecyclerViewAdapter.onTaskClickListener;
            isDoneCheck.setOnClickListener(this);
            itemView.setOnClickListener(this);
        }


        @Override
        public void onClick(View view) {
            int id = view.getId();
            if (id == R.id.todo_radio_button) {
                Log.d("TAG", "onClick: radio" + allTasks.get(getAdapterPosition()));
                onTaskClickListener.onRadioButtonClick(allTasks.get(getAdapterPosition()));

            } else if (id == R.id.todo_row_layout) {
                onTaskClickListener.onTaskClick(getAdapterPosition(), allTasks.get(getAdapterPosition()));
            }
        }
    }
}
