# GitTalent Backend

This repository contains GitTalent backend application. This was created for Couchbase Connect keynote 2016.

## Before building:

* Make sure you have a .github file with login information in your ~ folder (aka c:\users\your name\ on Windows)
* The .github file should be a text file with two lines: login=yourgithubname and password=yourgithubpassword
* Make sure you've created a bucket in Couchbase, and modify application.properties with the correct name if necessary.
* If running locally, set gittalent.cors.allowedOrigin in application.properties to http://localhost:4200, otherwise the Angular site will receive CORS errors.

## To build:

Execute a Maven build with: mvn spring-boot:run

## To import data from github

Make a GET request to http://localhost:8080/githubimport/
