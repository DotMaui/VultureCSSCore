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

package com.dotmaui.vulturecss.utils;

import com.yahoo.platform.yui.compressor.CssCompressor;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;

public final class MinifyWithYUI {

    public static String Process(String css) {

        StringWriter stringWriter = new StringWriter(1024);

        try {

            CssCompressor compressor = new CssCompressor(new StringReader(css));
            compressor.compress(stringWriter, 8000);

        } catch (IOException ex) {
            //System.err.println(ex.toString());
        }

        return stringWriter.toString();

    }

}
