package com.rdm.android.learningwithnationalparks.adapters;

import android.os.Parcel;
import android.os.Parcelable;

public class Sound implements Parcelable {
    public static final String LOG_TAG = Sound.class.getSimpleName();
    private String mTitle;
    private String mDescription;
    private int mAudioResourceId;
    private int mImageResourceId;
    //boolean for marking whether or not the playAudioImage is visible in the Adapter
    public boolean isVisible;

    public Sound(String title, String description, int audioResourceId, int imageResourceId,
                 boolean isVisible) {
        mTitle = title;
        mDescription = description;
        mAudioResourceId = audioResourceId;
        mImageResourceId = imageResourceId;
        this.isVisible = isVisible;
    }

    public boolean getIsVisible() {
        return isVisible;
    }

    public void setIsVisible(boolean isVisible) {
        this.isVisible = isVisible;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    /**
     * Return the image resource ID of the sound.
     */
    public int getImageResourceId() {
        return mImageResourceId;
    }

    /**
     * Return the audio resource ID of the sound.
     */

    public int getAudioResourceId() {
        return mAudioResourceId;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mTitle);
        dest.writeString(this.mDescription);
        dest.writeInt(this.mAudioResourceId);
        dest.writeInt(this.mImageResourceId);
        dest.writeByte(this.isVisible ? (byte) 1 : (byte) 0);
    }

    protected Sound(Parcel in) {
        this.mTitle = in.readString();
        this.mDescription = in.readString();
        this.mAudioResourceId = in.readInt();
        this.mImageResourceId = in.readInt();
        this.isVisible = in.readByte() != 0;
    }

    public static final Parcelable.Creator<Sound> CREATOR = new Parcelable.Creator<Sound>() {
        @Override
        public Sound createFromParcel(Parcel source) {
            return new Sound(source);
        }

        @Override
        public Sound[] newArray(int size) {
            return new Sound[size];
        }
    };
}
