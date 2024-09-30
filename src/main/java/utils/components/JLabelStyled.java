package utils.components;

import javax.swing.*;

public class JLabelStyled extends JLabel {
    public JLabelStyled(String text) {
        super(text);

        setFont(new ComonTextStyle(12));
    }

    public JLabelStyled(String text, int fs) {
        super(text);

        setFont(new ComonTextStyle(fs));
    }
}
