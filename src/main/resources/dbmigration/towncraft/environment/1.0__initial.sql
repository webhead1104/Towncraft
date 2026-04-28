-- apply changes
create table towncraft_users
(
    id   uuid not null,
    data json,
    constraint pk_towncraft_users primary key (id)
);

