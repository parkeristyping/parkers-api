# parkers-api

A simple example Clojure API.

## Requirements

This project requires Clojure 1.8.0 and Leiningen for development.

## Usage

This library provides two executables:

- `$ lein run cli /path/to/a/file.whatever`
- `$ lein run server`

### CLI

`$ lein run cli /path/to/a/file.whatever`

This command reads the file at the provided path, parses it into records, and prints out three different orderings of the records:

1. Sorted by gender (females before males) then by last name ascending.
2. Sorted by birth date, ascending.
3. Sorted by last name, descending.

### Server

`$ lein run server`

This command starts a local web server on port 3000 and also an nREPL server on port 9998.

The web server provides the following endpoints:

- POST /records - Expects request to contain a comma-, pipe-, or space-delimited line corresponding to a record. Parses the line and adds it to a collection of records. Returns JSON of the parsed record.
- GET /records/gender - Returns records sorted by gender.
- GET /records/birthdate - Returns records sorted by birthdate.
- GET /records/name - Returns records sorted by name.

Example requests:

- `$ curl http://127.0.0.1:3000/records/gender`
- `$ curl -H "Content-Type: text/plain" -d 'West,Kanye,male,red,6/8/1977' http://127.0.0.1:3000/records`

## Tests

Run the tests with `$ lein test` from the project root.

## Potential Improvements / Next Steps

- Add better validation / error handling for malformed inputs
- Consider stratifying the record hashmaps a bit. Some ideas:
  - Make them Clojure records
  - Use Schema or similar library
  - Bump Clojure version to 1.9 and use clojure.spec
- Update CLI to accept multiple files in a row and aggregate them into the same record collection
- Update Ring configuration so that you don't have to specify "Content-Type" on POST requests

## License

Copyright © 2017 Parker Lawrence

Distributed under the Eclipse Public License either version 1.0 or (at
your option) any later version.
