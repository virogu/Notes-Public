ssh-add命令是把专用密钥添加到ssh-agent的高速缓存中,从而提高ssh的认证速度。

### 命令语法
```
ssh-add [-cDdLlXx] [-t life] [file ...]
ssh-add -s pkcs11
ssh-add -e pkcs11
```

### 命令选项
```
-D：删除ssh-agent中的所有密钥.
-d：从ssh-agent中的删除密钥
-e pkcs11：删除PKCS#11共享库pkcs1提供的钥匙。
-s pkcs11：添加PKCS#11共享库pkcs1提供的钥匙。
-L：显示ssh-agent中的公钥
-l：显示ssh-agent中的密钥
-t life：对加载的密钥设置超时时间，超时ssh-agent将自动卸载密钥
-X：对ssh-agent进行解锁
-x：对ssh-agent进行加锁
```

### 开启ssh-agent
默认操作系统是不开启ssh-agent的，需要手动打开

- Linux:
    > ssh-agent bash

- Windows: 
  
    使用管理员打开Power Shell后执行：
    > Start-Service ssh-agent

    也可以设置自启动：
    >Set-Service ssh-agent -StartupType Auto

### 把专用密钥添加到ssh-agent的高速缓存中
> ssh-add ~/.ssh/id_dsa

### 从ssh-agent中删除密钥
> ssh-add -d ~/.ssh/id_dsa.pub
