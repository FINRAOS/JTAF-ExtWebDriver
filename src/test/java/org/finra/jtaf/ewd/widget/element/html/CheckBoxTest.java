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
import org.finra.jtaf.ewd.widget.ICheckBox;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;


public class CheckBoxTest{
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
    public void setValueTestCheck() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.setValue("check");
        String exp = "CheckBox Checked";
        Assert.assertEquals(exp, content.getText());
    }
    
    @Test
    public void setValueTestUncheck() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.setValue("uncheck");
        String exp = "CheckBox Unchecked";
        Assert.assertEquals(exp, content.getText());
    }
    
    /**
     * Pass in something not a String to setValue, expect horrible, horrible things to happen
     * @throws Exception
     */
    @Test(expected=WidgetException.class)
    public void setValueTestWidgetRuntimeException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.setValue(new Element("check"));
        String exp = "CheckBox Unchecked";
        Assert.assertEquals(exp, content.getText());
    }
    
    /**
     * Pass in String not "check" or "uncheck" to setValue, again expect horrible, horrible things to happen
     * @throws Exception
     */
    @Test(expected=WidgetException.class)
    public void setValueTestException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.setValue(new Element("ZZZZZZZZZZZZZZZ"));
        String exp = "CheckBox Unchecked";
        Assert.assertEquals(exp, content.getText());
    }
   
    @Test
    public void testGetValueChecked() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        Assert.assertEquals(c.getValue(), "checked");
    }
    
    @Test
    public void testGetValueUnchecked() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        Assert.assertEquals(c.getValue(), "unchecked");
    }
    
    @Test(expected=WidgetException.class)
    public void testGetValueException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testzzzUncheckBox']");
        Assert.assertEquals(c.getValue(), "checked");
    }
    
    @Test
    public void testIsCheckedTrue() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        Assert.assertTrue(c.isChecked());
    }
    
    @Test
    public void testIsCheckedFalse() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        Assert.assertFalse(c.isChecked());
    }
    
    @Test(expected=WidgetException.class)
    public void testIsCheckedException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testzzzUncheckBox']");
        Assert.assertFalse(c.isChecked());
    }
   
    @Test
    public void testCheck() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.check();
        String exp = "CheckBox Checked";
        Assert.assertEquals(exp, content.getText());
    }

    @Test(expected=WidgetException.class)
    public void testCheckException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='zzzzz']");
        IElement content = new Element("//div[@id='content']");
        c.check();
        String exp = "CheckBox Checked";
        Assert.assertEquals(exp, content.getText());
    }

    @Test
    public void testUncheck() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        IElement content = new Element("//div[@id='content']");
        c.waitForElementPresent();
        c.uncheck();
        String exp = "CheckBox Unchecked";
        Assert.assertEquals(exp, content.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testUncheckException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='zzzzz']");
        IElement content = new Element("//div[@id='content']");
        c.uncheck();
        String exp = "CheckBox Unchecked";
        Assert.assertEquals(exp, content.getText());
    }
    
    @Test
    public void testWaitForChecked() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        c.check();
        c.waitForChecked();
    }
    
  
    @Test(expected=WidgetException.class)
    public void testWaitForCheckedException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testCheckBox']");
        c.uncheck();
        c.waitForChecked();
    }
    
    @Test
    public void testWaitForUnchecked() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        c.check();
        c.waitForChecked();
    }
    
  
    @Test(expected=WidgetException.class)
    public void testWaitForUncheckedException() throws Exception {
        wd.open(url);

        ICheckBox c = new CheckBox("//input[@id='testUncheckBox']");
        c.check();
        c.waitForUnchecked();
    }
    
    
}
