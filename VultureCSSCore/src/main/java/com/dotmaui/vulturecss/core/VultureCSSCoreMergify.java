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

/**
 *
 * @author .Maui
 */
public class VultureCSSCoreMergify {

    public static CascadingStyleSheet MergeRules(ICommonsList<ICSSTopLevelRule> rules) {

        // It will contain the merge of style sheets.
        CascadingStyleSheet newStyleSheetWithAllDeclarations = new CascadingStyleSheet();
        int importRulesIndex = 0;

        List<String> alreadyInserted = new ArrayList<>();

        // [CSSStyleRule@0x003108bc: selectors=[[CSSSelector@0x6b09bb57: members=[[value=p; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=1; endLine=1; endColumn=1]; lastTokenArea=[beginLine=1; beginColumn=1; endLine=1; endColumn=1]]]]; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=1; endLine=1; endColumn=1]; lastTokenArea=[beginLine=1; beginColumn=1; endLine=1; endColumn=1]]]]; declarations=[[[CSSDeclaration@0x6536e911: property=color; expression=[members=[[value=red; optimizedValue=red; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=16; endLine=1; endColumn=18]; lastTokenArea=[beginLine=1; beginColumn=16; endLine=1; endColumn=18]]]]; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=16; endLine=1; endColumn=18]; lastTokenArea=[beginLine=1; beginColumn=16; endLine=1; endColumn=18]]]; important=false; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=9; endLine=1; endColumn=13]; lastTokenArea=[beginLine=1; beginColumn=16; endLine=1; endColumn=18]]]]]; SourceLocation=[firstTokenArea=[beginLine=1; beginColumn=1; endLine=1; endColumn=1]; lastTokenArea=[beginLine=1; beginColumn=25; endLine=1; endColumn=25]]]
        for (ICSSTopLevelRule rule : rules) {

            if (rule instanceof CSSMediaRule) {

                CSSMediaRule cssMediaRule = (CSSMediaRule) rule;

                String mediaQueryDef = cssMediaRule.getAllMediaQueries().get(0).getAsCSSString();

                if (alreadyInserted.contains(mediaQueryDef) == false) {

                    int endCulumn = cssMediaRule.getSourceLocation().getLastTokenEndColumnNumber();

                    for (ICSSTopLevelRule ruleCompare : rules) {

                        if (ruleCompare instanceof CSSMediaRule) {

                            CSSMediaRule cssMediaRuleCompare = (CSSMediaRule) ruleCompare;

                            String mediaQueryCompareDef = cssMediaRuleCompare.getAllMediaQueries().get(0).getAsCSSString();

                            int endCulumnCompare = cssMediaRuleCompare.getSourceLocation().getLastTokenEndColumnNumber();

                            if (mediaQueryDef.equals(mediaQueryCompareDef) && endCulumnCompare > endCulumn) {

                                alreadyInserted.add(mediaQueryDef);

                                for (int i = 0; i < cssMediaRuleCompare.getAllStyleRules().size(); i++) {

                                    cssMediaRule.addRule(cssMediaRuleCompare.getAllStyleRules().get(i));
                                }

                            }

                        }

                    }

                    newStyleSheetWithAllDeclarations.addRule(cssMediaRule);
                }

            } else if (rule instanceof CSSStyleRule) {

                CSSStyleRule cssStyleRule = (CSSStyleRule) rule;
                String allSelector = "";

                for (CSSSelector selector : cssStyleRule.getAllSelectors()) {
                    allSelector = allSelector.concat(selector.getAsCSSString()).concat(",");
                }

                allSelector = allSelector.substring(0, allSelector.length() - 1);

                if (alreadyInserted.contains(allSelector) == false && cssStyleRule.getDeclarationAtIndex(0) != null) {

                    int endCulumn = cssStyleRule.getDeclarationAtIndex(0).getSourceLocation().getLastTokenEndColumnNumber();

                    for (ICSSTopLevelRule ruleCompare : rules) {

                        if (ruleCompare instanceof CSSStyleRule) {

                            CSSStyleRule cssStyleRuleCompare = (CSSStyleRule) ruleCompare;

                            String allSelectorCompare = "";//cssStyleRuleCompare.getAllSelectors().get(0).getAsCSSString();

                            for (CSSSelector selector : cssStyleRuleCompare.getAllSelectors()) {
                                allSelectorCompare = allSelectorCompare.concat(selector.getAsCSSString()).concat(",");
                            }

                            allSelectorCompare = allSelectorCompare.substring(0, allSelectorCompare.length() - 1);

                            // If null is an empty rule, ex. .maui {}
                            if (cssStyleRuleCompare.getDeclarationAtIndex(0) != null) {

                                int endCulumnCompare = cssStyleRuleCompare.getDeclarationAtIndex(0).getSourceLocation().getLastTokenEndColumnNumber();

                                if (allSelector.equals(allSelectorCompare) && endCulumnCompare > endCulumn) {

                                    alreadyInserted.add(allSelector);

                                    for (CSSDeclaration declaration : cssStyleRuleCompare.getAllDeclarations()) {
                                        cssStyleRule.addDeclaration(declaration);

                                    }

                                }

                            }
                            
                        }

                    }

                    newStyleSheetWithAllDeclarations.addRule(cssStyleRule);

                }

            } else if (rule instanceof CSSImportRule) {

                newStyleSheetWithAllDeclarations.addImportRule(importRulesIndex, (CSSImportRule) rule);

                importRulesIndex++;

            } else if (rule != null) {

                // CSSViewportRule and others.
                newStyleSheetWithAllDeclarations.addRule(rule);

            } else {

                throw new java.lang.UnsupportedOperationException();

            }

        }

        ICommonsList<ICSSTopLevelRule> allRules = newStyleSheetWithAllDeclarations.getAllRules();

        for (ICSSTopLevelRule rule : allRules) {

            if (rule instanceof CSSMediaRule) {

                CascadingStyleSheet styleSheetTmp = new CascadingStyleSheet();

                CSSMediaRule cssMediaRule = (CSSMediaRule) rule;

                for (CSSStyleRule optimized : cssMediaRule.getAllStyleRules()) {
                    styleSheetTmp.addRule(optimized);
                }

                cssMediaRule.removeAllRules();

                CascadingStyleSheet styleSheetTmp2 = MergeRules(styleSheetTmp.getAllRules());

                for (CSSStyleRule optimized : styleSheetTmp2.getAllStyleRules()) {
                    cssMediaRule.addRule(optimized);
                }

            }

        }

        return newStyleSheetWithAllDeclarations;

    }

}
