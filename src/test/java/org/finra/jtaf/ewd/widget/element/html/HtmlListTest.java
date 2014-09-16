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
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class HtmlListTest {

    public static String url = "http://localhost:29090/simpleapp/staticwidgetsapp.html";
	public ExtWebDriver wd;

	protected String htmlListLocator = "//ul[@id=\"htmlListTest1\"]";
    protected String badHtmlListLocator = "//ul[@id=\"badHtmlListTest\"]";
    protected String brokenLocator = "//ul[@id=\"brokenLocator\"]";

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
    public void testGetItemCount() throws WidgetException{
    	wd.open(url);
    	HtmlList htmlList = new HtmlList(htmlListLocator);
    	Assert.assertEquals(4, htmlList.getItemCount());
    }

    @Test
    public void testGetItem() throws WidgetException{
    	wd.open(url);
    	HtmlList htmlList = new HtmlList(htmlListLocator);
    	IElement e = htmlList.getItem(1);
    	Assert.assertEquals("UL1", e.getText());
    }

    @Test
    public void testGetItems() throws WidgetException{
    	wd.open(url);
    	HtmlList htmlList = new HtmlList(htmlListLocator);
    	Assert.assertEquals(4, htmlList.getItems().size());
    	IElement firstElement = htmlList.getItems().get(0);
    	Assert.assertEquals("UL1", firstElement.getText());
    }

    @Test
    public void testGetItemCountException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try{
	    	wd.open(url);
	    	HtmlList htmlList = new HtmlList(brokenLocator);
	    	htmlList.getItemCount();
    	}
    	catch(WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
    @Test
    public void testGetItemException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try{
	    	wd.open(url);
	    	HtmlList htmlList = new HtmlList(badHtmlListLocator);
	    	htmlList.getItem(15);
    	}
    	catch(WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
    @Test
    public void testGetItemsException() throws WidgetException{
    	boolean exceptionThrown = false;
    	try{
	    	wd.open(url);
	    	HtmlList htmlList = new HtmlList(brokenLocator);
	    	htmlList.getItems();
    	}
    	catch(WidgetException we){
    		exceptionThrown = true;
    	}
    	Assert.assertTrue(exceptionThrown);
    }
}
