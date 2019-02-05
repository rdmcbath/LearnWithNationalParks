package com.rdm.android.learningwithnationalparks.networkFlickr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Description implements Parcelable {
    @SerializedName("_content")
    @Expose
    private String content;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.content);
    }

    public Description() {
    }

    protected Description(Parcel in) {
        this.content = in.readString();
    }

    public static final Parcelable.Creator<Description> CREATOR = new Parcelable.Creator<Description>() {
        @Override
        public Description createFromParcel(Parcel source) {
            return new Description(source);
        }

        @Override
        public Description[] newArray(int size) {
            return new Description[size];
        }
    };
}

