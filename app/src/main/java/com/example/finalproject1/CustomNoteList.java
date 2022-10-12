package com.example.finalproject1;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.PopupWindow;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomNoteList extends BaseAdapter {
    private Activity context;
    ArrayList<Note> notes;
    SQLiteDatabaseHandler db;
    BaseAdapter ba;

    public CustomNoteList(Activity context, ArrayList<Note> notes, SQLiteDatabaseHandler db) {
        this.context = context;
        this.notes = notes;
        this.db = db;
    }

    public static class ViewHolder {
        TextView textViewNote;
        TextView textViewKeterangan;
        LinearLayout edit;
        ImageView delete;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        LayoutInflater inflater = context.getLayoutInflater();
        ViewHolder vh;
        if (convertView == null) {
            vh = new ViewHolder();
            row = inflater.inflate(R.layout.row_item, null, true);

            vh.textViewNote = (TextView) row.findViewById(R.id.textViewNote);
            vh.textViewKeterangan = (TextView) row.findViewById(R.id.textViewKeterangan);
            vh.edit = (LinearLayout) row.findViewById(R.id.edit);
            vh.delete = (ImageView) row.findViewById(R.id.delete);

            //store the holder with the view
            row.setTag(vh);
        } else {
            vh = (ViewHolder) convertView.getTag();
        }

        vh.textViewNote.setText(notes.get(position).getNoteName());
        vh.textViewKeterangan.setText(notes.get(position).getKeterangan());
        final int positionPopup = position;
        vh.edit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                Log.d("Save: ", "" + positionPopup);
                editPopup(positionPopup);
            }
        });

        vh.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Last Index", "" + positionPopup);
                //integer index
                db.deleteNote(notes.get(positionPopup));

                notes = (ArrayList) db.getAllNote();
                Log.d("Country Size", "" + notes.size());
                notifyDataSetChanged();
            }
        });
        return row;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public int getCount() {
        return notes.size();
    }

    public void editPopup(final int positionPopup) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context);
        LayoutInflater inflater = context.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.edit_popup, null);
        dialogBuilder.setView(dialogView);


        AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();
        final EditText noteEdit = (EditText) dialogView.findViewById(R.id.editTextNote);
        final EditText keteranganEdit = (EditText) dialogView.findViewById(R.id.editTextKeterangan);
        noteEdit.setText(notes.get(positionPopup).getNoteName());
        keteranganEdit.setText(notes.get(positionPopup).getKeterangan());
        Button save = (Button) dialogView.findViewById(R.id.save_popup);
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String noteStr = noteEdit.getText().toString();
                String keteranganStr = keteranganEdit.getText().toString();
                Note note = notes.get(positionPopup);
                note.setNoteName(noteStr);
                note.setKeterangan(keteranganStr);
                db.updateNote(note);
                notes = (ArrayList) db.getAllNote();
                notifyDataSetChanged();
                for (Note note1 : notes) {
                    String log = "Id: " + note1.getId() + " ,Name: " + note1.getNoteName() +
                            " ,Population: " + note1.getKeterangan();

                    //Writing country to log
                    Log.d("Name: ", log);
                }
                alertDialog.dismiss();
            }
        });

    }
}
