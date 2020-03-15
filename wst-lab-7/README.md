# Лабораторная работа 7

## Окружение

```bash
$ java -version
java version "1.8.0_211"
Java(TM) SE Runtime Environment (build 1.8.0_211-b12)
Java HotSpot(TM) 64-Bit Server VM (build 25.211-b12, mixed mode)
$ psql -V    
psql (PostgreSQL) 10.12 (Ubuntu 10.12-0ubuntu0.18.04.1)
```

## Настройка 

[Настройка postgres](db/README.md)

Сборка проекта: 
```bash
mvn clean package
```

**standalone-версия:** `standalone/target/standalone-1.0-SNAPSHOT-jar-with-dependencies.jar`

**Клиент:** `client/target/client-1.0-SNAPSHOT-jar-with-dependencies.jar`

# Установка Apache JUDDI

```bash
wget http://archive.apache.org/dist/juddi/juddi/3.3.7/juddi-distro-3.3.7.zip
unzip juddi-distro-3.3.7.zip
cd juddi-distro-3.3.7.zip
juddi-tomcat-3.3.7/bin/startup.sh 
```

После запуска Apache JUDDI доступен по адресу `http://localhost:8080/juddi-gui/`

Данные для входа прописаны в `juddi-tomcat-3.3.7/conf/tomcat-users.xml` и по умолчанию `uddiadmin:da_password1`.

При использовании JRE 1.8 в `juddi-tomcat-3.3.7/logs/juddi.log` может появляться такая ошибка:
```
Caused by: org.apache.ws.commons.schema.XmlSchemaException: External DTD: Failed to read external DTD 'XMLSchema.dtd', because 'file' access is not allowed due to restriction set by the accessExternalDTD property.
```
Эта ошибка ломает логин в веб-морде и весь эндпоинт `/juddiv3/`. Фиксится установкой параметра `javax.xml.accessExternalDTD` в `all`.
Например, через переменные окружения: 
```bash
JAVA_OPTS=-Djavax.xml.accessExternalDTD=all juddi-tomcat-3.3.7/bin/startup.sh
```