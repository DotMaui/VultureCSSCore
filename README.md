# VultureCSS Core
Remove dead css from your stylesheets

VultureCSS Core analyzes the HTML and CSS code to remove all unused CSS. This process is carried out without altering the website's appearance.
It can be used either as a JAR file or via an API.

The current version is 0.3.2 BETA.

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
List<Carcass> carcasses = v.Process();

carcasses.forEach((c) -> {
    System.out.println(c.getUsedCSS());
});
```
###### Or specify only one HTML page, all included CSS files will be processed.
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

###### Merge and optimize CSS from urls
```
List<String> urls = new ArrayList<>();
urls.add("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css");
urls.add("https://dotmaui.com/assets/css/production/dotmaui.com_app_min.css?v=2");

String result = VultureCSSCore.mergeAndOptimizeCSSFromUrls(urls);

System.out.println(result);
```

For bug reports, support or customization requests you can also contact us via Telegram at [@DotMaui](https://t.me/DotMaui).

###### Third party libraries
- [jsoup](https://github.com/jhy/jsoup)
- [HtmlUnit](https://github.com/HtmlUnit/htmlunit)
- [ph-css](https://github.com/phax/ph-css)
- [jStyleParser](https://github.com/radkovo/jStyleParser)
