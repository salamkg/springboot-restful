create table if not exists users (
    id bigint auto_increment primary key,
    username varchar(100) not null,
    password varchar(100) not null,
    role_id bigint not null,
    foreign key (role_id) references roles(id)
);
--drop table users;

create table if not exists tasks (
    id bigint auto_increment primary key,
    name varchar(128),
    description varchar(128),
    status varchar(128),
    position varchar(128),
    due_date date,
    priority varchar(128),
    user_id bigint not null,
    task_list_id bigint not null,
    foreign key (user_id) references users(id)
    foreign key (task_list_id) references task_list(id)
);
--drop table tasks;

create table if not exists no_name (
    id bigint auto_increment primary key,
    user_id bigint,
    foreign key (user_id) references users(id)
);