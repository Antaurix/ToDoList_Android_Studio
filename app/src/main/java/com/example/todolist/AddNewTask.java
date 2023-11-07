package com.example.todolist;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.todolist.Model.ToDoModel;
import com.example.todolist.Utils.DataBaseHelper2;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class AddNewTask extends BottomSheetDialogFragment {

    public static final String TAG = "ADDNewTask";

    private EditText mEditText;
    private Button mSaveButton;
    private DataBaseHelper2 myDB;

    // Radio Buttons
    private RadioGroup radioGroup;
    private RadioButton radioUrgent;
    private RadioButton radioNormal;
    private RadioButton radioNegligible;
    private String selectedPriority;

    public static AddNewTask newInstance()
    {
        return new AddNewTask();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.add_new_task,container,false);
        return v;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mEditText = view.findViewById(R.id.editext);
        mSaveButton = view.findViewById(R.id.button_save);
        myDB = new DataBaseHelper2(getActivity());
        boolean isUpdate = false;

        Bundle bundle = getArguments();
        if (bundle != null){
            isUpdate=true;
            String task = bundle.getString("task");
            mEditText.setText(task);

            if(task.length()>0){
                mSaveButton.setEnabled(false);
            }
        }
        mEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.toString().equals("")){
                    mSaveButton.setEnabled(false);
                    mSaveButton.setBackgroundColor(Color.DKGRAY);
                }
                else{
                    mSaveButton.setEnabled(true);
                    mSaveButton.setBackgroundColor(getResources().getColor(R.color.SaveBtnColor));
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        radioGroup = view.findViewById(R.id.radioGroup);
        radioUrgent = view.findViewById(R.id.radioUrgent);
        radioNormal = view.findViewById(R.id.radioNormal);
        radioNegligible = view.findViewById(R.id.radioNegligible);


        if (bundle != null) {
            String priority = bundle.getString("priority");
            if (priority != null) {
                if (priority.equals("urgent")) {
                    radioUrgent.setChecked(true);
                } else if (priority.equals("normal")) {
                    radioNormal.setChecked(true);
                } else if (priority.equals("negligible")) {
                    radioNegligible.setChecked(true);
                }
            }
        }


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                if (checkedId == R.id.radioUrgent) {
                    selectedPriority = "urgent";
                } else if (checkedId == R.id.radioNormal) {
                    selectedPriority = "normal";
                } else if (checkedId == R.id.radioNegligible) {
                    selectedPriority = "negligible";
                }
                mSaveButton.setEnabled(true);
            }
        });

        mSaveButton.setEnabled(false);

        final boolean finalIsUpdate = isUpdate;
        mSaveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String text = mEditText.getText().toString();

                // Show a message to the user if they try to save the task without choosing a priority level
                if (selectedPriority == null) {
                    Toast.makeText(getActivity(), "Please select a priority level for the task", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (finalIsUpdate){
                    myDB.updateTask(bundle.getInt("id"),text, selectedPriority);
                }
                else {
                    ToDoModel item = new ToDoModel();
                    item.setTask(text);
                    item.setStatus(0);
                    item.setPriority(selectedPriority);
                    myDB.insertTask(item);
                }
                dismiss();
            }
        });

    }

    @Override
    public void onDismiss(@NonNull DialogInterface dialog) {
        super.onDismiss(dialog);
        Activity activity = getActivity();
        if (activity instanceof OnDialogCloseListner){
            ((OnDialogCloseListner)activity).onDialogClose(dialog);
        }
    }

}




