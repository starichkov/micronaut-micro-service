create table customers
(
    id          bigint       not null auto_increment,
    first_name  varchar(255) not null,
    last_name   varchar(255) not null,
    email       varchar(255) not null,
    created_at  timestamp    not null default current_timestamp(),
    modified_at timestamp    not null default current_timestamp(),
    version     bigint       not null,
    primary key (id)
);

create table orders
(
    id          bigint    not null auto_increment,
    customer_id bigint,
    created_at  timestamp not null default current_timestamp(),
    modified_at timestamp not null default current_timestamp(),
    shipped_at  timestamp,
    primary key (id),
    foreign key (customer_id) REFERENCES customers (id)
);

create table products
(
    id          bigint         not null auto_increment,
    name        varchar(255)   not null,
    description text,
    price       decimal(15, 2) not null,
    created_at  timestamp      not null default current_timestamp(),
    modified_at timestamp      not null default current_timestamp(),
    version     bigint         not null,
    primary key (id)
);

create table order_items
(
    order_id   bigint not null,
    product_id bigint not null,
    quantity   int    not null default 1,
    primary key (order_id, product_id),
    foreign key (order_id) REFERENCES orders (id),
    foreign key (product_id) REFERENCES products (id)
)