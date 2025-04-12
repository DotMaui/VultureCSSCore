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

import com.dotmaui.vulturecss.models.VultureCSSOptions;
import com.dotmaui.vulturecss.models.WhiteListRule;
import com.helger.commons.collection.impl.ICommonsList;
import com.helger.css.decl.CSSSelector;
import com.helger.css.decl.CSSStyleRule;
import com.helger.css.decl.CSSWritableList;
import java.util.ArrayList;
import java.util.List;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;
import org.jsoup.select.Selector;

public class VultureCSSCoreHTMLChecker {

    /**
     * Pseudo-classes and Pseudo-elements are not considered, they are removed
     * from the selector.
     */
    private final List<String> pseudoClasses = new ArrayList<>();
    private final List<String> pseudoClassesWithbrackets = new ArrayList<>();
    private final Document htmlDocument;
    private final VultureCSSOptions options;

    /**
     * @param html
     * @param options
     */
    public VultureCSSCoreHTMLChecker(String html, VultureCSSOptions options) {

        this.htmlDocument = Jsoup.parse(html);
        this.options = options;

        /**
         * Jsoup raises an exception when pseudo-classes and pseudo-elements are
         * present in the selector. To avoid this I exclude pseudo-classes and
         * pseudo-elements in the selection through Jsoup, what is important to
         * verify is the selector.
         *
         * @link https://developer.mozilla.org/fr/docs/Web/CSS/Pseudo-classes
         * @link https://developer.mozilla.org/en-US/docs/Web/CSS/Pseudo-elements
         */
        pseudoClasses.add("::visited");
        pseudoClasses.add("::valid");
        pseudoClasses.add("::target");
        pseudoClasses.add("::spelling-error");
        pseudoClasses.add("::selection");
        pseudoClasses.add("::scope");
        pseudoClasses.add("::root");
        pseudoClasses.add("::right");
        pseudoClasses.add("::required");
        pseudoClasses.add("::read-write");
        pseudoClasses.add("::read-only");
        pseudoClasses.add("::placeholder-shown");
        pseudoClasses.add("::placeholder");
        pseudoClasses.add("::out-of-range");
        pseudoClasses.add("::optional");
        pseudoClasses.add("::only-of-type");
        pseudoClasses.add("::only-child");
        pseudoClasses.add("::marker");
        pseudoClasses.add("::link");
        pseudoClasses.add("::left");
        pseudoClasses.add("::last-of-type");
        pseudoClasses.add("::last-child");
        pseudoClasses.add("::invalid");
        pseudoClasses.add("::indeterminate");
        pseudoClasses.add("::in-range");
        pseudoClasses.add("::hover");
        pseudoClasses.add("::grammar-error");
        pseudoClasses.add("::fullscreen");
        pseudoClasses.add("::focus");
        pseudoClasses.add("::first-of-type");
        pseudoClasses.add("::first-line");
        pseudoClasses.add("::first-letter");
        pseudoClasses.add("::first-child");
        pseudoClasses.add("::first");
        pseudoClasses.add("::enabled");
        pseudoClasses.add("::empty");
        pseudoClasses.add("::disabled");
        pseudoClasses.add("::default");
        pseudoClasses.add("::cue");
        pseudoClasses.add("::checked");
        pseudoClasses.add("::before");
        pseudoClasses.add("::backdrop");
        pseudoClasses.add("::any");
        pseudoClasses.add("::after");
        pseudoClasses.add("::active");
        pseudoClasses.add("::-webkit-slider-thumb");
        pseudoClasses.add("::-webkit-slider-runnable-track");
        pseudoClasses.add("::-webkit-progress-value");
        pseudoClasses.add("::-webkit-progress-bar");
        pseudoClasses.add("::-ms-value");
        pseudoClasses.add("::-ms-track");
        pseudoClasses.add("::-ms-tooltip");
        pseudoClasses.add("::-ms-ticks-before");
        pseudoClasses.add("::-ms-ticks-after");
        pseudoClasses.add("::-ms-thumb");
        pseudoClasses.add("::-ms-reveal");
        pseudoClasses.add("::-ms-fill-upper");
        pseudoClasses.add("::-ms-fill-lower");
        pseudoClasses.add("::-ms-fill");
        pseudoClasses.add("::-ms-expand");
        pseudoClasses.add("::-ms-clear");
        pseudoClasses.add("::-ms-check");
        pseudoClasses.add("::-ms-browse");
        pseudoClasses.add("::-moz-range-track");
        pseudoClasses.add("::-moz-range-thumb");
        pseudoClasses.add("::-moz-range-progress");
        pseudoClasses.add("::-moz-progress-bar");
        pseudoClasses.add(":visited");
        pseudoClasses.add(":valid");
        pseudoClasses.add(":target");
        pseudoClasses.add(":scope");
        pseudoClasses.add(":root");
        pseudoClasses.add(":right");
        pseudoClasses.add(":required");
        pseudoClasses.add(":read-write");
        pseudoClasses.add(":read-only");
        pseudoClasses.add(":placeholder-shown");
        pseudoClasses.add(":placeholder");
        pseudoClasses.add(":out-of-range");
        pseudoClasses.add(":optional");
        pseudoClasses.add(":only-of-type");
        pseudoClasses.add(":only-child");
        pseudoClasses.add(":link");
        pseudoClasses.add(":left");
        pseudoClasses.add(":last-of-type");
        pseudoClasses.add(":last-child");
        pseudoClasses.add(":invalid");
        pseudoClasses.add(":indeterminate");
        pseudoClasses.add(":in-range");
        pseudoClasses.add(":hover");
        pseudoClasses.add(":fullscreen");
        pseudoClasses.add(":focus-within");
        pseudoClasses.add(":focus-visible");
        pseudoClasses.add(":focus");
        pseudoClasses.add(":first-of-type");
        pseudoClasses.add(":first-line");
        pseudoClasses.add(":first-letter");
        pseudoClasses.add(":first-child");
        pseudoClasses.add(":first");
        pseudoClasses.add(":enabled");
        pseudoClasses.add(":empty");
        pseudoClasses.add(":disabled");
        pseudoClasses.add(":defined");
        pseudoClasses.add(":default");
        pseudoClasses.add(":checked");
        pseudoClasses.add(":blank");
        pseudoClasses.add(":before");
        pseudoClasses.add(":any-link");
        pseudoClasses.add(":any");
        pseudoClasses.add(":after");
        pseudoClasses.add(":active");

        // Extra.
        pseudoClasses.add(":-ms-input-placeholder");

        pseudoClassesWithbrackets.add(":where()");
        pseudoClassesWithbrackets.add(":nth-col()");
        pseudoClassesWithbrackets.add(":nth-last-col()");
        pseudoClassesWithbrackets.add(":dir()");
        pseudoClassesWithbrackets.add(":lang()");
        pseudoClassesWithbrackets.add(":nth-of-type()");
        pseudoClassesWithbrackets.add(":nth-last-of-type()");
        pseudoClassesWithbrackets.add(":nth-last-child()");
        pseudoClassesWithbrackets.add(":nth-child()");
        pseudoClassesWithbrackets.add(":not()");
        pseudoClassesWithbrackets.add("::where()");
        pseudoClassesWithbrackets.add("::nth-col()");
        pseudoClassesWithbrackets.add(":nth-last-col()");
        pseudoClassesWithbrackets.add("::dir()");
        pseudoClassesWithbrackets.add("::lang()");
        pseudoClassesWithbrackets.add("::nth-of-type()");
        pseudoClassesWithbrackets.add("::nth-last-of-type()");
        pseudoClassesWithbrackets.add("::nth-last-child()");
        pseudoClassesWithbrackets.add("::nth-child()");
        pseudoClassesWithbrackets.add("::not()");

    }

    public boolean isSelectorUsed(String selector) {

        // Ownership classes, like :-moz go wrong. You can remove them by setting removeOwnershipClasses to true.    
        if (this.removeVendorPseudoClasses == true
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
            newSelector = newSelector.substring(0, newSelector.length() - 2);
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

        Elements elm;

        // If the search raises an exception, the rule is considered valid 
        // and included in the final result.
        try {
            elm = this.htmlDocument.select(newSelector);
        } catch (Selector.SelectorParseException e) {
            //System.out.println(selector);
            //System.out.println(newSelector);
            //System.err.println(e.toString());
            return true;
        }

        return (!elm.isEmpty());

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

            if (!"".equals(selectorAsString)) {

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
        for (String pseudo : this.pseudoClasses) {
            selector = selector.replace(pseudo, "");
        }

        // It is possible that pseudo classes are also used inside the not(),
        // so the result would be :not() which would obviously return error
        // Because the not can't be empty.
        for (String pseudo : this.pseudoClassesWithbrackets) {
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
