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
import org.finra.jtaf.ewd.session.SessionManager;
import org.finra.jtaf.ewd.widget.IElement;
import org.finra.jtaf.ewd.widget.IInteractiveElement;
import org.finra.jtaf.ewd.widget.ITextField;
import org.finra.jtaf.ewd.widget.Keys;
import org.finra.jtaf.ewd.widget.WidgetException;
import org.finra.jtaf.ewd.widget.element.html.Input;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class InteractiveElementTest {

    public static String url = "http://localhost:29090/simpleapp/elementstestapp.html";

	public ExtWebDriver wd;
	public static String button = "//button[@id='myButton']";
	public static String invisibleButton = "//button[@id='myInvisibleButton']";

	private String getButton(String id){
		return "//button[@id='" + id + "']";
	}
	
	private String getInput(String id){
		return "//input[@id='" + id + "']";
	}
	
	private String getSpan(String id){
		return "//span[@id='" + id + "']";
	}
	
	private String getDiv(String id){
		return "//div[@id='" + id + "']";
	}
	
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
    public void testClick() throws Exception {
        wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//button[@id='myButton']");
        ie.waitForElementPresent();
        ie.click();
        Assert.assertEquals("Clicked", (new Element("//div[@id='content']")).getText());
    }
    
    @Test
    public void testClickJSClick() throws Exception {
        wd.open(url);
        wd.setClickMode(true);
        IInteractiveElement ie = new InteractiveElement("//button[@id='myButton']");
        Element content = new Element("//div[@id='content']");
        ie.waitForElementPresent();
        ie.click();
        wd.setClickMode(false);
        Assert.assertEquals("Clicked", content.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testClickException() throws Exception {
        wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//button[@id='zzzz");
        ie.click();
    }
    
    @Test
    public void testDoubleClick() throws Exception {
        wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("dblClickTest"));
        ie.waitForElementPresent();
        ie.doubleClick();
        Assert.assertEquals("double clicked", (new Element("//div[@id='content']")).getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testDoubleClickException() throws Exception {
        wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//button[@id='zzzz");
        ie.doubleClick();
    }
    
    @Test
    public void testRightClick() throws Exception {
        wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("rightClickTest"));
        ie.waitForElementPresent();
        ie.rightClick();
        Assert.assertEquals("right clicked", (new Element("//div[@id='content']")).getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testRightClickException() throws Exception {
        wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad");
        ie.rightClick();
    }
    
    @Test
    public void testIsEnabledFalse() throws WidgetException {
        wd.open(url);
        IInteractiveElement ie = new InteractiveElement(getButton("disabledBtn"));
        ie.waitForElementPresent();
        Assert.assertFalse("isEnabled should return false", ie.isEnabled());
    
    }

    @Test
    public void testIsEnabledTrue() throws WidgetException {
        wd.open(url);
        IInteractiveElement ie = new InteractiveElement(getInput("rightClickTest"));
        ie.waitForElementPresent();
        Assert.assertTrue("isEnabled should return true", ie.isEnabled());
        	
    }
    
    @Test(expected=WidgetException.class)
    public void testIsEnabledException() throws WidgetException {
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad");
        Assert.assertTrue("isEnabled should return true", ie.isEnabled());
        
    }

    @Test(expected=WidgetException.class)
    public void testDragAndDropException() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//div[bad");
        IInteractiveElement destination = new InteractiveElement("//div[alsoBad");
        ie.dragAndDrop(destination);       	
    }
      
    @Test(expected=WidgetException.class)
    public void testDragAndDropByOffsetException() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//div[bad");
        ie.dragAndDropByOffset(0, 50);       	
    }
    
    @Test
    public void testKeyDown() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement(getInput("inputFieldTest"));
        IElement element = new Element(getDiv("content"));
        ie.waitForElementPresent();
        ie.keyDown(Keys.SHIFT);
        Assert.assertEquals("KeyDown", element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testKeyDownException() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.keyDown(Keys.SHIFT);
    }    
    
    @Test
    public void testKeyUp() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("inputFieldTest"));
        IElement element = new Element(getDiv("content"));
        ie.waitForElementPresent();
        ie.keyDown(Keys.SHIFT);
        ie.keyUp(Keys.SHIFT);
        Assert.assertEquals("KeyUp", element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testKeyUpException() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.keyUp(Keys.SHIFT);
    }
    
    @Test
    public void testClickAndHold() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("mouseEvents"));
        IElement element = new Element(getDiv("content"));
        
        ie.waitForElementPresent();
        ie.clickAndHold();
        Assert.assertEquals("Mouse is Down", element.getText());
           
    }
    
    @Test(expected=WidgetException.class)
    public void testClickAndHoldException() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.clickAndHold();          
    }
    
    @Test
    public void testReleaseClickAndHold() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("mouseEvents"));
        IElement element = new Element(getDiv("content"));
        
        ie.waitForElementPresent();
        ie.clickAndHold();
        ie.releaseClickAndHold();
        Assert.assertEquals("Mouse Released", element.getText());
           
    }
    
    @Test(expected=WidgetException.class)
    public void testReleaseClickAndHoldException() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.releaseClickAndHold();          
    }
    
    @Test
    public void testSendKeys() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("inputFieldTest"));
        IElement element = new Element(getDiv("textcontent"));
        ie.waitForElementPresent();
        ie.sendKeys("abcd");
        Assert.assertEquals("abcd", element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testSendKeysException() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.sendKeys("abcd");
    }
    
    @Test
    public void testMouseMove() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getDiv("dest"));
        IElement element = new Element(getDiv("content"));
        ie.waitForElementPresent();
        ie.mouseMove();
        Assert.assertEquals("Mouse Move Triggered", element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testMouseMoveException() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//div[bad]");
        ie.mouseMove();
    }

    @Test(expected=WidgetException.class)
    public void testMouseMoveOutException() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement("//div[bad]");
        ie.mouseMoveOut();
    }

    @Test
    public void testMouseOver() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("mouseEvents"));
        IElement element = new Element(getDiv("content"));
        ie.waitForElementPresent();
        ie.mouseOver();
        Assert.assertEquals("Mouse Over", element.getText());
    }
    
    @Test(expected=WidgetException.class)
    public void testMouseOverException() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement("//input[bad]");
        ie.mouseOver();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetValue() throws WidgetException{
    	wd.open(url);
        IInteractiveElement ie = new InteractiveElement(getInput("mouseEvents"));
        ie.waitForElementPresent();
        ie.getValue();
    }
    
    @Test(expected=UnsupportedOperationException.class)
    public void testGetLabel() throws WidgetException{
    	wd.open(url);

        IInteractiveElement ie = new InteractiveElement(getInput("mouseEvents"));
        ie.waitForElementPresent();
        ie.getLabel();
    }
    
    @Test
    public void testType() throws WidgetException{
    	wd.open(url);
    	IInteractiveElement field = new InteractiveElement("//input[@id='inputFieldTest']");
        field.waitForElementPresent();
    	field.type("hello");
        IElement element = new Element(getDiv("textcontent"));
        Assert.assertEquals("hello", element.getText());
    }
    
    @Test
    public void testJavascriptType() throws WidgetException{
    	wd.open(url);
    	wd.setTypeMode(true);
    	IInteractiveElement field = new InteractiveElement("//input[@id='inputFieldTest']");
        field.waitForElementPresent();
    	field.type("hello");
    	wd.setTypeMode(false);
    	Input i = new Input(field.getLocator());
        Assert.assertEquals("hello", i.getValue());
    }
    
    @Test(expected=WidgetException.class)
    public void testTypeException() throws WidgetException{
    	wd.open(url);
    	IInteractiveElement field = new InteractiveElement("//input[dff]");
    	field.type("hello");
    }
    
    @Test
    public void testTypeAppend() throws WidgetException{
    	wd.open(url);
    	IInteractiveElement field = new InteractiveElement("//input[@id='inputFieldTest']");
        IElement element = new Element(getDiv("textcontent"));
        
    	field.waitForElementPresent();
    	field.typeAppend("hello");
        Assert.assertEquals("hello", element.getText());
        field.typeAppend("world");
        Assert.assertEquals("helloworld", element.getText());
        
    }
    
    @Test(expected=WidgetException.class)
    public void testTypeAppendException() throws WidgetException{
    	wd.open(url);
    	IInteractiveElement field = new InteractiveElement("//input[dff]");
    	field.type("hello");
    }
    
    @Test
    public void testHighlight() throws WidgetException{
    	wd.open(url);
    	HighlightProvider highDriver = (HighlightProvider) wd;
    	
    	IInteractiveElement b = new InteractiveElement("//button[@id='myButton']");
    	b.click();
    	Assert.assertEquals(getRgb(b.getCssValue("background-color")), highDriver.getHighlightColor("put"));
//    	  	
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