# VultureCSSCore
Remove dead css from your stylesheets

VultureCSSCore analyze the HTML code and the CSS code and remove all the unused CSS code. All this, of course, will be done without changing the websites' appearance. 
It can be used via JAR file, website or via API.

The current version is 0.2.3 BETA.

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
###### Or specify only one HTML page, all included CSS files will be checked.
```
java -jar VultureCSSCore.jar -html https://dotmaui.com
```

```
URL url = new URL("https://dotmaui.com");
VultureCSSCore v = new VultureCSSCore();
v.setHtmlUrl(url);
List<Carcass> carcasses = v.Process();

carcasses.forEach((c) -> {
    System.out.println(c.getUsedCSS());
});
```

If you don't use JAVA in your project you can use VultureCSS through API. [View the complete documentation](https://api.dotmaui.com/vulturecss/).

Or use VultureCSS via the website: [vulturecss.dotmaui.com](https://vulturecss.dotmaui.com/)

If you like this project you can support it by offering us a coffee. [You can donate through PayPal](https://dotmaui.com/donate/).

###### Third party libraries
- [jsoup](https://github.com/jhy/jsoup)
- [HtmlUnit](https://github.com/HtmlUnit/htmlunit)
- [ph-css](https://github.com/phax/ph-css)
- [jStyleParser](https://github.com/radkovo/jStyleParser)
