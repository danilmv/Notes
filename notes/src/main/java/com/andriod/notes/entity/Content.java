package com.andriod.notes.entity;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class Content implements Parcelable {
    public enum ContentType implements Serializable {
        Text, Image, PDF, Video, HTML, HRef, File
    }

    private final ContentType type;
    private String value;
    private final boolean isLink;

    public Content(ContentType type, String value, boolean isLink) {
        this.type = type;
        this.value = value;
        this.isLink = isLink;
    }

    protected Content(Parcel in) {
        type = ContentType.valueOf(in.readString());
        value = in.readString();
        isLink = in.readByte() != 0;
    }

    public ContentType getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public boolean isLink() {
        return isLink;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(type.name());
        dest.writeString(value);
        dest.writeByte((byte) (isLink ? 1 : 0));
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Content> CREATOR = new Creator<Content>() {
        @Override
        public Content createFromParcel(Parcel in) {
            return new Content(in);
        }

        @Override
        public Content[] newArray(int size) {
            return new Content[size];
        }
    };
}
