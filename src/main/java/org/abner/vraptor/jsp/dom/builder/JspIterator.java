package org.abner.vraptor.jsp.dom.builder;

import java.io.InputStream;
import java.util.Scanner;

public class JspIterator {

    private static final int LINE_FEED_SIZE = 1;

    public static final JspIterator EMPTY_ITERATOR = new JspIterator();

    private Scanner scanner;
    private int colNumber = 0;
    private LineIterator lineIterator;

    public JspIterator(InputStream is) {
        scanner = new Scanner(is);
    }

    private JspIterator() {
        scanner = new Scanner("");
    }

    public boolean hasNext() {
        if (lineHasNext()) {
            return true;
        }
        while (scanner.hasNext()) {
            lineIterator = new LineIterator(scanner.nextLine());
            if (lineHasNext()) {
                return true;
            }
        }
        return false;
    }

    private boolean lineHasNext() {
        if (lineIterator != null) {
            if (lineIterator.hasNext()) {
                return true;
            } else {
                colNumber += lineIterator.getColIndex() + LINE_FEED_SIZE;
                lineIterator = null;
            }
        }
        return false;
    }

    public void close() {
        scanner.close();
    }

    public String next() {
        return lineIterator.next();
    }

    public int getColNumber() {
        return colNumber + lineIterator.getColIndex();
    }
}