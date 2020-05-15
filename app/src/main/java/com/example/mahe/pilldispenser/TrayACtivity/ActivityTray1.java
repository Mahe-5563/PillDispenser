package com.example.mahe.pilldispenser.TrayACtivity;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mahe.pilldispenser.ClassFiles.Tray;
import com.example.mahe.pilldispenser.R;
import com.example.mahe.pilldispenser.TraysActivity;
import com.google.gson.Gson;

public class ActivityTray1 extends AppCompatActivity {


    CheckBox cbmornbf, cbmornaf, cbaftbf, cbaftaf, cbnightbf, cbnightaf;
    Button save;
    ImageButton edit;
    TextView tabname;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;



    Tray t1, tr1, tr2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tray1);
        if (getSupportActionBar() != null) getSupportActionBar().setTitle("Tray 1");

        cbmornbf = findViewById(R.id.cb_morn_bf);
        cbmornaf = findViewById(R.id.cb_morn_af);
        cbaftbf = findViewById(R.id.cb_aft_bf);
        cbaftaf = findViewById(R.id.cb_aft_af);
        cbnightbf = findViewById(R.id.cb_night_bf);
        cbnightaf = findViewById(R.id.cb_night_af);
        save = findViewById(R.id.btn_save);
        edit = findViewById(R.id.imgbtn_edit);
        tabname = findViewById(R.id.tv_tab_name);


        sharedPreferences = getSharedPreferences(getString(R.string.SharedPreferencesDBNAME), MODE_PRIVATE);
        editor = sharedPreferences.edit();

        t1 = new Tray();

        //Checking for tablets, if they are set.
        Gson gson = new Gson();
        String json = sharedPreferences.getString(getString(R.string.tray1), "");
        //String json2 = sharedPreferences.getString(getString(R.string.tray2), "");
        //Toast.makeText(this, json, Toast.LENGTH_SHORT).show();
        tr1 = gson.fromJson(json, Tray.class);
        //tr2 = gson.fromJson(json2, Tray.class);


        /*Statement to check if the checkboxes are null. If they are null, then proceed to filling the values
         else, display the filled values. */

        if(tr1 != null)
        {

            setChecked();

        }

        else
        {

            t1.setMorn_bef_food(0);
            t1.setMorn_aft_food(0);
            t1.setAftnoon_bef_food(0);
            t1.setAftnoon_aft_food(0);
            t1.setNight_bef_food(0);
            t1.setNight_aft_food(0);

        }
        //t1.setTab_name();


        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                editTextPrompt();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int a = valueAssignment();

                if (a != -1) {
                    doIt();
                }

            }
        });


    }

    private void doIt() {

        Tray myt = new Tray();

        myt.setTab_name(tabname.getText().toString());

        myt.setNight_aft_food(0);
        myt.setNight_bef_food(0);

        myt.setAftnoon_aft_food(0);
        myt.setAftnoon_bef_food(0);

        myt.setMorn_aft_food(0);
        myt.setMorn_bef_food(0);


        int count = 0;
        if (cbmornbf.isChecked()) {
            myt.setMorn_bef_food(1);
            count++;
        }

        if (cbmornaf.isChecked()) {
            myt.setMorn_aft_food(1);
            count++;
        }

        if (cbaftbf.isChecked()) {
            myt.setAftnoon_bef_food(1);
            count++;
        }

        if (cbaftaf.isChecked()) {
            myt.setAftnoon_aft_food(1);
            count++;
        }

        if (cbnightbf.isChecked()) {
            myt.setNight_bef_food(1);
            count++;
        }

        if (cbnightaf.isChecked()) {
            myt.setNight_aft_food(1);
            count++;
        }


        Gson gson = new Gson();
        String json = gson.toJson(myt);
        editor.putString(getString(R.string.tray1), json);
        editor.apply();
        Toast.makeText(this, "STORED: " + t1.getTab_name(), Toast.LENGTH_SHORT).show();
        startActivity(new Intent(ActivityTray1.this, TraysActivity.class));
        finish();

    }

    private int valueAssignment() {
        int count = 0;


        if (cbmornbf.isChecked()) {
            t1.setMorn_bef_food(1);
            count++;
        }

        if (cbmornaf.isChecked()) {
            t1.setMorn_aft_food(1);
            count++;
        }

        if (cbaftbf.isChecked()) {
            t1.setAftnoon_bef_food(1);
            count++;
        }

        if (cbaftaf.isChecked()) {
            t1.setAftnoon_aft_food(1);
            count++;
        }

        if (cbnightbf.isChecked()) {
            t1.setNight_bef_food(1);
            count++;
        }

        if (cbnightaf.isChecked()) {
            t1.setNight_aft_food(1);
            count++;
        }

        if (count == 0) {
            Toast.makeText(this, "PLease select a Value", Toast.LENGTH_SHORT).show();

            return -1;
        }

        return 0;
    }

    private void editTextPrompt() {

        LayoutInflater li = LayoutInflater.from(this);
        View promptsView = li.inflate(R.layout.edittextprompt, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);

        alertDialogBuilder.setView(promptsView);

        final EditText userInput = promptsView
                .findViewById(R.id.et_tab_name);


        alertDialogBuilder
                .setCancelable(false)
                .setPositiveButton("SAVE",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                String text = userInput.getText().toString().trim();

                                t1.setTab_name(text);
                                setTabNAme(text);

                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }

    @SuppressLint("SetTextI18n")
    private void setTabNAme(String text) {

        //assert text != null;
        if (text != null) {

            tabname.setText(text);
            tabname.setVisibility(View.VISIBLE);

        } else {
            tabname.setText("Tablet Name");
        }
    }

    @Override
    protected void onStart() {

        // to set the Checkbox checked default if the values are present

        setChecked();

        super.onStart();
    }

    private void setChecked() {

        if(tr1.getMorn_bef_food() == 1)
        {
            cbmornbf.setChecked(true);
        }

        if(tr1.getMorn_aft_food() == 1)
        {
            cbmornaf.setChecked(true);
        }

        if(tr1.getAftnoon_bef_food() == 1)
        {
            cbaftbf.setChecked(true);
        }

        if(tr1.getAftnoon_aft_food() == 1)
        {
            cbaftaf.setChecked(true);
        }

        if(tr1.getNight_bef_food() == 1)
        {
            cbnightbf.setChecked(true);
        }

        if(tr1.getNight_aft_food() == 1)
        {
            cbnightaf.setChecked(true);
        }
        if(tr1.getTab_name() != null)
        {
            tabname.setText(tr1.getTab_name());
        }

    }
}
