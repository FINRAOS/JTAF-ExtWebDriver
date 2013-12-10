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

import java.util.Arrays;
import java.util.List;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.IDropDown;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class DropDownTest{
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
    public void setValueTest() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTest']");
        IElement content = new Element("//div[@id='content']");
        d.waitForElementPresent();
        d.setValue("Delta");
        Assert.assertEquals("D", content.getText());
    }
    
    /**
     * Pass in something not a String to setValue, expect horrible, horrible things to happen
     * @throws Exception
     */
    @Test(expected=WidgetException.class)
    public void setValueTestWidgetRuntimeException() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTest']");
        d.waitForElementPresent();
        d.setValue(new Element("check"));
    }
    
    /**
     * Pass in String not "check" or "uncheck" to setValue, again expect horrible, horrible things to happen
     * @throws Exception
     */
    @Test(expected=WidgetException.class)
    public void setValueTestException() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTest']");
        d.waitForElementPresent();
        d.setValue("Oscar");
    }
   
    @Test
    public void testGetValue() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTestGet']");
        String exp = "Oscar";
        Assert.assertEquals(exp, d.getValue());
    }
    
    @Test(expected=WidgetException.class)
    public void testGetValueException() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='z']");
        String exp = "Oscar";
        Assert.assertEquals(exp, d.getValue());
    }
    
    @Test
    public void testSelectOption() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTest']");
        String exp = "C";
        IElement content = new Element("//div[@id='content']");
        d.selectOption("Charlie");
        Assert.assertEquals(exp, content.getText());
    }

    @Test(expected=WidgetException.class)
    public void testSelectOptionException() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTest']");
        String exp = "C";
        IElement content = new Element("//div[@id='content']");
        d.selectOption("Epsilon");
        Assert.assertEquals(exp, content.getText());
    }
    
    @Test
    public void testGetOption() throws Exception {
        wd.open(url);
        List<String> exp = Arrays.asList(new String[] {"Oscar", "Mike", "Charlie", "Echo"});
        IDropDown d = new DropDown("//select[@id='selectTestGet']");        
        Assert.assertEquals(exp, d.getOptions());
    }
    
    @Test(expected=WidgetException.class)
    public void testGetOptionException() throws Exception {
        wd.open(url);
        List<String> exp = Arrays.asList(new String[] {"Oscar", "Mike", "Charlie", "Echo"});
        IDropDown d = new DropDown("//select[@id='zz']");        
        Assert.assertEquals(exp, d.getOptions());
    }
    
    @Test
    public void testGetSelectedOption() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTestGet']");
        String exp = "Oscar";
        Assert.assertEquals(exp, d.getSelectedOption());
    }

    @Test
    public void testGetSelectedOptionNoValue() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='selectTestGetValue']");
        String exp = "";
        Assert.assertEquals(exp, d.getSelectedOption());
    }
    
    
    @Test(expected=WidgetException.class)
    public void testGetSelectedOptionException() throws Exception {
        wd.open(url);

        IDropDown d = new DropDown("//select[@id='x']");
        String exp = "Echo";
        Assert.assertEquals(exp, d.getSelectedOption());
    }

   
      
}
