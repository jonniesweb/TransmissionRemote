# Transmission Remote

Transmission Remote is an Android application which allows you to remotely control [Transmission BitTorrent client](https://www.transmissionbt.com).  
It works through the web interface, so remote access must be enabled in Transmission preferences.

This repository is a maintained fork of [y-polek/TransmissionRemote](https://github.com/y-polek/TransmissionRemote). Version 1.0.0 modernizes the app for current Android releases while retaining its existing application ID and saved-server compatibility.

## Android support

- Minimum Android version: Android 8.0 (API 26)
- Target Android version: Android 16 (API 36)
- Compile SDK: API 37
- Build toolchain: Android Gradle Plugin 9.3.2, Gradle 9.5, Java 17, and KSP2

Server connections require HTTPS with a certificate trusted by Android. Cleartext HTTP and user-installed certificate authorities are intentionally not trusted.

## Build and test

Install JDK 17 and Android SDK platform 37, then run:

```sh
./gradlew test lintDebug lintRelease assembleDebug assembleRelease
```

Run the instrumentation suite on an API 36 emulator with:

```sh
./gradlew connectedDebugAndroidTest
```


## License
```
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
