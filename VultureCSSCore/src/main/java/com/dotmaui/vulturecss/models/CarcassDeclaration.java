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
package com.dotmaui.vulturecss.models;

import com.helger.css.decl.CSSDeclaration;

/**
 *
 * @author .Maui
 */
public class CarcassDeclaration {

    public CarcassDeclaration(boolean isImportant, String selector, CSSDeclaration declaration) {
        this.isImportant = isImportant;
        this.selector = selector;
        this.declaration = declaration;
    }

    private boolean isImportant;

    /**
     * Get the value of isImportant
     *
     * @return the value of isImportant
     */
    public boolean isIsImportant() {
        return isImportant;
    }

    /**
     * Set the value of isImportant
     *
     * @param isImportant new value of isImportant
     */
    public void setIsImportant(boolean isImportant) {
        this.isImportant = isImportant;
    }

    private String selector;

    /**
     * Get the value of selector
     *
     * @return the value of selector
     */
    public String getSelector() {
        return selector;
    }

    /**
     * Set the value of selector
     *
     * @param selector new value of selector
     */
    public void setSelector(String selector) {
        this.selector = selector;
    }

    private CSSDeclaration declaration;

    /**
     * Get the value of declaration
     *
     * @return the value of declaration
     */
    public CSSDeclaration getDeclaration() {
        return declaration;
    }

    /**
     * Set the value of declaration
     *
     * @param declaration new value of declaration
     */
    public void setDeclaration(CSSDeclaration declaration) {
        this.declaration = declaration;
    }

}
