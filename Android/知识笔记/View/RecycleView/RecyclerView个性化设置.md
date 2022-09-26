## 属性
### 预览视图样式
```xml
tools:itemCount="13" //预览item数量
tools:listitem="@layout/item_goal" //预览item视图
tools:itemCount="5" //线性布局item数量
tools:layoutManager="androidx.recyclerview.widget.GridLayoutManager" //预览布局管理器
tools:spanCount="5" //网格布局列数
```

### 滑动到底部时显示padding，滑动过程中不显示
```xml
android:clipToPadding="false"
android:paddingBottom="60dp"
```
## 方法

### 加载item动画
```java
@Override
public void onBindViewHolder(@NonNull InnerHolder holder, int position) {
    holder.binding.getRoot().setAnimation(AnimationUtils.loadAnimation(holder.itemView.getContext(), R.anim.scale));
}
```
### 设置间距
```java
SpaceItemDecoration dividerItemDecoration = new SpaceItemDecoration(16);
binding.recyclerView.addItemDecoration(dividerItemDecoration);
```
### 加载更多
```java
private boolean isLoading;

binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
    @Override
    public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
        if (dy <= 0) {
            return;
        }
        boolean slideToBottom = RecyclerViewUtils.isSlideToBottom(recyclerView);
        if (slideToBottom) {
            boolean nextPage = viewModel.getPagingResult().isNextPage();
            if (nextPage && !isLoading) {
                isLoading = true;
                binding.progressBar.setVisibility(View.VISIBLE);
                viewModel.loadFoodList();
            }
        }
    }
});
```

### 判断滑动状态 是否到底部
```java
protected boolean isSlideToBottom(RecyclerView recyclerView) {
    if (recyclerView == null)
        return false;
    return recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset() >=
            recyclerView.computeVerticalScrollRange();
}
```

## 流式布局
1. 引入google流式布局
google/flexbox-layout： Flexbox for Android (github.com)
```groovy
dependencies {
    implementation 'com.google.android.flexbox:flexbox:3.0.0'
}
```
2. 使用
```java
FlexboxLayoutManager manager = new FlexboxLayoutManager(requireContext());
binding.recyclerView.setLayoutManager(manager);
```