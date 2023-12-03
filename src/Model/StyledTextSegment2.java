package Model;

import javax.swing.text.AttributeSet;
import java.io.Serializable;

public class StyledTextSegment2 implements Serializable {
    private String text;
    private AttributeSet attributes;

    public StyledTextSegment2(String text, AttributeSet attributes) {
        this.text = text;
        this.attributes = attributes;
    }

    public String getText() {
        return text;
    }

    public AttributeSet getAttributes() {
        return attributes;
    }
}
