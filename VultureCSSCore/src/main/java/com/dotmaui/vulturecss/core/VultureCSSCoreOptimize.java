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

import com.dotmaui.vulturecss.models.CarcassDeclaration;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.ICSSTopLevelRule;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author mauri
 */
public class VultureCSSCoreOptimize {

    public static CSSMediaRule OptimizeMediaRule(CSSMediaRule cssMediaRule) {

        CSSMediaRule cssMediaRuleNew = new CSSMediaRule();

        ICommonsList<ICSSTopLevelRule> allStyleRules = cssMediaRule.getAllRules();

        List<CSSStyleRule> allStyleRulesAsList = new ArrayList<>();

        for (ICSSTopLevelRule styleRule : allStyleRules) {

            CSSStyleRule cssStyleRule = (CSSStyleRule) styleRule;

            allStyleRulesAsList.add(cssStyleRule);
        }

        List<CSSStyleRule> allStyleRulesOptimizedAsList = OptimizeCSSStyleRules(allStyleRulesAsList);

        cssMediaRuleNew.addMediaQuery(cssMediaRule.getMediaQueryAtIndex(0));

        for (CSSStyleRule optimizedStyleRule : allStyleRulesOptimizedAsList) {
            cssMediaRuleNew.addRule(optimizedStyleRule);
        }

        return cssMediaRuleNew;

    }


    public static List<CSSStyleRule> OptimizeCSSStyleRules(List<CSSStyleRule> rules) {

        List<CSSStyleRule> allStyleRules = new ArrayList<>();

        
        
        for (ICSSTopLevelRule rule : rules) {

            CSSStyleRule cssStyleRule = (CSSStyleRule) rule;
                       

            // Ex: [color: red, color:orange, ...]
            List<CSSDeclaration> initialDeclarations = cssStyleRule.getAllDeclarations();

            // in this list I will insert the unique rules with which I will rebuild the css later
            List<CarcassDeclaration> carcassDeclarations = new ArrayList<>();

            for (int i = initialDeclarations.size() - 1; i >= 0; i--) {

                // Ex: color
                String selector = initialDeclarations.get(i).getAsCSSString().split(":")[0].trim();

                // Ex: red
                String definition = initialDeclarations.get(i).getAsCSSString().split(":")[1].trim();

                boolean finded = false;
                boolean isImportant = false;

                if (definition.endsWith("!important") || definition.endsWith("!important;")) {
                    isImportant = true;
                }

                
                
                for (CarcassDeclaration carcassDeclaration : carcassDeclarations) {

                    // Ex: color
                    String carcassSelector = carcassDeclaration.getDeclaration().getAsCSSString().split(":")[0].trim();

                    //System.out.println( selector  + " == " + carcassSelector );
                    
                    if (selector.equals(carcassSelector)) {

                        if (isImportant) {
                            carcassDeclaration.setDeclaration(initialDeclarations.get(i));
                        }

                        finded = true;

                    }

                }

                if (!finded) {

                    // Ex: (false, color, color:red).
                    carcassDeclarations.add(new CarcassDeclaration(isImportant, selector, initialDeclarations.get(i)));

                }
                
               // System.out.println( carcassDeclarations.size()  + " == "  );

            }

            CSSStyleRule newCssStyleRule = new CSSStyleRule();
            newCssStyleRule.addSelector(cssStyleRule.getSelectorAtIndex(0));

            for (CarcassDeclaration carcassDeclaration : carcassDeclarations) {

                newCssStyleRule.addDeclaration(carcassDeclaration.getDeclaration());

                //System.out.println("carcassDeclaration: " + carcassDeclaration.getDeclaration().getAsCSSString());
            }

            allStyleRules.add(newCssStyleRule);

        }

        return allStyleRules;

    }

}
