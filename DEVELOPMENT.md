# Toritark App development instructions

## Set up

### local.properties

```properties
sdk.dir=/home/user/Android/Sdk
signing.keyAlias=...
signing.keyPassword=...
signing.storeFile=...
signing.storePassword=...
signIn.local.google.serverClientId=...
signIn.production.google.serverClientId=...
amplitude.apiKey=...
mixpanel.apiKey=...
kochava.android.appGuid=...
appodeal.ios.key=..
```

### IDEA run configuration

Edit the Android run configuration.

1. Remove `Gradle-aware make` from `Before Launch`
2. Add `Gradle task`, task = `build`, arguments = `-Pbuildkonfig.flavor=production -PapiHost=api.toritark.com -PapiPort=443 -PapiIsHttps=true`
2. For local version, add `Gradle task`, task = `build`, arguments = `-Pbuildkonfig.flavor=local -PapiHost=192.168.1.3 -PapiPort=8000 -PapiIsHttps=false`

## Building release

```shell
./scripts/build-release.sh
```