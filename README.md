# Code-Sharing-Platform

DESCRIPTION: 

This project was designed for sharing code snippets. It has two possible restrictions:

- View Restriction - certain amount of views for the code snippet after which it will be deleted from the database.

- Time Restriction - amount of time after which the code snippet will be deleted from the database

Two type of interfaces are implemented: API and web interface.

API interface contains mappings:

GET:

- /api/code/{uuid} : it presents code snippet with unique identifier in JSON format, uuid in the mapping is a path variable.

- /api/code/latest : it shows up to 10 most recent snippets without any restrictions.

POST:

- /api/code/new : it allows to create a new code snippet after sending code, time and view restriction in JSON format.

Web interface contains mappings:

GET:

- /code/{uuid} : shows a code snippet with unique identifier on the web page

- /code/new : shows a web page on which code, time and view restriction should be inputted, after that data is send to JavaScript function which which converts it to JSON and call POST method "api/code/new".

- /code/latest : shows a web page with up to 10 latest snippets without restrictions.

All web pages are shown with style designed in style.css

RUNNING PROGRAM:

Unfortunately to run this project you've got to download and run it locally. This problem is caused by H2 database not being supported by Heroku. Sorry for the inconvenience, further improvements are planned.

Applications port: 8889

Format for posting a code snippet via api/code/new:

{

    "code": "code_snippet",
    
    "time": time_restiction,
    
    "views": views_restriction
    
}

If you do not want to set any restrictions while creating a snippet type "0" in those fields.

DOCUMENTATION:

To get more information about project structure open "program_structure".
