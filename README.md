
# react-native-filtering-web-view

### Manual installation

#### Android

1. Open up `android/app/src/main/java/[...]/MainActivity.java`
  - Add `import com.reactlibrary.RNFilteringWebView;` to the imports at the top of the file
  - Add `new RNFilteringWebView()` to the list returned by the `getPackages()` method
2. Append the following lines to `android/settings.gradle`:
  	```
  	include ':react-native-filtering-web-view'
  	project(':react-native-filtering-web-view').projectDir = new File(rootProject.projectDir, '../libs/FOLDER_NAME/android')
  	```
3. Insert the following lines inside the dependencies block in `android/app/build.gradle`:
  	```
      compile project(':react-native-filtering-web-view')
  	```

## Usage
```javascript
import FilteringWebView from 'PATH/TO/FilteringWebView';

<FilteringWebView
	...standardWebViewProps
	openInternally={['google.com', 'facebook.com', 'app.facebook.com']}
/>;
```
*Note*: The openInternally list should be based on host, not domain
