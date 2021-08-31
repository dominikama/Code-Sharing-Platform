# Code-Sharing-Platform

DESCRIPTION: 

This project was designed for sharing code snippets. It has two possible restrictions:

- View Restriction - certain amount of views for the code snippet after which it will be deleted from the database.

- Time Restriction - amount of time after which the code snippet will be deleted from the database

Two types of interfaces are implemented: API and web interface.

Web interface contains mappings:

GET:

- https://serene-stream-51733.herokuapp.com/code/{uuid} : shows a code snippet with unique identifier on the web page, uuid should be replaced with token value

- https://serene-stream-51733.herokuapp.com/code/new : shows a web page on which code, time and views restriction should be inputted, after that data is send to JavaScript function which converts it to JSON and calls POST method "api/code/new".

- https://serene-stream-51733.herokuapp.com/getAll : all available snippets without restricions
- https://serene-stream-51733.herokuapp.com/more - more information about the project
- https://serene-stream-51733.herokuapp.com/ - home page

All web pages are designed with bootstrap.

API interface contains mappings:

GET:

- https://serene-stream-51733.herokuapp.com/api/code/{uuid} : it presents a code snippet with unique identifier in JSON format, uuid in the URL is a path variable and should be replaced with token value.

- https://serene-stream-51733.herokuapp.com/api/code/latest : it shows up to 10 most recent snippets without any restrictions.

POST:

- https://serene-stream-51733.herokuapp.com/api/code/new : it allows to post a new code snippet after sending code, time and views restriction in JSON format.

RUNNING PROGRAM:
The program is now available on heroku: https://serene-stream-51733.herokuapp.com
Format for posting a code snippet via api/code/new:

{

    "code": "code_snippet",
    
    "time": time_restiction,
    
    "views": views_restriction
    
}

If you do not want to set any restrictions while creating a snippet type "0" in those fields.

RELESE NOTES:
This is version 2 of the project. Crucial changes:
- PostgreSql instead of H2 database
- Switched from gradle to maven
- switched from Freemarker to Thymeleaf 
- Deletetd CodeList interface and implementation
