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
import com.dotmaui.vulturecss.core.VultureCSSCore;
import com.dotmaui.vulturecss.models.Carcass;
import com.dotmaui.vulturecss.models.VultureCSSOptions;
import java.net.URL;
import java.util.List;

public class VultureTest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {
        // TODO code application logic here

        URL url = new URL("https://api.dotmaui.com");
                
        VultureCSSOptions opt = new VultureCSSOptions();
        opt.setUseStaticHTMLFromWebPage(false);
        opt.setCDNMode(false);
        opt.setMergeAll(true);
        opt.setDotMauiApiKey("");
        VultureCSSCore v = new VultureCSSCore();
        v.setHtmlUrl(url);
        v.setOptions(opt);
        List<Carcass> carcasses = v.Process();

        carcasses.forEach((c) -> {
            System.out.println(c.getPath());
            System.out.println(c.getCdnUrl());
        });

    }

}
