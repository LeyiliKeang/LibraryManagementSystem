package groupwork.my.document;


import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

public class RegexDocument extends PlainDocument {
    private String regex;

    public RegexDocument() {
        super();
    }

    public RegexDocument(String regex) {
        this();
        this.regex = regex;
    }

    @Override
    public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
        if (str == null) {
            return;
        }
        if (regex != null) {
            if (!new StringBuilder(getText(0, getLength())).insert(offs, str).toString().matches(regex)) {
                return;
            }
            super.insertString(offs, str, a);
        } else {
            super.insertString(offs, str, a);
        }
    }
}
