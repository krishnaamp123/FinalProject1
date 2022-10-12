package com.example.finalproject1;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;

import java.util.ArrayList;
import java.util.List;

public class SQLiteDatabaseHandler extends SQLiteOpenHelper {

    //DATABASE VERSION
    private static final int DATABASE_VERSION = 1;

    //DATABASE NAME
    private static final String DATABASE_NAME = "noteData";

    //Table Name in Database Note
    private static final String TABEL_NOTE = "Note";

    //Country Tabel Colums names
    private static final String KEY_ID = "id";
    private static final String NOTE_NAME = "Note_Name";
    private static final String KETERANGAN = "Keterangan";

    public SQLiteDatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    //CREATING TABLE
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_NOTE_TABLE = "CREATE TABLE " + TABEL_NOTE + "(" + KEY_ID
                + " INTEGER PRIMARY KEY," + NOTE_NAME + " TEXT,"
                + KETERANGAN + " TEXT" + ")";
        db.execSQL(CREATE_NOTE_TABLE);

    }

    //Upgrading Database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABEL_NOTE);
        onCreate(db);
    }

    void addNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NOTE_NAME, note.getNoteName()); //input note name
        values.put(KETERANGAN, note.getKeterangan()); //input keterangan

        //inserting row
        db.insert(TABEL_NOTE, null, values);
        db.close();
    }

    //getting single note
    Note getNote(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABEL_NOTE, new String[] {KEY_ID, NOTE_NAME, KETERANGAN}, KEY_ID
                + "=?", new String[] {String.valueOf(id)}, null, null, null, null);
        if(cursor != null) {
            cursor.moveToFirst();
        }

         Note note = new Note(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));

        return note;
    }

    //getting All Note
    public List<Note> getAllNote() {
        List<Note> noteList = new ArrayList<Note>();
        //select all query
        String selectQuery = "SELECT * FROM " + TABEL_NOTE;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        if(cursor.moveToFirst()) {
            do {
                Note note = new Note();
                note.setId(Integer.parseInt(cursor.getString(0)));
                note.setNoteName(cursor.getString(1));
                note.setKeterangan(cursor.getString(2));
                noteList.add(note);
            }while(cursor.moveToNext());
        }
        return noteList;
    }

    //updating single note
    public int updateNote(Note note) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(NOTE_NAME, note.getNoteName());
        values.put(KETERANGAN, note.getKeterangan());

        //updating row
        return db.update(TABEL_NOTE, values, KEY_ID + "=?",
                new String[] {String.valueOf(note.getId())});
    }

    //Deleting Single Note
    public void deleteNote(Note note){
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_NOTE,KEY_ID + "=?",
                new String[] {String.valueOf(note.getId()) });
        db.close();
    }

    //Delete all note
    public void deleteAlNote() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABEL_NOTE, null, null);
        db.close();
    }

    //Getting Note count
    public int getNotesCount() {
        String countQuery = "SELECT * FROM " + TABEL_NOTE;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        //return count
        return cursor.getCount();
    }
}
