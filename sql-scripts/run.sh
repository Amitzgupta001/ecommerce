#!/bin/sh
set -e

psql -v ON_ERROR_STOP=1 --username "$POSTGRES_USER" --dbname "$POSTGRES_DB" <<-EOSQL
	  CREATE DATABASE product;
	  CREATE DATABASE users;
	  CREATE DATABASE orders;
EOSQL