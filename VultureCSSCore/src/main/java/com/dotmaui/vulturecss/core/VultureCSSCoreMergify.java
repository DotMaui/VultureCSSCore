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

import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSImportRule;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CascadingStyleSheet;
import com.helger.css.decl.ICSSTopLevelRule;
import java.util.ArrayList;
import java.util.List;

public class VultureCSSCoreMergify {

    /**
     * Merges and optimizes CSS rules from a list of ICSSTopLevelRule.
     *
     * @param rules The list of CSS top-level rules to merge.
     * @return A CascadingStyleSheet containing the merged rules.
     */
    public static CascadingStyleSheet MergeRules(ICommonsList<ICSSTopLevelRule> rules) {

        CascadingStyleSheet newStyleSheetWithAllDeclarations = new CascadingStyleSheet();
        int importRulesIndex = 0;
        List<String> alreadyInserted = new ArrayList<>();

        // Iterate over each rule to combine CSS rules intelligently
        for (ICSSTopLevelRule rule : rules) {

            if (rule instanceof CSSMediaRule cssMediaRule) {
                String mediaQueryDef = cssMediaRule.getAllMediaQueries().get(0).getAsCSSString();

                if (!alreadyInserted.contains(mediaQueryDef)) {
                    int endColumn = cssMediaRule.getSourceLocation().getLastTokenEndColumnNumber();

                    // Merge media queries with the same definition
                    for (ICSSTopLevelRule ruleCompare : rules) {
                        if (ruleCompare instanceof CSSMediaRule cssMediaRuleCompare) {
                            String mediaQueryCompareDef = cssMediaRuleCompare.getAllMediaQueries().get(0).getAsCSSString();
                            int endColumnCompare = cssMediaRuleCompare.getSourceLocation().getLastTokenEndColumnNumber();

                            if (mediaQueryDef.equals(mediaQueryCompareDef) && endColumnCompare > endColumn) {
                                alreadyInserted.add(mediaQueryDef);

                                // Add all style rules from the comparison rule
                                for (CSSStyleRule styleRule : cssMediaRuleCompare.getAllStyleRules()) {
                                    cssMediaRule.addRule(styleRule);
                                }
                            }
                        }
                    }

                    // Add the merged media rule to the final stylesheet
                    newStyleSheetWithAllDeclarations.addRule(cssMediaRule);
                }

            } else if (rule instanceof CSSStyleRule cssStyleRule) {

                // Combine CSSStyleRules with the same selector
                StringBuilder allSelector = new StringBuilder();
                for (CSSSelector selector : cssStyleRule.getAllSelectors()) {
                    allSelector.append(selector.getAsCSSString()).append(",");
                }

                String selectorString = allSelector.substring(0, allSelector.length() - 1);

                if (!alreadyInserted.contains(selectorString) && cssStyleRule.getDeclarationAtIndex(0) != null) {
                    int endColumn = cssStyleRule.getDeclarationAtIndex(0).getSourceLocation().getLastTokenEndColumnNumber();

                    // Compare and merge style rules with identical selectors
                    for (ICSSTopLevelRule ruleCompare : rules) {
                        if (ruleCompare instanceof CSSStyleRule cssStyleRuleCompare) {
                            StringBuilder allSelectorCompare = new StringBuilder();
                            for (CSSSelector selector : cssStyleRuleCompare.getAllSelectors()) {
                                allSelectorCompare.append(selector.getAsCSSString()).append(",");
                            }

                            String selectorCompareString = allSelectorCompare.substring(0, allSelectorCompare.length() - 1);

                            if (cssStyleRuleCompare.getDeclarationAtIndex(0) != null) {
                                int endColumnCompare = cssStyleRuleCompare.getDeclarationAtIndex(0).getSourceLocation().getLastTokenEndColumnNumber();

                                // If the selectors match and the current rule ends later, merge the declarations
                                if (selectorString.equals(selectorCompareString) && endColumnCompare > endColumn) {
                                    alreadyInserted.add(selectorString);

                                    // Add all declarations from the comparison rule
                                    for (CSSDeclaration declaration : cssStyleRuleCompare.getAllDeclarations()) {
                                        cssStyleRule.addDeclaration(declaration);
                                    }
                                }
                            }
                        }
                    }

                    newStyleSheetWithAllDeclarations.addRule(cssStyleRule);
                }

            } else if (rule instanceof CSSImportRule cSSImportRule) {
                // Handle import rules separately
                newStyleSheetWithAllDeclarations.addImportRule(importRulesIndex, cSSImportRule);
                importRulesIndex++;

            } else if (rule != null) {
                // Handle other types of CSS rules (e.g., viewport, supports)
                newStyleSheetWithAllDeclarations.addRule(rule);
            } else {
                throw new UnsupportedOperationException();
            }
        }

        // Optimize the media rules by merging nested style rules
        ICommonsList<ICSSTopLevelRule> allRules = newStyleSheetWithAllDeclarations.getAllRules();
        for (ICSSTopLevelRule rule : allRules) {
            if (rule instanceof CSSMediaRule cSSMediaRule) {
                CascadingStyleSheet tempStyleSheet = new CascadingStyleSheet();
                for (CSSStyleRule optimized : cSSMediaRule.getAllStyleRules()) {
                    tempStyleSheet.addRule(optimized);
                }
                cSSMediaRule.removeAllRules();
                CascadingStyleSheet mergedTempStyleSheet = MergeRules(tempStyleSheet.getAllRules());
                for (CSSStyleRule optimized : mergedTempStyleSheet.getAllStyleRules()) {
                    cSSMediaRule.addRule(optimized);
                }
            }
        }

        return newStyleSheetWithAllDeclarations;
    }

}
