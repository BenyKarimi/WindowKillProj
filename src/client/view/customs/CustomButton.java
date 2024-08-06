package client.view.customs;

import javax.swing.*;

public class CustomButton extends JButton {
    public CustomButton(String x) {
        super(x);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setFocusable(false);
    }
}
