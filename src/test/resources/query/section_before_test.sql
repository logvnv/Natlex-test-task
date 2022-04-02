delete from section_geo_classes;
delete from geo_class;
delete from section;

insert into geo_class(code, name) values
('GC10', 'Geo Class 10'),
('GC11', 'Geo Class 11');

insert into section(id, name) values
(1, 'Section 1'),
(2, 'Section 2');

insert into section_geo_classes(section_id, geo_classes_code) values
(1, 'GC11'),
(2, 'GC10');

alter sequence hibernate_sequence restart with 3;