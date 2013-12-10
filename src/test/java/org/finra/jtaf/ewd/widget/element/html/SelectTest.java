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

import java.util.ArrayList;
import java.util.List;

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class SelectTest {

    public static String url = "http://localhost:29090/simpleapp/interactiveelements.html";
	public ExtWebDriver wd;
	
    protected String selectLocator = "//select[@id=\"selectTest\"]";
    protected String selectMultipleLocator = "//select[@id=\"selectTestMultiple\"]";
    protected String brokeLocator = "//input[@id=\"broken\"]";
    
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
    public void testGetOptions() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectLocator);
    	List<String> options = s.getOptions();
    	Assert.assertEquals("Car", options.get(0));
    	Assert.assertEquals("Bike", options.get(1));
    	Assert.assertEquals("Truck", options.get(2));
    	Assert.assertEquals("Feet", options.get(3));
    }
    @Test(expected=WidgetException.class)
    public void testGetOptionsException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	List<String> options = s.getOptions();    	
    }
    @Test
    public void testGetSelectedOptions() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectLocator);
    	List<String> options = s.getSelectedOptions();
    	Assert.assertEquals("Car", options.get(0));
    }
    @Test(expected=WidgetException.class)
    public void testGetSelectedOptionsException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	List<String> options = s.getSelectedOptions();    	
    }
    @Test
    public void testSelectOption() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectLocator);
    	s.selectOption("Truck");
    	String selectedOption = s.getSelectedOption();
    	Assert.assertEquals("Truck", selectedOption);
    }
    @Test(expected=WidgetException.class)
    public void testSelectOptionException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	s.selectOption("not there");
    }
    @Test
    public void testGetSelectedOption() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectLocator);
    	String selectedOption = s.getSelectedOption();
    	Assert.assertEquals("Car", selectedOption);
    }
    @Test(expected=WidgetException.class)
    public void testGetSelectedOptionException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	String selectedOption = s.getSelectedOption();
    	
    }
    @Test
    public void testSelectMultipleOptions() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectMultipleLocator);
    	List<String> options = new ArrayList<String>();
    	options.add("Car");
    	options.add("Truck");
    	s.selectMultipleOptions(options);
        Assert.assertEquals(options, s.getSelectedOptions());
    }
    @Test(expected=WidgetException.class)
    public void testSelectMultipleOptionsException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	List<String> options = new ArrayList<String>();
    	s.selectMultipleOptions(options);	
    }
    @Test
    public void testDeselectAllOptions() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(selectMultipleLocator);
        s.selectOption("Car");
    	s.selectOption("Truck");
    	s.deselectAllOptions();
    	Assert.assertEquals(null, s.getSelectedOption());
    }
    @Test(expected=WidgetException.class)
    public void testDeselectAllOptionsException() throws WidgetException{
    	wd.open(url);
    	Select s = new Select(brokeLocator);
    	s.deselectAllOptions();
    }
    
}
