/**
 * 
 */
package org.finra.jtaf.ewd.widget.element;

import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.SearchContext;
import org.openqa.selenium.WebElement;

/**
 * An {@link By} construct which stored the locator as String.
 * @author niels
 *
 */
public abstract class StringLocatorAwareBy extends By {
    
    private final String locator;
    
    private final By by;

    /**
     * @param locator
     * @param by
     */
    public StringLocatorAwareBy(String locator, By by) {
        super();
        this.locator = locator;
        this.by = by;
    }

    /**
     * @return the locator
     */
    public String getLocator() {
        return locator;
    }

    /**
     * @return the by
     */
    public By getBy() {
        return by;
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.By#findElement(org.openqa.selenium.SearchContext)
     */
    @Override
    public WebElement findElement(SearchContext context) {
        return by.findElement(context);
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.By#findElements(org.openqa.selenium.SearchContext)
     */
    @Override
    public List<WebElement> findElements(SearchContext context) {
        return by.findElements(context);
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.By#equals(java.lang.Object)
     */
    @Override
    public boolean equals(Object o) {
        return by.equals(o);
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.By#hashCode()
     */
    @Override
    public int hashCode() {
        return by.hashCode();
    }

    /* (non-Javadoc)
     * @see org.openqa.selenium.By#toString()
     */
    @Override
    public String toString() {
        return by.toString();
    }
    
    public static EByXpath xpath(String locator) {
        return new EByXpath(locator);
    }

}
