package com.rdm.android.learningwithnationalparks.networkFlickr;

import retrofit2.Call;
import retrofit2.http.GET;

public interface RetrofitFlickr {

    //This call executes the photo search and retrieves public photos from the photostream of the
    //National Park Service. Here is what the query looks like:
    //https://api.flickr.com/services/rest/?method=flickr.people.getpublicphotos&api_key=
    //(MYAPIKEYHERE)&user_id=42600860@N02&extras=url_o,description&format=json&nojsoncallback=1

    @GET("rest/?method=flickr.people.getPhotos&api_key=88c1855d07cdbccd65b6825822f0ea90&user_id=42600860%40N02&extras=url_z,description&format=json&nojsoncallback=1")
    Call<FlickrResponse> getPhotostream();

    // Photostream from the Flickr Group US National Parks: https://www.flickr.com/groups/usnationalparks/
    @GET("rest/?method=flickr.groups.pools.getPhotos&api_key=88c1855d07cdbccd65b6825822f0ea90&group_id=57347939@N00&extras=url_c,description&format=json&nojsoncallback=1")
    Call<FlickrResponse> getParkGroupPhotostream();

	// Photostream from the Flickr Group US National Parks and Places: https://www.flickr.com/groups/usnationalparksandplaces/
	@GET("rest/?method=flickr.groups.pools.getPhotos&api_key=88c1855d07cdbccd65b6825822f0ea90&group_id=39889516@N00&extras=url_c,description&format=json&nojsoncallback=1")
	Call<FlickrResponse> getParkPlacesGroupPhotostream();
}
