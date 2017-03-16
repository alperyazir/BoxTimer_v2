package com.example.admin.boxtimer_v2;

import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;


public class EditTimeFragmentDialog extends DialogFragment {


    private Button buttonIncreaseMin;
    private Button buttonIncreaseSec;
    private Button buttonDecreaseMin;
    private Button buttonDecreaseSec;
    private Button buttonDone;
    private EditText editTextMin;
    private EditText editTextSec;

    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }


    public EditTimeFragmentDialog() {
    }

    public static EditTimeFragmentDialog newInstance(String title) {
        EditTimeFragmentDialog frag = new EditTimeFragmentDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        frag.setArguments(args);
        return frag;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_edit_time_fragment_dialog, container);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // Get field from view
        editTextMin = (EditText) view.findViewById(R.id.editTextDialogMin);
        editTextSec = (EditText) view.findViewById(R.id.editTextDialogSec);
        buttonDone = (Button)    view.findViewById(R.id.buttonDialogDone);
        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                listener.onFinishEditDialog(editTextMin.getText().toString() +":"+ editTextSec.getText().toString() );
                dismiss();
            }
        });

    }

}
