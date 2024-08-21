package editor;

import java.awt.FontMetrics;
import java.util.Iterator;

public class EditorUtility {
    public static int getTextWidth(String text, FontMetrics fontMetrics) {
        Iterator<String> iterable = text.lines().iterator();
        int width = 0;
        while (iterable.hasNext()) {
            String line = iterable.next();
            int lineWidth = fontMetrics.stringWidth(line);
            if (lineWidth > width) {
                width = lineWidth;
            }
        }

        return width;
    }

    public static int getLineHeight(String text, FontMetrics fontMetrics) {
        int lineHeight = fontMetrics.getHeight();
        return lineHeight;
    }

    public static int getTextHeight(String text, FontMetrics fontMetrics) {
        int height = (int) (getLineHeight(text, fontMetrics) * text.lines().count());
        return height;
    }
}
