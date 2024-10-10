/*
 * The MIT License
 *
 * Copyright 2024 .Maui | dotmaui.com.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.dotmaui.vulturecss.core;

import com.dotmaui.api.cssmin.DotMauiCSSMinifyClient;
import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.CSSUtility.getRulesFromString;
import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.extractAllStyleSheetsUrls;
import com.dotmaui.vulturecss.models.Carcass;
import com.dotmaui.vulturecss.models.VultureCSSOptions;
import com.dotmaui.vulturecss.utils.Functions;
import com.dotmaui.vulturecss.utils.Interface;
import com.dotmaui.vulturecss.utils.MinifyWithPhCSS;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONObject;
import static com.dotmaui.vulturecss.utils.Interface.downloadFromUrl;
import static com.dotmaui.vulturecss.utils.Interface.downloadRenderedPage;

public class VultureCSSCore {

    public VultureCSSCore() {
    }

    public VultureCSSCore(String css) {
        this.css = css;
    }

    public VultureCSSCore(String css, String html) {
        this.css = css;
        this.html = html;
    }

    public VultureCSSCore(String css, String html, VultureCSSOptions options) {
        this.css = css;
        this.html = html;
        this.options = options;
    }

    public VultureCSSCore(URL cssUrl) {
        this.cssUrl = cssUrl;
    }

    public VultureCSSCore(URL cssUrl, URL htmlUrl) {
        this.htmlUrl = htmlUrl;
        this.cssUrl = cssUrl;
    }

    public VultureCSSCore(URL cssUrl, URL htmlUrl, VultureCSSOptions options) {
        this.htmlUrl = htmlUrl;
        this.cssUrl = cssUrl;
        this.options = options;
    }

    /**
     *
     * @return String
     * @throws Exception
     */
    public List<Carcass> Process() throws Exception {

        if (this.options == null) {
            this.options = new VultureCSSOptions();
        }

        this.css = (this.css == null) ? "" : this.css.trim();
        this.html = (this.html == null) ? "" : this.html.trim();

        if (!this.css.equals("") && this.cssUrl != null) {
            throw new Exception("It is currently not possible to specify URLs and strings simultaneously");
        }

        if (!this.html.equals("") && this.htmlUrl != null) {
            throw new Exception("It is currently not possible to specify URLs and strings simultaneously");
        }

        if (this.options.isCDNMode() && Functions.isNullOrWhitespace(this.options.getDotMauiApiKey())) {
            throw new Exception("Specify a valid API key");
        }

        if (this.cssUrl != null) {

            this.css = downloadFromUrl(this.cssUrl);

            if (this.css == null) {
                throw new Exception("The download of the CSS file has failed");
            }

        }

        if (this.htmlUrl != null) {

            if (this.options.isUseStaticHTMLFromWebPage()) {
                this.html = downloadFromUrl(this.htmlUrl);
            } else {
                this.html = downloadRenderedPage(this.htmlUrl);
            }

            if (this.html == null) {
                throw new Exception("The download of the HTML file has failed");
            }

        }

        if (this.css.equals("") && this.html.equals("")) {
            throw new Exception("Nothing to process");
        }

        List<Carcass> carcasses = new ArrayList<>();

        if (!"".equals(this.html) && "".equals(this.css)) {

            carcasses = extractAllStyleSheetsUrls(html, this.htmlUrl);

            for (Carcass c : carcasses) {

                URL css_url = new URL(c.getPath());

                String css_string_from_url = Interface.downloadFromUrl(css_url);
                String used_css = "";

                try {
                    used_css = CompareCSSHTML.Process(this.html, css_string_from_url, this.options);
                } catch (Exception ex) {
                    c.setParseError(true);
                }

                c.setUsedCSS(used_css);

            }

            if (this.options.isMergeAll()) {

                String merged_css = "";

                for (Carcass c : carcasses) {
                    merged_css = Functions.concat(merged_css, c.getUsedCSS());
                }

                carcasses = new ArrayList<>();

                Carcass c = new Carcass();
                c.setUsedCSS(merged_css);

                carcasses.add(c);

            }

        } else if (!"".equals(this.html)) {

            Carcass carcass = new Carcass();
            String used_css = "";

            try {
                used_css = CompareCSSHTML.Process(this.html, this.css, this.options);
            } catch (Exception ex) {
                carcass.setParseError(true);
            }

            carcass.setUsedCSS(used_css);
            carcasses.add(carcass);

        } else {

            String used_css = MinifyWithPhCSS.Process(this.css);

            Carcass carcass = new Carcass();
            carcass.setUsedCSS(used_css);
            carcasses.add(carcass);

        }

        // If CDN mode is enabled, I cycle through all the carcasses and save each CSS in a file on the CDN.
        if (this.options.isCDNMode()) {

            DotMauiCSSMinifyClient client = new DotMauiCSSMinifyClient(this.options.getDotMauiApiKey());
            client.setMode(1);

            int i = 1;

            for (Carcass c : carcasses) {

                String file_name = "c".concat(String.valueOf(i)).concat(".css");

                i++;

                client.setName(file_name);

                String responseCdn = client.minifyCSSFromString(c.getUsedCSS());
                JSONObject obj = new JSONObject(responseCdn);

                String fileUrl = obj.getString("url");

                c.setCdnUrl(fileUrl);

            }

        }

        return carcasses;

    }

    private static String mergeAndOptimize(String css) throws Exception {

        CascadingStyleSheet newStyleSheetWithAllDeclarationsFinal = new CascadingStyleSheet();

        ICommonsList<ICSSTopLevelRule> rules = getRulesFromString(css);

        CascadingStyleSheet newStyleSheetWithAllDeclarations = VultureCSSCoreMergify.MergeRules(rules);

        ICommonsList<ICSSTopLevelRule> mergedRules = newStyleSheetWithAllDeclarations.getAllRules();

        int importRulesIndex = 0;

        for (ICSSTopLevelRule ruleCompare : mergedRules) {

            if (ruleCompare instanceof CSSMediaRule cSSMediaRule) {

                CSSMediaRule optimizedMediaRule = VultureCSSCoreOptimize.optimizeMediaRule(cSSMediaRule);
                newStyleSheetWithAllDeclarationsFinal.addRule(optimizedMediaRule);

            } else if (ruleCompare instanceof CSSStyleRule cssStyleRuleToOptimize) {

                List<CSSStyleRule> cssStyleRuleList = new ArrayList<>();
                cssStyleRuleList.add(cssStyleRuleToOptimize);

                List<CSSStyleRule> optimizedCSSStyleRuleList = VultureCSSCoreOptimize.optimizeCSSStyleRules(cssStyleRuleList);

                for (ICSSTopLevelRule optimizedRuleCompare : optimizedCSSStyleRuleList) {

                    newStyleSheetWithAllDeclarationsFinal.addRule(optimizedRuleCompare);

                }

            } else if (ruleCompare instanceof CSSImportRule cSSImportRule) {

                newStyleSheetWithAllDeclarationsFinal.addImportRule(importRulesIndex, cSSImportRule);

                importRulesIndex++;

            } else if (ruleCompare != null) {

                // CSSViewportRule and others.
                newStyleSheetWithAllDeclarationsFinal.addRule(ruleCompare);

            } else {

                throw new java.lang.UnsupportedOperationException();

            }
        }

        final CSSWriterSettings aSettings;
        aSettings = new CSSWriterSettings(ECSSVersion.LATEST, false);

        final CSSWriter aWriter = new CSSWriter(aSettings);

        aWriter.setHeaderText("");

        return MinifyWithPhCSS.Process(aWriter.getCSSAsString(newStyleSheetWithAllDeclarationsFinal));
    }

    /**
     * Merges the CSS content from a list of URLs into a single string and
     * optimizes it. This method downloads CSS files from the provided URLs,
     * merges them into one, and then performs optimization.
     *
     * @param urls A list of URLs pointing to CSS files to be merged.
     * @return A string containing the merged and optimized CSS content.
     * @throws MalformedURLException If any URL is malformed.
     * @throws Exception If an error occurs during download or optimization.
     */
    public static String mergeAndOptimizeCSSFromUrls(List<String> urls) throws MalformedURLException, Exception {

        StringBuilder mergedCSS = new StringBuilder();

        // Download each CSS file and append it to the merged CSS content.
        for (String urlString : urls) {

            // Download the CSS content from the current URL.
            String tempCSS = downloadFromUrl(new URL(urlString));

            // Append the downloaded CSS content to the mergedCSS StringBuilder.
            if (tempCSS != null) {
                mergedCSS.append(tempCSS);
            }

        }

        // Call the mergeAndOptimize function with the concatenated CSS content.
        return mergeAndOptimize(mergedCSS.toString());
    }

    /**
     *
     * @param css
     * @return
     * @throws java.lang.Exception
     */
    public static String mergeAndOptimizeCSSFromString(String css) throws Exception {

        return mergeAndOptimize(css);

    }

    private URL htmlUrl;

    /**
     * Get the value of htmlUrl
     *
     * @return the value of htmlUrl
     */
    public URL getHtmlUrl() {
        return htmlUrl;
    }

    /**
     * Set the value of htmlUrl
     *
     * @param htmlUrl new value of htmlUrl
     */
    public void setHtmlUrl(URL htmlUrl) {
        this.htmlUrl = htmlUrl;
    }

    private URL cssUrl;

    /**
     * Get the value of cssUrl
     *
     * @return the value of cssUrl
     */
    public URL getCssUrl() {
        return cssUrl;
    }

    /**
     * Set the value of cssUrl
     *
     * @param cssUrl new value of cssUrl
     */
    public void setCssUrl(URL cssUrl) {
        this.cssUrl = cssUrl;
    }

    private int cssToReturn = 0;

    /**
     * Get the value of whichCssToReturn
     *
     * @return the value of whichCssToReturn
     */
    public int getCssToReturn() {
        return cssToReturn;
    }

    /**
     * Set the value of whichCssToReturn
     *
     * @param whichCssToReturn new value of whichCssToReturn
     */
    public void setCssToReturn(int whichCssToReturn) {
        this.cssToReturn = whichCssToReturn;
    }

    private String css;

    /**
     * Get the value of css
     *
     * @return the value of css
     */
    public String getCss() {
        return css;
    }

    /**
     * Set the value of css
     *
     * @param css new value of css
     */
    public void setCss(String css) {
        this.css = css;
    }
    private String html;

    /**
     * Get the value of html
     *
     * @return the value of html
     */
    public String getHtml() {
        return html;
    }

    /**
     * Set the value of html
     *
     * @param html new value of html
     */
    public void setHtml(String html) {
        this.html = html;
    }

    private boolean minify = true;

    /**
     * Get the value of Minify
     *
     * @return the value of Minify
     */
    public boolean isMinify() {
        return minify;
    }

    /**
     * Set the value of Minify
     *
     * @param minify
     */
    public void setMinify(boolean minify) {
        this.minify = minify;
    }

    private VultureCSSOptions options;

    /**
     * Get the value of options
     *
     * @return the value of options
     */
    public VultureCSSOptions getOptions() {
        return options;
    }

    /**
     * Set the value of options
     *
     * @param options new value of options
     */
    public void setOptions(VultureCSSOptions options) {
        this.options = options;
    }

}
