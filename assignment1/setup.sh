#!/bin/bash

test_home=/Users/bdeininger/cs455/assignment1/

for i in `cat machine_list`
do
    echo 'logging into ' ${i}
    gnome-terminal -x bash -c "ssh -t ${i}"
done