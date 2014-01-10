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

import org.finra.jtaf.ewd.ExtWebDriver;
import org.finra.jtaf.ewd.HighlightProvider;
import org.finra.jtaf.ewd.impl.ClientProperties;
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.IInteractiveElement;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.html.Button;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ElementTest {

    public static String url = "http://localhost:29090/simpleapp/elementstestapp.html";
    
	public ExtWebDriver wd;
	public static String button = "//button[@id='myButton']";
	public static String invisibleButton = "//button[@id='myInvisibleButton']";

	private String getButton(String id){
		return "//button[@id='" + id + "']";
	}
	
	private String getSpan(String id){
		return "//span[@id='" + id + "']";
	}
	
	private String getDiv(String id){
		return "//div[@id='" + id + "']";
	}
	
    @Before
    public void setup() throws Exception {
        wd = SessionManager.getInstance().getNewSession();
    }

    @After
    public void teardown() {
        wd.close();
        SessionManager.getInstance().removeSession(wd);
    }
    
    @Test
    public void testGetLocator() throws WidgetException{
        wd.open(url);
    	String test = "//DIV[@id='this_is_my_locator']";
    	IElement e = new Element(test);
    	Assert.assertEquals("Test get locator should return the value passed in", test, e.getLocator());
    }
    
    @Test
    public void testIsElementPresentTrue() throws WidgetException{
        wd.open(url);
        IElement e = new Element(button);
    	Assert.assertTrue("Test isElementPresent on an element that does exist returns true", e.isElementPresent());
    } 
    @Test
    public void testIsElementPresentFalse() throws WidgetException{
        wd.open(url);
    	String test = "//button[@id='myButtonDoesntExist']";
    	IElement e = new Element(test);
    	Assert.assertFalse("Test isElementPresent on an element that doesn't exist returns false", e.isElementPresent());
    }

//    @Test(expected=WidgetException.class)
//    public void testIsElementPresentException() throws WidgetException{
//        wd.open(url);
//        String test = "//button[@id='myButtonDo";
//        IElement e = new Element(test);
//        e.isElementPresent();
//    }
    
    @Test
    public void testIsElementPresentTimeoutTrue() throws WidgetException{
        wd.open(url);
        IElement e = new Element(button);
    	Assert.assertTrue("Test isElementPresent on an element that does exist returns true", e.isElementPresent(500));
    } 
    @Test
    public void testIsElementPresentTimeoutFalse() throws WidgetException{
        wd.open(url);
    	String test = "//button[@id='myButtonDoesntExist']";
    	IElement e = new Element(test);
    	Assert.assertFalse("Test isElementPresent on an element that doesn't exist returns false", e.isElementPresent(500));
    }

//    @Test(expected=WidgetException.class)
//    public void testIsElementPresentTimeoutException() throws WidgetException{
//        wd.open(url);
//        String test = "//button[@id='myButtonDo";
//        IElement e = new Element(test);
//        e.isElementPresent(500);
//    }
    
    @Test
    public void testIsElementNotPresentFalse() throws WidgetException{
        wd.open(url);
        IElement e = new Element(button);
    	Assert.assertFalse("Test isElementPresent on an element that does not exist returns false", e.isElementNotPresent());
    } 
    @Test
    public void testIsElementNotPresentTrue() throws WidgetException{
        wd.open(url);
    	String test = "//button[@id='myButtonDoesntExist']";
    	IElement e = new Element(test);
    	Assert.assertTrue("Test isElementPresent on an element that doesn't exist returns true", e.isElementNotPresent());
    }

//    @Test(expected=WidgetException.class)
//    public void testIsElementNotPresentException() throws WidgetException{
//        wd.open(url);
//        String test = "//button[@id='myButtonDo";
//        IElement e = new Element(test);
//        e.isElementNotPresent();
//    }

    @Test
    public void testIsElementNotPresentTimeoutFalse() throws WidgetException{
        wd.open(url);
        IElement e = new Element(button);
    	Assert.assertFalse("Test isElementPresent on an element that does not exist returns false", e.isElementNotPresent(500));
    } 
    @Test
    public void testIsElementNotPresentTimeoutTrue() throws WidgetException{
        wd.open(url);
    	String test = "//button[@id='myButtonDoesntExist']";
    	IElement e = new Element(test);
    	Assert.assertTrue("Test isElementPresent on an element that doesn't exist returns true", e.isElementNotPresent(500));
    }

//    @Test(expected=WidgetException.class)
//    public void testIsElementNotPresentTimeoutException() throws WidgetException{
//        wd.open(url);
//        String test = "//button[@id='myButtonDo";
//        IElement e = new Element(test);
//        e.isElementNotPresent(500);
//    }
    
    @Test
    public void testIsElementVisibleFalse() throws WidgetException{
    	wd.open(url);
    	IElement hidden = new Element(invisibleButton);
    	Assert.assertFalse("Testing isElementVisible on a non-hidden button should be return true", hidden.isElementVisible());
    }
    
    @Test
    public void testIsElementVisibleTrue() throws WidgetException{
    	wd.open(url);
    	IElement hidden = new Element(button);
    	Assert.assertTrue("Testing isElementVisible on a hidden button should be return false", hidden.isElementVisible());	
    }
    
    @Test(expected=WidgetException.class)
    public void testIsElementVisibleException() throws WidgetException{
    	wd.open(url);
    	IElement hidden = new Element(null);
    	hidden.isElementVisible();
    }
    
    @Test
    public void testIsElementVisibleTimeoutFalse() throws WidgetException{
    	wd.open(url);
    	IElement hidden = new Element(invisibleButton);
    	Assert.assertFalse("Testing isElementVisible on a non-hidden button should be return true", hidden.isElementVisible(500));
    }
    
    @Test
    public void testIsElementVisibleTimeoutTrue() throws WidgetException{
    	wd.open(url);
    	IElement hidden = new Element(button);
    	Assert.assertTrue("Testing isElementVisible on a hidden button should be return false", hidden.isElementVisible(500));	
    }

//    @Test(expected=WidgetException.class)
//    public void testIsElementVisibleTimeoutException() throws WidgetException{
//        wd.open(url);
//        IElement hidden = new Element(null);
//        hidden.isElementVisible(500);
//    }
    
    @Test
    public void testWaitForElementPresent() throws WidgetException{
    	wd.open(url);
    	
    	IInteractiveElement clickButton = new Button(getButton("testWaitButton"));
    	IElement waitForElement = new Element(getSpan("waitForSpan"));
    	
    	clickButton.click();
    	waitForElement.waitForElementPresent();
    }
    
    @Test(expected=WidgetException.class)
    public void testWaitForElementPresentTimeout() throws WidgetException{
    	wd.open(url);
    	
    	IInteractiveElement clickButton = new Button(getButton("testWaitButton"));
    	IElement waitForElement = new Element(getSpan("failMe"));
    	
    	clickButton.click();
    	waitForElement.waitForElementPresent();
    }
    
    @Test
    public void testWaitForElementNotPresent() throws WidgetException{
    	wd.open(url);
    	
    	IInteractiveElement clickButton = new Button(getButton("testWaitElementNotPresentButton"));
    	IElement waitForElement = new Element(getSpan("someText"));
    	
    	clickButton.click();
    	waitForElement.waitForElementNotPresent();
    }
    
    @Test(expected=WidgetException.class)
    public void testWaitForElementNotPresentTimeout() throws WidgetException{
    	wd.open(url);

    	IElement waitForElement = new Element(getSpan("someText"));
    	
    	waitForElement.waitForElementNotPresent();
    }
    
    @Test
    public void testWaitForVisible() throws WidgetException{
    	wd.open(url);
    	
    	IInteractiveElement clickButton = new Button(getButton("visibilityTest"));
    	IElement element = new Element(getButton("myInvisibleButton"));
    	
    	clickButton.click();
    	element.waitForVisible();
    }

    @Test(expected=WidgetException.class)
    public void testWaitForVisibleException() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getButton("myInvisibleButton"));    	
    	element.waitForVisible();    	
    }
    
    @Test
    public void testWaitForNotVisible() throws WidgetException{
    	wd.open(url);
    	
    	IInteractiveElement clickButton = new Button(getButton("nonVisibilityTest"));
    	IElement element = new Element(getButton("visibilityTest"));
    	
    	clickButton.click();
    	element.waitForNotVisible();
    }

    @Test(expected=WidgetException.class)
    public void testWaitForNotVisibleException() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getButton("visibilityTest"));    	
    	element.waitForNotVisible();    	
    }
    
    @Test
    public void testGetText() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getSpan("someText"));
    	String exp = "Here is some text";
    	Assert.assertEquals(exp, element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testGetTextException() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getSpan("INVALIDsomeText"));
    	element.getText();
    }
    
    @Test
    public void testIsAttributePresentTrue() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getDiv("content"));
    	Assert.assertTrue("Test isAttributePresent returns true", element.isAttributePresent("randomAttrib"));
    	
    }
    
    
    // * TODO: Instead of returning false, HTMLUnit throws the WidgetException, try running this test in firefox
     
    @Test
    public void testIsAttributePresentFalse() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getDiv("content"));
    	Assert.assertFalse("Test isAttributePresent returns false", element.isAttributePresent("someAttribute"));
    	
    }
    
    @Test(expected=WidgetException.class)
    public void testIsAttributePresentException() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getDiv("myContent"));
    	element.isAttributePresent("randomAttrib");
    }
    
    @Test
    public void testGetAttribute() throws WidgetException{
    	wd.open(url);
    	
    	IElement element = new Element(getDiv("content"));
    	String expected = "Hello";
    	Assert.assertEquals("Test getAttribute returns expected string", expected, element.getAttribute("randomAttrib"));
    	
    }
    
//    @Test(expected=WidgetException.class)
//    public void testGetAttributeException() throws WidgetException{
//    	wd.open(url);
//
//    	IElement element = new Element(getDiv("NotMyContent"));
//    	element.getAttribute("randomAttrib");
//    }
    
    @Test
    public void testWaitForAttributeEqualTo() throws WidgetException{
    	wd.open(url);

		IInteractiveElement clickButton = new Button(getButton("attributeChanger"));
    	IElement element = new Element(getDiv("content"));
    	
    	clickButton.click();
    	element.waitForAttributeEqualTo("randomAttrib", "Goodbye");	
    }
    
    @Test(expected=WidgetException.class)
    public void testWaitForAttributeEqualToException() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getDiv("content"));
    	
    	element.waitForAttributeEqualTo("randomAttrib", "Goodbye");	
    }
    
    @Test
    public void testWaitForAttributeNotEqualTo() throws WidgetException{
    	wd.open(url);

		IInteractiveElement clickButton = new Button(getButton("attributeChanger"));
    	IElement element = new Element(getDiv("content"));
    	
    	clickButton.click();
    	element.waitForAttributeNotEqualTo("randomAttrib", "Hello");	
    }
    
    @Test(expected=WidgetException.class)
    public void testWaitForAttributeNotEqualToException() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getDiv("content"));
    	
    	element.waitForAttributeNotEqualTo("randomAttrib", "Hello");	
    }
    
    @Test
    public void testWaitForText() throws WidgetException{
    	wd.open(url);
		IInteractiveElement clickButton = new Button(getButton("testWaitButton"));
    	IElement element = new Element(getDiv("content"));
    	
    	clickButton.click();
    	element.waitForText();
    }
    
    @Test(expected=WidgetException.class)
    public void testWaitForTextException() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getDiv("content"));
    	element.waitForText();
    }
    
    @Test 
    public void testGetLocationX() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getButton("testWaitButton"));
    	int val = -1;
    	val = element.getLocationX();	
    	Assert.assertNotEquals("Test that getLocationX returned an int value", -1, val);
    }
    
    @Test (expected=WidgetException.class)
    public void testGetLocationXException() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getButton("zzztestWaitButton"));
    	int val = -1;
    	val = element.getLocationX();	
    	Assert.assertNotEquals(-1, val);
    }
    
    @Test 
    public void testGetLocationY() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getButton("testWaitButton"));
    	int val = -1;
    	val = element.getLocationY();	
    	Assert.assertNotEquals("Test that getLocationY returned an int value", -1, val);
    }
    
    @Test (expected=WidgetException.class)
    public void testGetLocationYException() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getButton("zzztestWaitButton"));
    	int val = -1;
    	val = element.getLocationY();	
    	Assert.assertNotEquals(-1, val);
    }
    
    @Test
    public void testHasTextTrue() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getDiv("removeContent"));
    	
        Assert.assertTrue("Test that hasText returns true", element.hasText("some"));    	
    }
    
    @Test
    public void testHasTextFalse() throws WidgetException{
    	wd.open(url);
    	IElement element = new Element(getDiv("removeContent"));
        Assert.assertFalse("Test that hasText returns false", element.hasText("This Be"));    	
    }
    
    @Test(expected=WidgetException.class)
    public void testHasTextException() throws WidgetException{
    	wd.open(url);
		IElement element = new Element(getDiv("zzzzcontent"));
    	
    	element.hasText("Here");
    }
    
    @Test
    public void testGetCssValue() throws WidgetException{
    	wd.open(url);
		IElement element = new Element(getButton("myInvisibleButton"));
    	
    	Assert.assertEquals("Test that getCssValue returns the correct attribute", "hidden", element.getCssValue("visibility"));
    	
    }
    
    @Test(expected=WidgetException.class)
    public void testGetCssValueException() throws WidgetException{
    	wd.open(url);
		IElement element = new Element(getButton("zzzmyInvisibleButton"));
    	
    	Assert.assertEquals("Test that getCssValue returns the correct attribute", "hidden", element.getCssValue("visibility"));
    	
    }
    
    
     //*Not part of IElement, but part of Element 
     
    
    @Test
    public void testSetGUIDriver() throws Exception{
        ExtWebDriver otherwd = null;
        try {
            otherwd = SessionManager.getInstance().getNewSession(false);
            wd.open(url);
            otherwd.open(url);
            Element content = new Element(getDiv("content"));
            IInteractiveElement button = new Button(getButton("testWaitButton"));
            button.click();
            content.waitForText();
            String session1Str = content.getText();

            content.setGUIDriver(otherwd);

            content.waitForElementPresent();
            String session2Str = content.getText();

            Assert.assertNotEquals("Test that Element successfully switched sessions", session1Str, session2Str);
        } finally {
            if(otherwd != null) {
                otherwd.close();
                SessionManager.getInstance().removeSession(otherwd);
            }
        }
    }
    
    @Test
    public void testGetInnerHtml() throws WidgetException {
    	wd.open(url);
    	IElement e = new Element("someText");
    	String html = e.getInnerHTML();
    	Assert.assertEquals("Here is some text", html);
    }
    
    @Test
    public void testGetChildNodesValuesText() throws WidgetException{
    	wd.open(url);
    	IElement e = new Element("inner1");
    	String[] actual = e.getChildNodesValuesText();
    	String[] expected = new String[]{"hello world", "welcome"};
    	Assert.assertArrayEquals(expected, actual);
    }

    @Test
    public void testCSSSelector() throws WidgetException{
    	wd.open(url);
    	IElement e = new Element("#myButton");
    	Assert.assertEquals("Click me", e.getText());
    }
    @Test
    public void testCSSSelector2() throws WidgetException{
    	wd.open(url);
    	IElement e = new Element("#removeContent>span");
    	Assert.assertEquals("Here is some text", e.getText());
    }
    
    @Test
    public void testHighlight() throws WidgetException{
    	wd.open(url);
    	HighlightProvider highDriver = (HighlightProvider) wd;
    
    	Element e1 = new Element(getButton("testOverTime"));
    	e1.findElement();
    	Assert.assertEquals(highDriver.getHighlightColor("find"),getRgb(e1.getCssValue("background-color")) );
    	
    	Element e2 = new Element(getButton("myButton"));
    	e2.isAttributePresent("xyz");
    	Assert.assertEquals(getRgb(e2.getCssValue("background-color")), highDriver.getHighlightColor("get"));
    }
    
    public String getRgb(String rgba){
    	if(rgba.startsWith("rgba") & rgba.split(",").length>3){
    		String[] splits = rgba.substring(rgba.indexOf("a")+1).split(",");
    		return "rgb"+splits[0]+","+splits[1]+","+splits[2]+")";
    	}
    	else
    		return rgba;
    }
}
