#!/bin/sh
curl -i -H "Accept:application/sparql-results+json" http://localhost/~cristianolongo/opendatahacklab/php-transparent-proxy/proxy.php?query=SELECT+%2A+WHERE+%7B%3Fx+%3Fy+%3Fz%7D
