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

import com.dotmaui.vulturecss.models.VultureCSSOptions;
import com.dotmaui.vulturecss.models.WhiteListRule;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSWritableList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Selector;

public class VultureCSSCoreHTMLChecker {

    /**
     * CSS pseudo-classes and pseudo-elements to ignore during selector
     * matching.
     *
     * Jsoup does not support these selectors and throws a
     * {@link Selector.SelectorParseException}. They are removed before matching
     * so that only the underlying CSS selector is evaluated against the HTML.
     *
     * References:
     * https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-classes
     * https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-elements
     */
    private static final List<String> PSEUDO_CLASSES = List.of("::visited",
            "::valid",
            "::target",
            "::spelling-error",
            "::selection",
            "::scope",
            "::root",
            "::right",
            "::required",
            "::read-write",
            "::read-only",
            "::placeholder-shown",
            "::placeholder",
            "::out-of-range",
            "::optional",
            "::only-of-type",
            "::only-child",
            "::marker",
            "::link",
            "::left",
            "::last-of-type",
            "::last-child",
            "::invalid",
            "::indeterminate",
            "::in-range",
            "::hover",
            "::grammar-error",
            "::fullscreen",
            "::focus",
            "::first-of-type",
            "::first-line",
            "::first-letter",
            "::first-child",
            "::first",
            "::enabled",
            "::empty",
            "::disabled",
            "::default",
            "::cue",
            "::checked",
            "::before",
            "::backdrop",
            "::any",
            "::after",
            "::active",
            "::-webkit-slider-thumb",
            "::-webkit-slider-runnable-track",
            "::-webkit-progress-value",
            "::-webkit-progress-bar",
            "::-ms-value",
            "::-ms-track",
            "::-ms-tooltip",
            "::-ms-ticks-before",
            "::-ms-ticks-after",
            "::-ms-thumb",
            "::-ms-reveal",
            "::-ms-fill-upper",
            "::-ms-fill-lower",
            "::-ms-fill",
            "::-ms-expand",
            "::-ms-clear",
            "::-ms-check",
            "::-ms-browse",
            "::-moz-range-track",
            "::-moz-range-thumb",
            "::-moz-range-progress",
            "::-moz-progress-bar",
            ":visited",
            ":valid",
            ":target",
            ":scope",
            ":root",
            ":right",
            ":required",
            ":read-write",
            ":read-only",
            ":placeholder-shown",
            ":placeholder",
            ":out-of-range",
            ":optional",
            ":only-of-type",
            ":only-child",
            ":link",
            ":left",
            ":last-of-type",
            ":last-child",
            ":invalid",
            ":indeterminate",
            ":in-range",
            ":hover",
            ":fullscreen",
            ":focus-within",
            ":focus-visible",
            ":focus",
            ":first-of-type",
            ":first-line",
            ":first-letter",
            ":first-child",
            ":first",
            ":enabled",
            ":empty",
            ":disabled",
            ":defined",
            ":default",
            ":checked",
            ":blank",
            ":before",
            ":any-link",
            ":any",
            ":after",
            ":active",
            ":-ms-input-placeholder");

    private static final List<String> PSEUDO_CLASSES_WITH_BRACKETS = List.of(":where()",
            ":nth-col()",
            ":nth-last-col()",
            ":dir()",
            ":lang()",
            ":nth-of-type()",
            ":nth-last-of-type()",
            ":nth-last-child()",
            ":nth-child()",
            ":not()",
            "::where()",
            "::nth-col()",
            ":nth-last-col()",
            "::dir()",
            "::lang()",
            "::nth-of-type()",
            "::nth-last-of-type()",
            "::nth-last-child()",
            "::nth-child()",
            "::not()");

    private final Document htmlDocument;
    private final VultureCSSOptions options;

    /**
     * @param html
     * @param options
     */
    public VultureCSSCoreHTMLChecker(String html, VultureCSSOptions options) {

        this.htmlDocument = Jsoup.parse(html);
        this.options = options;

    }

    public boolean isSelectorUsed(String selector) {

        // Vendor pseudo-classes may cause parsing issues. They can be ignored by setting removeVendorPseudoClasses to true.
        if (this.removeVendorPseudoClasses
                && (selector.contains("::-moz")
                || selector.contains("::-webkit")
                || selector.contains("::-ms-"))) {

            return false;

        }

        String newSelector = removePseudoClasses(selector);

        // Pseudo classes like ::after and :before used globally (see bootstrap as example) would be in error.
        // Therefore they are always considered valid, they'll probably (if not definitely) be used.
        if (newSelector.equals("") || newSelector.startsWith(":")) {
            return true;
        }

        newSelector = newSelector.trim();

        if (newSelector.endsWith(">")) {
            newSelector = newSelector.substring(0, newSelector.length() - 1).trim();
        }

        // Whitelist.
        if (this.options.getWhiteListRules() != null) {

            for (WhiteListRule whiteListRule : this.options.getWhiteListRules()) {

                if (whiteListRule.getType() == WhiteListRule.CONTAINING && selector.toLowerCase().contains(whiteListRule.getSelector().toLowerCase())) {
                    return true;
                } else if (whiteListRule.getType() == WhiteListRule.EQUALS && whiteListRule.getSelector().toLowerCase().equals(selector.toLowerCase())) {
                    return true;
                }

            }

        }

        try {
            return !this.htmlDocument.select(newSelector).isEmpty();
        } catch (Selector.SelectorParseException e) {
            return true;
        }

    }

    /**
     *
     * @param rule
     * @return
     */
    public CSSStyleRule multiSelectorControl(CSSStyleRule rule) {

        ICommonsList<CSSSelector> allSelectors = rule.getAllSelectors();
        ICommonsList<CSSSelector> finalSelectors = new CSSWritableList<>();

        CSSStyleRule finalRule = new CSSStyleRule();

        for (CSSSelector selector : allSelectors) {

            String selectorAsString = selector.getAsCSSString().trim();

            if (!selectorAsString.isEmpty()) {

                if (isSelectorUsed(selectorAsString)) {
                    finalSelectors.add(selector);
                }

            }

        }

        if (finalSelectors.size() > 0) {

            finalSelectors.forEach((selector) -> {
                finalRule.addSelector(selector);
            });

            rule.getAllDeclarations().forEach((declaration) -> {
                finalRule.addDeclaration(declaration);
            });

        }

        return finalRule;

    }

    /**
     *
     * @internal Examples of output of this function
     *
     * :root -> :root a:after -> a a[href^="javascript:"]:after ->
     * a[href^="javascript:"]
     *
     * @param selector
     * @return String
     */
    private String removePseudoClasses(String selector) {

        if ("".equals(selector) || selector.startsWith(":") || !selector.contains(":")) {
            return selector;
        }

        // Pseudo standard classes.
        for (String pseudo : VultureCSSCoreHTMLChecker.PSEUDO_CLASSES) {
            selector = selector.replace(pseudo, "");
        }

        // It is possible that pseudo classes are also used inside the not(),
        // so the result would be :not() which would obviously return error
        // Because the not can't be empty.
        for (String pseudo : VultureCSSCoreHTMLChecker.PSEUDO_CLASSES_WITH_BRACKETS) {
            selector = selector.replace(pseudo, "");
        }

        return selector;

    }

    private boolean removeVendorPseudoClasses;

    /**
     * Get the value of removeVendorPseudoClasses
     *
     * @return the value of removeVendorPseudoClasses
     */
    public boolean isRemoveVendorPseudoClasses() {
        return removeVendorPseudoClasses;
    }

    /**
     * Set the value of removeVendorPseudoClasses
     *
     * @param removeVendorPseudoClasses new value of removeVendorPseudoClasses
     */
    public void setRemoveVendorPseudoClasses(boolean removeVendorPseudoClasses) {
        this.removeVendorPseudoClasses = removeVendorPseudoClasses;
    }

}
