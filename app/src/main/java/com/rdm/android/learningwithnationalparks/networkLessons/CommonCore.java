package com.rdm.android.learningwithnationalparks.networkLessons;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "commonCore")
public class CommonCore implements Parcelable {

    @SerializedName("stateStandards")
    @Expose
    private String stateStandards;
    @SerializedName("mathStandards")
    @Expose
    private List<String> mathStandards;
    @SerializedName("additionalStandards")
    @Expose
    private String additionalStandards;
    @SerializedName("elaStandards")
    @Expose
    private List<String> elaStandards;

    public String getStateStandards() {
        return stateStandards;
    }

    public void setStateStandards(String stateStandards) {
        this.stateStandards = stateStandards;
    }

    public List<String> getMathStandards() {
        return mathStandards;
    }

    public void setMathStandards(List<String> mathStandards) {
        this.mathStandards = mathStandards;
    }

    public String getAdditionalStandards() {
        return additionalStandards;
    }

    public void setAdditionalStandards(String additionalStandards) {
        this.additionalStandards = additionalStandards;
    }

    public List<String> getElaStandards() {
        return elaStandards;
    }

    public void setElaStandards(List<String> elaStandards) {
        this.elaStandards = elaStandards;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.stateStandards);
        dest.writeStringList(this.mathStandards);
        dest.writeString(this.additionalStandards);
        dest.writeStringList(this.elaStandards);
    }

    public CommonCore() {
    }

    protected CommonCore(Parcel in) {
        this.stateStandards = in.readString();
        this.mathStandards = in.createStringArrayList();
        this.additionalStandards = in.readString();
        this.elaStandards = in.createStringArrayList();
    }

    public static final Parcelable.Creator<CommonCore> CREATOR = new Parcelable.Creator<CommonCore>() {
        @Override
        public CommonCore createFromParcel(Parcel source) {
            return new CommonCore(source);
        }

        @Override
        public CommonCore[] newArray(int size) {
            return new CommonCore[size];
        }
    };
}

