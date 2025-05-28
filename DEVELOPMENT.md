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
appodeal.android.key=c1a9c2c6f83e756e6bc0572a732474f3d0eefba1bc67e3af
appodeal.ios.key=3bd8b3a757d8fb52be113ab228f056579fc5874d65458486
revenuecat.android.key=goog_IHKxeidXhhGXNRxtvwRuCRgKuXi
revenuecat.ios.key=goog_IHKxeidXhhGXNRxtvwRuCRgKuXi
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