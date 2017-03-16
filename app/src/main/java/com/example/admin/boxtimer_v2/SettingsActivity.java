package com.example.admin.boxtimer_v2;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

public class SettingsActivity extends AppCompatActivity implements EditTimeFragmentDialog.EditNameDialogListener {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_settings);
    }

    public void buttonClicked(View view){
        FragmentManager fm = getSupportFragmentManager();
        EditTimeFragmentDialog alertDialog = EditTimeFragmentDialog.newInstance("Some title");
        alertDialog.show(fm, "fragment_alert");
    }

    @Override
    public void onFinishEditDialog(String inputText) {
        Toast.makeText(this, "Hi, " + inputText, Toast.LENGTH_SHORT).show();

    }
}
