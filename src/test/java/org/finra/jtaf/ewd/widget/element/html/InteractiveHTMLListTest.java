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

import java.util.List;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.IInteractiveElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InteractiveHTMLListTest {
    public static String url = "http://localhost:29090/simpleapp/interactiveelements.html";
	public ExtWebDriver wd;
	
	protected String htmlListLocator = "//ul[@id=\"htmlListTest\"]";
    protected String badHtmlListLocator = "//ul[@id=\"badHtmlListTest\"]";
    
    
	@Before
    public void setup() throws Exception {
        wd = SessionManager.getInstance().getCurrentSession();
        
    }

    @After
    public void teardown() {
        wd.close();
        SessionManager.getInstance().removeSession(wd);
    }

    /*
     * Only testable on real browsers
     */
    @Test
    public void testGetInteractiveItem() throws WidgetException{
    	wd.open(url);
    	InteractiveHtmlList hList = new InteractiveHtmlList(htmlListLocator);
    	IInteractiveElement ie = hList.getInteractiveItem(1);
        ie.click();
    	//This needs to happen since its a button
    	Button b = new Button("//input[@id=\"button\"]");
    	Assert.assertEquals("interacted:0", b.getAttribute("value"));
    }
    
    @Test
    public void testGetInteractiveItems() throws WidgetException{
    	wd.open(url);
    	InteractiveHtmlList hList = new InteractiveHtmlList(htmlListLocator);
    	List<IInteractiveElement> ieLists = hList.getInteractiveItems();
    	Assert.assertEquals(4, ieLists.size());
    }
    
    @Test(expected=WidgetException.class)
    public void testGetInteractiveItemException() throws WidgetException{
    	wd.open(url);
    	InteractiveHtmlList hList = new InteractiveHtmlList(badHtmlListLocator);
    	IInteractiveElement ie = hList.getInteractiveItem(1);
    	ie.click();
    	//This needs to happen since its a button
    	Button b = new Button("//input[@id=\"button\"]");
    }

    @Test(expected=WidgetException.class)
    public void testGetInteractiveItemException2() throws WidgetException{
        wd.open(url);
        InteractiveHtmlList hList = new InteractiveHtmlList(badHtmlListLocator);
        IInteractiveElement ie = hList.getInteractiveItem(10);
        ie.click();
        //This needs to happen since its a button
        Button b = new Button("//input[@id=\"button\"]");
    }
    
    @Test(expected=WidgetException.class)
    public void testGetInteractiveItemsException() throws WidgetException{
    	wd.open(url);
    	InteractiveHtmlList hList = new InteractiveHtmlList(badHtmlListLocator);
    	List<IInteractiveElement> ieLists = hList.getInteractiveItems();
    }
}
