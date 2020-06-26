#!/usr/bin/env bash
mkdir ~/$2/
sudo apt-get install rsync
find . -name $1 -print0 | while IFS= read -r -d $'\0' file; do
    rsync -rR $(dirname "$file") ~/$2/
done
