## 关闭Windows的下载目录的自动分组功能

升级到 windows 11 后，下载目录总是会自动分组，手动关了之后，下次打开又会自动分组。这里通过修改注册表彻底关闭。

键盘快捷键(win+R)，打开运行输入regedit，注册表找到下面的内容：

```
HKEY_LOCAL_MACHINE\SOFTWARE\Microsoft\Windows\CurrentVersion\Explorer\FolderTypes\{885a186e-a440-4ada-812b-db871b942259}\TopViews\{00000000-0000-0000-0000-000000000000}
```

修改右侧内容区域：

`GroupBy`：从 `System.DateModified` 改为

 ```
 System.Null
 ```

`SortByList`：从 `prop:System.DateModified` 改为 

```
prop:System.ItemNameDisplay
```

然后重启电脑

方法来自：[如何彻底禁用 win10 资源管理器的分组依据功能](https://www.v2ex.com/t/629634)

如果修改提示 写入值的新内容时出错，右键 `{00000000-0000-0000-0000-000000000000}` 项，选择 `权限`，弹出窗口中点击 高级，然后弹出窗口的左上角的 所有者，点击 更改。弹出的 `选择用户或组` 窗口中点击 `高级` ，然后点击 `立即找查` ，在搜索结果的选项中选择 `Administrators` ，确定之后就可以正常修改了。

