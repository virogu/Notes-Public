sudo passwd root

su root



sudo gedit /etc/pam.d/gdm-autologin

//注释 auth requied pam_succeed_if.so user != root quiet success



sudo gedit /etc/pam.d/gdm-password

//注释 auth requied pam_succeed_if.so user != root quiet success



此时重启计算机，使用root账户登陆，出现错误提示：Error found when loading/root/.profile:mesg: ttyname失败: 对设备不适当的ioctl操作，As a result the session will not be configured correctly.You shoud fix the problem as soon as feasible

运行：

sudo gedit /root/.profile

在行"mesg n || true"前添加"tty -s && "，变为"tty -s && mesg n || true"，此时重启计算机，使用root账户登陆正常。

