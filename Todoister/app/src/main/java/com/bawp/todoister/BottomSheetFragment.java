package com.bawp.todoister;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.Group;
import androidx.lifecycle.ViewModelProvider;

import com.bawp.todoister.model.Priority;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.bawp.todoister.util.Utils;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.chip.Chip;
import com.google.android.material.snackbar.Snackbar;

import java.util.Calendar;
import java.util.Date;

public class BottomSheetFragment extends BottomSheetDialogFragment implements View.OnClickListener {
    private EditText taskName;
    private ImageButton calenderButton;
    //for priority radio button and their groups
    private ImageButton priorityButton;
    private RadioGroup priorityRadioGroup;
    private RadioButton selectedRadioButton;
    private int selectedButtonId;

    private ImageButton saveButton;
    private CalendarView calendarView;

    private Date dueDate;
    private Calendar calendar = Calendar.getInstance();

    private SharedViewModel sharedViewModel;
    //some views are grouped so that we don't have to get all id's manually
    private Group calendarGroup;
    private boolean isEdit;

    private Priority priority;

    public BottomSheetFragment() {
    }

    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.bottom_sheet, container, false);
        calenderButton = view.findViewById(R.id.today_calendar_button);
        calendarGroup = view.findViewById(R.id.calendar_group);
        calendarView = view.findViewById(R.id.calendar_view);
        priorityButton = view.findViewById(R.id.priority_todo_button);
        priorityRadioGroup = view.findViewById(R.id.radioGroup_priority);
        taskName = view.findViewById(R.id.enter_todo_et);
        saveButton = view.findViewById(R.id.save_todo_button);

        Chip todayChip = view.findViewById(R.id.today_chip);
        todayChip.setOnClickListener(this);
        Chip tomorrowChip = view.findViewById(R.id.tomorrow_chip);
        tomorrowChip.setOnClickListener(this);
        Chip nextWeekChip = view.findViewById(R.id.next_week_chip);
        nextWeekChip.setOnClickListener(this);
        sharedViewModel = new ViewModelProvider(requireActivity())
                .get(SharedViewModel.class);
        resetForm();
        return view;
    }

    private void resetForm() {
        if (!sharedViewModel.getIsEdit()) {
            taskName.setText("");
            Log.d("TAG", "onCreateView: heyyyyyy");
            priorityRadioGroup.clearCheck();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (sharedViewModel.getSelectedItem().getValue() != null) {
            Task selectedTask = sharedViewModel.getSelectedItem().getValue();
            taskName.setText("");
            taskName.setText(selectedTask.getTask().trim());
            Log.d("TAG", "onViewCreated: From activity to fragment" + selectedTask);
        }

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        isEdit = sharedViewModel.getIsEdit();
        saveButton.setOnClickListener(view1 -> {
            String task = taskName.getText().toString().trim();
            if (!TextUtils.isEmpty(task) && dueDate != null && priority != null) {

                if (isEdit) {
                    //entered task name will be same
                    //task that we are updating is in sharedViewModel
                    Task updatedTask = sharedViewModel.getSelectedItem().getValue();
                    assert updatedTask != null;
                    updatedTask.setTask(task);
                    updatedTask.setCreatedDate(Calendar.getInstance().getTime());
                    updatedTask.setDueDate(dueDate);
                    updatedTask.setPriority(priority);

                    updatedTask.setDone(false);
                    TaskViewModel.update(updatedTask);
                } else {
                    Task myTask = new Task(task, priority,
                            dueDate,
                            Calendar.getInstance().getTime(),
                            false);
                    TaskViewModel.insert(myTask);
                }
            } else {
                Snackbar.make(saveButton, R.string.empty_task_stext, Snackbar.LENGTH_SHORT).show();
            }

            if (this.isVisible()) {
                dismiss();

            }
        });
        calenderButton.setOnClickListener(view12 -> {
            //toggle -> if already visible then make invisible otherwise make visible
            calendarGroup.setVisibility(calendarGroup.getVisibility() == View.GONE ? View.VISIBLE : View.GONE);
            Utils.hideSoftKeyBoard(view12);
        });
        calendarView.setOnDateChangeListener((calendarView, year, month, dayOfMonth) -> {
            calendar.clear();
            calendar.set(year, month, dayOfMonth);
            dueDate = calendar.getTime();

        });

        priorityButton.setOnClickListener(view13 -> {
            priorityRadioGroup.setVisibility(
                    priorityRadioGroup.getVisibility() == View.VISIBLE ? View.GONE : View.VISIBLE
            );

            Utils.hideSoftKeyBoard(view13);
            priorityRadioGroup.setOnCheckedChangeListener((radioGroup, selectedRadioButtonId) -> {
                if (priorityRadioGroup.getVisibility() == View.VISIBLE) {

                    if (selectedRadioButtonId == R.id.radioButton_high) {
                        priority = Priority.HIGH;
                    } else if (selectedRadioButtonId == R.id.radioButton_med) {
                        priority = Priority.MEDIUM;
                    } else {
                        priority = Priority.LOW;
                    }
                } else {
                    priority = Priority.LOW;
                }

            });
        });

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.today_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 0);
            dueDate = calendar.getTime();
            Log.d("DUE_DATE", "onViewCreated: " + dueDate.toString());
        } else if (id == R.id.tomorrow_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            dueDate = calendar.getTime();
            Log.d("DUE_DATE", "onViewCreated: " + dueDate.toString());
        } else if (id == R.id.next_week_chip) {
            calendar.add(Calendar.DAY_OF_YEAR, 7);
            dueDate = calendar.getTime();
            Log.d("DUE_DATE", "onViewCreated: " + dueDate.toString());
        }

    }
}