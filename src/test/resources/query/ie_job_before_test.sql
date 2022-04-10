delete from import_export_job;

/*
 type:
     0 - IMPORT,
     1 - EXPORT;
 status:
     0 - DONE,
     1 - ERROR,
     2 - IN PROGRESS;
 */
insert into import_export_job(id, type, status, message, started_at) values
(1, 0, 0, 'import done', '0001-01-01 00:00:00.00'),
(2, 0, 1, 'import error', '0001-01-01 00:00:00.00'),
(3, 0, 2, 'import in progress', '0001-01-01 00:00:00.00'),
(4, 1, 0, 'export done', '0001-01-01 00:00:00.00'),
(5, 1, 1, 'export error', '0001-01-01 00:00:00.00'),
(6, 1, 2, 'export in progress', '0001-01-01 00:00:00.00');

alter sequence hibernate_sequence restart with 10;
