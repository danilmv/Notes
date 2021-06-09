package com.andriod.notes.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class Note implements Parcelable {

    private String header;
    private String date;
    private String folder;


    private final List<Content> content;

    private boolean favorite;

    public Note(String header, String date, String folder) {
        this.header = header;
        this.date = date;
        this.folder = folder;

        content = new ArrayList<>();
    }

    public Note(String header, String date) {
        this(header, date, null);
    }

    protected Note(Parcel in) {
        header = in.readString();
        date = in.readString();
        folder = in.readString();
        content = in.createTypedArrayList(Content.CREATOR);
        favorite = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(header);
        dest.writeString(date);
        dest.writeString(folder);
        dest.writeTypedList(content);
        dest.writeByte((byte) (favorite ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Note> CREATOR = new Creator<Note>() {
        @Override
        public Note createFromParcel(Parcel in) {
            return new Note(in);
        }

        @Override
        public Note[] newArray(int size) {
            return new Note[size];
        }
    };

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    public void addContent(Content content) {
        this.content.add(content);
    }

    public int getContentSize(){
        return content.size();
    }

    public Content getContent(int index){
        return content.get(index);
    }

    public boolean checkSearch(String query){
        if (header==null || header.isEmpty()) return false;
        return header.contains(query);
    }
}
