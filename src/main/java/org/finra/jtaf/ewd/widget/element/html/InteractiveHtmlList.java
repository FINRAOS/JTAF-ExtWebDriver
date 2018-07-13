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
import org.finra.jtaf.ewd.widget.IInteractiveElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.By;

/**
 * 
 * Interactive HTML List element better suited for interactive
 * lists than HTML list
 *
 */
public class InteractiveHtmlList extends HtmlList implements IElement {

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
    public InteractiveHtmlList(String locator) {
        super(locator);
    }

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
    public InteractiveHtmlList(By locator) {
        super(locator);
    }
    
    /**
     * 
     * @param index the index of the item you want to get
     * @return new interactive element based on the index
     * @throws WidgetException
     */
    public IInteractiveElement getInteractiveItem(int index) throws WidgetException {
        IElement elem = getItem(index);
        return new InteractiveElement(elem.getByLocator());
    }

    /**
     * 
     * @return a list of interactive elements
     * @throws WidgetException
     */
    public List<IInteractiveElement> getInteractiveItems() throws WidgetException {
        List<IElement> items = getItems();
        List<IInteractiveElement> interactiveItems = new ArrayList<IInteractiveElement>();

        for (IElement e : items) {
            interactiveItems.add(new InteractiveElement(e.getByLocator()));
        }

        return interactiveItems;
    }

}
