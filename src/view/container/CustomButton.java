package view.container;

import javax.swing.*;
import java.awt.*;

import static controller.constant.Constants.SHOW_COLOR;

public class CustomButton extends JButton {
    public CustomButton(String x) {
        super(x);
        this.setContentAreaFilled(false);
        this.setOpaque(false);
        this.setBorderPainted(false);
        this.setFocusable(false);
    }
}
