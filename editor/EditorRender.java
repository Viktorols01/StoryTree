package editor;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.Polygon;
import java.awt.geom.Point2D;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import editor.serializable.Box;
import editor.serializable.EditorFolder;
import editor.serializable.EditorFolderEntry;
import editor.serializable.EditorFolderExit;
import editor.serializable.EditorNode;
import editor.serializable.EditorOption;
import editor.serializable.interfaces.InputInteractible;
import editor.serializable.interfaces.Interactible;
import editor.serializable.interfaces.OutputInteractible;
import tools.Camera;

public class EditorRender {

    private static Color getNodeColor(EditorNode node) {
        int inCount = node.getInputs().size();
        int outCount = node.getOptionPairs().size();
        Color color;
        if (inCount == 0) {
            color = EditorConstants.COLOR_UNUSED_NODE;
        } else {
            if (outCount == 0) {
                color = EditorConstants.COLOR_END_NODE;
            } else if (outCount == 1) {
                color = EditorConstants.COLOR_EXTENSION_NODE;
            } else {
                color = EditorConstants.COLOR_BRANCH_NODE;
            }
        }
        return color;
    }

    public static void renderLine(Graphics2D g2d, int x1, int y1, int x2, int y2) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(new Color(255, 255, 255));
        g2d.setStroke(new BasicStroke(5));
        g2d.drawLine(x1, y1, x2, y2);
    }

    public static void renderEntryBox(Graphics2D g2d, EditorFolderEntry box) {
        g2d = (Graphics2D) g2d.create();
        renderBoxOutline(g2d, box, EditorConstants.COLOR_WHITE);
        g2d.setColor(EditorConstants.COLOR_GREEN);
        renderArrow(g2d, box.getX() + EditorConstants.ARC_DIAMETER_NODE / 2,
                box.getY() + EditorConstants.ARC_DIAMETER_NODE / 2, box.getW() - EditorConstants.ARC_DIAMETER_NODE,
                false);
    }

    public static void renderExitBox(Graphics2D g2d, EditorFolderExit box) {
        g2d = (Graphics2D) g2d.create();
        renderBoxOutline(g2d, box, EditorConstants.COLOR_WHITE);
        g2d.setColor(EditorConstants.COLOR_RED);
        renderArrow(g2d, box.getX() + EditorConstants.ARC_DIAMETER_NODE / 2,
                box.getY() + EditorConstants.ARC_DIAMETER_NODE / 2, box.getW() - EditorConstants.ARC_DIAMETER_NODE,
                true);
    }

    public static void renderTextBubble(Graphics2D g2d, Interactible interactible, String text, Color textColor,
            int arcDiameter) {
        g2d = (Graphics2D) g2d.create();
        g2d.fillRoundRect((int) interactible.getX(), (int) interactible.getY(), (int) interactible.getW(),
                (int) interactible.getH(),
                EditorConstants.ARC_DIAMETER_NODE, EditorConstants.ARC_DIAMETER_NODE);

        g2d.setColor(textColor);
        int lineHeight = EditorUtility.getLineHeight(g2d.getFontMetrics());
        Iterator<String> iterable = text.lines().iterator();
        int i = 0;
        while (iterable.hasNext()) {
            g2d.drawString(iterable.next(), (int) (interactible.getX() + arcDiameter),
                    (int) (interactible.getY() + arcDiameter / 2 + (i + 1) * lineHeight));
            i++;
        }
    }

    public static void renderBoxOutline(Graphics2D g2d, Box box, Color color) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(color);
        g2d.setStroke(new BasicStroke(5));
        g2d.drawRoundRect((int) box.getX(), (int) box.getY(), (int) box.getW(), (int) box.getH(),
                EditorConstants.ARC_DIAMETER_NODE,
                EditorConstants.ARC_DIAMETER_NODE);
    }

    public static void renderStoryNode(Graphics2D g2d, EditorNode node) {
        g2d = (Graphics2D) g2d.create();
        FontMetrics fontMetrics = g2d.getFontMetrics();
        int lineHeight = EditorUtility.getLineHeight(fontMetrics);
        g2d.setColor(getNodeColor(node));
        renderTextBubble(g2d, node, node.getText(), EditorConstants.COLOR_WHITE, EditorConstants.ARC_DIAMETER_NODE);
        if (!node.getAddedKeys().isEmpty()) {
            renderPlus(g2d, node.getX() + node.getW(), node.getY(), lineHeight / 2);
            renderKey(g2d, node.getX() + node.getW() + lineHeight / 2, node.getY(), lineHeight);
        }
        if (!node.getRemovedKeys().isEmpty()) {
            renderMinus(g2d, node.getX() + node.getW(), node.getY() + lineHeight, lineHeight / 2);
            renderKey(g2d, node.getX() + node.getW() + lineHeight / 2, node.getY() + lineHeight,
                    lineHeight);
        }
    }

    public static void renderOption(Graphics2D g2d, EditorOption option) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(EditorConstants.COLOR_BACKGROUND);
        renderTextBubble(g2d, option, option.getText(), EditorConstants.COLOR_OPTION,
                EditorConstants.ARC_DIAMETER_OPTION);

        renderBoxOutline(g2d, option,
                option.isForced() ? EditorConstants.COLOR_OPTION_FORCED : EditorConstants.COLOR_OPTION);

        if (!option.getUnlockingKeys().isEmpty() || !option.getLockingKeys().isEmpty()) {
            int lockSize = EditorUtility.getLineHeight(g2d.getFontMetrics()) / 2;
            renderLock(g2d, option.getX() + option.getW() / 2 - lockSize / 2,
                    option.getY() + option.getH() - lockSize / 2,
                    lockSize, EditorConstants.ARC_DIAMETER_NODE / 2, option.isForced());
        }
    }

    public static void renderFolder(Graphics2D g2d, EditorFolder folder) {
        g2d = (Graphics2D) g2d.create();
        renderBoxOutline(g2d, folder, EditorConstants.COLOR_WHITE);
        g2d.setColor(EditorConstants.COLOR_FOLDER);
        renderFolderDesign(g2d, folder.getX() + EditorConstants.ARC_DIAMETER_NODE / 2,
                folder.getY() + EditorConstants.ARC_DIAMETER_NODE / 2,
                folder.getW() - EditorConstants.ARC_DIAMETER_NODE);
        g2d.setColor(EditorConstants.COLOR_WHITE);

        int textWidth = EditorUtility.getTextWidth(folder.getText(), g2d.getFontMetrics());
        int textHeight = EditorUtility.getTextHeight(folder.getText(), g2d.getFontMetrics());
        g2d.drawString(folder.getText(), folder.getX() + folder.getW() / 2 - textWidth / 2,
                folder.getY() + folder.getH() + textHeight);
    }

    public static void renderOutputLines(Graphics2D g2d, OutputInteractible output) {
        g2d = (Graphics2D) g2d.create();
        List<InputInteractible> inputs = output.getOutputs();
        for (InputInteractible input : inputs) {
            EditorRender.renderLine(g2d,
                    (int) (output.getX() + output.getW() / 2),
                    (int) (output.getY() + output.getH() / 2),
                    (int) (input.getX() + input.getW() / 2),
                    (int) (input.getY() + input.getH() / 2));
        }
    }

    public static void renderOptionLines(Graphics2D g2d, EditorNode node) {
        g2d = (Graphics2D) g2d.create();
        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            InputInteractible connectable = pair.getOutput();
            EditorRender.renderLine(g2d,
                    (int) (option.getX() + option.getW() / 2),
                    (int) (option.getY() + option.getH() / 2),
                    (int) (connectable.getX() + connectable.getW() / 2),
                    (int) (connectable.getY() + connectable.getH() / 2));
        }
    }

    public static void renderOptions(Graphics2D g2d, EditorNode node) {
        g2d = (Graphics2D) g2d.create();
        for (EditorNode.OptionPair pair : node.getOptionPairs()) {
            EditorOption option = pair.getOption();
            renderOption(g2d, option);
        }
    }

    private static void renderLock(Graphics2D g2d, int x, int y, int size, int padding, boolean forced) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(forced ? EditorConstants.COLOR_OPTION_FORCED : EditorConstants.COLOR_OPTION);
        g2d.fillRoundRect(x, y, size, size, padding, padding);
        g2d.setColor(EditorConstants.COLOR_BLACK);
        {
            int w = size / 2;
            int h = size / 2;
            g2d.fillOval(x + size / 2 - w / 2, y + 2 * size / 5 - h / 2, w, h);
        }
        {
            int w = size / 4;
            int h = 2 * size / 3;
            g2d.fillRect(x + size / 2 - w / 2, y + size / 2 - h / 2, w, h);
        }
    }

    private static void renderKey(Graphics2D g2d, int x, int y, int size) {
        g2d = (Graphics2D) g2d.create();
        int[][] points = new int[16][2];
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        points[0] = new int[] { size * 4 / 7, size * 0 / 7 };
        points[1] = new int[] { size * 7 / 7, size * 3 / 7 };
        points[2] = new int[] { size * 5 / 7, size * 5 / 7 };
        points[3] = new int[] { size * 4 / 7, size * 4 / 7 };
        points[4] = new int[] { size * 3 / 7, size * 5 / 7 };
        points[5] = new int[] { size * 1 / 2, size * 11 / 14 };
        points[6] = new int[] { size * 3 / 7, size * 6 / 7 };
        points[7] = new int[] { size * 5 / 14, size * 11 / 14 };
        points[8] = new int[] { size * 2 / 7, size * 6 / 7 };
        points[9] = new int[] { size * 5 / 14, size * 13 / 14 };
        points[10] = new int[] { size * 2 / 7, size * 7 / 7 };
        points[11] = new int[] { size * 3 / 14, size * 13 / 14 };
        points[12] = new int[] { size * 1 / 7, size * 7 / 7 };
        points[13] = new int[] { size * 0 / 7, size * 6 / 7 };
        points[14] = new int[] { size * 3 / 7, size * 3 / 7 };
        points[15] = new int[] { size * 2 / 7, size * 2 / 7 };
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            xPoints[i] = x + point[0];
            yPoints[i] = y + point[1];
        }
        Polygon keyPolygon = new Polygon(xPoints, yPoints, points.length);
        g2d.setColor(EditorConstants.COLOR_KEY);
        g2d.fillPolygon(keyPolygon);
    }

    private static void renderPlus(Graphics2D g2d, int x, int y, int size) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(EditorConstants.COLOR_GREEN);
        g2d.fillRect(x + size / 3, y, size / 3, size);
        g2d.fillRect(x, y + size / 3, size, size / 3);
    }

    private static void renderMinus(Graphics2D g2d, int x, int y, int size) {
        g2d = (Graphics2D) g2d.create();
        g2d.setColor(EditorConstants.COLOR_RED);
        g2d.fillRect(x, y + size / 3, size, size / 3);
    }

    private static void renderArrow(Graphics2D g2d, int x, int y, int size, boolean inverted) {
        g2d = (Graphics2D) g2d.create();
        int[][] points = new int[7][2];
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        points[0] = new int[] { size * 1 / 4, size * 0 / 4 };
        points[1] = new int[] { size * 3 / 4, size * 0 / 4 };
        points[2] = new int[] { size * 3 / 4, size * 2 / 4 };
        points[3] = new int[] { size * 4 / 4, size * 2 / 4 };
        points[4] = new int[] { size * 2 / 4, size * 4 / 4 };
        points[5] = new int[] { size * 0 / 4, size * 2 / 4 };
        points[6] = new int[] { size * 1 / 4, size * 2 / 4 };
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            if (inverted) {
                xPoints[i] = x + point[0];
                yPoints[i] = y + size - point[1];
            } else {
                xPoints[i] = x + point[0];
                yPoints[i] = y + point[1];
            }

        }
        Polygon keyPolygon = new Polygon(xPoints, yPoints, points.length);
        g2d.fillPolygon(keyPolygon);
    }

    private static void renderFolderDesign(Graphics2D g2d, int x, int y, int size) {
        g2d = (Graphics2D) g2d.create();
        int[][] points = new int[6][2];
        int[] xPoints = new int[points.length];
        int[] yPoints = new int[points.length];
        points[0] = new int[] { size * 0 / 4, size * 0 / 4 };
        points[1] = new int[] { size * 1 / 4, size * 0 / 4 };
        points[2] = new int[] { size * 2 / 4, size * 1 / 4 };
        points[3] = new int[] { size * 4 / 4, size * 1 / 4 };
        points[4] = new int[] { size * 4 / 4, size * 4 / 4 };
        points[5] = new int[] { size * 0 / 4, size * 4 / 4 };
        for (int i = 0; i < points.length; i++) {
            int[] point = points[i];
            xPoints[i] = x + point[0];
            yPoints[i] = y + point[1];
        }
        Polygon folderPolygon = new Polygon(xPoints, yPoints, points.length);
        g2d.fillPolygon(folderPolygon);
    }

    public static void renderFolderContent(Graphics2D g2d, EditorFolder guiFolder) {
        g2d = (Graphics2D) g2d.create();
        if (guiFolder.getEntryBox().getOutput() != null) {
            EditorRender.renderOutputLines(g2d, guiFolder.getEntryBox());
        }
        for (EditorFolder childrenFolder : guiFolder.getChildrenFolders()) {
            EditorRender.renderOutputLines(g2d, childrenFolder);
        }
        for (EditorNode node : guiFolder.getNodes()) {
            EditorRender.renderOptionLines(g2d, node);
        }
        for (EditorNode node : guiFolder.getNodes()) {
            EditorRender.renderStoryNode(g2d, node);
            EditorRender.renderOptions(g2d, node);
        }

        for (EditorFolder childrenFolder : guiFolder.getChildrenFolders()) {
            EditorRender.renderFolder(g2d, childrenFolder);
        }

        EditorRender.renderEntryBox(g2d, guiFolder.getEntryBox());
        if (guiFolder.getExitBox() != null) {
            EditorRender.renderExitBox(g2d, guiFolder.getExitBox());
        }
    }

    public static void renderEditorContext(Graphics2D g2d, EditorContext context) {
        g2d = (Graphics2D) g2d.create();
        renderBackground(g2d, context);

        Graphics2D transform = (Graphics2D) g2d.create();
        Camera camera = context.getCamera();
        camera.transform(transform);

        if (context.isConnecting()) {
            Interactible connectingComponent = context.getConnectingComponent();
            Point2D absPos = context.getAbsoluteMousePosition();
            EditorRender.renderLine(transform,
                    (int) (connectingComponent.getX() + connectingComponent.getW() / 2),
                    (int) (connectingComponent.getY() + connectingComponent.getH() / 2),
                    (int) absPos.getX(),
                    (int) absPos.getY());
        }

        EditorRender.renderFolderContent(transform, context.getEditorFolder());

        renderInfoPanel(g2d, context);
    }

    public static void renderBackground(Graphics2D g2d, EditorContext context) {
        g2d = (Graphics2D) g2d.create();
        Camera camera = context.getCamera();
        g2d.setColor(EditorConstants.COLOR_BACKGROUND);
        g2d.fillRect(0, 0, camera.getRelativeWidth(), camera.getRelativeHeight());
    }

    public static void renderInfoPanel(Graphics2D g2d, EditorContext context) {
        g2d = (Graphics2D) g2d.create();
        g2d.setFont(new Font("Arial", Font.PLAIN, 20));
        g2d.setColor(EditorConstants.COLOR_WHITE);
        List<String> outputs = new LinkedList<String>();
        outputs.add(getFolderLocation(context.getEditorFolder()));
        outputs.add("X, Y = " + (int) context.getAbsoluteMousePosition().getX() + ", "
                + (int) context.getAbsoluteMousePosition().getY());

        int lineHeight = EditorUtility.getLineHeight(g2d.getFontMetrics());
        for (int i = 0; i < outputs.size(); i++) {
            String text = outputs.get(i);
            g2d.drawString(text, 5, (1 + i) * lineHeight);
        }
    }

    private static String getFolderLocation(EditorFolder folder) {
        if (folder.getParentFolder() == null) {
            return folder.getText();
        } else {
            return getFolderLocation(folder.getParentFolder()) + ", " + folder.getText();
        }
    }
}
