/*
 * The MIT License
 *
 * Copyright 2020 .Maui | dotmaui.com.
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

import com.helger.css.ECSSVersion;

/**
 *
 * @author .Maui
 */
public class VultureCSSOptions {

    private String DotMauiApiKey = null;

    /**
     * Get the value of dotMauiApiKey
     *
     * @return the value of dotMauiApiKey
     */
    public String getDotMauiApiKey() {
        return DotMauiApiKey;
    }

    /**
     * Set the value of dotMauiApiKey
     *
     * @param dotMauiApiKey new value of dotMauiApiKey
     */
    public void setDotMauiApiKey(String dotMauiApiKey) {
        this.DotMauiApiKey = dotMauiApiKey;
    }

    private boolean CDNMode = false;

    /**
     * Get the value of cdnMode
     *
     * @return the value of cdnMode
     */
    public boolean isCdnMode() {
        return CDNMode;
    }

    /**
     * Set the value of cdnMode
     *
     * @param cdnMode new value of cdnMode
     */
    public void setCdnMode(boolean cdnMode) {
        this.CDNMode = cdnMode;
    }

    private boolean UseStaticHTMLFromWebPage = true;

    public VultureCSSOptions() {
        this.ECSSVersion = ECSSVersion.LATEST;
    }

    /**
     * Get the value of UseStaticHTMLFromWebPage
     *
     * @return the value of UseStaticHTMLFromWebPage
     */
    public boolean isUseStaticHTMLFromWebPage() {
        return this.UseStaticHTMLFromWebPage;
    }

    /**
     * Set the value of UseStaticHTMLFromWebPage
     *
     * @param UseStaticHTMLFromWebPage new value of UseStaticHTMLFromWebPage
     */
    public void setUseStaticHTMLFromWebPage(boolean UseStaticHTMLFromWebPage) {
        this.UseStaticHTMLFromWebPage = UseStaticHTMLFromWebPage;
    }

    private boolean MinifyCSSOutput = true;

    /**
     * Get the value of MinifyCSSOutput
     *
     * @return the value of MinifyCSSOutput
     */
    public boolean isMinifyCSSOutput() {
        return this.MinifyCSSOutput;
    }

    /**
     * Set the value of MinifyCSSOutput
     *
     * @param MinifyCSSOutput new value of MinifyCSSOutput
     */
    public void setMinifyCSSOutput(boolean MinifyCSSOutput) {
        this.MinifyCSSOutput = MinifyCSSOutput;
    }

    private ECSSVersion ECSSVersion;

    /**
     * Get the value of ECSSVersion
     *
     * @return the value of ECSSVersion
     */
    public ECSSVersion getECSSVersion() {
        return ECSSVersion;
    }

    /**
     * Set the value of ECSSVersion
     *
     * @param ECSSVersion new value of ECSSVersion
     */
    public void setECSSVersion(ECSSVersion ECSSVersion) {
        this.ECSSVersion = ECSSVersion;
    }

}
