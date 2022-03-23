最近新使用一个代码部署运维平台Spug，拉取代码时配置仓库地址，部署机上如果使用http拉取的话需要每次都输入用户名，密码，平台没有提供这种功能，只会执行git clone -v 加上配置的仓库地址，根本没有办法输入用户名和密码，加上公司的私有云上部署的gitlab不支持外网使用ssh拉取仓库，所以只能用http方式拉取，而且还不能每次都要输入用户名密码，网上找了一下解决方法，做个记录。

### 全局配置git
git先要安装上，这不用说了。

1. 先用git config --global user.name 'username'和git config --global user.email 'xxx@xxx.com'配置一下用户名和邮箱
2. 生成ssh公钥：ssh-keygen -t rsa -C "xxxxx@xxxxx.com"，查看~/.ssh/id_rsa.pub文件内容，获取到你的public key，粘贴到GitLabssh公钥管理处即可
3. 使用git clone http://git.gitxxx.com/xxx.git，先测试一下，看能不能拉取成功。如果成功，向下进行。此时还是会询问用户名和密码的。

### 免密拉取配置
1. cd到~/目录下，创建一个文件：touch .git-credentials
2. vim .git-credentials
3. 然后输入https://{username}:{password}@git.gitxx.com，比如http://fengjiaheng:password@git.gitxx.com(IP的话类似这样: 123.456.789:1234)
4. 然后执行：git config --global credential.helper store
5. 然后使用git config --list或者查看一下~/.gitconfig文件，会发现多了一行[credential] helper = store
6. 这时候再用 git 拉取仓库就不需要输入用户名和密码了。
注意：第4步必须要做，否则做完4、5步之后也不能免密码拉取成功，需要再次执行第4步骤。
