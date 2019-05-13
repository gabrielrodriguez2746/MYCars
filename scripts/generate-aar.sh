#!/usr/bin/env bash
# The first parameter is the name of the module that need to be compiled and the second one
# is the artifact version
# Example: sh scripts/generate-aar.sh base 1.0

if [[ -d "libs/com/mycars/$1" ]]; then rm -Rf libs/com/mycars/$1; fi
mkdir -p libs/com/mycars/$1/$2

./gradlew $1:aR

mv $1/build/outputs/aar/$1-release.aar libs/com/mycars/$1/$2/$1-$2.aar
