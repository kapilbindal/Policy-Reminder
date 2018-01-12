package com.example.kapil.policyreminder;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import com.example.kapil.policyreminder.db.RecordDatabaseHelper;
import com.example.kapil.policyreminder.db.RecordTable;
import com.example.kapil.policyreminder.model.Record;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class AddNewActivity extends AppCompatActivity {
    Calendar myCalender = Calendar.getInstance();
    public EditText edName,edPolNum,edExpDate,edComp,edPolType,edMobNum,edEmail;
    Button btnAdd;
    private int mYear, mMonth, mHour, mMinute,mDay;
    private String mDate;
    public static final String TAG = "RECORD";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new);
        edName = findViewById(R.id.edName);
        edPolNum = findViewById(R.id.edPolNum);
        edExpDate = findViewById(R.id.edExpDate);
        edComp = findViewById(R.id.edComp);
        edPolType = findViewById(R.id.edPolType);
        edMobNum = findViewById(R.id.edMobNum);
        edEmail = findViewById(R.id.edEmail);
        btnAdd = findViewById(R.id.btnAdd);

        final DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int date) {
                myCalender.set(Calendar.YEAR,year);
                myCalender.set(Calendar.MONTH,month);
                myCalender.set(Calendar.DAY_OF_MONTH,date);
                updateLabel();
            }
        };
        edExpDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(AddNewActivity.this,date,
                        myCalender.get(Calendar.YEAR),
                        myCalender.get(Calendar.MONTH),
                        myCalender.get(Calendar.DAY_OF_MONTH)
                ).show();
            }
        });

        RecordDatabaseHelper myDbHelper = new RecordDatabaseHelper(this);
        final SQLiteDatabase writeDb = myDbHelper.getWritableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int ID = (int) RecordTable.addRecord(new Record(0,
                                edName.getText().toString(),
                                edPolNum.getText().toString(),
                                edExpDate.getText().toString(),
                                edComp.getText().toString(),
                                edPolType.getText().toString(),
                                edMobNum.getText().toString(),
                                edEmail.getText().toString()),
                        writeDb);
                //Log.d(TAG, "onClick: " + ID);

                myCalender.set(Calendar.HOUR_OF_DAY,18);
                myCalender.set(Calendar.MINUTE,44);
                myCalender.set(Calendar.SECOND,0);

                Intent intent = new Intent(AddNewActivity.this, AlarmReceiver.class);
                intent.putExtra("Name",edName.getText().toString());

                PendingIntent pendingIntent = PendingIntent.getBroadcast(AddNewActivity.this, ID, intent, 0);
                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);

                Intent i = new Intent(AddNewActivity.this,MainActivity.class);
                startActivity(i);
            }
        });
    }
    private void updateLabel(){
        String myFormat = "dd/MM/yy";
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);
        edExpDate.setText(sdf.format(myCalender.getTime()));
    }
}
