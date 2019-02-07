package com.rdm.android.learningwithnationalparks.networkLessons;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

@Entity(tableName = "datum")
public class Datum implements Parcelable {

	@SerializedName("id")
	@Expose
	@PrimaryKey
	@ColumnInfo(name = "lessonId")
	private Long id;

    @SerializedName("commonCore")
    @Expose
    private CommonCore commonCore;

    @SerializedName("gradeLevel")
    @Expose
    private String gradeLevel;

    @SerializedName("questionObjective")
    @Expose
    private String questionObjective;

    @SerializedName("subject")

    @Expose
    private String subject;

    @SerializedName("title")
    @Expose
    private String title;

    @SerializedName("duration")
    @Expose
    private String duration;

    @SerializedName("url")
    @Expose
    private String url;

    @SerializedName("parks")
    @Expose
    private List<String> parks;

    public Datum(String gradeLevel, String questionObjective, String subject, String title, Long id, String duration, String url) {

        this.gradeLevel = gradeLevel;
        this.questionObjective = questionObjective;
        this.subject = subject;
        this.title = title;
        this.id = id;
        this.duration = duration;
        this.url = url;
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

    public CommonCore getCommonCore() {
        return commonCore;

    }

    public void setCommonCore(CommonCore commonCore) {
        this.commonCore = commonCore;

    }

    public String getGradeLevel() {
        return gradeLevel;
    }

    public void setGradeLevel(String gradeLevel) {
        this.gradeLevel = gradeLevel;
    }

    public String getQuestionObjective() {
        return questionObjective;
    }

    public void setQuestionObjective(String questionObjective) {
        this.questionObjective = questionObjective;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
	    this.subject = subject;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getParks() {
        return parks;
    }

    public void setParks(List<String> parks) {
        this.parks = parks;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
	    dest.writeParcelable(this.commonCore, flags);
	    dest.writeString(this.gradeLevel);
	    dest.writeString(this.questionObjective);
	    dest.writeString(this.subject);
	    dest.writeString(this.title);
	    dest.writeValue(this.id);
	    dest.writeString(this.duration);
	    dest.writeString(this.url);
	    dest.writeStringList(this.parks);
    }

    public Datum(List<Datum> data) {
    }

    public Datum(Parcel in) {
        this.commonCore = in.readParcelable(CommonCore.class.getClassLoader());
        this.gradeLevel = in.readString();
        this.questionObjective = in.readString();
        this.subject = in.readString();
        this.title = in.readString();
        this.id = (Long) in.readValue(Long.class.getClassLoader());
        this.duration = in.readString();
        this.url = in.readString();
        this.parks = in.createStringArrayList();
    }

    public static final Parcelable.Creator<Datum> CREATOR = new Parcelable.Creator<Datum>() {
        @Override
        public Datum createFromParcel(Parcel source) {
            return new Datum(source);
        }

        @Override
        public Datum[] newArray(int size) {
            return new Datum[size];
        }

    };

}