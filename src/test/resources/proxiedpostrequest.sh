#!/bin/sh
curl -H "Content-Type: application/sparql-query" -i -H "Accept: text/csv" --data-binary @query.sparql http://localhost/~cristianolongo/opendatahacklab/php-transparent-proxy/proxy.php