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

import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.CSSUtility.checkIfRuleIsUsed;
import com.dotmaui.vulturecss.models.Carcass;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.reader.CSSReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Optional;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.commons.collection.impl.CommonsArrayList;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import java.net.URL;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;

public class VultureCSSCoreParser {

    /**
     * Utility class for CSS rule extraction and validation from a string
     * representation.
     */
    public class CSSUtility {

        /**
         * Parses the given CSS string and returns a list of top-level rules.
         *
         * @param css The CSS string to be parsed.
         * @return A list of top-level CSS rules, or an empty list if the
         * parsing fails.
         * @throws UnsupportedEncodingException if the encoding is unsupported.
         * @throws IOException if an I/O error occurs.
         */
        public static ICommonsList<ICSSTopLevelRule> getRulesFromString(String css) throws UnsupportedEncodingException, IOException {
            // Read the CSS string and return all top-level rules
            return Optional.ofNullable(CSSReader.readFromString(css, ECSSVersion.LATEST))
                    .map(CascadingStyleSheet::getAllRules)
                    .orElse(new CommonsArrayList<>()); // Return empty list if null
        }

        /**
         * Checks if the given CSSStyleRule is used in the HTML based on the
         * selectors.
         *
         * @param rule The CSS rule to check.
         * @param htmlChecker The checker instance responsible for validating
         * the rule against HTML.
         * @return The rule if used, otherwise null.
         */
        public static CSSStyleRule checkIfRuleIsUsed(CSSStyleRule rule, VultureCSSCoreHTMLChecker htmlChecker) {
            // Get all selectors from the rule
            ICommonsList<CSSSelector> allSelectors = rule.getAllSelectors();

            // Handle multi-selector rules
            if (allSelectors.size() > 1) {
                return htmlChecker.multiSelectorControl(rule);
            }

            // Handle single-selector rules
            if (htmlChecker.isSelectorUsed(allSelectors.get(0).getAsCSSString())) {
                return rule;
            }

            // Return the rule if it has both selectors and declarations, otherwise null
            return (rule.hasDeclarations() && rule.hasSelectors()) ? rule : null;
        }
    }

    /**
     * Extracts and returns a CSSMediaRule containing only the style rules that
     * are used in the provided HTML context.
     *
     * @param mediaRule The media rule to check for used CSS rules.
     * @param htmlChecker An instance of VultureCSSCoreHTMLChecker used to
     * verify if the CSS selectors are used.
     * @return A new CSSMediaRule containing only the used rules, or null if no
     * rules are used.
     */
    public static CSSMediaRule getUsedRulesFromMediaRule(CSSMediaRule mediaRule, VultureCSSCoreHTMLChecker htmlChecker) {

        // List to store the used CSSStyleRules
        ICommonsList<CSSStyleRule> usedRules = new CommonsArrayList<>();

        // Iterate through all style rules in the media rule
        for (CSSStyleRule rule : mediaRule.getAllStyleRules()) {

            // Check if the rule is used; if used, it will return the rule, otherwise null
            CSSStyleRule ruleIfUsed = checkIfRuleIsUsed(rule, htmlChecker);

            // If the rule is used, add it to the list of used rules
            if (ruleIfUsed != null) {
                usedRules.add(ruleIfUsed);
            }
        }

        // If there are any used rules, return a new media rule containing them
        if (!usedRules.isEmpty()) {

            // Create a new CSSMediaRule for storing the used rules
            CSSMediaRule finalMediaRule = new CSSMediaRule();

            // Add all media queries from the original rule to the new one
            mediaRule.getAllMediaQueries().forEach(finalMediaRule::addMediaQuery);

            // Add the used style rules to the new media rule
            usedRules.forEach(finalMediaRule::addRule);

            return finalMediaRule;

        } else {
            // Return null if no rules are used
            return null;
        }
    }

    /**
     * Filters and returns a CSSSupportsRule containing only the CSS rules that
     * are used in the provided HTML context.
     *
     * @param supportsRule The supports rule to be filtered for used CSS rules.
     * @param htmlChecker An instance of VultureCSSCoreHTMLChecker used to check
     * if the selectors are used in the HTML.
     * @return A new CSSSupportsRule containing only the used rules, or null if
     * no rules are used.
     */
    public static CSSSupportsRule getUsedRulesFromSupportsRule(CSSSupportsRule supportsRule, VultureCSSCoreHTMLChecker htmlChecker) {

        // List to store the used CSSStyleRules
        ICommonsList<CSSStyleRule> usedRules = new CommonsArrayList<>();

        // Iterate through all style rules in the supports rule
        for (CSSStyleRule rule : supportsRule.getAllStyleRules()) {

            // Check if the rule is used in the HTML context
            CSSStyleRule ruleIfUsed = checkIfRuleIsUsed(rule, htmlChecker);

            // If the rule is used, add it to the list of used rules
            if (ruleIfUsed != null) {
                usedRules.add(ruleIfUsed);
            }
        }

        // If any rules are used, return a new CSSSupportsRule with the used rules
        if (!usedRules.isEmpty()) {

            // Create a new CSSSupportsRule for storing the used rules
            CSSSupportsRule finalSupportsRule = new CSSSupportsRule();

            // Add all support condition members from the original rule to the new one
            supportsRule.getAllSupportConditionMembers().forEach(finalSupportsRule::addSupportConditionMember);

            // Add the used style rules to the new supports rule
            usedRules.forEach(finalSupportsRule::addRule);

            return finalSupportsRule;

        } else {
            // Return null if no rules are used
            return null;
        }
    }

    /**
     * Extracts and returns a list of all CSS stylesheet URLs from the given
     * HTML content.
     *
     * @param html The HTML content as a string.
     * @param htmlUrl The base URL of the HTML document to resolve relative
     * paths.
     * @return A list of Carcass objects, each containing the URL of a
     * stylesheet.
     */
    public static List<Carcass> extractAllStyleSheetsUrls(String html, URL htmlUrl) {

        // List to store the extracted CSS stylesheet URLs
        List<Carcass> carcasses = new ArrayList<>();

        // Parse the HTML document using Jsoup
        Document doc = Jsoup.parse(html);

        // Select all link elements with the rel attribute set to 'stylesheet'
        Elements cssLinks = doc.select("link[rel='stylesheet']");

        // Iterate through the selected link elements
        for (Element cssLink : cssLinks) {

            // Create a new Carcass object to store the stylesheet URL
            Carcass c = new Carcass();
            c.setPath(cssLink.attr("href"));

            // If the URL is relative, resolve it against the base URL
            if (!c.getPath().startsWith("http") && htmlUrl != null) {
                try {
                    URL mergedURL = new URL(htmlUrl, c.getPath());
                    c.setPath(mergedURL.toString());
                } catch (MalformedURLException ex) {
                    // Handle the exception (e.g., log or rethrow)
                }
            }

            // Add the Carcass object to the list
            carcasses.add(c);
        }

        return carcasses;
    }

}
