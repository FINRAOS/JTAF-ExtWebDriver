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

import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.Element;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

/**
 * 
 * HTML List widget implementation for representing a list of
 * items/interactive items
 *
 */
public class HtmlList extends Element implements IElement {

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
    public HtmlList(String locator) {
        super(locator);
    }

    /**
     * 
     * @return int of the count of items
     * @throws WidgetException
     */
    public int getItemCount() throws WidgetException {
        if (isElementNotPresent()) {
            throw new WidgetException("Element doesn't exist", getLocator());
        }

        List<WebElement> elements = super.getWebElement().findElements(By.xpath("./li"));
        return elements.size();
    }

    /**
     * 
     * @param iterator
     * @return IElement found with the iterator
     * @throws WidgetException
     */
    public IElement getItem(int iterator) throws WidgetException {
        IElement e = new Element(getLocator());
        List<WebElement> elements = null;
        try {
            elements = e.getWebElement().findElements(By.xpath("./li"));
        } catch (Exception exc) {
            throw new WidgetException("Element not found", getLocator(), exc);
        }

        if (iterator <= elements.size()) {
            WebElement we = elements.get(iterator - 1);
            String key = "htmllistitemattribute";
            long value = System.currentTimeMillis();
            eval("arguments[0].setAttribute('" + key + "', '" + value + "')", we);
            return new Element("//*[@htmllistitemattribute='" + value + "']");
        } else {
            throw new WidgetException("No element at number: " + iterator, getLocator());
        }
    }

    /**
     * 
     * @return List of interactive elements
     * @throws WidgetException
     */
    public List<IElement> getItems() throws WidgetException {
        if (isElementNotPresent()) {
            throw new WidgetException("Element doesn't exist", getLocator());
        }

        List<IElement> l = new ArrayList<IElement>();
        IElement e = new Element(getLocator());
        List<WebElement> elements = e.getWebElement().findElements(By.xpath("./li"));
        for (WebElement we : elements) {
            String key = "htmllistitemattribute";
            long value = System.currentTimeMillis();
            eval("arguments[0].setAttribute('" + key + "', '" + value + "')", we);
            IElement ielement = new Element("//*[@htmllistitemattribute='" + value + "']");
            l.add(ielement);
        }

        return l;
    }

    /**
     * 
     * @param javascript
     *            the JS to evaluate/execute
     * @param element
     *            the element to use
     * @throws WidgetException
     */
    private void eval(String javascript, WebElement element) throws WidgetException {
        WebDriver wd = getGUIDriver().getWrappedDriver();
        try {
            ((JavascriptExecutor) wd).executeScript(javascript, element);
        } catch (Exception e) {
            long time = System.currentTimeMillis() + 2000;
            boolean success = false;
            while (!success && System.currentTimeMillis() < time) {
                try {
                    ((JavascriptExecutor) wd).executeScript(javascript, element);
                    success = true;
                } catch (Exception e2) {
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e1) {
                        // Ignore
                    }
                    e = e2;
                }
            }

            if (!success) {
                throw new RuntimeException(e);
            }
        }
    }
}
