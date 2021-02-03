/*
 * The MIT License
 *
 * Copyright 2021 .Maui | dotmaui.com.
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

import com.dotmaui.vulturecss.models.Carcass;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CSSWritableList;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.reader.CSSReader;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class VultureCSSCoreParser {
    //private static final Logger LOGGER = LoggerFactory.getLogger (VultureCSSCoreParser.class);

    /**
     *
     * @param css
     * @return
     * @throws UnsupportedEncodingException
     * @throws IOException
     */
    public static ICommonsList<ICSSTopLevelRule> GetRulesFromString(String css) throws UnsupportedEncodingException, IOException {

        CascadingStyleSheet readFromString = CSSReader.readFromString(css, ECSSVersion.LATEST);
        ICommonsList<ICSSTopLevelRule> allRules = null;

        if (readFromString != null) {
            allRules = readFromString.getAllRules();
        } 
        
        return allRules;

    }

    public static CSSStyleRule CheckIfRuleIsUsed(CSSStyleRule rule, VultureCSSCoreHTMLChecker htmlChecker) {

        CSSStyleRule finalRule = null;

        ICommonsList<CSSSelector> allSelectors = rule.getAllSelectors();

        if (allSelectors.size() > 1) {

            finalRule = htmlChecker.multiSelectorControl((CSSStyleRule) rule);

        } else if (htmlChecker.isSelectorUsed(allSelectors.get(0).getAsCSSString())) {

            finalRule = rule;

        }

        if (finalRule != null && finalRule.hasDeclarations() && finalRule.hasSelectors()) {
            return finalRule;
        } else {
            return null;
        }

    }

    /**
     *
     * @param mediaRule
     * @param htmlChecker
     * @return
     */
    public static CSSMediaRule GetUsedRulesFromMediaRule(CSSMediaRule mediaRule, VultureCSSCoreHTMLChecker htmlChecker) {

        ICommonsList<CSSStyleRule> usedRules = new CSSWritableList<>();

        for (CSSStyleRule rule : mediaRule.getAllStyleRules()) {

            CSSStyleRule ruleIfUsedNullIfNot = CheckIfRuleIsUsed(rule, htmlChecker);

            if (ruleIfUsedNullIfNot != null) {
                usedRules.add(ruleIfUsedNullIfNot);
            }

        }

        if (usedRules.size() > 0) {

            CSSMediaRule finalMediaRule = new CSSMediaRule();

            mediaRule.getAllMediaQueries().forEach((mediaQuery) -> {
                finalMediaRule.addMediaQuery(mediaQuery);
            });

            usedRules.forEach((rule) -> {
                finalMediaRule.addRule(rule);
            });

            return finalMediaRule;
            
        } else {
            return null;

        }
    }

    /**
     *
     * @param supportsRule
     * @param htmlChecker
     * @return
     */
    public static CSSSupportsRule GetUsedRulesFromSupportsRule(CSSSupportsRule supportsRule, VultureCSSCoreHTMLChecker htmlChecker) {

        ICommonsList<CSSStyleRule> usedRules = new CSSWritableList<>();

        for (CSSStyleRule rule : supportsRule.getAllStyleRules()) {

            CSSStyleRule ruleIfUsedNullIfNot = CheckIfRuleIsUsed(rule, htmlChecker);

            if (ruleIfUsedNullIfNot != null) {
                usedRules.add(ruleIfUsedNullIfNot);
            }

        }

        if (usedRules.size() > 0) {

            CSSSupportsRule finalSupportsRule = new CSSSupportsRule();

            supportsRule.getAllSupportConditionMembers().forEach((supportsCondition) -> {
                finalSupportsRule.addSupportConditionMember(supportsCondition);
            });

            usedRules.forEach((rule) -> {
                finalSupportsRule.addRule(rule);
            });

            //used_css.append(finalMediaRule.getAsCSSString());
            return finalSupportsRule;
        } else {
            return null;

        }
    }

    public static List<Carcass> ExtractAllStyleSheetsUrls(String html, URL html_url) {

        List<Carcass> Carcasses = new ArrayList<>();

        Document doc = Jsoup.parse(html);
        Elements links_css = doc.select("link[rel='stylesheet']");

        for (Element link_css : links_css) {

            Carcass c = new Carcass();
            c.setPath(link_css.attr("href"));

            if (!c.getPath().startsWith("http") && html_url != null) {

                URL mergedURL;

                try {
                    mergedURL = new URL(html_url, c.getPath());
                    c.setPath(mergedURL.toString());
                } catch (MalformedURLException ex) {
                }

            }

            Carcasses.add(c);

        }

        return Carcasses;

    }

}
