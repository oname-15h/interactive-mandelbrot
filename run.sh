#!/bin/bash
cd ./bin
java run &
chrt -p 99 $$
