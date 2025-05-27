#!/bin/bash

./gradlew --no-daemon -Pbuildkonfig.flavor=production -PapiHost=api.toritark.com -PapiPort=443 -PapiIsHttps=true bundleRelease