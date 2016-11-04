# HELLO Round ProgressBar
## app effect

![effect](https://github.com/victorfan336/RoundProgressBar/blob/master/progress~1.gif)

<br/>
##1.some attribute<br/>
```Java
  <declare-styleable name="RoundProgressBar">
        <attr name="text_color" format="color" />
        <attr name="progress_color" format="color" />
        <attr name="stroke_color" format="color" />
        <attr name="progress_background_color" format="color" />
        <attr name="text_size" format="dimension" />
        <attr name="stroke_width" format="dimension" />
    </declare-styleable>
```
<br/>
You can custom youself style!
<br/>
##2.how to use it<br/>
```Java
<com.victor.myprogressbar.RoundProgressBar
        xmlns:app="http://schemas.android.com/apk/res-auto"
        android:id="@+id/progress"
        android:layout_width="150dp"
        android:layout_height="150dp"
        android:layout_centerInParent="true"
        app:progress_background_color="#00ff00"
        app:progress_color="#408A40"
        app:stroke_width="1dp"
        app:stroke_color="#A0A0A0"
        app:text_size="20dp"
        app:text_color="#000000"
        />
```
<br/>
### 注意
该自定义UI加了流畅处理，避免进度过快而出现的闪进现象！
