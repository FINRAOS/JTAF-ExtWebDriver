/*
 * (C) Copyright 2013 Java Test Automation Framework Contributors.
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.finra.jtaf.ewd.widget.element;

import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.IInteractiveElement;
import org.finra.jtaf.ewd.widget.Keys;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.openqa.selenium.By;
import org.openqa.selenium.Dimension;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

/**
 * Element which is interactable on screen
 */
public class InteractiveElement extends Element implements IInteractiveElement
{

    /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
    public InteractiveElement(String locator)
    {
        super(locator);
    }

    /**
     * 
     * @param locator
    *            XPath, ID, name, CSS Selector, class name, or tag name
     */
	public InteractiveElement(String locator) {
		super(locator);
	}

   /**
    *
    * @param locator an By-expression.
    */
    public InteractiveElement(By locator) {
        super(locator);
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IClickableElement#click()
     */
    @Override
    public void click() throws WidgetException
    {
        try
        {
            final WebElement webElement = getWebElement();

            if (!getGUIDriver().isJavascriptClickMode())
            {
                synchronized (InteractiveElement.class)
                {
                    getGUIDriver().focus();
                    highlight(HIGHLIGHT_MODES.PUT);
                    webElement.click();
                }
            }
            else
            {
                highlight(HIGHLIGHT_MODES.PUT);
                try
                {
                    eval("arguments[0].click();");
                }
                catch (WidgetException we)
                {
                    throw new RuntimeException(we);
                }
            }

            getGUIDriver().selectLastFrame();
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while clicking element",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IClickableElement#doubleClick()
     */
    @Override
    public void doubleClick() throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.doubleClick(getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while double clicking element",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IClickableElement#rightClick()
     */
    @Override
    public void rightClick() throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.contextClick(getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while right clicking element",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#isEnabled()
     */
    @Override
    public boolean isEnabled() throws WidgetException
    {
        try
        {
            return getWebElement().isEnabled();
        }
        catch (Exception e)
        {
            throw new WidgetException(
                    "Error while determing whether element is enabled",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IInteractiveElement#dragAndDrop(IElement element)
     */
    @Override
    public void dragAndDrop(IElement element) throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.dragAndDrop(this.getWebElement(),
                        new InteractiveElement(element.getByLocator()).getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException(
                    "Error while performing drag and drop from " + getByLocator()
                            + " to " + element.getByLocator(), getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IInteractiveElement#dragAndDropByOffset(IElement element)
     */
    @Override
    public void dragAndDropByOffset(int xOffset, int yOffset) throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.dragAndDropBy(getWebElement(), xOffset, yOffset).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException(
                    "Error while performing drag and drop from " + getByLocator()
                            + " offset by X: " + xOffset + " Y: " + yOffset, getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#keyDown()
     */
    @Override
    public void keyDown(Keys theKey) throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                for (org.openqa.selenium.Keys key : org.openqa.selenium.Keys.values())
                {
                    if (key.name().equals(theKey.name()))
                    {
                        builder.keyDown(getWebElement(), key).build().perform();
                        break;
                    }
                }
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing key down using "
                    + theKey.name(), getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#keyUp()
     */
    @Override
    public void keyUp(Keys theKey) throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                for (org.openqa.selenium.Keys key : org.openqa.selenium.Keys.values())
                {
                    if (key.name().equals(theKey.name()))
                    {
                        builder.keyUp(getWebElement(), key).build().perform();
                        break;
                    }
                }
            }

        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing key up using "
                    + theKey.name(), getByLocator(), e);
        }
    }
    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#clickAndHold()
     */
    @Override
    public void clickAndHold() throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.clickAndHold(getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing click and hold",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IInteractiveElement#releaseClickAndHold()
     */
    @Override
    public void releaseClickAndHold() throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.release(getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while releasing click and hold",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see
     * qc.automation.framework.widget.IInteractiveElement#sendKeys(CharSequence
     * keysToSend)
     */
    @Override
    public void sendKeys(CharSequence keysToSend) throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.sendKeys(getWebElement(), keysToSend).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while sending keys", getByLocator(),
                    e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see qc.automation.framework.widget.IInteractiveElement#type(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void type(String text) throws WidgetException
    {
        try
        {
            if (getGUIDriver().isJavascriptTypeMode())
            {

				// Replaces apostrophes that have not been escaped with escaped variations
                // Slashes need to be escaped multiple times.  Once for Java's string escaping
                // and again for the RegEx engine escaping.
                final String theText = text.replaceAll("(\\\\')|(\\')", "\\\\'");
                highlight(HIGHLIGHT_MODES.PUT);
                try
                {
                    eval("arguments[0].value='" + theText + "';");
                }
                catch (WidgetException we)
                {
                    throw new RuntimeException(we);
                }
                try
                {
                    Thread.sleep(500);
                }
                catch (InterruptedException e)
                {
                }
            }
            else
            {
                // TODO Test this.
                synchronized (InteractiveElement.class)
                {
                    getGUIDriver().focus();
                    click();
                    WebElement webElement = getWebElement();
                    highlight(HIGHLIGHT_MODES.PUT);
                    webElement.clear();
                    webElement.sendKeys(text);
                }
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while trying to type at " + getByLocator(), getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     *
     * @see qc.automation.framework.widget.IInteractiveElement#typeAppend(java.lang.String,
     * java.lang.String)
     */
    @Override
    public void typeAppend(String text) throws WidgetException
    {
        try
        {
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                click();
                WebElement e = getWebElement();
                e.sendKeys(text);
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while trying to type append ", getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#mouseMove()
     */
    @Override
    public void mouseMove() throws WidgetException
    {
        try
        {
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.moveToElement(getWebElement()).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing mouse move to",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#mouseMoveOut()
     */
    @Override
    public void mouseMoveOut() throws WidgetException
    {
        try
        {
            // TODO: Need to figure out whether 10 is sufficient
            int offsetAmount = 10;

            WebElement elem = getWebElement();
            Dimension dim = elem.getSize();
            int width = dim.getWidth();
            Actions builder = new Actions(getGUIDriver().getWrappedDriver());
            synchronized (InteractiveElement.class)
            {
                getGUIDriver().focus();
                builder.moveToElement(getWebElement(), width + offsetAmount, 0).build().perform();
            }
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing mouse move out",
                    getByLocator(), e);
        }
    }

    /*
     * (non-Javadoc)
     * 
     * @see qc.automation.framework.widget.IInteractiveElement#mouseOver()
     */
    @Override
    public void mouseOver() throws WidgetException
    {
        try
        {
            fireEvent("mouseover");
        }
        catch (Exception e)
        {
            throw new WidgetException("Error while performing mouse over",
                    getByLocator(), e);
        }
    }

    /**
     * Throw Unsupported Operation exception if getValue() on Interactive element is invoked
     */
    @Override
    public Object getValue() throws WidgetException
    {
        throw new UnsupportedOperationException("getValue() is not supported by InteractiveElement");
    }

    /**
     * Throw Unsupported Operation exception if getLabel() on Interactive element is invoked
     */
    @Override
    public String getLabel() throws WidgetException
    {
        throw new UnsupportedOperationException("getLabel() is not supported by InteractiveElement");
    }
}
