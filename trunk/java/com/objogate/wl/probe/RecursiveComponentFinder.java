package com.objogate.wl.probe;

import javax.swing.MenuElement;
import java.awt.Component;
import java.awt.Container;
import java.awt.Window;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import com.objogate.wl.ComponentFinder;
import com.objogate.wl.internal.NoDuplicateList;

public class RecursiveComponentFinder<T extends Component> implements ComponentFinder<T> {
    private final ComponentFinder<? extends Component> parentOrOwnerFinder;
    private final Class<T> componentType;
    private final Matcher<? super T> criteria;
    
    private List<T> found = new NoDuplicateList<T>();
    
    public RecursiveComponentFinder(Class<T> componentType, Matcher<? super T> criteria, ComponentFinder<? extends Component> parentOrOwnerFinder) {
        this.parentOrOwnerFinder = parentOrOwnerFinder;
        this.componentType = componentType;
        this.criteria = criteria;
    }
    
    public boolean isSatisfied() {
        return parentOrOwnerFinder.isSatisfied();
    }
    
    public List<T> components() {
        return found;
    }
    
    public void probe() {
        parentOrOwnerFinder.probe();
        
        found.clear();
        searchWithin(parentOrOwnerFinder.components());
    }
    
    private void searchWithin(Component[] components) {
        searchWithin(Arrays.asList(components));
    }

    private void searchWithin(MenuElement[] elements) {
        List<Component> list = new ArrayList<Component>();
        for (MenuElement element : elements) {
            list.add(element.getComponent());
        }
        searchWithin(list);
    }

    private void searchWithin(List<? extends Component> components) {
        for (Component component : components) {
            searchWithin(component);
        }
    }
    
    private void searchWithin(Component component) {
        if (componentType.isInstance(component) && criteria.matches(component)) {
            found.add(componentType.cast(component));
        }
        else {
            if (component instanceof Container) {
                searchWithin(((Container) component).getComponents());
            }
            
            if (component instanceof Window) {
                searchWithin(((Window)component).getOwnedWindows());
            }

            if ( component instanceof MenuElement) {
                searchWithin(((MenuElement)component).getSubElements());
            }
        }
    }
    
    public void describeTo(Description description) {
        description.appendText(componentType.getSimpleName())
                   .appendText(" that is ")
                   .appendDescriptionOf(criteria)
                   .appendText(", within a ")
                   .appendDescriptionOf(parentOrOwnerFinder);
    }
}