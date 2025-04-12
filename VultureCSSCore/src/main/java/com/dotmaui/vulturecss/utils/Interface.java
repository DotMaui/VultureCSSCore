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
package com.dotmaui.vulturecss.utils;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import org.htmlunit.Page;
import org.htmlunit.WebClient;
import org.htmlunit.WebResponse;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.charset.StandardCharsets;

public class Interface {

    /**
     * Downloads content from the given URL.
     *
     * @param url The URL from which to download content as a {@link URL}
     * object.
     * @return The content as a {@link String} or {@code null} if an error
     * occurs.
     */
    public static String downloadFromUrl(URL url) {
        StringBuilder content = new StringBuilder(); // Use StringBuilder for efficiency
        try {
            // Use try-with-resources to automatically close resources
            try (InputStream is = url.openStream(); BufferedReader br = new BufferedReader(new InputStreamReader(is))) {

                String line;
                while ((line = br.readLine()) != null) {
                    content.append(line); // Append each line to StringBuilder
                }
            }
        } catch (IOException ioe) {
            System.err.println("IO error: " + ioe.getMessage()); // Log the exception
            return null;
        }

        return content.toString(); // Convert StringBuilder to a string
    }

    /**
     * Downloads and returns the fully rendered content of a web page using a
     * headless browser. This method leverages the WebClient to load and render
     * the page, handling any JavaScript or dynamic content before returning the
     * complete HTML.
     *
     * <p>
     * It uses the `WebClient` from the HtmlUnit library to simulate a
     * browser-like experience. As such, it is more suitable for downloading
     * content from pages that require JavaScript execution to display the full
     * content (e.g., single-page applications).</p>
     *
     * @param url The URL of the web page to be downloaded as a {@link URL}
     * object. The URL should point to a valid and accessible web page.
     * @return The rendered HTML content of the web page as a {@link String}.
     * @throws IOException If an I/O error occurs during the downloading or
     * rendering process.
     */
    public static String downloadRenderedPage(URL url) throws IOException {

        try (WebClient webClient = new WebClient()) { // Using try-with-resources to ensure WebClient is closed
            Page page = webClient.getPage(url);
            WebResponse response = page.getWebResponse();
            return response.getContentAsString(); // Return the rendered HTML as a string
        }
    }

    /**
     * Reads the entire content of a file as a string using Java's
     * `Files.readString()` method. This method is more concise and performs
     * better with modern JDKs.
     *
     * <p>
     * The method uses UTF-8 as the character encoding to read the file
     * content.</p>
     *
     * @param filePath The path of the file to be read as a {@link String}. The
     * file path should be valid and point to a readable file.
     * @return A {@link String} containing the contents of the file. If an
     * exception occurs, it returns an empty string.
     * @throws IOException If an I/O error occurs reading from the file.
     */
    public static String readFileContent(String filePath) throws IOException {
        // Read all content from the specified file path using UTF-8 encoding
        return Files.readString(Path.of(filePath), StandardCharsets.UTF_8);
    }

    /**
     * Reads given resource file as a string.
     *
     * @param fileName
     * @return
     * @throws java.io.IOException
     * @link https://stackoverflow.com/questions/6068197/utils-to-read-resource-text-file-to-string-java#46613809
     *
     * @param fileName path to the resource file
     * @return the file's contents
     * @throws IOException if read fails for any reason
     */
    public static String getResourceFileAsString(String fileName) throws IOException {
        ClassLoader classLoader = ClassLoader.getSystemClassLoader();
        try (InputStream is = classLoader.getResourceAsStream(fileName)) {
            if (is == null) {
                return null;
            }
            try (InputStreamReader isr = new InputStreamReader(is); BufferedReader reader = new BufferedReader(isr)) {
                return reader.lines().collect(Collectors.joining(System.lineSeparator()));
            }
        }
    }

    /**
     * Checks if the given path is valid.
     *
     * <p>
     * This method attempts to create a Path object from the provided string. If
     * the string is null or cannot be converted to a valid Path, the method
     * returns false.</p>
     *
     * @param path The path to be validated as a {@link String}.
     * @return {@code true} if the path is valid; {@code false} otherwise.
     */
    public static boolean isValidPath(String path) {
        // Return false if the path is null
        if (path == null) {
            return false;
        }

        // Try to create a Path object; return false if it fails
        try {
            Paths.get(path);
        } catch (InvalidPathException ex) {
            return false; // Invalid path
        }

        return true; // Valid path
    }
}
