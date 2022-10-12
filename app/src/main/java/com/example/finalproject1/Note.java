package com.example.finalproject1;

public class Note {
    int id;
    String noteName;
    String keterangan;

    public Note(){
        super();
    }

    public Note(int i, String noteName, String keterangan ){
        this.id = i;
        this.noteName = noteName;
        this.keterangan = keterangan;
    }

    public Note(String noteName, String keterangan){
        this.noteName = noteName;
        this.keterangan = keterangan;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNoteName() {
        return noteName;
    }

    public void setNoteName(String noteName) {
        this.noteName = noteName;
    }

    public String getKeterangan() {
        return keterangan;
    }

    public void setKeterangan(String keterangan) {
        this.keterangan = keterangan;
    }

}
