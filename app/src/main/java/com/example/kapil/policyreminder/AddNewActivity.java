package com.example.kapil.policyreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.kapil.policyreminder.db.RecordDatabaseHelper;
import com.example.kapil.policyreminder.db.RecordTable;
import com.example.kapil.policyreminder.model.Record;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddNewActivity extends AppCompatActivity {
    Calendar myCalender = Calendar.getInstance();
    Calendar myTime = Calendar.getInstance();
    public EditText edName,edPolNum,edExpDate,edComp,edPolType,edMobNum,edEmail,edTime;
    Button btnAdd;
    public static final String TAG = "RECORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        edName = findViewById(R.id.edName);
        edPolNum = findViewById(R.id.edPolNum);
        edExpDate = findViewById(R.id.edExpDate);
        edTime = findViewById(R.id.edTime);
        edComp = findViewById(R.id.edComp);
        edPolType = findViewById(R.id.edPolType);
        edMobNum = findViewById(R.id.edMobNum);
        edEmail = findViewById(R.id.edEmail);
        btnAdd = findViewById(R.id.btnAdd);

        edTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final int hour = myCalender.get(Calendar.HOUR_OF_DAY);
                final int minute = myCalender.get(Calendar.MINUTE);
                final int sec = myCalender.get(Calendar.SECOND);
                new TimePickerDialog(AddNewActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        myCalender.set(Calendar.HOUR_OF_DAY, i);
                        myCalender.set(Calendar.MINUTE, i1);
                        myCalender.set(Calendar.SECOND, 0);
                        edTime.setText(i + ":" + i1 + ":" + sec);
                    }
                }, hour, minute, false).show();
            }
        });

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                myCalender.set(Calendar.YEAR, year);
                myCalender.set(Calendar.MONTH, month);
                myCalender.set(Calendar.DAY_OF_MONTH, date);
                updateLabel();
            }
        };
        edExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(AddNewActivity.this, date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });
        final String name = edName.getText().toString();
        final String polNum = edPolNum.getText().toString();
        final String expDate = edExpDate.getText().toString();
        String company = edComp.getText().toString();
        String polType = edPolType.getText().toString();
        String mobNum = edMobNum.getText().toString();
        String email = edEmail.getText().toString();

        RecordDatabaseHelper myDbHelper = new RecordDatabaseHelper(this);
        final SQLiteDatabase writeDb = myDbHelper.getWritableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (!edPolNum.getText().toString().isEmpty() && !edName.getText().toString().isEmpty() && !edExpDate.getText().toString().isEmpty()
                        && !edComp.getText().toString().isEmpty() && !edPolType.getText().toString().isEmpty() && !edMobNum.getText().toString().isEmpty()
                        && !edEmail.getText().toString().isEmpty()) {
                    int ID = (int) RecordTable.addRecord(new Record(0,
                                    edName.getText().toString(),
                                    edPolNum.getText().toString(),
                                    edExpDate.getText().toString(),
                                    edComp.getText().toString(),
                                    edPolType.getText().toString(),
                                    edMobNum.getText().toString(),
                                    edEmail.getText().toString()),
                            writeDb);

                    Intent intent = new Intent(AddNewActivity.this, AlarmReceiver.class);
                    intent.putExtra("ID", ID);
                    intent.putExtra("Name", edName.getText().toString());
                    intent.putExtra("NUMBER", edMobNum.getText().toString());
                    intent.putExtra("POLICYNUM", edPolNum.getText().toString());

                    PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewActivity.this, ID, intent, 0);
                    AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                    alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);

                    Intent i = new Intent(AddNewActivity.this, MainActivity.class);
                    startActivity(i);
                }
                else{
                    Toast.makeText(AddNewActivity.this, "All Fields are necesssary!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edExpDate.setText(sdf.format(myCalender.getTime()));
    }
}
