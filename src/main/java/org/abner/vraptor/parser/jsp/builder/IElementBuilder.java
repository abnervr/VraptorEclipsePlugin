package org.abner.vraptor.parser.jsp.builder;

import java.util.ArrayList;
import java.util.List;

import org.abner.vraptor.InvalidJspException;
import org.abner.vraptor.parser.jsp.Element;
import org.abner.vraptor.parser.jsp.IElement;
import org.abner.vraptor.parser.jsp.TextElement;

public class IElementBuilder {

    public static List<IElement> buildElements(JspIterator iterator) {
        return buildElements(null, iterator);
    }

    public static List<IElement> buildElements(String elementEnd, JspIterator iterator) {
        List<IElement> elements = new ArrayList<>();
        while (iterator.hasNext()) {
            String value = iterator.next();

            if (isElementStart(value)) {
                elements.add(buildElement(iterator, value));
            } else if (elementEnd != null && value.startsWith(elementEnd)) {
                elementEnd = null;
                break;
            } else {
                TextElement textElement = getLastTextElement(elements);
                textElement.append(value, iterator.getColNumber());
            }
        }
        if (elementEnd != null) {
            throw new InvalidJspException("End of element: " + elementEnd + " not found");
        }
        return elements;
    }

    public static Element buildElement(JspIterator iterator, String value) {
        ElementBuilder builder = new ElementBuilder(iterator);
        return builder.build(value);
    }

    private static TextElement getLastTextElement(List<IElement> elements) {
        int lastIndex = elements.size() - 1;
        if (lastIndex >= 0 &&
                        elements.get(lastIndex) instanceof TextElement) {
            return (TextElement) elements.get(lastIndex);
        }
        TextElement textElement = new TextElement();
        elements.add(textElement);
        return textElement;
    }


    static boolean isElementStart(String value) {
        return value.matches("<[a-zA-Z].*");
    }

}
