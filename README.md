
# react-native-custom-android-webview
**Note:** Android only
Fork of the react-native webview that adds a couple new functions:

1. Ability to pass in an array of hostnames to open in a custom chrome tab
2. Ability to download PDFs / ZIPs / etc. automatically

Supports all the same props as the default react native webview

### Additional Props
In addition to usual React Native WebView props:

| Props          | Type            | Notes & Example                                               |
|----------------|-----------------|---------------------------------------------------------------|
| openInternally | `Array[string]` | ```['google.com', 'facebook.com', 'app.facebook.com']````     |
| toolbarColour  | `String`        | Hex code value for the toolbarColour on the Chrome custom tab |


### Manual installation

#### Step 1: Install npm package
```
npm install react-native-custom-android-webview
```

#### Step 2: Setup Android Project

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.ovoenergy.customwebview.CustomWebViewPackage;` to the imports at the top of the file
  - Add `new CustomWebViewPackage()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
		include ':react-native-custom-android-webview'
project(':react-native-custom-android-webview').projectDir = new File(rootProject.projectDir, '../node_modules/react-native-custom-android-webview/android')
		```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
			compile project(':react-native-custom-android-webview')
  	```

## Usage
```javascript
import CustomWebView from 'react-native-custom-android-webview';

<CustomWebView
	...standardWebViewProps
	openInternally={['google.com', 'facebook.com', 'app.facebook.com']}
/>;
```
*Note*: The openInternally list should be based on host, not domain

## License
This project is licensed under the MIT License.
