# IndicatorView
[![](https://jitpack.io/v/bkhezry/IndicatorView.svg)](https://jitpack.io/#bkhezry/IndicatorView)

<img src="https://github.com/bkhezry/IndicatorView/blob/master/screenshots/view.gif" width = "300" height = "507.6" align=center />

## Usage

##### Step 1

Add it in your root build.gradle at the end of repositories:
```java
allprojects {
    repositories {
        ...
        maven { url 'https://jitpack.io' }
    }
}
```
Add the dependency
```java
dependencies {
	        implementation 'com.github.bkhezry:IndicatorView:1.0.3'
	}
```

##### Step 2

```xml
    <com.github.bkhezry.indoctorview.IndicatorView
        android:background="@color/colorPrimary"
        android:id="@+id/circleIndicator"
        android:layout_width="match_parent"
        android:layout_height="50dp"
        android:layout_alignParentBottom="true"
        app:circle_color="@android:color/darker_gray"
        app:click_color="#fafafa"
        app:colors="@array/colors"
        app:duration="800"
        app:radius="14dp"
        app:scale="0.5">
        <ImageView
            android:layout_width="wrap_content"
            android:layout_height="wrap_content"
            android:src="@drawable/msg" />
            ........
    </com.github.bkhezry.indoctorview.IndicatorView>
```

## License:

License under [Apache 2.0 License](http://opensource.org/licenses/Apache-2.0)
