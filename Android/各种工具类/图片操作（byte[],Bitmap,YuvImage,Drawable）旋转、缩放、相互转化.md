## byte[],Bitmap,YuvImage,Drawable 旋转、缩放、相互转化
### 1、byte[](YuvImage )->Bitmap
在相机开发中，Camera获取到摄像头每一帧的图像数据byte[]，有时候需要把它转为Bitmap
```java
@Override
public void onPreviewFrame(final byte[] data, Camera camera) {
    camera.setPreviewCallback(null);

    if (mCamera == null)
        return;

    Camera.Parameters parameters = camera.getParameters();
    int width = parameters.getPreviewSize().width;
    int height = parameters.getPreviewSize().height;

    YuvImage yuv = new YuvImage(data, parameters.getPreviewFormat(), width, height, null);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    yuv.compressToJpeg(new Rect(0, 0, width, height), 50, out);
    byte[] bytes = out.toByteArray();
    final Bitmap bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
    this.mCamera.setPreviewCallback(this);
}
```

### 2、byte[](YuvImage )顺时针旋转90度
Camera获取到摄像头图像帧数据，直接把byte[]数据进行图像旋转。
```java
private byte[] rotateYUV420Degree90(byte[] data, int imageWidth, int imageHeight) 
{
    byte [] yuv = new byte[imageWidth*imageHeight*3/2];
    // Rotate the Y luma
    int i = 0;
    for(int x = 0;x < imageWidth;x++)
    {
        for(int y = imageHeight-1;y >= 0;y--)                               
        {
            yuv[i] = data[y*imageWidth+x];
            i++;
        }
    }
    // Rotate the U and V color components 
    i = imageWidth*imageHeight*3/2-1;
    for(int x = imageWidth-1;x > 0;x=x-2)
    {
        for(int y = 0;y < imageHeight/2;y++)                                
        {
            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+x];
            i--;
            yuv[i] = data[(imageWidth*imageHeight)+(y*imageWidth)+(x-1)];
            i--;
        }
    }
    return yuv;
}
```
Android很多使用zxing扫码工具，关于获取预览帧进行旋转的代码，大多数都是错误的，包括徐医生的在内的。
我从Zxing官方项目，下载源码进行了优化和精简，使用了很多不错的特性。
[源码地址](https://github.com/JantHsueh/ZxingAndroid)

### 3 、从resources中获取Bitmap
```java
Resources res = getResources();
Bitmap bmp = BitmapFactory.decodeResource(res, R.drawable.icon);
```

### 4、Bitmap → byte[]
```java
public byte[] Bitmap2Bytes(Bitmap bm) {
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    bm.compress(Bitmap.CompressFormat.PNG, 100, baos);
    return baos.toByteArray();
 }
```
### 5、byte[] → Bitmap
```java
public Bitmap Bytes2Bimap(byte[] b) {
    if (b.length != 0) {
        return BitmapFactory.decodeByteArray(b, 0, b.length);
    } else {
        return null;
    }
}
```
### 6、将Drawable转化为Bitmap
```java
public static Bitmap drawableToBitmap(Drawable drawable) {
    // 取 drawable 的长宽
    int w = drawable.getIntrinsicWidth();
    int h = drawable.getIntrinsicHeight();

    // 取 drawable 的颜色格式
    Bitmap.Config config = drawable.getOpacity() != PixelFormat.OPAQUE ? Bitmap.Config.ARGB_8888 : Bitmap.Config.RGB_565;
    // 建立对应 bitmap
    Bitmap bitmap = Bitmap.createBitmap(w, h, config);
    // 建立对应 bitmap 的画布
    Canvas canvas = new Canvas(bitmap);
    drawable.setBounds(0, 0, w, h);
    // 把 drawable 内容画到画布中
    drawable.draw(canvas);
    return bitmap;
}
```
### 7、Bitmap转换成Drawable
```java
Bitmap bm=xxx; //xxx根据你的情况获取
BitmapDrawable bd= new BitmapDrawable(getResource(), bm); 
//因为BtimapDrawable是Drawable的子类，最终直接使用bd对象即可。
```
### 8、Bitmap缩放
```java
public static Bitmap zoomBitmap(Bitmap bitmap, int width, int height) {
    int w = bitmap.getWidth();
    int h = bitmap.getHeight();
    Matrix matrix = new Matrix();
    float scaleWidth = ((float) width / w);
    float scaleHeight = ((float) height / h);
    matrix.postScale(scaleWidth, scaleHeight);
    Bitmap newbmp = Bitmap.createBitmap(bitmap, 0, 0, w, h, matrix, true);
    return newbmp;
}
```

### 9、Drawable缩放
```java
public static Drawable zoomDrawable(Drawable drawable, int w, int h) {
    int width = drawable.getIntrinsicWidth();
    int height = drawable.getIntrinsicHeight();
    // drawable转换成bitmap
    Bitmap oldbmp = drawableToBitmap(drawable);
    // 创建操作图片用的Matrix对象
    Matrix matrix = new Matrix();
    // 计算缩放比例
    float sx = ((float) w / width);
    float sy = ((float) h / height);
    // 设置缩放比例
    matrix.postScale(sx, sy);
    // 建立新的bitmap，其内容是对原bitmap的缩放后的图
    Bitmap newbmp = Bitmap.createBitmap(oldbmp, 0, 0, width, height,matrix, true);
    return new BitmapDrawable(newbmp);
}
```

