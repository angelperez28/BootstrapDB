# Example application for Play+AngularJS+RequireJS+SB Admin Bootstrap Template.

## About

This template application shows how to organize your application with
[Playframework 2](http://www.playframework.com), [WebJars](http://www.webjars.org),
[RequireJS](http://www.requirejs.org) and [AngularJS](http://www.angularjs.org).

This is based on the seed application (https://github.com/mariussoutier/play-angular-require-seed), which shows an alternative way of organizing modules than the official
[Angular-Play-Seed](https://github.com/typesafehub/angular-seed-play).

## Demo
See https://play-angular-requirejs-sbadmin.herokuapp.com/

or deploy your own

[![Deploy](https://www.herokucdn.com/deploy/button.png)](https://heroku.com/deploy)

## Code Organization

The JavaScript modules are organized as follows:

    |- app
    |-- assets
    |--- js             <- contains all the JavaScript/CoffeeScript modules
    |---- app.js        <- app module, wires everything together
    |---- main.js       <- tells RequireJS how to load modules and bootstraps the app
    |---- common/       <- a module, in this case
    |----- main.js      <- main file of the module, loads all sub-files in this folder
    |----- filters.js   <- common's filters
    |----- directives/  <- common's directives
    |----- services/    <- common's services
    |---- ...


## Trying It Out

Firstly, clone this git repository:
```bash
git clone https://github.com/mmizutani/play-angular-require-sbadmin.git
```
Then run the `activator` shell/batch script in the top directory
```bash
./activator
```
to enter the sbt console.
Finally, build and launch the application in either the development mode
```bash
> ~run
```
or the production mode
```bash
> start
```

### Dev Mode

* Load dependencies via `sbt update`
* Run via `sbt ~run`
* Go to [localhost:9000](http://localhost:9000)

This uses the normal JavaScript files and loads libraries from the downloaded WebJars.

### Compile RequireJS Modules
```
> web-stage
```

### Prod Mode

Running:

* Run `sbt start -Dconfig.resource=prod.conf`

Deployment:

* Produce executable via `sbt clean dist`
* Extract `unzip target/universal/play-angular-requirejs-sbadmin-1.0.zip`
* Run `play-angular-requirejs-sbadmin-1.0/bin/play-angular-requirejs-sbadmin -Dhttp.port=9000 -Dconfig.resource=prod.conf`


This uses the uglified JavaScript files, versioned and compressed assets, and loads WebJars resources from the jsDelivr CDN.
