/*
 * The MIT License
 *
 * Copyright 2026 .Maui | dotmaui.com.
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

import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.getUsedRulesFromMediaRule;
import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.getUsedRulesFromSupportsRule;

import com.dotmaui.vulturecss.jstyleparser.VultureCSSWithjStyleParser;
import com.dotmaui.vulturecss.models.VultureCSSOptions;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.ECSSVersion;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSSupportsRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import com.helger.css.reader.CSSReader;
import com.helger.css.writer.CSSWriter;
import com.helger.css.writer.CSSWriterSettings;
import cz.vutbr.web.css.CSSException;
import java.io.IOException;

/**
 * Provides methods to compare CSS rules against HTML content and generate
 * a cleaned stylesheet containing only the rules that are actually used.
 *
 * If the initial CSS parsing fails, the CSS is processed with jStyleParser
 * and then parsed again.
 *
 * @author .Maui
 * @version 1.1
 */
public class CompareCSSHTML {

    /**
     * Processes the provided HTML and CSS and returns a stylesheet containing
     * only the CSS rules used by the HTML.
     *
     * @param html    the HTML content used to check CSS selector usage
     * @param css     the raw CSS stylesheet to process
     * @param options the CSS processing options
     * @return the cleaned CSS stylesheet as a string
     * @throws IOException if an I/O error occurs while correcting the CSS
     * @throws IllegalArgumentException if the CSS cannot be parsed
     */
    public static String process(String html, String css, VultureCSSOptions options) throws IOException,CSSException  {

        CascadingStyleSheet initialCSS = parseCSS(css);

        if (initialCSS == null) {
            throw new IllegalArgumentException("Failed to parse CSS");
        }

        CascadingStyleSheet finalCSS = new CascadingStyleSheet();

        if (html == null || html.isBlank()) {
            return writeCSS(finalCSS, options);
        }

        VultureCSSCoreHTMLChecker htmlChecker = new VultureCSSCoreHTMLChecker(html, options);
        ICommonsList<ICSSTopLevelRule> rules = initialCSS.getAllRules();

        for (ICSSTopLevelRule rule : rules) {

            if (rule instanceof CSSStyleRule styleRule) {
                addStyleRuleIfUsed(finalCSS, styleRule, htmlChecker);

            } else if (rule instanceof CSSMediaRule mediaRule) {
                CSSMediaRule finalMediaRule = getUsedRulesFromMediaRule(mediaRule, htmlChecker);

                if (finalMediaRule != null) {
                    finalCSS.addRule(finalMediaRule);
                }

            } else if (rule instanceof CSSSupportsRule supportsRule) {
                CSSSupportsRule finalSupportsRule = getUsedRulesFromSupportsRule(supportsRule, htmlChecker);

                if (finalSupportsRule != null) {
                    finalCSS.addRule(finalSupportsRule);
                }

            } else {
                finalCSS.addRule(rule);
            }
        }

        return writeCSS(finalCSS, options);
    }

private static CascadingStyleSheet parseCSS(String css) throws IOException, CSSException {

    CascadingStyleSheet initialCSS = CSSReader.readFromString(css, ECSSVersion.LATEST);

    if (initialCSS != null) {
        return initialCSS;
    }

    String correctedCSS = VultureCSSWithjStyleParser.ParseCSS(css);
    return CSSReader.readFromString(correctedCSS, ECSSVersion.LATEST);
}

    private static void addStyleRuleIfUsed(
            CascadingStyleSheet finalCSS,
            CSSStyleRule styleRule,
            VultureCSSCoreHTMLChecker htmlChecker) {

        ICommonsList<CSSSelector> selectors = styleRule.getAllSelectors();

        if (selectors.isEmpty()) {
            return;
        }

        if (selectors.size() > 1) {
            CSSStyleRule resultRule = htmlChecker.multiSelectorControl(styleRule);

            if (resultRule != null && resultRule.hasDeclarations() && resultRule.hasSelectors()) {
                finalCSS.addRule(resultRule);
            }

            return;
        }

        String selector = selectors.get(0).getAsCSSString();

        if (htmlChecker.isSelectorUsed(selector)) {
            finalCSS.addRule(styleRule);
        }
    }

    private static String writeCSS(CascadingStyleSheet finalCSS, VultureCSSOptions options) {

        CSSWriterSettings writerSettings = new CSSWriterSettings(ECSSVersion.LATEST, false);

        if (options.isMinifyCSSOutput()) {
            writerSettings.setOptimizedOutput(true);
            writerSettings.setRemoveUnnecessaryCode(true);
        }

        CSSWriter cssWriter = new CSSWriter(writerSettings);
        cssWriter.setHeaderText("");

        return cssWriter.getCSSAsString(finalCSS);
    }
}
