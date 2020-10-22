create table tasks (
           id int8 generated by default as identity,
            change_date timestamp not null,
            complete boolean not null,
            creation_date timestamp not null,
            description varchar(255),
            title varchar(255) not null,
            urgency int4,
            list_id int8,
            primary key (id)
        )

GO

alter table if exists tasks
       add constraint FK41bm5fcswq91u94amuvfgxjam
       foreign key (list_id)
       references lists
GO