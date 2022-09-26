## TextView 跑马灯

```xml
<TextView
    android:id="@+id/textView"
    android:layout_width="wrap_content"
    android:layout_height="wrap_content"
    android:textSize="40sp"
    android:singleLine="true"
    android:ellipsize="marquee"
    android:focusableInTouchMode="true"
    android:focusable="true"
    android:clickable="true"
    android:marqueeRepeatLimit="marquee_forever"
    android:text="人民日报生活漫步：莫让千元打车费毁了专车" />
```

```xml
focusableInTouchMode="true"
focusable="true"
clickable="true"
singleLine="true"
android:ellipsize="marquee"
```
以上五个属性必须设置

`marqueeReatLimit`属性是滚动重复次数，设置为`marquee_forever` 意思为一直都是滚动模式。
