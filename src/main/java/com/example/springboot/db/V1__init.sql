-- USERS
create table if not exists users(
    id bigint primary key,
    username varchar(100) not null unique,
    email varchar(150) not null unique,
    password varchar(255) not null,
    first_name varchar(100),
    last_name varchar(100),
    role varchar(100),
    status varchar(100),
    avatar varchar(255)
);

-- PROJECTS
create table if not exists projects(
    id bigint primary key,
    name varchar(255),
    key varchar(100) not null unique ,
    type varchar(100) not null ,
    avatar varchar(255),
    project_url varchar(255),
    created_at timestamp not null ,
    updated_at timestamp,
    is_deleted boolean,
    is_deleted_at timestamp,
    lead_id bigint references users(id)
);

--BOARDS
create table if not exists boards(
    id bigint primary key,
    project_id bigint references projects(id) on delete cascade
);

--BOARD_COLUMNS
create table if not exists board_columns(
    id bigint primary key,
    name varchar(150),
    description varchar(255),
    position int,
    board_id bigint not null references boards(id) on delete cascade,
    created_at timestamp not null,
    updated_at timestamp
);

--BOARD_MEMBERS
create table if not exists board_members(
    id bigint primary key ,
    role varchar(100),
    board_id bigint not null references boards(id),
    user_id bigint not null references users(id)
);

--TASKS
create table if not exists tasks(
    id bigint primary key,
    name varchar(255),
    description varchar(255),
    key varchar(100) unique not null,
    position int,
    priority varchar(150),
    status varchar(150),
    author_id bigint not null references users(id),
    board_id bigint not null references boards(id),
    board_column_id bigint not null references board_columns(id) on delete cascade,
    parent_task_id bigint references tasks(id),
    project_id bigint not null references projects(id),
    link_type varchar(100),
    created_at timestamp not null,
    updated_at timestamp
);