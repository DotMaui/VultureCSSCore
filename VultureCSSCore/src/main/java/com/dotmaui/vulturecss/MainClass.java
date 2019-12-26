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
package com.dotmaui.vulturecss;

import com.dotmaui.vulturecss.core.VultureCSSCore;
import com.dotmaui.vulturecss.utils.Interface;
import com.dotmaui.vulturecss.utils.MinifyWithYUI;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import org.apache.log4j.BasicConfigurator;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

public class MainClass {

    /**
     * @param args the command line arguments
     * @throws org.apache.commons.cli.ParseException
     */
    public static void main(String[] args) throws ParseException, Exception {

        BasicConfigurator.configure();
        
        /**
         * Disable log4j logging from Java code.
         * @link https://stackoverflow.com/questions/8709357/how-to-disable-log4j-logging-from-java-code/8711378#8711378
         */
        Logger.getLogger("ac.biu.nlp.nlp.engineml").setLevel(Level.OFF);
        Logger.getLogger("org.BIU.utils.logging.ExperimentLogger").setLevel(Level.OFF);
        Logger.getRootLogger().setLevel(Level.OFF);                     
        
        Options options = new Options();
        Option help = new Option("help", "print this message");
        Option css = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("The CSS to be optimized. It can be a path or a url.")
                .create("css");

        Option html = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("The HTML to compare. It can be a path or a url.")
                .create("html");

        Option output = OptionBuilder.withArgName("file")
                .hasArg()
                .withDescription("Output file. If omitted the result will be displayed on the screen.")
                .create("out");

        options.addOption(help);
        options.addOption(css);
        options.addOption(html);
        options.addOption(output);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        if (cmd.hasOption("help")) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java -jar VultureCSSCore.jar", options);
            return;
        }

        if (!cmd.hasOption("css")) {
            throw new Exception("CSS is mandatory");
        }

        String final_result;
        String css_to_optimize = cmd.getOptionValue("css");
        String css_to_optimize_content;

        try {
            java.net.URL u = new java.net.URL(css_to_optimize);
            css_to_optimize_content = Interface.DownloadFromUrl(u);
        } catch (MalformedURLException ex) {
            css_to_optimize_content = Interface.readLineByLineJava8(css_to_optimize);
        }

        if (cmd.hasOption("html")) {

            String html_to_compare = cmd.getOptionValue("html");
            String html_to_compare_content;

            try {
                java.net.URL u = new java.net.URL(html_to_compare);
                html_to_compare_content = Interface.DownloadFromUrl(u);
            } catch (MalformedURLException ex) {
                html_to_compare_content = Interface.readLineByLineJava8(html_to_compare);
            }

            VultureCSSCore v = new VultureCSSCore(css_to_optimize_content, html_to_compare_content);
            final_result = v.Process();

        } else {
            final_result = MinifyWithYUI.Process(css_to_optimize_content);
        }

        if (cmd.hasOption("out")) {
            
            try (PrintWriter out = new PrintWriter( cmd.getOptionValue("out"))) {
                out.println(final_result);
            }

        } else {
            System.out.print(final_result);
        }
        

    }

}
