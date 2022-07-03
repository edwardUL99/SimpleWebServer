SimpleWebServer
==
A simple project written in Java to implement a simple Web Server that can accept requests with various methods and
paths. It is done to gain a closer understanding into HTTP. It provides some default request handling and also configurable
request handling.

There is not much Javadoc documentation due to it being a quick project for experimentation purposes.

## Requirements
- Developed using Java 11
- Maven 3.6.3

## Build
To build the project, run on the command line:
```
mvn clean install
```

## Run
To run the project after a build, use the following command:
```
java -jar target/simple-web-server-1.0-SNAPSHOT.jar -p 8080 -s $HOME/server_dir
```
Where -p is the port to run the server on and -s points to a directory where static server content can be stored. This
directory must have a directory called `http` where the static content is stored. A favicon.ico file can be stored here for
browsers too. Static content can be retrieved through the `/static/<filename>` endpoint.

You can omit these flags by passing in properties either through environment variables or Java system properties.
The following environment variables are defined:
- **SIMPLE_SERVER_DIR**: holds the value of the server directory (parent directory of the `http` directory)
- **SIMPLE_SERVER_PORT**: holds the port to start the server on

The following system properties are supported (to be passed in with -D flag):
- **server.dir**: holds the value of the server directory (parent directory of the `http` directory)
- **server.port**: holds the port to start the server on

If any of these are set and command line arguments are passed in, the command line args override them (so choose either
and not both)

## Endpoints
To register an endpoint, you need to create a request handler which is registered against a request method and path. All handlers
must implement the RequestHandler interface. These implementations can be registered in the static block of the [RegisteredPaths](src/main/java/io/github/edwardUL99/simple/web/RegisteredPaths.java) static block, for example:
```java
static {
    configure(); // configures from paths.json on classpath

    register(RequestMethod.GET, "/static/**", new StaticGetHandler()); // for static file path handling
    register(RequestMethod.GET, "/favicon.ico", new FaviconGetHandler()); // for browsers retrieving favicons
}
```

You can also add the entry to the [paths.json](src/main/resources/paths.json) by specifying the path and fully qualified class name
under the appropriate method, for example:
```json
{
  "paths": {
    "GET": {
      "/path/**": "io.github.edwardUL99.simple.web.handlers.PathWildcardedHandler"
    },
    "POST": {
    },
    "PUT": {
    },
    "PATCH": {
    },
    "DELETE": {
    }
  }
}
```
As seen in this example, the paths can be wildcarded, where /path/** will match any path that starts with /path/.

See the [handlers](src/main/java/io/github/edwardUL99/simple/web/handlers) directory for examples of the static and favicon
request handlers.

You can return a [ResponseEntity](src/main/java/io/github/edwardUL99/simple/web/requests/response/ResponseEntity.java) object
to return objects in JSON format so it doesn't always have to be HTML returns. Use the [ResponseBuilders](src/main/java/io/github/edwardUL99/simple/web/requests/response/ResponseBuilders.java)
utility class for static factories to return response builders for any other responses that don't have to be JSON but any format you wish.