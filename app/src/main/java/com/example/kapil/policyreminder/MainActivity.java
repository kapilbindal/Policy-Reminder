package com.example.kapil.policyreminder;

import android.Manifest;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.Toast;

import com.example.kapil.policyreminder.adapters.RecordAdapter;
import com.example.kapil.policyreminder.db.RecordDatabaseHelper;
import com.example.kapil.policyreminder.db.RecordTable;
import com.example.kapil.policyreminder.model.Record;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends AppCompatActivity {
    RecyclerView rvRecordsList;
    RecordAdapter recordAdapter;
    public static final String TAG = "ht";
    ArrayList<Record> records = null;
    Button btnSelect;

    @Override
    protected void onStart() {
        super.onStart();
        RecordDatabaseHelper myDbHelper = new RecordDatabaseHelper(this);
        final SQLiteDatabase writeDb = myDbHelper.getWritableDatabase();
        records = RecordTable.getAllRecords(writeDb);
        recordAdapter = new RecordAdapter(records);
        rvRecordsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        rvRecordsList.setAdapter(recordAdapter);
        recordAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rvRecordsList = findViewById(R.id.rvRecordsList);
        btnSelect = findViewById(R.id.btnSelectFile);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        int perm = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.SEND_SMS);
        int perm2 = ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE);
        if (perm == PackageManager.PERMISSION_GRANTED && perm2 == PackageManager.PERMISSION_GRANTED){

            final RecordDatabaseHelper myDbHelper = new RecordDatabaseHelper(this);
            final SQLiteDatabase writeDb = myDbHelper.getWritableDatabase();

            ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                @Override
                public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                    return false;
                }
                @Override
                public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                    int position = viewHolder.getAdapterPosition() + 1;
                    Log.e(TAG, "onSwiped: " + position );
                    int demoId = RecordTable.getRecord(position,writeDb).getId();
                    Log.e(TAG, "onSwiped: " + demoId );
                    RecordTable.deleteRecord(writeDb, demoId);
                    Log.e(TAG, "onSwiped: " + records.size() );
                    records.remove(viewHolder.getAdapterPosition());
                    records.trimToSize();
                    recordAdapter.notifyDataSetChanged();
                }
            });

            itemTouchHelper.attachToRecyclerView(rvRecordsList);
            btnSelect.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    /*
                    File file = new File(Environment.getExternalStorageDirectory(),"myfile.csv");
                    String line = "" ;
                    try {
                        FileInputStream fin = new FileInputStream(file);
                        BufferedReader reader = new BufferedReader(
                                new InputStreamReader(fin, Charset.forName("UTF-8"))
                        );
                        while ((line = reader.readLine()) != null) {
                            String[] tokens = line.split(",");
                            Record record = new Record(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]);
                            int ID = (int) RecordTable.addRecord(record,writeDb);

                            String[] splitdate = record.getExpiryDate().split("-");
                            Log.e(TAG, "onClick: " + splitdate[0] + " " + splitdate[1] +  " " + splitdate[2] );
                            Calendar myCalender = Calendar.getInstance();
                            myCalender.set(Calendar.HOUR_OF_DAY, 10);
                            myCalender.set(Calendar.MINUTE, 30);
                            myCalender.set(Calendar.SECOND, 0);

                            int demoMonth = Integer.parseInt(splitdate[1]) - 1 ;
                            int demoDate =  Integer.parseInt(splitdate[0]) - 15;
                            if(demoDate <=0){
                                demoDate = demoDate + 30;
                                demoMonth = demoMonth - 1;
                                if(demoMonth<0) demoMonth = demoMonth + 12;
                            }
                            myCalender.set(Calendar.YEAR, Integer.parseInt(splitdate[2]));
                            myCalender.set(Calendar.MONTH, demoMonth );
                            myCalender.set(Calendar.DAY_OF_MONTH, demoDate);

                            Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                            intent.putExtra("ID", ID);
                            intent.putExtra("Name", record.getName());
                            intent.putExtra("NUMBER", record.getMobileNum());
                            intent.putExtra("POLICYNUM", record.getPolicyNum());

                            PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ID, intent, 0);
                            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                            alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);
                        }
                    }catch (IOException e){
                        Log.e(TAG, "onClick: " + e );
                        e.printStackTrace();
                    }
                    records = RecordTable.getAllRecords(writeDb);
                    recordAdapter = new RecordAdapter(records);
                    rvRecordsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                    rvRecordsList.setAdapter(recordAdapter);
                    recordAdapter.notifyDataSetChanged();*/
                }
            });

            FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   startActivity(new Intent(MainActivity.this,AddNewActivity.class));
                }
            });
        }
        else{
            ActivityCompat.requestPermissions(MainActivity.this, // request permission to be granted
                    new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE},
                    44);
        }
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if(requestCode == 44){
            if(grantResults[0] == PackageManager.PERMISSION_GRANTED){
                final RecordDatabaseHelper myDbHelper = new RecordDatabaseHelper(this);
                final SQLiteDatabase writeDb = myDbHelper.getWritableDatabase();
                final SQLiteDatabase readDb = myDbHelper.getReadableDatabase();

                records = RecordTable.getAllRecords(readDb);
                rvRecordsList = findViewById(R.id.rvRecordsList);
                recordAdapter = new RecordAdapter(records);
                rvRecordsList.setLayoutManager(new LinearLayoutManager(this));
                rvRecordsList.setAdapter(recordAdapter);

                ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {
                    @Override
                    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                        return false;
                    }

                    @Override
                    public void onSwiped(RecyclerView.ViewHolder viewHolder, int swipeDir) {
                        int position = viewHolder.getAdapterPosition() + 1;
                        int demoId = RecordTable.getRecord(position,writeDb).getId();
                        //Log.e(TAG, "onSwiped: " + demoPolicyNum );
                        RecordTable.deleteRecord(writeDb,demoId);
                        //Log.e(TAG, "onSwiped: " + writeDb.toString() );
                        records.remove(viewHolder.getAdapterPosition());
                        records.trimToSize();
                        //Log.d(TAG, "onSwiped: " + position);
                        recordAdapter.notifyDataSetChanged();
                    }
                });

                itemTouchHelper.attachToRecyclerView(rvRecordsList);

                btnSelect.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        File file = new File(Environment.getExternalStorageDirectory(),"myfile.csv");
                        String line = "" ;
                        try {
                            FileInputStream fin = new FileInputStream(file);
                            BufferedReader reader = new BufferedReader(
                                    new InputStreamReader(fin, Charset.forName("UTF-8"))
                            );
                            while ((line = reader.readLine()) != null) {
                                String[] tokens = line.split(",");
                                Record record = new Record(tokens[0],tokens[1],tokens[2],tokens[3],tokens[4],tokens[5],tokens[6]);
                                int ID = (int) RecordTable.addRecord(record,writeDb);

                                String[] splitdate = record.getExpiryDate().split("-");
                                Log.e(TAG, "onClick: " + splitdate[0] + " " + splitdate[1] +  " " + splitdate[2] );
                                Calendar myCalender = Calendar.getInstance();
                                myCalender.set(Calendar.HOUR_OF_DAY, 11);
                                myCalender.set(Calendar.MINUTE, 55);
                                myCalender.set(Calendar.SECOND, 0);

                                int demoMonth = Integer.parseInt(splitdate[1]) - 1 ;
                                int demoDate =  Integer.parseInt(splitdate[0]) - 15;
                                if(demoDate <=0){
                                    demoDate = demoDate + 30;
                                    demoMonth = demoMonth - 1;
                                    if(demoMonth<0) demoMonth = demoMonth + 12;
                                }
                                myCalender.set(Calendar.YEAR, Integer.parseInt(splitdate[2]));
                                myCalender.set(Calendar.MONTH, demoMonth );
                                myCalender.set(Calendar.DAY_OF_MONTH, demoDate);

                                Intent intent = new Intent(MainActivity.this, AlarmReceiver.class);
                                intent.putExtra("ID", ID);
                                intent.putExtra("Name", record.getName());
                                intent.putExtra("NUMBER", record.getMobileNum());
                                intent.putExtra("POLICYNUM", record.getPolicyNum());

                                PendingIntent pendingIntent = PendingIntent.getBroadcast(MainActivity.this, ID, intent, 0);
                                AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
                                alarmManager.set(AlarmManager.RTC_WAKEUP, myCalender.getTimeInMillis(), pendingIntent);
                            }
                        }catch (IOException e){
                            Log.e(TAG, "onClick: " + e );
                            e.printStackTrace();
                        }
                        records = RecordTable.getAllRecords(writeDb);
                        recordAdapter = new RecordAdapter(records);
                        rvRecordsList.setLayoutManager(new LinearLayoutManager(MainActivity.this));
                        rvRecordsList.setAdapter(recordAdapter);
                        recordAdapter.notifyDataSetChanged();
                    }
                });

                FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
                fab.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        startActivity(new Intent(MainActivity.this,AddNewActivity.class));
                        }
                });
            }else{
                Toast.makeText(this, "Sending SMS require this permission", Toast.LENGTH_SHORT).show();
                new AlertDialog.Builder(this).setMessage("We need this permission to send SMS.\n" +
                        "Please allow this permission").setPositiveButton("Give Permission", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},
                                44
                        );
                    }
                }).setNegativeButton("No Thanks", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        ActivityCompat.requestPermissions(MainActivity.this,
                                new String[]{Manifest.permission.SEND_SMS,Manifest.permission.READ_EXTERNAL_STORAGE},
                                44
                        );
                    }
                }).create().show();
            }
        }
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
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
