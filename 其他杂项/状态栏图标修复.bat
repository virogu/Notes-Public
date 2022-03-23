reg delete "HKEY_CURRENT_USER\Software\Classes\Local Settings\Software\Microsoft\Windows\CurrentVersion\TrayNotify" /va /f

taskkill /f /im explorer.exe & start explorer.exe