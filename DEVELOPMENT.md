# Toritark App development instructions

## Set up

### local.properties

```properties
sdk.dir=/home/user/Android/Sdk
api.local.host=192.168.1.3
api.local.port=8000
api.local.isHttps=false
api.production.host=api.toritark.com
api.production.port=443
api.production.isHttps=true
signIn.local.google.serverClientId=...
signIn.production.google.serverClientId=...
amplitude.apiKey=...
mixpanel.apiKey=...
kochava.android.appGuid=...
```

### IDEA run configuration

Edit the Android run configuration.

1. Remove `Gradle-aware make` from `Before Launch`
2. Add `Gradle task`, task = `build`, arguments = `-Pbuildkonfig.flavor=local`