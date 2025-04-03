-- This is for testing purpose of tables with prefix _users, _products ...

create table if not exists _users (
                        id serial primary key,
                        username varchar(50) unique,
                        email varchar(100) unique,
                        password varchar(128) not null,
                        created_at timestamp default current_timestamp,
                        updated_at timestamp default current_timestamp
);
-- drop table _users;

create table if not exists _categories (
                             id serial primary key,
                             name varchar(100) not null
);
-- drop table _categories;

create table if not exists _products (
                           id serial primary key,
                           name varchar(100) not null,
                           description text,
                           price decimal(10, 2) not null,
                           quantity int not null,
                           category_id int references _categories(id) on delete set null,
                           created_at timestamp default current_timestamp,
                           updated_at timestamp default current_timestamp
);
-- drop table _products;

create table if not exists _orders (
                         id serial primary key,
                         user_id int references _users(id) on delete cascade,
                         status varchar(50) not null check (status in('pending', 'completed', 'cancelled')),
                         total decimal(10, 2) not null,
                         created_at timestamp default current_timestamp,
                         updated_at timestamp default current_timestamp
);
-- drop table _orders;

create table if not exists _order_items (
                              id serial primary key,
                              order_id int references _orders(id) on delete cascade,
                              product_id int references _products(id) on delete cascade,
                              quantity int not null,
                              price decimal(10, 2) not null
);
-- drop table _order_items;

create table if not exists _reviews (
                          id serial primary key,
                          user_id int references _users(id) on delete cascade,
                          product_id int references _products(id) on delete cascade,
                          comment text,
                          rating int not null check (rating >= 1 and rating <= 5),
                          created_at timestamp default current_timestamp
);
-- drop table _reviews;

INSERT INTO _users (username, email, password, created_at, updated_at)
VALUES
    ('alice', 'alice@example.com', 'password123', '2025-01-01 10:00:00', '2025-01-01 10:00:00'),
    ('bob', 'bob@example.com', 'password456', '2025-01-02 11:00:00', '2025-01-02 11:00:00'),
    ('charlie', 'charlie@example.com', 'password789', '2025-02-01 12:00:00', '2025-02-01 12:00:00'),
    ('diana', 'diana@example.com', 'password321', '2025-02-05 13:00:00', '2025-02-05 13:00:00'),
    ('eve', 'eve@example.com', 'password654', '2025-03-01 14:00:00', '2025-03-01 14:00:00');

insert into _categories(name) values
    ('Electronics'),('Computers'),('Automative'),('Baby'),('Beauty and Care'),('Fashion'),('Health'),('Home and Kitchen'),('Industrial'),('Tools');
INSERT INTO _categories (name) VALUES
    ('Electronics'),
    ('Clothing'),
    ('Home Appliances'),
    ('Books'),
    ('Toys');

INSERT INTO _products (name, description, price, quantity, category_id, created_at, updated_at)
VALUES
    ('Smartphone', 'Latest smartphone with great features', 499.99, 100, 1, '2025-01-10 10:00:00', '2025-01-10 10:00:00'),
    ('Laptop', 'High-performance laptop for gaming and work', 799.99, 50, 1, '2025-01-12 11:00:00', '2025-01-12 11:00:00'),
    ('T-shirt', 'Cotton T-shirt with printed design', 19.99, 200, 2, '2025-02-01 12:00:00', '2025-02-01 12:00:00'),
    ('Jeans', 'Comfortable denim jeans', 49.99, 150, 2, '2025-02-05 13:00:00', '2025-02-05 13:00:00'),
    ('Washing Machine', 'Energy-efficient washing machine', 299.99, 30, 3, '2025-02-10 14:00:00', '2025-02-10 14:00:00'),
    ('Microwave', 'Compact microwave oven', 99.99, 70, 3, '2025-02-15 15:00:00', '2025-02-15 15:00:00'),
    ('Fiction Book', 'Bestselling fiction book', 14.99, 500, 4, '2025-03-01 16:00:00', '2025-03-01 16:00:00'),
    ('Cookbook', 'Cookbook with delicious recipes', 24.99, 300, 4, '2025-03-05 17:00:00', '2025-03-05 17:00:00'),
    ('Lego Set', 'Creative Lego building set', 39.99, 150, 5, '2025-03-10 18:00:00', '2025-03-10 18:00:00'),
    ('Action Figure', 'Superhero action figure', 29.99, 200, 5, '2025-03-12 19:00:00', '2025-03-12 19:00:00');

INSERT INTO _orders (user_id, status, total, created_at, updated_at)
VALUES
    (1, 'completed', 699.98, '2025-03-15 10:00:00', '2025-03-15 10:00:00'),
    (2, 'pending', 549.99, '2025-03-16 11:00:00', '2025-03-16 11:00:00'),
    (3, 'completed', 799.98, '2025-03-17 12:00:00', '2025-03-17 12:00:00'),
    (4, 'cancelled', 99.99, '2025-03-18 13:00:00', '2025-03-18 13:00:00'),
    (5, 'completed', 1299.96, '2025-03-19 14:00:00', '2025-03-19 14:00:00');

INSERT INTO _order_items (order_id, product_id, quantity, price)
VALUES
    (1, 1, 1, 499.99), -- Smartphone
    (1, 2, 1, 199.99), -- Laptop
    (2, 3, 2, 19.99), -- T-shirt
    (3, 4, 1, 49.99), -- Jeans
    (3, 6, 2, 99.99), -- Microwave
    (4, 7, 1, 14.99), -- Fiction Book
    (5, 1, 2, 499.99), -- Smartphone
    (5, 2, 1, 799.99), -- Laptop
    (5, 8, 1, 24.99); -- Cookbook

INSERT INTO _reviews (product_id, user_id, rating, comment, created_at)
VALUES
    (1, 1, 5, 'Great smartphone, very fast and has an excellent camera!', '2025-03-15 10:00:00'),
    (2, 2, 4, 'Laptop is great for work, but a bit heavy for gaming.', '2025-03-16 11:00:00'),
    (3, 3, 4, 'Nice T-shirt, fits well and feels comfortable.', '2025-03-17 12:00:00'),
    (4, 4, 5, 'Jeans are very comfortable and stylish, I love them!', '2025-03-18 13:00:00'),
    (5, 5, 3, 'The washing machine works, but the spin cycle is a bit loud.', '2025-03-19 14:00:00'),
    (6, 1, 4, 'Compact microwave, does its job well.', '2025-03-15 10:00:00'),
    (7, 2, 5, 'Fantastic fiction book, I could not put it down!', '2025-03-16 11:00:00'),
    (8, 3, 5, 'Great cookbook, many new recipes to try!', '2025-03-17 12:00:00'),
    (9, 4, 4, 'Lego set was fun, but some pieces were hard to fit together.', '2025-03-18 13:00:00'),
    (10, 5, 3, 'Action figure is okay, but not very detailed.', '2025-03-19 14:00:00');

-- 1.Вывести список всех заказов пользователя, включая товары, которые были в каждом заказе.
-- Для каждого заказа должен выводиться: ID заказа, дата создания заказа, список товаров
-- (название и количество), и общая стоимость заказа.
select
    o.id order_id,
    o.created_at order_date,
    p.name product_name,
    p.id,
    p.quantity,
    o.total
from _orders o
         join _order_items oi on oi.order_id = o.id
         join _products p on p.id = oi.product_id
where o.user_id = 5;

-- 2.Найти топ-5 самых продаваемых товаров (по количеству проданных единиц) в этом месяце.
select
    p.name product,
    sum(oi.quantity) as total_sold
from _products p
         join _order_items oi on oi.product_id = p.id
         join _orders o on o.id = oi.order_id
where o.created_at >= date_trunc('month', current_date) and o.created_at < date_trunc('month', current_date) + interval '1 month'
group by p.id
order by total_sold desc
    limit 5;

-- 3.Вывести список всех товаров в категории с названием "Electronics" (или другой категории по выбору) с количеством на складе и ценой.
select
    p.name product,
    p.quantity,
    p.price
from _products p
         join _categories c on p.category_id = c.id
where c.name = 'Electronics';

select * from _products;
select * from _reviews;

-- 4.Посчитать средний рейтинг всех товаров и вывести товары, у которых рейтинг ниже среднего
select avg(rating)
from _reviews;

explain analyze
select
    p.name,
    r.rating as avg_pr_rating
from _reviews r
         join _products p on r.product_id = p.id
group by p.id, r.id
having avg(r.rating) < (select avg(rating) from _reviews);


-- 5.Найти всех пользователей, которые оставили более 3 отзывов на разные товары.
select u.username, count(distinct r.product_id)
from _users u
         join _reviews r on r.user_id = u.id
group by u.id
having count(distinct r.product_id) >= 2;


select * from _reviews;