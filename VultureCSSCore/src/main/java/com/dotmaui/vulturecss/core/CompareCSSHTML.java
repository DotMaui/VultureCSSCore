/*
 * The MIT License
 *
 * Copyright 2023 .Maui | dotmaui.com.
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

import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.GetUsedRulesFromMediaRule;
import static com.dotmaui.vulturecss.core.VultureCSSCoreParser.GetUsedRulesFromSupportsRule;
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
import java.io.IOException;

public class CompareCSSHTML {

    /**
     *
     * @param html
     * @param css
     * @param options
     * @return
     * @throws IOException
     */
    public static String Process(String html, String css, VultureCSSOptions options) throws IOException, Exception {

        CascadingStyleSheet finalCSS = new CascadingStyleSheet();
        CascadingStyleSheet initialCSS = CSSReader.readFromString(css, ECSSVersion.LATEST);

        // I try to get a correct CSS through jStyleParser.
        // ph-css returns null in case of unrecoverable errors in CSS. 
        // jStyleParser instead removes the invalid rule and correctly parsers the rest.
        // I don't use jStyleParser as the main parser because it seems less updated and with more bugs
        if (initialCSS == null) {

            String new_css = VultureCSSWithjStyleParser.ParseCSS(css);
            initialCSS = CSSReader.readFromString(new_css, ECSSVersion.LATEST);

        }

        if (initialCSS == null) {

            throw new Exception("Failed to parse CSS");

        } else if (html != null) {

            VultureCSSCoreHTMLChecker htmlChecker = new VultureCSSCoreHTMLChecker(html, options);

            ICommonsList<ICSSTopLevelRule> rules = initialCSS.getAllRules();

            for (int i = 0; i < rules.size(); i++) {

                if (rules.get(i) instanceof CSSStyleRule) {

                    CSSStyleRule rule = (CSSStyleRule) rules.get(i);

                    ICommonsList<CSSSelector> allSelectors = rule.getAllSelectors();

                    if (allSelectors.size() > 1) {

                        CSSStyleRule resultRule = htmlChecker.multiSelectorControl((CSSStyleRule) rule);

                        if (resultRule != null && resultRule.hasDeclarations() && resultRule.hasSelectors()) {
                            finalCSS.addRule(resultRule);
                        }

                    } else if (htmlChecker.isSelectorUsed(allSelectors.get(0).getAsCSSString())) {
                        finalCSS.addRule(rule);
                    }

                } else if ((rules.get(i) instanceof CSSMediaRule)) {

                    CSSMediaRule mediaRule = (CSSMediaRule) rules.get(i);
                    CSSMediaRule finalMediaRule = GetUsedRulesFromMediaRule(mediaRule, htmlChecker);

                    if (finalMediaRule != null) {
                        finalCSS.addRule(finalMediaRule);
                    }

                } else if ((rules.get(i) instanceof CSSSupportsRule)) {

                    CSSSupportsRule supportsRule = (CSSSupportsRule) rules.get(i);
                    CSSSupportsRule finalsupportsRule = GetUsedRulesFromSupportsRule(supportsRule, htmlChecker);

                    if (finalsupportsRule != null) {
                        finalCSS.addRule(finalsupportsRule);
                    }

                } else {
                    finalCSS.addRule(rules.get(i));
                }

            }

        }

        final CSSWriterSettings aSettings;
        aSettings = new CSSWriterSettings(ECSSVersion.LATEST, false);

        if (options.isMinifyCSSOutput()) {
            aSettings.setOptimizedOutput(true);
            aSettings.setRemoveUnnecessaryCode(true);
        }

        final CSSWriter aWriter = new CSSWriter(aSettings);
        //aWriter.setContentCharset(StandardCharsets.UTF_8.name());
        aWriter.setHeaderText("");

        return aWriter.getCSSAsString(finalCSS);
    }

}
