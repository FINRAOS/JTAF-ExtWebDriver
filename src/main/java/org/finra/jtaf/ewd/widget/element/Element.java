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
package org.finra.jtaf.ewd.widget.element;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.sax.SAXSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.ccil.cowan.tagsoup.Parser;
import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.HighlightProvider;
import org.finra.jtaf.ewd.TimeOutException;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.timer.WaitForConditionTimer;
import org.finra.jtaf.ewd.timer.WaitForConditionTimer.ITimerCallback;
import org.finra.jtaf.ewd.timer.WidgetTimeoutException;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.XMLReader;

/**
 * This is the base class for any element
 */
public class Element implements IElement {
	private ExtWebDriver gd;
	private final String locator;
	private static final long DEFAULT_INTERVAL = 100;

	protected enum HIGHLIGHT_MODES {
		FIND, GET, PUT
	}

	 /**
     * 
     * @param locator
     *            XPath, ID, name, CSS Selector, class name, or tag name
     */
	public Element(String locator) {
		this.locator = locator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getLocator()
	 */
	@Override
	public String getLocator() {
		return locator;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isElementPresent()
	 */
	@Override
	public boolean isElementPresent() throws WidgetException {
		try {
			return isElementPresent_internal();
		} catch (Exception e) {
			throw new WidgetException("Error while determining whether element is present", locator, e);
		}
	}

	/**
	 * Use the JavaXPath to determine if  element is present. If not, then try finding element.
	 * Return false if the element does not exist
	 * @return
	 * 		true or false 
	 * @throws WidgetException
	 */
	private boolean isElementPresent_internal() throws WidgetException {
		try {
			try {
				if (isElementPresentJavaXPath())
					return true;
			} catch (Exception e) {
				// Continue
			}

			findElement();
			return true;
		} catch (NoSuchElementException e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isElementPresent(java.lang.Long)
	 */
	@Override
	public boolean isElementPresent(long timeout) throws WidgetException {
		final long start = System.currentTimeMillis();
		final long end = start + timeout;
		while (true) {
			if (isElementPresent()) {
				return true;
			}

			// Putting the condition here will measure the time more accurately.
			if (System.currentTimeMillis() >= end) {
				return false;
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isElementPresent(java.lang.Boolean)
	 */
	@Override
	public boolean isElementPresent(boolean isJavaXPath) throws WidgetException {
		try {
			if (isJavaXPath) {
				return isElementPresentJavaXPath();
			} else {
				findElement();
				return true;
			}
		} catch (NoSuchElementException e) {
			return false;
		} catch (Exception e) {
			return false;
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isElementVisible()
	 */
	@Override
	public boolean isElementVisible() throws WidgetException {
		try {
			return isVisible() && (getLocationX() > 0) && (getLocationY() > 0);
		} catch (Exception e) {
			throw new WidgetException("Error while determining whether element is visible", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isElementVisible(java.lang.Long)
	 */
	@Override
	public boolean isElementVisible(long timeout) throws WidgetException {
		long start = System.currentTimeMillis();
		long end = start + timeout;
		while (System.currentTimeMillis() < end) {
			if (isElementPresent() && isVisible()) {
				return true;
			}
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
		return false;
	}

	/**
	 * Determine if the element is visible or not
	 * @return
	 * 		true or false
	 * @throws WidgetException
	 */
	private boolean isVisible() throws WidgetException {
		return findElement().isDisplayed();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForElementPresent()
	 */
	@Override
	public void waitForElementPresent() throws WidgetException {
		try {
			waitForElementPresent(getGUIDriver().getMaxRequestTimeout());
		} catch (Exception e) {
			throw new WidgetException("Error while waiting for element to be present", locator, e);
		}
	}
	
	

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForElementPresent(java.lang.Long)
	 */
	@Override
	public void waitForElementPresent(long time) throws WidgetException {
		try {
			waitForCommand(new ITimerCallback() {

				@Override
				public boolean execute() {
					try {
						return isElementPresent();
					} catch (WidgetException e) {
						return false;
					}
				}

				@Override
				public String toString() {
					return "Waiting for Element: " + locator + " to be present";
				}
			}, time);
		} catch (WidgetTimeoutException e) {
			throw new WidgetException("Error while waiting for element to be not present", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForElementNotPresent()
	 */
	public void waitForElementNotPresent() throws WidgetException {
		waitForElementNotPresent(getGUIDriver().getMaxRequestTimeout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForElementPresent(java.lang.Long)
	 */
	public void waitForElementNotPresent(final long time) throws WidgetException {
		try {
			waitForCommand(new ITimerCallback() {

				@Override
				public boolean execute() throws WidgetException {
					return !isElementPresent();
				}

				@Override
				public String toString() {
					return "Waiting for element with locator " + locator + " to not be present";
				}
			}, time);
		} catch (Exception e) {
			throw new WidgetException("Error while waiting for element to be not present", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForVisible()
	 */
	public void waitForVisible() throws WidgetException {
		waitForVisible(getGUIDriver().getMaxRequestTimeout());
	}

	/**
	 * Waits for specific element at locator to be visible within period of
	 * specified time
	 * 
	 * @param time
	 *            Milliseconds
	 * @throws WidgetException
	 */
	public void waitForVisible(final long time) throws WidgetException {
		try {
			waitForCommand(new ITimerCallback() {

				@Override
				public boolean execute() throws WidgetException {
					return isVisible();
				}

				@Override
				public String toString() {
					return "Waiting for element with locator: " + locator + " to be visible";
				}
			}, time);
		} catch (Exception e) {
			throw new WidgetException("Error while waiting for element to be visible", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#waitForNotVisible()
	 */
	public void waitForNotVisible() throws WidgetException {
		waitForNotVisible(getGUIDriver().getMaxRequestTimeout());
	}

	/**
	 * Waits for specific element at locator to be not visible within period of
	 * specified time
	 * 
	 * @param time
	 *            Milliseconds
	 * @throws WidgetException
	 */
	public void waitForNotVisible(final long time) throws WidgetException {
		try {
			waitForCommand(new ITimerCallback() {

				@Override
				public boolean execute() throws WidgetException {
					return !isVisible();
				}

				@Override
				public String toString() {
					return "Waiting for element with locator: " + locator + " to not be visible";
				}
			}, time);
		} catch (Exception e) {
			throw new WidgetException("Error while waiting for element to be not visible", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#getAttribute(java.lang.String)
	 */
	@Override
	public boolean isAttributePresent(String attributeName) throws WidgetException {
		try {
			WebElement webElement = null;
			String attrValue = null;

			// WebDriver get attribute
			try {
				webElement = findElement();
				attrValue = webElement.getAttribute(attributeName);
				highlight(webElement, HIGHLIGHT_MODES.GET);
			} catch (Exception e) {
				throw new RuntimeException("Exception happened at locator: " + locator + " attribute: " + attributeName + e.getMessage());
			}

			if (attrValue != null) {
				return true;
			}

			return false;
		} catch (Exception e) {
			throw new WidgetException("Error while determining if attribute is present: " + attributeName, locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#getAttribute(java.lang.String)
	 */
	@Override
	public String getAttribute(String attributeName) throws WidgetException {
		try {
			WebElement webElement = null;
			String attr = null;

			// WebDriver get attribute
			try {
				webElement = findElement();
				attr = webElement.getAttribute(attributeName);
				highlight(webElement, HIGHLIGHT_MODES.GET);
			} catch (NoSuchElementException e) {
				throw e;
			}

			if (attr != null && !attr.equals("")) {
				return attr;
			}

			return "";
		} catch (Exception e) {
			throw new WidgetException("Error while fetching attribute " + attributeName, locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getLocationX()
	 */
	@Override
	public int getLocationX() throws WidgetException {
		try {
			Point p = findElement().getLocation();
			return p.getX();
		} catch (Exception e) {
			throw new WidgetException("Error while fetching X location", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getLocationY()
	 */
	@Override
	public int getLocationY() throws WidgetException {
		try {
			Point p = findElement().getLocation();
			return p.getY();
		} catch (Exception e) {
			throw new WidgetException("Error while fetching Y location", locator, e);
		}
	}

	/**
	 * Tell Selenium to use the provided WebDriver instance
	 * @param guiDriver
	 * 			ExtendedWebDriver object
	 */
	public void setGUIDriver(ExtWebDriver guiDriver) {
		gd = guiDriver;
	}


	/**
	 * Get the WebDriver object to interact with the UI elements
	 * @return ExtendedWebDriver
	 * 			specific type of WebDriver object (browser-specific)
	 */
	public final ExtWebDriver getGUIDriver() {
		if (gd != null)
			return gd;
		return SessionManager.getInstance().getCurrentSession();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getText()
	 */
	@Override
	public String getText() throws WidgetException {
		try {
			WebElement webElement = findElement();
			highlight(webElement, HIGHLIGHT_MODES.GET);
			return webElement.getText();
		} catch (Exception e) {
			throw new WidgetException("Error while fetching text", locator, e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#hasText()
	 */
	public boolean hasText(String text) throws WidgetException {
		try {
            Element element = new Element(getLocator());
            List<WebElement> childElements = element.getWebElement().findElements(By.xpath(".//*"));
            for(WebElement we : childElements) {
                if(we.getText().contains(text)) {
                    return true;
                }
            }

            return false;
        } catch (Exception e2) {
            throw new WidgetException("Error while determining if element has text '" + text + "'", getLocator(), e2);
        }
    }

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getCssValue(java.lang.String)
	 */
	@Override
	public String getCssValue(String propertyName) throws WidgetException {
		try {
			return getGUIDriver().getWrappedDriver().findElement(By.xpath(locator)).getCssValue(propertyName);
		} catch (Exception e) {
			throw new WidgetException("Error while getting CSS value", locator, e);
		}
	}

	
	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForAttributeEqualTo(java.lang.String, java.lang.String)
	 */
	public void waitForAttributeEqualTo(String attributeName, String attributeValue) throws WidgetTimeoutException {
		waitForAttributeEqualTo(attributeName, attributeValue, getGUIDriver().getMaxRequestTimeout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForAttributeEqualTo(java.lang.String, java.lang.String, java.lang.Long)
	 */
	public void waitForAttributeEqualTo(final String attributeName, final String attributeValue, long timeout) throws WidgetTimeoutException {
		waitForCommand(new ITimerCallback() {
			@Override
			public boolean execute() throws WidgetException {
				String val = getAttribute(attributeName);

				if (val == null && attributeName == null)
					return true;

				if (val == null || attributeName == null)
					return false;

				if (val.equals(attributeValue))
					return true;

				return false;
			}

			@Override
			public String toString() {
				return "Waiting for attribute, " + ((attributeName == null) ? "" : attributeName) + ", to be equal to: " + ((attributeValue == null) ? "" : attributeValue) + " - for the element with the locator: " + locator;
			}
		}, timeout);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForAttributeNotEqualTo(java.lang.String, java.lang.Stringg)
	 */
	@Override
	public void waitForAttributeNotEqualTo(String attributeName, String attributeValue) throws WidgetException {
		waitForAttributeNotEqualTo(attributeName, attributeValue, getGUIDriver().getMaxRequestTimeout());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForText()
	 */
	@Override
	public void waitForText() throws WidgetException {
		waitForText(getGUIDriver().getMaxRequestTimeout());

	}

	/**
	 * Find the element using xpath, id, name, class name or tag name
	 * @return WebElement
	 * 		the element identified by any of the properties
	 * @throws WidgetException
	 */

	protected WebElement findElement() throws WidgetException {
		String locator = getLocator();
		getGUIDriver().selectLastFrame();
		WebDriver wd = getGUIDriver().getWrappedDriver();

		WebElement webElement;
		try {
			webElement = wd.findElement(By.xpath(locator));
			if (webElement != null) {
				highlight(webElement, HIGHLIGHT_MODES.FIND);
				return webElement;
			}
		} catch (Exception e) {
		}

		try {
			webElement = wd.findElement(By.id(locator));
			if (webElement != null) {
				highlight(webElement, HIGHLIGHT_MODES.FIND);
				return webElement;
			}
		} catch (Exception e) {
		}

		try {
			webElement = wd.findElement(By.name(locator));
			if (webElement != null) {
				highlight(webElement, HIGHLIGHT_MODES.FIND);
				return webElement;
			}
		} catch (Exception e) {
		}

		 try {
			 webElement = wd.findElement(By.cssSelector(locator));
			 if(webElement != null) {
					highlight(webElement, HIGHLIGHT_MODES.FIND);
				 return webElement;
			 }
		 } catch (Exception e) {
		 }

		try {
			webElement = wd.findElement(By.className(locator));
			if (webElement != null) {
				highlight(webElement, HIGHLIGHT_MODES.FIND);
				return webElement;
			}
		} catch (Exception e) {
		}

		try {
			webElement = wd.findElement(By.tagName(locator));
			if (webElement != null) {
				highlight(webElement, HIGHLIGHT_MODES.FIND);
				return webElement;
			}
		} catch (Exception e) {
		}

		throw new NoSuchElementException("Could not find element at " + locator);
	}

	/**
	 * highlight an element for the FIND mode
	 */
	public void highlight() throws WidgetException {
		highlight(HIGHLIGHT_MODES.FIND);
	}



	/**
	 * Find the current element and highlight it according to the mode
	 * @param mode
	 * @throws WidgetException
	 */
	public void highlight(HIGHLIGHT_MODES mode) throws WidgetException {
		WebElement element = findElement();
		highlight(element, mode);
	}

	/**
	 * Set the background color of a particular web element to a certain color
	 * @param element
	 * 		the element to highlight
	 * @param color
	 * 		the color to use for highlight
	 */
	private void setBackgroundColor(WebElement element, String color) {
		try {
//			eval("arguments[0].style.backgroundColor = '" + color + "'");
			WebDriver wd = getGUIDriver().getWrappedDriver();
			((JavascriptExecutor) wd).executeScript("arguments[0].style.backgroundColor = '" + color + "'", element);
		} catch (Exception e) {
			// Since its debug only, dont care
		}
	}

	/**
	 * Highlight a web element by getting its color from the map loaded from client properties file
	 * @param webElement
	 * 		the element to be highlighted
	 * @param colorMode
	 * 		the mode which decides the color of highlight
	 */
	protected void highlight(WebElement webElement, String colorMode) {
		if (!(getGUIDriver() instanceof HighlightProvider)) {
			return;
		}
		HighlightProvider highDriver = (HighlightProvider) getGUIDriver();

		if (highDriver.isHighlight()) {
			setBackgroundColor(webElement, highDriver.getHighlightColor(colorMode));
		}
	}
	
	/**
	 * Highlight a web element depending on one of the three default modes FIND, GET and PUT
	 * @param webElement
	 * 		the element to be highlighted
	 * @param mode
	 * 		the mode which decides the color of highlight
	 */
	protected void highlight(WebElement webElement, HIGHLIGHT_MODES mode) {
		if (!(getGUIDriver() instanceof HighlightProvider)) {
			return;
		}
		HighlightProvider highDriver = (HighlightProvider) getGUIDriver();

		if (highDriver.isHighlight()) {
			setBackgroundColor(webElement, highDriver.getHighlightColor(mode.toString().toLowerCase()));
		}
	}
	
	/**
	 * Executes JavaScript code on the current element in the current frame or window.
	 * @param javascript
	 * 		the javascript code to be executed
	 */
	 @Override
		public void eval(String javascript) throws WidgetException {
	        WebElement element = getWebElement();
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getWebElement()
	 */
	@Override
	public WebElement getWebElement() throws WidgetException {
		return findElement();
	}

	/**
	 * wait for timeout amount of time
	 * @param callback
	 * @param timeout
	 * @throws WidgetTimeoutException
	 */
	protected void waitForCommand(ITimerCallback callback, long timeout) throws WidgetTimeoutException {
		WaitForConditionTimer t = new WaitForConditionTimer(getLocator(), callback);
		t.waitUntil(timeout);
	}

	/**
	 * Use the Java Xpath API to determine if the element is present or not
	 * @return boolean
	 * 		true or false as the case is
	 * @throws Exception
	 */
	private boolean isElementPresentJavaXPath() throws Exception {
		String xpath = getLocator();
		try {
			xpath = formatXPathForJavaXPath(xpath);
			NodeList nodes = getNodeListUsingJavaXPath(xpath);
			if (nodes.getLength() > 0) {
				return true;
			} else {
				return false;
			}
		} catch (Exception e) {
			throw new Exception("Error performing isElement present using Java XPath: " + xpath, e);
		}
	}

	/**
	 * Format the xpath to work with Java XPath API 
	 * @param xpath
	 * 		the xpath to be formatted
	 * @return String
	 * 		the formatted xpath
	 * @throws Exception
	 */
	private static String formatXPathForJavaXPath(String xpath) throws Exception {
		String newXPath = "";

		if (xpath.startsWith("xpath=")) {
			xpath = xpath.replace("xpath=", "");
		}

		boolean convertIndicator = true;
		boolean onSlash = false;
		boolean onSingleQuote = false;
		boolean onDoubleQuote = false;
		for (int i = 0; i < xpath.length(); i++) {
			char c = xpath.charAt(i);
			if (c == '/') {
				if (!onSingleQuote && !onDoubleQuote) {
					if (convertIndicator) {
						if (!onSlash) {
							onSlash = true;
						} else {
							onSlash = false;
						}
					} else {
						convertIndicator = true;
						onSlash = true;
					}
				}
			} else if (c == '[') {
				if (!onSingleQuote && !onDoubleQuote)
					convertIndicator = false;
			} else if (c == ']') {
				if (!onSingleQuote && !onDoubleQuote)
					convertIndicator = true;
			} else if (c == '\'') {
				if (!onSingleQuote)
					onSingleQuote = true;
				else
					onSingleQuote = false;
			} else if (c == '\"') {
				if (!onDoubleQuote)
					onDoubleQuote = true;
				else
					onDoubleQuote = false;
			}

			if (convertIndicator)
				newXPath = newXPath + String.valueOf(c).toLowerCase();
			else
				newXPath = newXPath + String.valueOf(c);
		}

		return newXPath;
	}

	/**
	 * Get the list of nodes which satisfy the xpath expression passed in
	 * @param xpath
	 * 		the input xpath expression
	 * @return
	 * 		the nodeset of matching elements
	 * @throws Exception
	 */
	private NodeList getNodeListUsingJavaXPath(String xpath) throws Exception {
		XPathFactory xpathFac = XPathFactory.newInstance();
		XPath theXpath = xpathFac.newXPath();

		String html = getGUIDriver().getHtmlSource();
		html = html.replaceAll(">\\s+<", "><");
		InputStream input = new ByteArrayInputStream(html.getBytes());

		XMLReader reader = new Parser();
		reader.setFeature(Parser.namespacesFeature, false);
		Transformer transformer = TransformerFactory.newInstance().newTransformer();

		DOMResult result = new DOMResult();
		transformer.transform(new SAXSource(reader, new InputSource(input)), result);

		Node htmlNode = result.getNode(); // This code gets a Node from the result.
		NodeList nodes = (NodeList) theXpath.evaluate(xpath, htmlNode, XPathConstants.NODESET);

		return nodes;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForText(java.lang.Long)
	 */
	@Override
	public void waitForText(long time) throws WidgetException, WidgetTimeoutException {
		waitForCommand(new ITimerCallback() {

			@Override
			public boolean execute() throws WidgetException {
				String val = getText();

				if (val == null || val.equals(""))
					return false;

				return true;
			}

			@Override
			public String toString() {
				return "Waiting for text on the element with the " + "locator: " + locator;
			}

		}, time);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForNotAttribute(java.lang.String, java.lang.String, java.lang.Long)
	 */
	@Override
	public void waitForNotAttribute(String attributeName, String pattern, long timeout) throws WidgetException {
		waitForAttributePatternMatcher(attributeName, pattern, timeout, false);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForNotAttribute(java.lang.String, java.lang.String)
	 */
	@Override
	public void waitForNotAttribute(String attributeName, String pattern) throws WidgetException {
		waitForNotAttribute(attributeName, pattern, getGUIDriver().getMaxRequestTimeout());
	}

	/**
	 * Matches an attribute value to a pattern that is passed.
	 * @param attributeName
	 * 		the attribute whose value os to be compared
	 * @param pattern
	 * 		the pattern to which to match
	 * @param timeout
	 * 		time in milliseconds after which request timeout occurs
	 * @param waitCondition
	 * 		true to match the attribute value and false to not match the value
	 * @throws WidgetException
	 */
	private void waitForAttributePatternMatcher(String attributeName, String pattern, long timeout, boolean waitCondition) throws WidgetException {
		long start = System.currentTimeMillis();
		long end = start + timeout;
		while (System.currentTimeMillis() < end) {
			String attribute = getAttribute(attributeName);
			if (attribute != null && attribute.matches(pattern) == waitCondition)
				return;
			try {
				Thread.sleep(DEFAULT_INTERVAL);
			} catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
		}
		throw new TimeOutException("waitForAttribute " + " : timeout : Locator : " + locator + " Attribute : " + attributeName);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#waitForAttribute(java.lang.String, java.lang.String, java.lang.Long)
	 */
	public void waitForAttribute(String attributeName, String pattern, long timeout) throws WidgetException {
		waitForAttributePatternMatcher(attributeName, pattern, timeout, true);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#waitForAttribute(java.lang.String, java.lang.String)
	 */
	public void waitForAttribute(String attributeName, String pattern) throws WidgetException {
		waitForAttribute(attributeName, pattern, getGUIDriver().getMaxRequestTimeout());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#waitForEnabled()
	 */
	@Override
	public void waitForEnabled() throws WidgetException {
		waitForEnabled(getGUIDriver().getMaxRequestTimeout());

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#waitForEnabled(java.lang.Long)
	 */
	@Override
	public void waitForEnabled(long time) throws WidgetException {
		waitForCommand(new ITimerCallback() {

			@Override
			public boolean execute() throws WidgetException {
				return isEnabled();
			}

			@Override
			public String toString() {
				return "Waiting for element with the locator: " + locator + " to be enabled.";
			}
		}, time);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#isEnabled()
	 */
	@Override
	public boolean isEnabled() throws WidgetException {
		return findElement().isEnabled();
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#waitForAttributeNotEqualTo(java.lang.String, java.lang.String, java.lang.Long)
	 */
	public void waitForAttributeNotEqualTo(final String attributeName, final String attributeValue, long timeout) throws WidgetTimeoutException {
		waitForCommand(new ITimerCallback() {

			@Override
			public boolean execute() throws WidgetException {
				String val = getAttribute(attributeName);

				if (val == null && attributeName == null)
					return true;

				if (val == null || attributeName == null)
					return false;

				if (!val.equals(attributeValue))
					return true;

				return false;
			}

			@Override
			public String toString() {
				return "Waiting for attribute, " + ((attributeName == null) ? "" : attributeName) + ", to not equal: " + ((attributeValue == null) ? "" : attributeValue) + " - for the element with the locator: " + locator;
			}
		}, timeout);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#isElementNotPresent()
	 */
	@Override
	public boolean isElementNotPresent() throws WidgetException {
		if (!isElementPresent()) {
            return true;
        }else {
            return false;
        }
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * qc.automation.framework.widget.IElement#isElementNotPresent(java.lang.Long)
	 */
	@Override
	public boolean isElementNotPresent(long timeout) throws WidgetException {
		final long start = System.currentTimeMillis();
		final long end = start + timeout;
		while (true) {
			if (!isElementPresent()) {
				return true;
			}

			// Once again, measures the time interval more accurately.
			if (System.currentTimeMillis() >= end) {
				return false;
			}
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
			}
		}
	}


	/**
	 * Fires a javascript event on a web element
	 * @param event
	 * 		the javascript code for the event
	 */
	@Override
	public void fireEvent(String event) throws WidgetException {
		try {
			String javaScript = null;
			if (event.startsWith("mouse")) {
				javaScript = "if(document.createEvent){var evObj = document.createEvent('MouseEvents');evObj.initEvent('" + event
						+ "', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('on" + event + "');}";
			} else {
				javaScript = "if(document.createEvent){var evObj = document.createEvent('HTMLEvents');evObj.initEvent('" + event
						+ "', true, false); arguments[0].dispatchEvent(evObj);} else if(document.createEventObject) { arguments[0].fireEvent('on" + event + "');}";
			}
			eval(javaScript);
		} catch (Exception e) {
			throw new WidgetException("Error while trying to fire event", getLocator(), e);
		}
	}

	/**
	 * Returns the text contained in the child nodes of the current element
	 * 
	 * @return Array of texts in children nodes
	 */
	@Override
	public String[] getChildNodesValuesText() throws WidgetException {
		WebElement we =  new Element(getLocator()).getWebElement();
		List<WebElement> childNodes = we.findElements(By.xpath("./*"));
		String[] childText = new String[childNodes.size()];
		int i = 0;
		for (WebElement element : childNodes) {
			childText[i] = element.getText();
			i++;
		}
		return childText;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#getInnerHTML()
	 */
	@Override
	public String getInnerHTML() throws WidgetException {
		WebElement element = findElement();
		WebDriver wd = getGUIDriver().getWrappedDriver();
		String innerHtml = (String) ((JavascriptExecutor) wd).executeScript("return arguments[0].innerHTML;", element);
		return innerHtml;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see qc.automation.framework.widget.IElement#highlight(java.lang.String)
	 */
	@Override
	public void highlight(String color) throws WidgetException {
		setBackgroundColor(findElement(), color);
	}
}

