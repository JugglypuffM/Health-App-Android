# Инструкция по добавлению файла конфигурации

Файл должен лежать в директории app/src/main/res/values/server.xml

Содержание файла 
```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="server_url">{server_url}</string>
    <integer name="server_port">{server_port}</integer>
</resources>
```