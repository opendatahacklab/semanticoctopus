#!/bin/sh
curl -i -H "Accept:text/csv" http://localhost:9003/sparql?query=SELECT+%2A+WHERE+%7B%3Fx+%3Fy+%3Fz%7D
