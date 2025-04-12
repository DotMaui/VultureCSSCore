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

import com.dotmaui.vulturecss.models.CarcassDeclaration;
import com.helger.css.decl.CSSDeclaration;
import com.helger.css.decl.CSSMediaRule;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.ICSSTopLevelRule;
import java.util.ArrayList;
import java.util.List;

public class VultureCSSCoreOptimize {

    /**
     * Optimizes a given CSSMediaRule by merging and deduplicating its contained CSSStyleRules.
     * 
     * @param cssMediaRule The CSSMediaRule to optimize.
     * @return A new optimized CSSMediaRule.
     */
    public static CSSMediaRule optimizeMediaRule(CSSMediaRule cssMediaRule) {

        CSSMediaRule optimizedMediaRule = new CSSMediaRule();
        List<CSSStyleRule> allStyleRules = new ArrayList<>();

        // Collect all style rules from the media rule
        for (ICSSTopLevelRule rule : cssMediaRule.getAllRules()) {
            allStyleRules.add((CSSStyleRule) rule);
        }

        // Optimize the style rules by removing duplicates
        List<CSSStyleRule> optimizedStyleRules = optimizeCSSStyleRules(allStyleRules);

        // Add the media query to the optimized media rule
        optimizedMediaRule.addMediaQuery(cssMediaRule.getMediaQueryAtIndex(0));

        // Add the optimized style rules to the new media rule
        for (CSSStyleRule optimizedRule : optimizedStyleRules) {
            optimizedMediaRule.addRule(optimizedRule);
        }

        return optimizedMediaRule;
    }

    /**
     * Optimizes a list of CSSStyleRules by removing duplicate declarations and handling
     * !important rules correctly.
     * 
     * @param rules The list of CSSStyleRules to optimize.
     * @return A list of optimized CSSStyleRules.
     */
    public static List<CSSStyleRule> optimizeCSSStyleRules(List<CSSStyleRule> rules) {

        List<CSSStyleRule> optimizedStyleRules = new ArrayList<>();

        // Iterate over each rule to optimize its declarations
        for (CSSStyleRule cssStyleRule : rules) {

            List<CSSDeclaration> originalDeclarations = cssStyleRule.getAllDeclarations();
            List<CarcassDeclaration> optimizedDeclarations = new ArrayList<>();

            // Process declarations in reverse order to ensure latest declarations take precedence
            for (int i = originalDeclarations.size() - 1; i >= 0; i--) {

                CSSDeclaration declaration = originalDeclarations.get(i);
                String property = declaration.getAsCSSString().split(":")[0].trim();
                String value = declaration.getAsCSSString().split(":")[1].trim();
                boolean isImportant = value.endsWith("!important") || value.endsWith("!important;");

                boolean found = false;

                // Check if the property has already been processed
                for (CarcassDeclaration optimizedDeclaration : optimizedDeclarations) {
                    String existingProperty = optimizedDeclaration.getDeclaration().getAsCSSString().split(":")[0].trim();

                    // If property matches and the new declaration is !important, replace the existing one
                    if (property.equals(existingProperty)) {
                        if (isImportant) {
                            optimizedDeclaration.setDeclaration(declaration);
                        }
                        found = true;
                        break;
                    }
                }

                // If property was not found, add it to the list of optimized declarations
                if (!found) {
                    optimizedDeclarations.add(new CarcassDeclaration(isImportant, property, declaration));
                }
            }

            // Create a new CSSStyleRule with optimized declarations
            CSSStyleRule optimizedStyleRule = new CSSStyleRule();

            // Copy the selectors from the original rule
            for (CSSSelector selector : cssStyleRule.getAllSelectors()) {
                optimizedStyleRule.addSelector(selector);
            }

            // Add the optimized declarations to the new rule
            for (CarcassDeclaration optimizedDeclaration : optimizedDeclarations) {
                optimizedStyleRule.addDeclaration(optimizedDeclaration.getDeclaration());
            }

            // Add the optimized rule to the final list
            optimizedStyleRules.add(optimizedStyleRule);
        }

        return optimizedStyleRules;
    }
}

