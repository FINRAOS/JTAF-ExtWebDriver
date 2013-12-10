/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package org.finra.jtaf.ewd.widget.element.html;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.IHyperLink;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class HyperLinkTest{
	public static String url = "http://localhost:29090/simpleapp/elementstestapp.html";

	ExtWebDriver wd;

    @Before
    public void setup() throws Exception {
        wd = SessionManager.getInstance().getCurrentSession();
    }

    @After
    public void teardown() {
        wd.close();
        SessionManager.getInstance().removeSession(wd);
    }

    @Test
    public void getHrefTest() throws Exception {
        wd.open(url);

        IHyperLink hl = new HyperLink("//a[@id='hyperlinkTest']");
        String exp = "http://www.finra.org";
        Assert.assertTrue(hl.getHref().startsWith(exp));
    }
    
    @Test(expected=WidgetException.class)
    public void getHrefTestException() throws Exception {
        wd.open(url);

        IHyperLink hl = new HyperLink("//a[@id='zz']");
        String exp = "http://www.finra.org";
        Assert.assertEquals(exp, hl.getHref());
    }
    
    @Test
    public void getValueTest() throws Exception {
        wd.open(url);

        IHyperLink hl = new HyperLink("//a[@id='hyperlinkTest']");
        String exp = "Hyperlink Test";
        Assert.assertEquals(exp, hl.getValue());
    }
    
    @Test(expected=WidgetException.class)
    public void getValueTestException() throws Exception {
        wd.open(url);

        IHyperLink hl = new HyperLink("//a[@id='zz']");
        String exp = "Hyperlink Test";
        Assert.assertEquals(exp, hl.getValue());
    }
    
}
