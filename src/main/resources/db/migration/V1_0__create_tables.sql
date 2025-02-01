create table notes
(
    id          bigint    not null auto_increment,
    title       varchar(255),
    content     text,
    created_at  timestamp not null default current_timestamp(),
    modified_at timestamp not null default current_timestamp(),
    version     bigint    not null,
    primary key (id)
);

create table tags
(
    id          bigint       not null auto_increment,
    label       varchar(255) not null,
    created_at  timestamp    not null default current_timestamp(),
    modified_at timestamp    not null default current_timestamp(),
    version     bigint       not null,
    primary key (id)
);

create table note_tag
(
    note_id bigint not null,
    tag_id  bigint not null,
    primary key (note_id, tag_id),
    foreign key (note_id) REFERENCES notes (id),
    foreign key (tag_id) REFERENCES tags (id)
)