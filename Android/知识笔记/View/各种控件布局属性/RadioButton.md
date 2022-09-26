# RadioButton
## 切换文字和按钮布局
RadioButton默认选择框是在左边显示的，文字显示在右边。
 
在某些场景下，我们想在右边显示选择框，左边显示文字，给RadioButton 添加一下两个属性就可实现这种效果 

```xml
android:layoutDirection="rtl"
android:textDirection="ltr"
```
两个属性的值分别设置成 `ltr` 和 `rtl` 可以实现4种布局效果