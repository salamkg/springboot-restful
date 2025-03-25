create table if not exists no_name (
    id bigint auto_increment primary key;
    name varchar(128),
    user_id bigint,
    foreign key (user_id) references users(id)
);

insert into no_name (id, name, user_id)
values (1, 'John Doe', 1),
       (2, 'Jack Smith', 2);