package com.bawp.todoister;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bawp.todoister.arrayadapter.OnTaskClickListener;
import com.bawp.todoister.arrayadapter.RecyclerViewAdapter;
import com.bawp.todoister.model.SharedViewModel;
import com.bawp.todoister.model.Task;
import com.bawp.todoister.model.TaskViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import static com.bawp.todoister.R.string.deleted_succesfully_message;
import static com.bawp.todoister.R.string.restored_task_text;

public class MainActivity extends AppCompatActivity implements OnTaskClickListener {
    private TaskViewModel viewModel;
    private RecyclerView todoRecyclerView;
    private int counter;
    private RecyclerViewAdapter recyclerViewAdapter;
    private BottomSheetFragment bottomSheetFragment;
    private SharedViewModel sharedViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        viewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(TaskViewModel.class);

        sharedViewModel = new ViewModelProvider(this)
                .get(SharedViewModel.class);

        bottomSheetFragment = new BottomSheetFragment();
        ConstraintLayout constraintLayout = findViewById(R.id.bottomSheet);
        BottomSheetBehavior<ConstraintLayout> bottomSheetBehavior = BottomSheetBehavior.from(constraintLayout);
        bottomSheetBehavior.setPeekHeight(BottomSheetBehavior.STATE_HIDDEN);
        counter = 0;
        /*****************Recycler View setup start***************************************************/

        todoRecyclerView = findViewById(R.id.recycler_view);
        todoRecyclerView.setHasFixedSize(true);
        todoRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        viewModel.getAllTasks().observe(this, tasks -> {
            recyclerViewAdapter = new RecyclerViewAdapter(tasks, MainActivity.this, this);
            todoRecyclerView.setAdapter(recyclerViewAdapter);
        });

        /************************Recycler View setup ends here*****************************************/

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(view -> {
            showBottomSheetDialog();
        });

    }

    private void showBottomSheetDialog() {
        bottomSheetFragment.show(getSupportFragmentManager(), bottomSheetFragment.getTag());
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i=new Intent(MainActivity.this,AboutActivity.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTaskClick(int adapterPosition, Task task) {
        //Log.d("TAG", "onTaskClick: "+task);
        sharedViewModel.setSelectedItem(task);
        //Log.d("TAG", "onTaskClick: "+task);
        sharedViewModel.setEdit(true);

        showBottomSheetDialog();
    }

    @Override
    public void onRadioButtonClick(Task task) {
        AlertDialog.Builder builder;
        builder=new AlertDialog.Builder(this);
        builder.setTitle("Are You Sure?").setCancelable(false);

        builder.setPositiveButton("Delete", (dialogInterface, i) -> {
            TaskViewModel.delete(task);
            recyclerViewAdapter.notifyDataSetChanged();
            Snackbar.make(todoRecyclerView, deleted_succesfully_message, Snackbar.LENGTH_SHORT).show();
        });
        builder.setNegativeButton("Cancel", (dialogInterface, i) -> {
            Toast.makeText(this, restored_task_text, Toast.LENGTH_LONG).show();
            finish();
            startActivity(getIntent());
        });

        builder.show();
    }
}