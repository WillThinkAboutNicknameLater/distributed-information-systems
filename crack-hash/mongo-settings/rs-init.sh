#!/bin/bash

sleep 10

mongosh --host mongodb-1:27017 -u "$MONGO_INITDB_ROOT_USERNAME" -p "$MONGO_INITDB_ROOT_PASSWORD" --authenticationDatabase admin "$MONGO_INITDB_DATABASE" <<EOF
var config = {
    "_id": "dbrs",
    "version": 1,
    "members": [
        {
            "_id": 1,
            "host": "mongodb-1:27017",
            "priority": 3
        },
        {
            "_id": 2,
            "host": "mongodb-2:27017",
            "priority": 2
        },
        {
            "_id": 3,
            "host": "mongodb-3:27017",
            "priority": 1
        }
    ]
};
rs.initiate(config, { force: true });
EOF
