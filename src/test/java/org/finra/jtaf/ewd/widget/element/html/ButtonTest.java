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
import org.finra.jtaf.ewd.widget.IButton;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ButtonTest{
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
    public void getLabelTest() throws Exception {
        wd.open(url);

        IButton b = new Button("//button[@id='myButton']");
        b.waitForElementPresent();
        String label = b.getLabel();
        Assert.assertEquals(label, (new Element("//button[@id='myButton']")).getText());
    }
    
    @Test(expected=WidgetException.class)
    public void getLabelTestException() throws Exception {
        wd.open(url);

        IButton b = new Button("//button[@id='myButton]");
        String label = b.getLabel();
        Assert.assertEquals("Click me", label);
    }
}
