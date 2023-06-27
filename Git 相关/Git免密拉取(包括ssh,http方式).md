## 配置Git免密拉取
### 基础安装配置
已安装配置好的可以略过
1. 先配置一下用户名和邮箱
   ```
   git config --global user.name 'username'
   git config --global user.email 'xxx@xxx.com'
   ```
2. 生成ssh公钥：
   ```
   ssh-keygen -t rsa -C "xxx@xxx.com"
   ```
### ssh免密拉取配置
1. 将  `~/.ssh/id_rsa.pub` 公钥文件内容粘贴到GitLab的ssh公钥管理处
2. 使用
   ```
   git clone git@xxx:xxx.git
   ```
   测试一下，一般可以直接拉取成功。
   
   但是有些私有云上部署的GitLab不支持使用ssh方式拉取仓库，所以只能用http方式拉取，类似这样
   ```
   git clone http://XXX/XXX.git
   ```
   使用http拉取的话默认又需要每次都输入用户名，密码。
### http免密拉取配置
1. 执行
   ```sh
   git config --global credential.helper store
   ```
   执行后再查看一下`~/.gitconfig`文件，会发现多了这样的内容
   ```
   [credential]
	   helper = store
   ```
   也可以不执行命令，手动将上面的内容加到 `~/.gitconfig` 文件中

   注意：这一步必须要做，下面步骤其实也可以省略。这一步执行完成之后下次首次使用http拉取时会提示输入账号名密码，输入后会自动将相关信息保存到`.git-credentials`文件中，再次拉取时就不需要账号密码了，也可以手动修改`.git-credentials`文件中的内容，首次使用http拉取时也可以直接免密拉取
2. cd到用户根目录 `~/` 下，查看有没有 `.git-credentials` 文件，没有的话则创建一个
   ```
   touch .git-credentials
   ```
   打开`.git-credentials`文件，在里面根据自己的配置添加以下内容
   ```
   https://{username}:{password}@{git地址}
   ```
   `{username}` 指的是git的账号名

   `{password}` 指的是git账号名的密码

   `{git地址}` 指的是git仓库的域名或IP和端口号

   类似下面这样
   ```
   http://name:pw12345@git.gitxx.com
   http://name:pw12345@123.456.789:1234
   ```
3. 这时候再用http的方式拉取仓库就不需要输入用户名和密码了
