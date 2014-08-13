package org.finra.jtaf.ewd.widget.element;

import org.openqa.selenium.By;

/**
 * {@link StringLocatorAwareBy} implementation of {@link ByXPath}.
 * @author niels
 *
 */
public class EByXpath extends StringLocatorAwareBy {
    
    public EByXpath(String locator) {
        super(locator, By.xpath(locator));
    }

}
