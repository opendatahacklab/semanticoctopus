#!/bin/sh
curl -H "Content-Type: x-www-form-urlencoded" -i -H "Accept:text/plain" --form query=@query.sparql http://localhost:9003/sparql