package com.rdm.android.learningwithnationalparks.networkLessons;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "lessonPlan")
public class LessonPlan implements Parcelable {

    @SerializedName("total")
    @Expose
    private Integer total;
    @SerializedName("data")
    @Expose
    private List<Datum> data;
    @SerializedName("limit")
    @Expose
    private Double limit;
    @SerializedName("start")
    @Expose
    private Double start;

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public List<Datum> getData() {
        return data;
    }

    public void setData(List<Datum> data) {
        this.data = data;
    }

    public Double getLimit() {
        return limit;
    }

    public void setLimit(Double limit) {
        this.limit = limit;
    }

    public Double getStart() {
        return start;
    }

    public void setStart(Double start) {
        this.start = start;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.total);
        dest.writeTypedList(this.data);
        dest.writeValue(this.limit);
        dest.writeValue(this.start);
    }

    public LessonPlan() {
    }

    protected LessonPlan(Parcel in) {
        this.total = (Integer) in.readValue(Integer.class.getClassLoader());
        this.data = in.createTypedArrayList(Datum.CREATOR);
        this.limit = (Double) in.readValue(Double.class.getClassLoader());
        this.start = (Double) in.readValue(Double.class.getClassLoader());
    }

    public static final Parcelable.Creator<LessonPlan> CREATOR = new Parcelable.Creator<LessonPlan>() {
        @Override
        public LessonPlan createFromParcel(Parcel source) {
            return new LessonPlan(source);
        }

        @Override
        public LessonPlan[] newArray(int size) {
            return new LessonPlan[size];
        }
    };

//    public static class DataStateDeserializer implements JsonDeserializer<LessonPlan> {
//
//        @Override
//        public LessonPlan deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
//            LessonPlan lessonPlan = new Gson().fromJson(json, LessonPlan.class);
//            return lessonPlan;
//        }
}


