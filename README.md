
# LearningWithNationalParks

This is my Capstone project from the Udacity Android Developer Nanodegree Program.

Learn with National Parks is a free tool for students, teachers and parents, using the United States National Parks Service as the contextual environment for learning.  The National Park Service has provided open source API’s and other data that allow students to learn, through using professionally developed lesson plans, seeing the many historical images available, and listening to various interesting sounds of nature recorded at the parks.  It is also intended to encourage users to find all parks (down to the local level) located near them, to explore the park information through Google maps, and hopefully be motivated to go and experience them in person.

The lesson plans are developed according to DOE standards and target various grade levels from early elementary through twelfth grade.  From https://www.nps.gov/subjects/digital/nps-data-api.htm : Lesson Plans - Objectives, grade level, subject, duration, and standards information for lesson plans about national parks for teachers to use in their classrooms.   In this app, lesson plans can be sorted by title or by subject. Users can save lesson plans for using later.  

The National Park Service Flickr photostream will show a grid of images. From https://www.flickr.com/services/api/flickr.people.getPhotos.html : Return photos from the given user's photostream. Only photos visible to the calling user will be returned. 
 
 
The sound gallery is a small sampling of the vast collection of sounds curated by the National Park Service, both for preservation and for user enjoyment. The raw sound files and descriptions were compiled from https://www.nps.gov/subjects/sound/gallery.htm and https://www.nps.gov/yell/learn/photosmultimedia/soundlibrary.htm

The park search activity uses Google Maps Android API https://developers.google.com/maps/ to find user current location and display it on a map fragment.  Using the Google Places API https://google-developers.appspot.com/places/android-api/start, the user can find all parks near their current location.  

There is a companion home widget for this app.  It is an image collection stack widget using the NPS Flickr photostream.  Clicking on the front image will open that image detail screen on the full app.

Data sources: National Parks Service, Flickr, Google Maps and Places.

Android developer skils demonstrated in this project are as follows.

Design and Plan Review:
Proposal contains detailed descriptions and includes user interface mocks and declares the app's primary features.
Outlines key constraints such as data persistence, and libraries used. 
Clearly outlines how a content provider will be implemented.
Describes a plan to implement the main features of the app via a set of well structured technical tasks.
UI mocks depict interaction stories that adhere to App quality guidelines.

Core Platform Development:
Integrates third-party libraries: Butterknife, Retrofit, Glide.
Validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash. 
Support for accessibility. 
Keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
Provides a widget to provide relevant information to the user on the home screen.

Google Play Services:
Integrates Google services, imported in build.gradle. 
Customizes the user’s experience by using the device's location and provides releveant information to the user.

Material Design:
App theme extends AppCompat, uses app bar, collapsting and associated toolbars, coordinator layouts, elevations, standard and simple transitions between activities.  Designed to conform to Google Material Design guidelines.

Building:
Builds from a clean repository checkout with no additional configuration, and deploys using the installRelease Gradle task.
Equipped with a signing configuration, and the keystore and passwords are included in the repository. 
All app dependencies are managed by Gradle.

Data Persistence:
App stores data locally by implementing a ContentProvider. 
Because the app pulls data from various API's, it usues both Retrofit and AsyncTask to complete this off the main thread.
Uses a Loader to move saved data to its views.
