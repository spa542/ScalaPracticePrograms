#!/bin/bash

spark-submit \
  --jars /usr/local/hive/hive-2.3.6/lib/mysql-connector-java.jar \
  --class DatasetsExample \
  DatasetsExample.jar
