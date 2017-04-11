#!/bin/sh
curl -H "Content-Type: application/sparql-query" -i -H "Accept:text/csv" --data-binary @query.sparql http://localhost:9003/sparql