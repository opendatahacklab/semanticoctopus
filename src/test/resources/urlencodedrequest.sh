#!/bin/sh
curl -i -H "Accept: application/sparql-results+json" --data-urlencode "query=SELECT * WHERE {?x ?y ?z} LIMIT 10" http://localhost:9003/sparql