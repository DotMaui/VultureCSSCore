/*
 * The MIT License
 *
 * Copyright 2024 .Maui | dotmaui.com.
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

public class Carcass {

    private String cdnUrl = null;

    /**
     * Get the value of cdnUrl
     *
     * @return the value of cdnUrl
     */
    public String getCdnUrl() {
        return cdnUrl;
    }

    /**
     * Set the value of cdnUrl
     *
     * @param cdnUrl new value of cdnUrl
     */
    public void setCdnUrl(String cdnUrl) {
        this.cdnUrl = cdnUrl;
    }

    private String path;

    /**
     * Get the value of path
     *
     * @return the value of path
     */
    public String getPath() {
        return path;
    }

    /**
     * Set the value of path
     *
     * @param path new value of path
     */
    public void setPath(String path) {
        this.path = path;
    }

    private String usedCSS;

    /**
     * Get the value of usedCSS
     *
     * @return the value of usedCSS
     */
    public String getUsedCSS() {
        return usedCSS;
    }

    /**
     * Set the value of usedCSS
     *
     * @param usedCSS new value of usedCSS
     */
    public void setUsedCSS(String usedCSS) {
        this.usedCSS = usedCSS;
    }

    private boolean parseError = false;

    /**
     * Get the value of parseError
     *
     * @return the value of parseError
     */
    public boolean isParseError() {
        return parseError;
    }

    /**
     * Set the value of parseError
     *
     * @param parseError new value of parseError
     */
    public void setParseError(boolean parseError) {
        this.parseError = parseError;
    }

}
