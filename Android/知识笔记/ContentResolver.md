## ContentResolver

### 遍历其他类型文件
```Kotlin
val cursor: Cursor? = requireContext().contentResolver.query(
    //数据源
    MediaStore.Files.getContentUri("external"),
    // 想要查询的信息
    arrayOf(
        MediaStore.Files.FileColumns._ID,
        MediaStore.Files.FileColumns.TITLE,
        MediaStore.Files.FileColumns.DISPLAY_NAME,
    ),
    //条件为文件类型
    MediaStore.Files.FileColumns.MIME_TYPE + "= ?",
    arrayOf(
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("docx"),
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("pdf"),
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("doc"),
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("xls"),
        MimeTypeMap.getSingleton().getMimeTypeFromExtension("xlsx"),
    ),
    //默认排序
    null
)

```