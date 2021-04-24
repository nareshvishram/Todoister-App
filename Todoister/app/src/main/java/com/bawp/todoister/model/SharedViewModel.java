package com.bawp.todoister.model;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class SharedViewModel extends ViewModel {
    private final MutableLiveData<Task> selectedItem = new MutableLiveData<>();
    private boolean isEdit;

    public boolean getIsEdit() {
        return isEdit;
    }

    public void setEdit(boolean edit) {
        this.isEdit = edit;
    }

    public LiveData<Task> getSelectedItem() {
        return selectedItem;
    }

    public void setSelectedItem(Task task) {
        //Log.d("TAG", "setSelectedItem: "+task);
        selectedItem.setValue(task);
    }
}
