package com.example.kapil.policyreminder.db;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.kapil.policyreminder.model.Record;

import java.util.ArrayList;

import static com.example.kapil.policyreminder.AddNewActivity.TAG;
import static com.example.kapil.policyreminder.db.Consts.*;

/**
 * Created by KAPIL on 09-01-2018.
 */

public class RecordTable {
    private RecordTable(){}
    public static final String TABLE_NAME = "Records";
    public interface Columns{
        String ID = "id";
        String NAME = "name";
        String POLIC_NUM = "policyNum";
        String EXPIRY_DATE = "expiryDate";
        String COMPANY = "company";
        String TYPE = "type";
        String MOBILE_NUM = "mobileNum";
        String EMAIL = "email";
    }
    public static final String CMD_CREATE_TABLE =
            CMD_CREATE_TABLE_INE +  TABLE_NAME
            + LBR
            + Columns.ID + TYPE_INT + TYPE_PK_AI + COMMA
            + Columns.NAME + TYPE_TEXT + COMMA
            + Columns.POLIC_NUM + TYPE_TEXT + COMMA
                    + Columns.EXPIRY_DATE + TYPE_TEXT + COMMA
                    + Columns.COMPANY + TYPE_TEXT + COMMA
                    + Columns.TYPE + TYPE_TEXT + COMMA
                    + Columns.MOBILE_NUM + TYPE_TEXT + COMMA
                    + Columns.EMAIL + TYPE_TEXT
                    + RBR + SEMI;
    public static long addRecord (Record record, SQLiteDatabase db) {
        ContentValues newRecord = new ContentValues();
        newRecord.put(Columns.NAME,record.getName());
        newRecord.put(Columns.POLIC_NUM, record.getPolicyNum());
        newRecord.put(Columns.EXPIRY_DATE, record.getExpiryDate());
        newRecord.put(Columns.COMPANY, record.getCompany());
        newRecord.put(Columns.TYPE, record.getType());
        newRecord.put(Columns.MOBILE_NUM, record.getMobileNum());
        newRecord.put(Columns.EMAIL, record.getEmail());

        return db.insert(TABLE_NAME, null, newRecord);
    }
    public static void deleteRecord(SQLiteDatabase db, int id){
        db.delete(TABLE_NAME,"ID="+id,null);
        getTableAsString(db,TABLE_NAME);
    }

    public static  String getTableAsString(SQLiteDatabase db, String tableName) {
        Log.d(TAG, "getTableAsString called");
        String tableString = String.format("Table %s:\n", tableName);
        Cursor allRows  = db.rawQuery("SELECT * FROM " + tableName, null);
        if (allRows.moveToFirst() ){
            String[] columnNames = allRows.getColumnNames();
            do {
                for (String name: columnNames) {
                    tableString += String.format("%s: %s\n", name,
                            allRows.getString(allRows.getColumnIndex(name)));
                    Log.e(TAG, "getTableAsString: " + tableString );
                }
                tableString += "\n";

            } while (allRows.moveToNext());
        }

        return tableString;
    }



    public static Record getRecord(int id, SQLiteDatabase db){
        id = 1;
        Cursor cursor = db.query(TABLE_NAME,new String[]{
                Columns.ID,
                Columns.NAME,
                Columns.POLIC_NUM,
                Columns.EXPIRY_DATE,
                Columns.COMPANY,
                Columns.TYPE,
                Columns.MOBILE_NUM,
                Columns.EMAIL
        }, Columns.ID + "=?", new String[]{String.valueOf(id)},null,null,null,null);

        if(cursor!= null && cursor.moveToFirst()) {

            Record record = new Record(Integer.parseInt(cursor.getString(0)), cursor.getString(1), cursor.getString(2),
                    cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6), cursor.getString(7));

            return record;
        }
        else return null;
    }

    public static ArrayList<Record> getAllRecords (SQLiteDatabase db) {
        Cursor c = db.query(
                TABLE_NAME,
                new String[]{Columns.ID, Columns.NAME, Columns.POLIC_NUM,Columns.EXPIRY_DATE,Columns.COMPANY,Columns.TYPE,Columns.MOBILE_NUM,Columns.EMAIL},
                null,
                null,
                null,
                null,
                null
        );
        ArrayList<Record> Records = new ArrayList<>();
        c.moveToFirst();
        int idIndex = c.getColumnIndex(Columns.ID);
        int nameIndex = c.getColumnIndex(Columns.NAME);
        int policyNumIndex = c.getColumnIndex(Columns.POLIC_NUM);
        int expiryDateIndex = c.getColumnIndex(Columns.EXPIRY_DATE);
        int companyIndex = c.getColumnIndex(Columns.COMPANY);
        int typeIndex = c.getColumnIndex(Columns.TYPE);
        int mobileNumIndex = c.getColumnIndex(Columns.MOBILE_NUM);
        int emailIndex = c.getColumnIndex(Columns.EMAIL);

        while (!c.isAfterLast()) {
            Records.add(new Record(
                    c.getInt(idIndex),
                    c.getString(nameIndex),
                    c.getString(policyNumIndex),
                    c.getString(expiryDateIndex),
                    c.getString(companyIndex),
                    c.getString(typeIndex),
                    c.getString(mobileNumIndex),
                    c.getString(emailIndex)
            ));

            c.moveToNext();
        }
        return Records;
    }

}
