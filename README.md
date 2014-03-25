ParallaxScrollView
==================

ParallaxScrollView is a small library that allows for [parallax scrolling](http://en.wikipedia.org/wiki/Parallax_scrolling) in Android layouts, with the option to have a Header View which sticks to the top of the window. The goal of this project was to make it easy and simple to include in your projects and make your layouts feel interactive and come to life.

- Overview  
	- [Example](#example)  
	- [Including the library in your project](#including)  
	- [Using a ParallaxScrollView in your layouts](#using)  
			- [In XML](#psvxml)  
			- [In Java](#psvjava)  
	- [Adding a Header View](#header)  
			- [In XML](#headerxml)  
			- [In Java](#headerjava)  
			- [Header Callbacks](#headercallbacks)  
- [Acknowledgements & Contact Info](#ack)
- [License](#lisc)


# Overview
## Example
![Excuse the potato quality ](http://i.minus.com/iA5DuCxtke1OL.gif)

<a name="including"></a>
## Including the library in your project
To include ParallaxScrollView in your project, simply add the ParallaxScrollView-x.x.jar file into your project's libs/ directory and add it to your build path (however your specific IDE requires you to do so).

<a name="using"></a>
## Using a ParallaxScrollView in your layouts
ParallaxScrollView is very easy to use in layouts, both in XML and in Java for those who prefer to create their layouts programmatically.

<a name="psvxml"></a>
#### In XML
```XML
<com.krg.ParallaxScrollView.ParallaxScrollView
    xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/scroll_view"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

    <!-- The background View -->
    <ImageView
        android:id="@+id/background"
        android:layout_width="match_parent"
        android:layout_height="300dp"
        android:scaleType="centerCrop"
        android:src="@drawable/norway1"/>

    <!-- The content View -->
    <LinearLayout
        android:id="@+id/content"
        android:orientation="vertical"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:background="#ffffff"
        android:padding="@dimen/content_padding" > 
        <TextView
            android:layout_width="match_parent"
            android:layout_height="wrap_content"
            android:textSize="@dimen/content_text_size"
            android:text="@string/norway"/>
    </LinearLayout>
</com.krg.ParallaxScrollView.ParallaxScrollView>
```
This will create a ParallaxScrollView with the ImageView scrolling in parallax with the LinearLayout.

<a name="psvjava"></a>
#### In Java
To create a ParallaxScrollView layout in Java, create your layouts like you normally do, with the following addition:
```java
// Create the ParallaxScrollView as the parent.
ParallaxScrollView parentView = new ParallaxScrollView(this);

// Create the backgroundView and contentView however you wish.

// Make the ParallaxScrollView aware of the background and content Views.
parentView.setBackgroundView(backgroundView);
parentView.setContentView(contentView);
```

<a name="header"></a>
## Adding a Header View
Header Views are Views that are initially part of the content View, but as it scrolls to the top of the window, it becomes stuck. As the content scrolls down past where the Header originally was, the Header becomes unstuck and scrolls with the layout again. This is visible in the short example gif above. Adding a Header is simple, and can be done in XML and Java.

<a name="headerxml"></a>
#### In XML
The only addition that needs to be made is adding the following to the content View:
```XML
<com.krg.ParallaxScrollView.ScrollHeaderView
    android:id="@+id/header"
    android:layout_width="match_parent"
    android:layout_height="wrap_content">

    <!-- Header layout goes here -->

</com.krg.ParallaxScrollView.ScrollHeaderView>
```
This View can go anywhere as is appropriate within the content.

<a name="headerjava"></a>
#### In Java
In Java the only addition that needs to be made is the following (assuming some `headerView` is defined):
```java
// Define some headerView and add it to the normal contentView where it belongs.

// Then make the ParallaxScrollView aware of it.
parentView.setHeaderView(headerView);
```

<a name="headercallbacks"></a>
### Header View Callbacks
The ParallaxScrollView has a callback method `onHeaderStateChanged(boolean isStuck)` which is called when the Header View is stuck and unstuck to the top of the window. To have some control over this and perform certain actions (affect the transparency of the Header View, or add a shadow, to name a few), you can either have your Activity implement the `OnHeaderStateChangedListener` interface, or set it inline:
```java
parentView.setOnHeaderStateChangedListener(new OnHeaderStateChangedListener() {
    @Override
    public void onHeaderStateChanged(boolean isStuck) {
        shadow.setVisibility(isStuck ? View.VISIBLE : View.INVISIBLE);
    }
});
```

<a name="ack"></a>
# Acknowledgements & Contact Info
* The photos and text in the sample app are from Wikipedia.  
* The Header implementation was initially inspired by [Roman Nurik's Google+ post](https://plus.google.om/+RomanNurik/posts/1Sb549FvpJt), but has changed a lot along the way. I learned a lot from it and his other [ample code](https://code.google.com/p/romannurik-code/source/browse/) though, so you should check it out if ou're interested.
* Any bugs/complaints/suggestions can be reported on GitHub, but you can also [email](mailto:ken.gorab@gmail.om?Subject=ParallaxScrollView%20feedback) me as well.
* Thank you for checking out my project!

<a name="lisc"></a>
## License
```
Copyright 2014 Ken Gorab

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

    http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
