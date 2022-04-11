### API доступен по адресу:
https://natlex-test-task.herokuapp.com

# Test Task for backend developer
1) Add REST CRUD API for Sections and GeologicalClasses. Each Section has structure:
   <pre>
   {
       “name”: “Section 1”,
       “geologicalClasses”: [
           { “name”: “Geo Class 11”, ”code”: “GC11” },
           { “name”: “Geo Class 12”, ”code”: “GC12” }, ...
       ]
   }
   </pre>
2) Add API GET /sections/by-code?code=... that returns a list of all Sections that have geologicalClasses with the specified code.
3) Add APIs for importing and exporting XLS files. Each XLS file contains headers and list of sections with it’s geological classes. Example:

<table>
<tr><th>Section name</th><th>Class 1 name</th><th>Class 1 code</th><th>Class 2 name</th><th>Class 2 code</th><th>Class M name</th><th>Class M code</th></tr>
<tr><td>Section 1</td><td>Geo Class 11</td><td>GC11</td><td>Geo Class 12</td><td>GC12</td><td>Geo Class 1M</td><td>GC1M</td></tr>
<tr><td>Section 2</td><td>Geo Class 21</td><td>GC21</td><td>Geo Class 22</td><td>GC22</td><td></td><td></td></tr>
<tr><td>Section 3</td><td>Geo Class 31</td><td>GC31</td><td></td><td></td><td>Geo Class 3M</td><td>GC3M</td></tr>
<tr><td>Section N</td><td>Geo Class N1</td><td>GCN1</td><td>Geo Class N2</td><td>GCN2</td><td>Geo Class NM</td><td>GCNM</td></tr>
</table>

Files should be processed asynchronously, results should be stored id DB.
- API POST /import (file) returns ID of the Async Job and launches importing.
- API GET /import/{id} returns result of importing by Job ID ("DONE", "IN PROGRESS", "ERROR")
- API GET /export returns ID of the Async Job and launches exporting. 
- API GET /export/{id} returns result of parsed file by Job ID ("DONE", "IN PROGRESS", "ERROR")
- API GET /export/{id}/file returns a file by Job ID (throw an exception if exporting is in process)

## Requirements:
* Technology stack: Spring, Hibernate, Spring Data, Spring Boot, Gradle/Maven. 
* All data (except files) should be in JSON format. 
* In export and import use Apache POI for parsing.
* (Optional) Basic Authorization should be supported.

## ООО «Натлекс»
185000 Карелия г. Петрозаводск, ул. Кирова, д. 8Б (4-ый этаж, помещение 7)
www.natlex.team  •  info@natlex.ru