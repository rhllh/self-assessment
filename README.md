# self-assessment

[Click](src/main/java/vttp2022/day8/workshop) to jump to src/main/vttp2022/day8/workshop/

To run:
```
java -cp ./target/self-assessment.jar vttp2022.day8.workshop.Main [args]
```

## Task 1

- [x] Generate a Java Maven project
- [x] Create a remote git repository
- [x] Add/link the Maven project to a remote git repository
- [x] Perform an initial commit and push project to the remote repository

## Task 2

- [x] Create at least `Main`, `HttpServer` and `HttpClientConnection` classes
- [x] Add `Main` to Maven `pom.xml` as the starting class (`mainClass`)

## Task 3

- [x] The web server should accept the following command line options:
    - `--port <port number>` the port that the server will listen to. If this is not specified, then default to port 3000
    - `--docRoot <colon delimited list of directories>` one or more directories where HTML, CSS and JavaScript files and images are stored. If not specified, default to `static` directory in the current path.

Command line examples (for `self-assessment.jar`):
- [x] `java -cp ./myserver.jar` starts the server on port 3000, `docRoot` directory is ./target
- [x] `java -cp ./myserver.jar --port 8080` starts the server on port 8080, `docRoot` is `./target`
- [x] `java -cp ./myserver.jar --docRoot ./target:/opt/tmp/www` starts the server on port 3000, `docRoot` directories are `./target/` and `/opt/tmp/www`
- [x] `java -cp ./myserver.jar --port 8080 --docRoot ./target:/opt/tmp/www` starts the server on port 8080, `docRoot` directories are `./target/` and `/opt/tmp/www`

## Task 4

When the HTTP server starts, perform the following:
- [x] open a TCP connection and listen on the port from `port` option
- [ ] Check each path of the docRoot; for each path verify that
    - [ ] the path exists,
    - [ ] the path is a directory,
    - [ ] the path is readable by the server
- [ ] If any conditions fail, print the failure reason on the console, stop the server and exit the program with `System.exit(1)`

## Task 5

- [ ] Create a thread pool with 3 threads
- [ ] Server listens on the specified port and accepts incoming connections from the browser
- [ ] When connection is established, it is handled by a thread from the threadpool
- [ ] Main control thread goes back to waiting for new incoming connections

## Task 6

The client thread (handling the client connection) should perform the following tasks:

1. [ ] Read the first line from the incoming request
2. [ ] If the request is ***not a `GET` method***, send the following response:
    ```
    HTTP/1.1 405 Method Not Allowed\r\n
    \r\n
    <method name> not supported\r\n
    ```
    Close the connection and exit the thread.

3. [ ] If the ***requested resource is not found*** send the following response:
    ```
    HTTP/1.1 404 Not Found\r\n
    \r\n
    <resource name> not found\r\n
    ```
    Close the connection and exit the thread

    If the resource name is /, replace it with `/index.html` before performing a file search

4. [ ] If the ***resource is found*** in any of the `docRoot` directories, send the resource contents as bytes back to the client with the response
    ```
    HTTP/1.1 200 OK\r\n
    \r\n
    <resource contents in bytes>
    ```
    Close the connection and exit the thread

5. [ ] If the requested resource exists and ends with `.png`, then the resource is a PNG image. The response is
    ```
    HTTP/1.1 200 OK\r\n
    \r\n
    <resource contents as bytes>
    ```
    Close the connection and end the thread.

## Task 7

- [x] Create a directory called `static` at the root of the project folder
- [x] Write a HTML document `index.html` with
    - [x] any PNG image, which must be in the `static` folder,
    - [x] a header line `<h1>` with any text,
    - [x] a link to another HTML document in the `static` directory
    - [x] any text resources (CSS/JavaScript) referenced by `index.html` should also be placed in the `static` directory
- [x] Image, text and link must be positioned at the center (vertically and horizontally) of the browser's window; it should remain at the center when the browser's window is resized.

Run the HTTP server and see if you can access the HTML document from the server
```
http://localhost:<port>
http://localhost:<port>/index.html
```