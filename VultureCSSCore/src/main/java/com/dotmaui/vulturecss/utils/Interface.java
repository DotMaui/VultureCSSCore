/*
 * The MIT License
 *
 * Copyright 2019 .Maui | dotmaui.com.
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

import com.gargoylesoftware.htmlunit.Page;
import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.WebResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 *
 * @author .Maui
 */
public class Interface {

    /**
     *
     * @param url
     * @return
     */
    public static String DownloadFromUrl(URL url) {

        InputStream is = null;
        BufferedReader br;
        String line;
        String content = "";

        try {
            is = url.openStream();  // throws an IOException
            br = new BufferedReader(new InputStreamReader(is));

            while ((line = br.readLine()) != null) {
                content += line;
            }
        } catch (MalformedURLException mue) {
            return null;
        } catch (IOException ioe) {
            return null;
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } catch (IOException ioe) {
                // nothing to see here
            }
        }

        return content;
    }

    /**
     *
     * @param url
     * @return
     * @throws java.io.IOException
     */
    public static String DownloadRenderedPage(URL url) throws IOException {

        try (WebClient webClient = new WebClient()) {
            Page page = webClient.getPage(url);
            WebResponse response = page.getWebResponse();
            return response.getContentAsString();

        }

    }
    
    /**
     * @link https://howtodoinjava.com/java/io/java-read-file-to-string-examples/
     * @param filePath
     * @return 
     */
    public static String readLineByLineJava8(String filePath) 
{
    StringBuilder contentBuilder = new StringBuilder();
    try (Stream<String> stream = Files.lines( Paths.get(filePath), StandardCharsets.UTF_8)) 
    {
        stream.forEach(s -> contentBuilder.append(s).append("\n"));
    }
    catch (IOException e) 
    {
        e.printStackTrace();
    }
    return contentBuilder.toString();
}
 /**
 * Reads given resource file as a string.
 * @link https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java#46613809
 * @param fileName path to the resource file
 * @return the file's contents
 * @throws IOException if read fails for any reason
 */
public static String getResourceFileAsString(String fileName) throws IOException {
    ClassLoader classLoader = ClassLoader.getSystemClassLoader();
    try (InputStream is = classLoader.getResourceAsStream(fileName)) {
        if (is == null) return null;
        try (InputStreamReader isr = new InputStreamReader(is);
             BufferedReader reader = new BufferedReader(isr)) {
            return reader.lines().collect(Collectors.joining(System.lineSeparator()));
        }
    }
}
}
