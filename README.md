<!--
# license: Licensed to the Apache Software Foundation (ASF) under one
#         or more contributor license agreements.  See the NOTICE file
#         distributed with this work for additional information
#         regarding copyright ownership.  The ASF licenses this file
#         to you under the Apache License, Version 2.0 (the
#         "License"); you may not use this file except in compliance
#         with the License.  You may obtain a copy of the License at
#
#           http://www.apache.org/licenses/LICENSE-2.0
#
#         Unless required by applicable law or agreed to in writing,
#         software distributed under the License is distributed on an
#         "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
#         KIND, either express or implied.  See the License for the
#         specific language governing permissions and limitations
#         under the License.
-->

### This version of the cordova-plugin-screen-orientation plugin adds a native listener to get the device's current orientation, even when it's locked via orientation.lock()

Define window.onRotationUpdate, take in both rotation integer (0, 90, -90, 180), and device auto rotate locked boolean (true, false), as shown in the example below:

```js
window.onRotationUpdate = function(rotation, locked) {

}
```

Congratulations! You can now detect when the device itself has changed orientation, even when your cordova activity is locked in portrait/landscape mode.

### This plugin also disables the portrait upsidedown orientation on iOS when it is unlocked using orientation.unlock(), because why would you want that?

# Cordova Screen Orientation Plugin

Cordova plugin to set/lock the screen orientation in a common way for iOS, Android, and windows-uwp.  This plugin is based on [Screen Orientation API](http://www.w3.org/TR/screen-orientation/) so the api matches the current spec.

The plugin adds the following to the screen object (`window.screen`):

```js
// lock the device orientation
.orientation.lock('portrait')

// unlock the orientation
.orientation.unlock()

// current orientation
.orientation
```

## Install


```bash
cordova plugin add cordova-plugin-screen-orientation
```

## Supported Orientations

#### portrait-primary
> The orientation is in the primary portrait mode.

#### portrait-secondary
> The orientation is in the secondary portrait mode.

#### landscape-primary
> The orientation is in the primary landscape mode.

#### landscape-secondary
> The orientation is in the secondary landscape mode.

#### portrait
> The orientation is either portrait-primary or portrait-secondary (sensor).

#### landscape
> The orientation is either landscape-primary or landscape-secondary (sensor).

#### any
>  orientation is  unlocked - all orientations are supported.

## Usage

```js
// set to either landscape
screen.orientation.lock('landscape');

// allow user rotate
screen.orientation.unlock();

// access current orientation
console.log('Orientation is ' + screen.orientation.type);
```

## Events

Both android and iOS will fire the orientationchange event on the window object.
For this version of the plugin use the window object if you require notification.


### Example usage

```js
window.addEventListener("orientationchange", function(){
    console.log(screen.orientation.type); // e.g. portrait
});
```

The 'change' event listener has also been added to the screen.orientation object.

### Example usage

```js
screen.orientation.addEventListener('change', function(){
    console.log(screen.orientation.type); // e.g. portrait
});
    // OR

screen.orientation.onchange = function(){console.log(screen.orientation.type);
};

```
## Android Notes

The __screen.orientation__ property will not update when the phone is [rotated 180 degrees](http://www.quirksmode.org/dom/events/orientationchange.html).

## Windows UWP Notes

Windows store apps (windows-uwp) will only display orientation changes if the device has some sort of accelerometer.  The internal state of the "orientation" will still be kept, but the actual screen won't rotate unless the device supports it.

# Legacy Changelog

See [RELEASENOTES.md](RELEASENOTES.md) for the automated changelog.

# Changelog

## 2.0.0
* Common javascript for iOS, Android and Windows.

## 1.4.2
* [#101](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/pull/101) make iOS rotate as needed when lockOrientation is called

## 1.4.1
* [#89](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/pull/89) Fix for cordova >= 3.6.3

## 1.4.0
* Added Windows 8.1 Support
* [#54](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/pull/54) Background thread for ios
* [#64](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/pull/64) Orientation naming bug fixed
* Add portrait upside down to iOS default orientations

## 1.3.5-6
* Plugin added to npm

## 1.3.4
* Readme update

## 1.3.3
* [#53](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/pull/53) WP8 Support

## 1.3.2

* [#33](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/issues/33) iOS8 Delay Block

## 1.3.0

* [#23](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/issues/23) iOS8 flicker

## 1.2.0-1.2.1

* [#19](https://github.com/gbenvenuti/cordova-plugin-screen-orientation/issues/19) iOS8 Crash



Pull requests welcome.
