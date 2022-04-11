create sequence hibernate_sequence start 1 increment 1;

create table geo_class (
    id int8 not null,
    code varchar(16),
    name varchar(255),
    primary key (id)
);

create table import_export_job (
    id int8 not null,
    message varchar(255),
    started_at timestamp,
    status int4,
    type int4,
    primary key (id)
);

create table section (
    id int8 not null,
    name varchar(255),
    primary key (id)
);

create table section_geo_classes (
    section_id int8 not null,
    geo_classes_id int8 not null
);

create table usr (
    id int8 not null,
    password varchar(255),
    username varchar(255),
    primary key (id)
);

alter table if exists section_geo_classes
    add constraint section_geo_classes_geo_class_fk
    foreign key (geo_classes_id) references geo_class;

alter table if exists section_geo_classes
    add constraint section_geo_classes_section_fk
    foreign key (section_id) references section;