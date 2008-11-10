package com.objogate.wl.web;

import org.hamcrest.Description;
import org.openqa.selenium.WebElement;

import com.objogate.wl.Probe;

public abstract class ElementActionProbe implements Probe {
    private final AsyncElementDriver driver;
    
    private WebElement foundElement = null;
    
    public ElementActionProbe(AsyncElementDriver driver) {
        this.driver = driver;
    }
    
    public void probe() {
        foundElement = driver.findElement();
        if (foundElement != null) {
            action(foundElement);
        }
    }
    
    public boolean isSatisfied() {
        return foundElement != null;
    }
    
    public void describeTo(Description description) {
        // TODO Auto-generated method stub
    }

    public boolean describeFailureTo(Description description) {
        // TODO Auto-generated method stub
        return false;
    }

    protected abstract void action(WebElement element);
}