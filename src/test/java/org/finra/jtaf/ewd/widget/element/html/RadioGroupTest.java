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
import org.finra.jtaf.ewd.widget.IRadioGroup;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class RadioGroupTest {

    public static String url = "http://localhost:29090/simpleapp/interactiveelements.html";


    public ExtWebDriver wd;

    protected String radioLocator = "//input[@name=\"radioTest\"]";
    protected String radioOption1Locator = "//input[@name=\"radioTest\" and @value=\"Radio1\"]";
    protected String radioOption2Locator = "//input[@name=\"radioTest\" and @value=\"Radio2\"]";
    protected String brokenLocator = "//input[@id=\"broken\"]";

    private IRadioGroup radio = new RadioGroup(radioLocator);
    private IRadioGroup brokenRadio = new RadioGroup(brokenLocator);

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
    public void testSelect() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();
        radio.select("Radio1");
        Assert.assertTrue(new Element(radioOption1Locator).getAttribute("checked").equals("true"));
        Assert.assertTrue(new Element(radioOption2Locator).getAttribute("checked").equals(""));
    }

    @Test
    public void testSelectException() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();

        boolean exception = false;
        try {
            brokenRadio.select("test");
        } catch (WidgetException we){
            exception = true;
        }
        Assert.assertTrue("Exception wasn't thrown", exception);
    }

    @Test
    public void testSelectExceptionNoOption() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();

        boolean exception = false;
        try {
            radio.select("test");
        } catch (WidgetException we){
            exception = true;
        }
        Assert.assertTrue("Exception wasn't thrown", exception);
    }

    @Test
    public void testSetValueException() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();

        boolean exception = false;
        try {
            radio.setValue(new Boolean("true"));
        } catch (WidgetException we){
            exception = true;
        }
        Assert.assertTrue("Exception wasn't thrown", exception);
    }

    @Test
    public void testSelectedOption() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();
        radio.select("Radio2");
        Assert.assertNotEquals("Radio1", radio.getSelectedValue());
        Assert.assertEquals("Radio2", radio.getSelectedValue());
        radio.select("Radio1");
        Assert.assertNotEquals("Radio2", radio.getSelectedValue());
        Assert.assertEquals("Radio1", radio.getSelectedValue());
    }

    @Test
    public void testGetOptions() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();
        List<String> actualOptions = radio.getValues();
        Assert.assertEquals(2, actualOptions.size());
        Assert.assertEquals("Radio1", actualOptions.get(0));
        Assert.assertEquals("Radio2", actualOptions.get(1));
    }

    @Test
    public void testSelectedOptionException() throws WidgetException{
        wd.open(url);
        radio.waitForElementPresent();
        boolean exception = false;
        try {
            brokenRadio.getSelectedValue();
        } catch (WidgetException we){
            exception = true;
        }
        Assert.assertTrue("Exception wasn't thrown", exception);
    }
}
