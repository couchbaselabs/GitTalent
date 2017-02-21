# GitTalent Frontend

## Requirements

This project has a few requirements that must be satisfied before it can be run.

* Node.js 4.0 or higher
* Angular 2 CLI

Node.js is required because it comes with the Node Package Manager (NPM), a necessary tool for downloading Angular 2 packages.  With it the Angular 2 CLI can be installed.

## Testing the Angular 2 Front-End

The front-end to this example was developed using Angular 2 through the use of the Angular CLI.  To test the front-end, navigate into the **angular** directory with a Command Prompt or Terminal and execute the following:

```sh
npm install
```

The above command will install all of the Angular 2 project dependencies.

With the dependencies installed, one of two things can happen.  The project can be tested using the live-reload features of the Angular 2 CLI, or the project can be built.  To serve the application execute the following:

```sh
ng serve
```

With the local server running, it can be accessed from **http://localhost:4200** in a web browser.  To build the project, execute:

```sh
ng build --output-path=../public
```

## Resources

Couchbase - [http://www.couchbase.com](http://www.couchbase.com)
