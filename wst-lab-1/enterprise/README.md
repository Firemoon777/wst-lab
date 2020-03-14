# Версия для Glassfish

Установка GlassFish 4:
```bash
cd /opt
wget http://download.java.net/glassfish/4.1.1/release/glassfish-4.1.1.zip
unzip glassfish-4.1.1.zip
cd glassfish4/glassfish/lib
wget https://repo1.maven.org/maven2/org/postgresql/postgresql/9.2-1003-jdbc4/postgresql-9.2-1003-jdbc4.jar
/opt/glassfish4/bin/asadmin start-domain
```

Из-за бага Glassfish этой версии редактируем Derby, а не создаём новый JDBC Connection Pool.

При деплое указываем Context Root `/app`