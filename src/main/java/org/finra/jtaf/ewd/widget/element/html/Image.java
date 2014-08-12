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

import org.finra.jtaf.ewd.widget.IImage;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.InteractiveElement;
import org.openqa.selenium.By;

/**
 * 
 * Image widget implementation for an image element, using src
 *
 */
public class Image extends InteractiveElement implements IImage {

    public Image(String locator) {
        super(locator);
    }

    public Image(By locator) {
        super(locator);
    }
    
    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IImage#getSource()
     */
    @Override
    public String getSource() throws WidgetException {
        try {
            return getAttribute("src");
        } catch (Exception e) {
            throw new WidgetException("Error while fetching image source", getByLocator(), e);
        }
    }

}
