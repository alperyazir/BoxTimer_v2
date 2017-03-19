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

    private boolean isDialogRound = false;
    private Button buttonIncreaseMin;
    private Button buttonIncreaseSec;
    private Button buttonDecreaseMin;
    private Button buttonDecreaseSec;
    private Button buttonDone;
    private EditText editTextMin;
    private EditText editTextSec;

    public int round = 0;
    public int min = 0;
    public int sec = 0;
    public int totalTime = 0;


    // 1. Defines the listener interface with a method passing back data result.
    public interface EditNameDialogListener {
        void onFinishEditDialog(String inputText);
    }

    // Call this method to send the data back to the parent fragment
    public void sendBackResult() {
        // Notice the use of `getTargetFragment` which will be set when the dialog is displayed
        EditNameDialogListener listener = (EditNameDialogListener) getTargetFragment();
        listener.onFinishEditDialog(buttonIncreaseMin.getText().toString());
        dismiss();
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

    public void setTime(int mi,int se){
        min = mi;
        sec = se;
   }

    public void setTime(int mi){
        min = mi;
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

        buttonIncreaseMin = (Button)    view.findViewById(R.id.buttonIncreaseMin);
        buttonIncreaseSec = (Button)    view.findViewById(R.id.buttonIncreaseSec);
        buttonDecreaseMin = (Button)    view.findViewById(R.id.buttonDecreaseMin);
        buttonDecreaseSec = (Button)    view.findViewById(R.id.buttonDecreaseSec);

        editTextMin.setText(String.format("%02d", min));
        editTextSec.setText(String.format("%02d", sec));

        if(isDialogRound){
            editTextSec.setVisibility(view.GONE);
            buttonDecreaseSec.setVisibility(view.GONE);
            buttonIncreaseSec.setVisibility(view.GONE);
        }


        // Fetch arguments from bundle and set title
        String title = getArguments().getString("title", "Enter Name");
        getDialog().setTitle(title);

        buttonDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditNameDialogListener listener = (EditNameDialogListener) getActivity();
                totalTime = Integer.parseInt(editTextMin.getText().toString())*60 + Integer.parseInt(editTextSec.getText().toString());
                listener.onFinishEditDialog(""+totalTime);
                dismiss();
            }
        });


        buttonIncreaseMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(++min > 59){
                    min = 0;
                }
                    editTextMin.setText(String.format("%02d", min));
            }
        });

        buttonIncreaseSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(++sec > 59){
                    sec = 0;
                }
            editTextSec.setText(String.format("%02d", sec));
            }
        });

        buttonDecreaseMin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--min < 0){
                    min = 59;
                }

                editTextMin.setText(String.format("%02d", min));
            }
        });

        buttonDecreaseSec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(--sec < 0){
                    sec = 59;
                }
                editTextSec.setText(String.format("%02d", sec));
            }
        });

    }

    public void isRound(boolean b){
        isDialogRound = b;
    }






}
