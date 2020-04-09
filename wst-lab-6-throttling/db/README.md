# Модуль для общения с Postgres

Берём postgres, создаём базу данных, пользователя и таблицу.

```postgresql
create database wst;
create user wst_user with password '123456';
grant all privileges on database wst to wst_user;
```

Таблица:

```postgresql
create  table cats (
    id integer not null constraint cats_pkey primary key,
    name varchar(255),
    age integer,
    breed varchar(255),
    weight integer
);
```

Тестовые данные:
```postgresql
insert into cats values (1, 'Cat', 5, 'Haustier', 3000);
insert into cats values (2, 'NewKitten', 1, 'Haustier', 500);
```