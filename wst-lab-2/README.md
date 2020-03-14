# Лабораторная работа 2

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
