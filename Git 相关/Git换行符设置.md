```
#在vim下面查看当前文本的模式类型，一般为dos,unix
:set ff

#提交时转换为LF，检出时转换为CRLF
git config --global core.autocrlf true   

#提交时转换为LF，检出时不转换（以上问题使用此命令可解决）
git config --global core.autocrlf input   

#提交检出均不转换
git config --global core.autocrlf false

```

