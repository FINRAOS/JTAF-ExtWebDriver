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
package org.finra.jtaf.ewd.widget.element;

import java.io.File;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.utils.ScreenshotUtils;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.element.html.Image;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElementBrowserOnlyTest {

    public static String url = "http://localhost:29090/simpleapp/elementstestapp.html";

	public ExtWebDriver wd;
	public static String button = "//button[@id='myButton']";
	public static String invisibleButton = "//button[@id='myInvisibleButton']";
	
	private String getDiv(String id) {
		return "//div[@id='" + id + "']";
	}
	
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
    public void testScrollTo() throws Exception{
    	wd.open(url);

        IElement e = new Element(getDiv("scroll"));
        e.waitForElementPresent();
        Assert.assertTrue(!e.isVisibleWithinBoundsOfWindow());
        
        e.scrollTo();
        Assert.assertTrue(e.isVisibleWithinBoundsOfWindow());
    }
    
    @Test
    public void testScreenshotCompare() throws Exception {
    	wd.open(url);
    	
    	Image im = new Image("//img[@src=\"finralogo.jpg\"]");
    	im.waitForVisible();
    	
        ScreenshotUtils.assertSimilarToScreenshot(im.getLocator(), 
        		new File("src/test/resources/webapp/finralogo.jpg"), new File("testScreenshots/screenshot.png"));
    }
}