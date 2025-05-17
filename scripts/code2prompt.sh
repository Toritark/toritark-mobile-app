#!/bin/sh

code2prompt . --exclude "README.md,LICENSE.txt,*.json,*.xml,*.png,*.html,*.css,*.properties,*.jar,*.bat,*.sh,*.xcassets,*.xcodeproj,*.pbxproj,*.plist,gradlew,drawable*,mipmap*,*.webp,*/build*,*Test.kt,AllLanguages.kt" --tokens format --output-file "$1"