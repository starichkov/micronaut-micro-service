create table notes
(
    id          bigserial primary key,
    title       varchar(255),
    content     text,
    created_at  timestamp not null default current_timestamp,
    modified_at timestamp not null default current_timestamp,
    version     bigint    not null
);

create table tags
(
    id          bigserial primary key,
    label       varchar(255) not null,
    created_at  timestamp    not null default current_timestamp,
    modified_at timestamp    not null default current_timestamp,
    version     bigint       not null
);

create table note_tag
(
    note_id bigint not null,
    tag_id  bigint not null,
    primary key (note_id, tag_id),
    foreign key (note_id) references notes (id) on delete cascade,
    foreign key (tag_id) references tags (id) on delete cascade
);
