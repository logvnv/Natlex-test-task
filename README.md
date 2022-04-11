# Table of contents

- [API](#api)
   - [Registration](#registration)
   - [Geological classes](#geological-classes)
   - [Sections](#sections)
   - [Import](#import)
   - [Export](#export)
- [Task](#task)
   - [Task text](#test-task-for-backend-developer)
   - [Requirements](#requirements)
   - [Contacts](#contact)

# API
Available at https://natlex-test-task.herokuapp.com

## Registration
<b>/api/registration</b>

<table>
   <tr align="center">
      <th>Operation</th><th>Method</th><th>Request</th><th>Response</th>
   </tr>
   <tr align="center">
      <td>Add user</td><td>POST</td>
      <td><pre align="left">{
  "username": "NewUser",
  "password": "********"
}</pre></td>
      <td></td>
   </tr>
</table>

## Geological classes
      
<b>/api/geo-classes</b>
<table>
   <tr align="center">
      <th>Operation</th><th>Method</th><th>Request</th><th>Response</th>
   </tr>
   <tr align="center">
      <td>Read all</td><td rowspan="3">GET</td>
      <td>/</td>
      <td>
         <pre align="left">[
  {
    "id": 1,
    "code": "GC11",
    "name": "Geo class 11"
  }
]</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Read by code and name</td>
       <td>/by-code-and-name?code={<b>code</b>}&name={<b>name</b>}</td>
      <td rowspan="5">
         <pre align="left">{
  "id": 1,
  "code": "GC11",
  "name": "Geo class 11"
}</pre>
      </td>
   </tr>
    <tr align="center">
      <td>Read</td>
       <td rowspan="2" >/{<b>id</b>}</td>
   </tr>
   <tr align="center">
      <td>Delete</td><td>DELETE</td>
   </tr>
   <tr align="center">
      <td>Create</td><td>POST</td>
      <td>
         <pre align="left">{
  "code": "GC11",
  "name": "Geo class 11"
}</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Update</td><td>PUT</td>
      <td>
         <pre align="left">{
  "id": 1,
  "code": "GC11",
  "name": "Geo class 11"
}</pre>
      </td>
   </tr>
</table>

## Sections
<b>/api/sections</b>

<table>
   <tr align="center">
      <th>Operation</th><th>Method</th><th>Request</th><th>Response</th>
   </tr>
   <tr align="center">
      <td>Read all</td><td rowspan="4">GET</td>
      <td>/</td>
      <td rowspan="2">
         <pre align="left">[
  {
    "id": 1,
    "name": "Section 1",
    "geoClasses": [
       {
         "id": 1,
         "code": "GC11",
         "name": "Geo class 11"
       }
    ]
  }
]</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Read by geo code</td>
      <td>/by-code/code={<b>code</b>}</td>
   </tr>
   <tr align="center">
      <td>Read by name</td>
      <td>/by-name/name={<b>name</b>}</td>
      <td rowspan="5">
         <pre align="left">{
  "id": 1,
  "name": "Section 1",
  "geoClasses": [
     {
       "id": 1,
       "code": "GC11",
       "name": "Geo class 11"
     }
  ]
}</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Read</td>
      <td rowspan="2">/{<b>id</b>}</td>
   </tr>
   <tr align="center">
      <td>Delete</td><td>DELETE</td>
   </tr>
   <tr align="center">
      <td>Create</td><td>POST</td>
      <td>
         <pre align="left">{
  "name": "Section 1",
  "geoClasses": [1]
}</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Update</td><td>PUT</td>
     <td>
       <pre align="left">{
  "id": 1,
  "name": "Section 1",
  "geoClasses": [1]
}</pre>
     </td>
  </tr> 
</table>

## Import
<b>/api/import</b>

<table>
   <tr align="center">
      <th>Operation</th><th>Method</th><th>Request</th><th>Response</th>
   </tr>
   <tr align="center">
      <td>Import</td><td>POST</td>
      <td>file={<b>file</b>}</td>
      <td>1</td>
   </tr>
   <tr align="center">
      <td>Read status</td><td>GET</td>
      <td>/{<b>id</b>}</td>
      <td>
         <pre align="left">{
  "id": 1,
  "status": "DONE",
  "message": null
}</pre>
      </td>
   </tr>
</table>
   
## Export
<b>/api/export</b>

<table>
   <tr align="center">
      <th>Operation</th><th>Method</th><th>Request</th><th>Response</th>
   </tr>
   <tr align="center">
      <td>Export</td><td rowspan="3">GET</td>
      <td></td>
      <td>1</td>
   </tr>
   <tr align="center">
      <td>Read status</td>
      <td>/{<b>id</b>}</td>
      <td>
         <pre align="left">{
  "id": 1,
  "status": "DONE",
  "message": null
}</pre>
      </td>
   </tr>
   <tr align="center">
      <td>Read file</td>
      <td>/{<b>id</b>}/file</td>
      <td></td>
   </tr>
</table>

# Task
## Test Task for backend developer
1) Add REST CRUD API for Sections and GeologicalClasses. Each Section has structure:
   <pre align="left">
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
<tr align="center"><th>Section name</th><th>Class 1 name</th><th>Class 1 code</th><th>Class 2 name</th><th>Class 2 code</th><th>Class M name</th><th>Class M code</th></tr>
<tr align="center"><td>Section 1</td><td>Geo Class 11</td><td>GC11</td><td>Geo Class 12</td><td>GC12</td><td>Geo Class 1M</td><td>GC1M</td></tr>
<tr align="center"><td>Section 2</td><td>Geo Class 21</td><td>GC21</td><td>Geo Class 22</td><td>GC22</td><td></td><td></td></tr>
<tr align="center"><td>Section 3</td><td>Geo Class 31</td><td>GC31</td><td></td><td></td><td>Geo Class 3M</td><td>GC3M</td></tr>
<tr align="center"><td>Section N</td><td>Geo Class N1</td><td>GCN1</td><td>Geo Class N2</td><td>GCN2</td><td>Geo Class NM</td><td>GCNM</td></tr>
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

<a name="contact"></a>

## ООО «Натлекс»
185000 Карелия г. Петрозаводск, ул. Кирова, д. 8Б (4-ый этаж, помещение 7)
www.natlex.team  •  info@natlex.ru
