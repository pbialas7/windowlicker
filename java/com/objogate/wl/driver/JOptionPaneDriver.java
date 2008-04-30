package com.objogate.wl.driver;

import static com.objogate.wl.matcher.ComponentMatchers.withButtonText;
import static org.hamcrest.Matchers.anyOf;

import java.awt.Component;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JTextField;

import org.hamcrest.Matcher;
import org.hamcrest.core.IsEqual;

import com.objogate.wl.ComponentSelector;
import com.objogate.wl.Prober;
import com.objogate.wl.gesture.GesturePerformer;
import com.objogate.wl.matcher.JLabelTextMatcher;

/**
 * Driver for a JOptionPane
 * <p/>
 * <strong>You should make sure that the owner driver for this is for a top level frame,
 * otherwise this will not work as expected</strong>
 */
public class JOptionPaneDriver extends ComponentDriver<JOptionPane> {
    public JOptionPaneDriver(GesturePerformer gesturePerformer, ComponentSelector<JOptionPane> componentSelector, Prober prober) {
        super(gesturePerformer, componentSelector, prober);
    }

    public JOptionPaneDriver(ComponentDriver<? extends Component> parentOrOwner, ComponentSelector<JOptionPane> componentSelector) {
        super(parentOrOwner, componentSelector);
    }

    public JOptionPaneDriver(ComponentDriver<? extends Component> parentOrOwner, Class<JOptionPane> componentType, Matcher<? super JOptionPane>... matchers) {
        super(parentOrOwner, componentType, matchers);
    }

    public void clickOK() {
        clickButtonWithText("OK");
    }

    public void clickYes() {
        clickButtonWithText("Yes");
    }

    public void clickButtonWithText(String buttonText) {
        new AbstractButtonDriver<JButton>(this, the(JButton.class, withButtonText((buttonText)))).click();
    }

    public void typeText(String text) {
        JTextFieldDriver textField = new JTextFieldDriver(this, the(JTextField.class, ComponentDriver.named("OptionPane.textField")));
        textField.moveMouseToCenter();
        textField.typeText(text);
    }

    @SuppressWarnings("unchecked")
    public void selectValue(String value) {
        GeneralListDriver driver = new GeneralListDriver(this, JComponent.class, anyOf(ComponentDriver.named("OptionPane.comboBox"), ComponentDriver.named("OptionPane.list")));
        driver.selectItem(new JLabelTextMatcher(new IsEqual<String>(value)));
        clickOK();
    }
}
