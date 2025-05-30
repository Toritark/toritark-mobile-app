#!/bin/sh

# shellcheck disable=SC3030
exclude_items=(
    "README.md"
    "LICENSE.txt"
    "*.json"
    "*.xml"
    "*.png"
    "*.html"
    "*.css"
    "*.properties"
    "*.jar"
    "*.bat"
    "*.sh"
    "*.xcassets"
    "*.xcodeproj"
    "*.pbxproj"
    "*.plist"
    "gradlew"
    "drawable*"
    "mipmap*"
    "*.webp"
    "*/build*"
    "*Test.kt"
    "*androidUnitTest*"
    "AllLanguages.kt"
    "*/composeResources*"
    "*wasmJsMain*"
    "*iosLibs.versions.toml"
    "*img/*"
    "iosApp/*"
    "*data/analytics/*"
    "*di/module/*"
)

# shellcheck disable=SC3054
exclude_string=$(IFS=,; echo "${exclude_items[*]}")

code2prompt . --exclude "$exclude_string" --tokens format --no-clipboard --output-file "$1"
