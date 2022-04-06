delete from section_geo_classes;
delete from geo_class;
delete from section;

insert into geo_class(id, code, name) values
(1, 'GC10', 'Geo Class 10'),
(2, 'GC11', 'Geo Class 11');

insert into section(id, name) values
(1, 'Section 1'),
(2, 'Section 2');

insert into section_geo_classes(section_id, geo_classes_id) values
(1, 2),
(2, 1);

alter sequence hibernate_sequence restart with 3;