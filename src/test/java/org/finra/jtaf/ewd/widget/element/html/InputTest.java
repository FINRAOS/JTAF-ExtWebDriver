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
import org.finra.jtaf.ewd.widget.WidgetException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InputTest {

    public static String url = "http://localhost:29090/simpleapp/interactiveelements.html";
	public ExtWebDriver wd;

	protected String inputLocator = "//input[@id=\"inputTest\"]";
    protected String brokeLocator = "//input[@id=\"doesntexist\"]";

	@Before
    public void setup() throws Exception {
        wd = SessionManager.getInstance().getCurrentSession();

    }

    @After
    public void teardown() {
        try {
            wd.close();
        } finally {
            SessionManager.getInstance().removeSession(wd);
        }
    }

    @Test
    public void testTypeAppend() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(inputLocator);
    	in.typeAppend("ThisisATest");
    	Assert.assertEquals("TextTypeThisisATest", in.getValue());
    }

    @Test(expected=WidgetException.class)
    public void testTypeAppendException() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(brokeLocator);
    	in.typeAppend("1");
    }

    @Test
    public void testSetValue() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(inputLocator);
    	in.setValue("TestValue");
    	Assert.assertEquals("TestValue", in.getValue());
    }
    @Test
    public void testSetValueException() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(inputLocator);
    	boolean exception = false;
    	try{
    		in.setValue(1);
    	}catch (WidgetException we){
    		exception = true;
    	}
    	Assert.assertTrue(exception);

    }
    @Test
    public void testGetValue() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(inputLocator);
    	Assert.assertEquals("TextType", in.getValue());
    }

    @Test(expected=WidgetException.class)
    public void testGetValueException() throws WidgetException{
    	wd.open(url);
    	Input in = new Input(brokeLocator);
        in.getValue();
    }
}
