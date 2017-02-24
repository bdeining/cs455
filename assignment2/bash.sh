#!/bin/bash

osascript -e 'tell app "Terminal"
   do script "cd ~/cs455/assignment2 ; java cs455.scaling.client.Client \"localhost\" \"50000\" \"1\""
end tell'