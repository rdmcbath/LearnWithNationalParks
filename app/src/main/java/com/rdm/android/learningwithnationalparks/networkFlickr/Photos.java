package com.rdm.android.learningwithnationalparks.networkFlickr;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Photos implements Parcelable {

    @SerializedName("page")
    @Expose
    private Integer page;
    @SerializedName("pages")
    @Expose
    private Integer pages;
    @SerializedName("perpage")
    @Expose
    private Integer perpage;
    @SerializedName("total")
    @Expose
    private String total;
    @SerializedName("photo")
    @Expose
    private List<FlickrPhoto> photoItems = new ArrayList<FlickrPhoto>();

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getPages() {
        return pages;
    }

    public void setPages(Integer pages) {
        this.pages = pages;
    }

    public Integer getPerpage() {
        return perpage;
    }

    public void setPerpage(Integer perpage) {
        this.perpage = perpage;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public List<FlickrPhoto> getPhotoItems() {
        return photoItems;
    }

    public void setPhoto(List<FlickrPhoto> photoItems) {
        this.photoItems = photoItems;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeValue(this.page);
        dest.writeValue(this.pages);
        dest.writeValue(this.perpage);
        dest.writeString(this.total);
        dest.writeList(this.photoItems);
    }

    protected Photos(Parcel in) {
        this.page = (Integer) in.readValue(Integer.class.getClassLoader());
        this.pages = (Integer) in.readValue(Integer.class.getClassLoader());
        this.perpage = (Integer) in.readValue(Integer.class.getClassLoader());
        this.total = in.readString();
        this.photoItems = new ArrayList<FlickrPhoto>();
        in.readList(this.photoItems, FlickrPhoto.class.getClassLoader());
    }

    public static final Parcelable.Creator<Photos> CREATOR = new Parcelable.Creator<Photos>() {
        @Override
        public Photos createFromParcel(Parcel source) {
            return new Photos(source);
        }

        @Override
        public Photos[] newArray(int size) {
            return new Photos[size];
        }
    };
}
