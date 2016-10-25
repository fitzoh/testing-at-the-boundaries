#!/bin/sh
PORT=8090
echo $PORT
bundle exec rackup -p $PORT --host 127.0.0.1
