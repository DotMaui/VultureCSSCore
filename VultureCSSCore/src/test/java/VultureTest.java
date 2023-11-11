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
import com.dotmaui.vulturecss.core.VultureCSSCore;
import com.dotmaui.vulturecss.models.Carcass;
import com.dotmaui.vulturecss.models.VultureCSSOptions;
import com.dotmaui.vulturecss.models.WhiteListRule;
import java.net.URL;

import java.util.ArrayList;
import java.util.List;

public class VultureTest {

    /**
     * @param args the command line arguments
     * @throws java.lang.Exception
     */
    public static void main(String[] args) throws Exception {

        //Test1();
        //Test2();
        Test3();

    }

    private static void Test1() throws Exception {

        VultureCSSCore vulturecss_core = new VultureCSSCore();
        VultureCSSOptions vulturecss_options = new VultureCSSOptions();

        WhiteListRule wl = new WhiteListRule();
        wl.setSelector(".not-used-wl p");
        wl.setType(com.dotmaui.vulturecss.models.WhiteListRule.EQUALS);

        List<WhiteListRule> wlr = new ArrayList<>();
        wlr.add(wl);
        vulturecss_options.setWhiteListRules(wlr);

        String html = "<div class=container></div><div class='container red'></div>";
        String css = ".container{color:green} div{color:gold} .not-used{color:black} .not-used-too{color:black} .not-used-wl p{color:green}";

        vulturecss_core.setHtml(html);
        vulturecss_core.setCss(css);

        vulturecss_core.setOptions(vulturecss_options);
        List<Carcass> carcasses = vulturecss_core.Process();

        carcasses.forEach((c) -> {
            System.out.println(c.getUsedCSS());
        });

    }

    private static void Test2() throws Exception {

        URL htmlUrl = new URL("https://dotmaui.com");
        URL cssUrl = new URL("https://dotmaui.com/assets/css/production/dotmaui.com_app_min.css");
        VultureCSSCore v = new VultureCSSCore(cssUrl, htmlUrl);
        List<Carcass> carcasses = v.Process();

        carcasses.forEach((c) -> {
            System.out.println(c.getUsedCSS());
        });

    }

    private static void Test3() throws Exception {

        List<String> urls = new ArrayList<>();
        //urls.add("http://135.125.102.94/css/a.css");
        urls.add("https://maxcdn.bootstrapcdn.com/font-awesome/4.7.0/css/font-awesome.min.css");
        urls.add("https://dotmaui.com/assets/css/production/dotmaui.com_app_min.css?v=2");

        String result = VultureCSSCore.mergeAndOptimizeCSSFromUrls(urls);

        System.out.println(result);

    }

}
