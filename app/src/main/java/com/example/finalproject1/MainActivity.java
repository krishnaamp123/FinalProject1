package com.example.finalproject1;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.Toast;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    ArrayList<Note> notes;
    SQLiteDatabaseHandler db;
    ImageView btnSubmit;
    PopupWindow pwindo;
    Activity activity;
    ListView listView;
    CustomNoteList customNoteList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        activity = this;
        db = new SQLiteDatabaseHandler(this);
        listView = (ListView) findViewById(R.id.list);
        btnSubmit = (ImageView) findViewById(R.id.btnSubmit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPopup();
            }
        });
        Log.d("MainActivity ", "Before reading mainActivity");
        notes = (ArrayList) db.getAllNote();

        for (Note note : notes) {
            String log = "Id: " + note.getId() + " ,Note: " + note.getNoteName()
                    + " ,Keterangan: " + note.getKeterangan();
            Log.d("Note: ", log);
        }

        CustomNoteList customNoteList = new CustomNoteList(this, notes, db);
        listView.setAdapter(customNoteList);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Toast.makeText(getApplicationContext(), "You Selected " + notes.get(position).getNoteName() + " as Note", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void addPopup() {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_popup, null);
        dialogBuilder.setView(dialogView);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        final EditText noteEdit = (EditText) dialogView.findViewById(R.id.editTextNote);
        final EditText keteranganEdit = (EditText) dialogView.findViewById(R.id.editTextKeterangan);

        Button save = (Button) dialogView.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteStr = noteEdit.getText().toString();
                String keteranganStr = keteranganEdit.getText().toString();
                Note note = new Note(noteStr, keteranganStr);
                db.addNote(note);
                if (customNoteList == null) {
                    customNoteList = new CustomNoteList(activity, notes, db);
                    listView.setAdapter(customNoteList);
                }
                customNoteList.notes = (ArrayList) db.getAllNote();
                ((BaseAdapter) listView.getAdapter()).notifyDataSetChanged();

                for (Note note1 : notes) {
                    String log = "Id: " + note1.getId() + " ,Note: " + note1.getNoteName() +
                            " ,Keterangan: " + note1.getKeterangan();

                    //Writing note to log
                    Log.d("Name: ", log);
                }
                alertDialog.dismiss();
            }
        });
    }
}