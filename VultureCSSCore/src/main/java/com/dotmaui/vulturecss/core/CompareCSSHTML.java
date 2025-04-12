/*
 * The MIT License
 *
 * Copyright 2025 .Maui | dotmaui.com.
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
import java.io.IOException;

/**
 * This class provides methods to process and filter CSS based on HTML usage.
 * The main function reads CSS rules and removes unused rules based on the HTML content provided.
 * If the initial CSS parsing fails, it attempts to fix it using a secondary parser.
 * 
 * @version 1.1
 * @author .Maui
 */
public class CompareCSSHTML {

    /**
     * Processes the provided HTML and CSS to create a cleaned-up CSS stylesheet containing only
     * the rules used within the HTML.
     *
     * @param html     The HTML content to check the CSS against.
     * @param css      The raw CSS stylesheet to be processed.
     * @param options  The configuration options for the CSS processing.
     * @return         A string containing the minimized CSS based on the provided HTML.
     * @throws IOException If an I/O error occurs during the process.
     * @throws Exception   If CSS parsing fails or an unexpected error occurs.
     */
    public static String Process(String html, String css, VultureCSSOptions options) throws IOException, Exception {

        // Create a final CSS stylesheet that will contain the used rules.
        CascadingStyleSheet finalCSS = new CascadingStyleSheet();

        // Read the initial CSS stylesheet using ph-css.
        CascadingStyleSheet initialCSS = CSSReader.readFromString(css, ECSSVersion.LATEST);

        // If ph-css fails to parse the CSS (returns null), use jStyleParser to correct and re-parse.
        if (initialCSS == null) {
            String correctedCSS = VultureCSSWithjStyleParser.ParseCSS(css);
            initialCSS = CSSReader.readFromString(correctedCSS, ECSSVersion.LATEST);
        }

        // If the CSS is still null, throw an exception indicating a failure to parse the CSS.
        if (initialCSS == null) {
            throw new Exception("Failed to parse CSS");
        } else if (html != null) {

            // Create an HTML checker instance to verify the usage of CSS rules in the provided HTML.
            VultureCSSCoreHTMLChecker htmlChecker = new VultureCSSCoreHTMLChecker(html, options);

            // Get all the top-level rules from the initial CSS.
            ICommonsList<ICSSTopLevelRule> rules = initialCSS.getAllRules();

            // Iterate over each CSS rule and check if it is used within the HTML content.
            for (int i = 0; i < rules.size(); i++) {
                ICSSTopLevelRule rule = rules.get(i);

                if (rule instanceof CSSStyleRule styleRule) {

                    // Check all selectors in the style rule.
                    ICommonsList<CSSSelector> allSelectors = styleRule.getAllSelectors();

                    // Handle multiple selectors in a single rule.
                    if (allSelectors.size() > 1) {
                        CSSStyleRule resultRule = htmlChecker.multiSelectorControl(styleRule);
                        if (resultRule != null && resultRule.hasDeclarations() && resultRule.hasSelectors()) {
                            finalCSS.addRule(resultRule);
                        }
                    } else if (htmlChecker.isSelectorUsed(allSelectors.get(0).getAsCSSString())) {
                        finalCSS.addRule(styleRule);
                    }

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
        }

        // Configure CSS writer settings.
        final CSSWriterSettings writerSettings = new CSSWriterSettings(ECSSVersion.LATEST, false);

        if (options.isMinifyCSSOutput()) {
            writerSettings.setOptimizedOutput(true);
            writerSettings.setRemoveUnnecessaryCode(true);
        }

        // Write the final CSS as a string.
        final CSSWriter cssWriter = new CSSWriter(writerSettings);
        cssWriter.setHeaderText(""); // Set header text to an empty string.

        return cssWriter.getCSSAsString(finalCSS);
    }
}

