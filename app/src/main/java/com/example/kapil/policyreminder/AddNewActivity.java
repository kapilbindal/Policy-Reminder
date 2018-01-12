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
import java.util.Calendar;
import java.util.Locale;

public class AddNewActivity extends AppCompatActivity {
    Calendar myCalender = Calendar.getInstance();
    public EditText edName,edPolNum,edExpDate,edComp,edPolType,edMobNum,edEmail;
    Button btnAdd;
    long ID;
    private int mYear, mMonth, mHour, mMinute,mDay;
    private String mDate;
    public static final String TAG = "RECORD";
    PendingIntent pendingIntent;
    AlarmManager alarmManager;

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

        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);

        mYear = myCalender.get(Calendar.YEAR);
        mMonth = myCalender.get(Calendar.MONTH);
        mDay = myCalender.get(Calendar.DAY_OF_MONTH);

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
            //Calendar calendar = Calendar.getInstance();
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
        SQLiteDatabase readDb = myDbHelper.getReadableDatabase();

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ID = RecordTable.addRecord(new Record(0,
                                edName.getText().toString(),
                                edPolNum.getText().toString(),
                                edExpDate.getText().toString(),
                                edComp.getText().toString(),
                                edPolType.getText().toString(),
                                edMobNum.getText().toString(),
                                edEmail.getText().toString()),
                        writeDb);

                myCalender.set(Calendar.HOUR_OF_DAY,13);
                myCalender.set(Calendar.MINUTE,58);
                myCalender.set(Calendar.SECOND,0);
//                myCalender.set(Calendar.YEAR,mYear);
//                myCalender.set(Calendar.MONTH,mMonth);
//                myCalender.set(Calendar.DAY_OF_MONTH,mDay);

                Intent intent = new Intent(AddNewActivity.this, AlarmReceiver.class);
                pendingIntent = PendingIntent.getBroadcast(AddNewActivity.this, 0, intent, 0);

                alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), 10000, pendingIntent);

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
