# VultureCSSCore
Remove dead css from your stylesheets

VultureCSSCore analyze the HTML code and the CSS code and remove all the unused CSS code. All this, of course, will be done without changing the websites' appearance. 
It can be used via JAR file, website or via API.

The current version is 0.1.1 BETA.

## Usage

###### Command line
```
java -jar VultureCSSCore.jar -css style.css -html index.html -out style.min.css
java -jar VultureCSSCore.jar -css https://example.com/style.css -html https://example.com
```
###### Or include the library inside your project
```
URL htmlUrl = new URL("https://dotmaui.com");
URL cssUrl = new URL("https://dotmaui.com/style.css");
VultureCSSCore v = new VultureCSSCore(cssUrl, htmlUrl);
String used_css = v.Process();
```


