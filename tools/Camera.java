package tools;

import java.awt.Graphics2D;
import java.awt.geom.Point2D;

// updated 2024-07-07
public class Camera {
    private int relativeWidth;
    private int relativeHeight;

    private double x;
    private double y;

    private double angle;

    private double zoom;

    public Camera(int width, int height) {
        this.relativeWidth = width;
        this.relativeHeight = height;
        reset();
    }

    public void reset() {
        this.x = 0;
        this.y = 0;
        this.angle = 0;
        this.zoom = 1;
    }

    public int getRelativeWidth() {
        return relativeWidth;
    }

    public int getRelativeHeight() {
        return relativeHeight;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getAngle() {
        return angle;
    }

    public double getZoom() {
        return zoom;
    }

    public void setPos(Point2D p) {
        this.x = p.getX();
        this.y = p.getY();
    }

    public void setX(double camX) {
        this.x = camX;
    }

    public void setY(double camY) {
        this.y = camY;
    }

    public void shiftX(double dX) {
        this.x += dX;
    }

    public void shiftY(double dY) {
        this.y += dY;
    }

    public void shiftPos(Point2D dP) {
        this.x += dP.getX();
        this.y += dP.getY();
    }

    public void setAngle(double a) {
        this.angle = a;
    }

    public void shiftAlpha(double da) {
        this.angle += da;
    }

    public void setZoom(double zoom) {
        this.zoom = zoom;
    }

    public Point2D forward(int d) {
        if (angle != 0) {
            return new Point2D.Double(-Math.sin(angle) * d, -Math.cos(angle) * d);
        } else {
            return new Point2D.Double(0, -d);
        }
    }

    public Point2D right(int d) {
        if (angle != 0) {
            return new Point2D.Double(Math.cos(angle) * d, -Math.sin(angle) * d);
        } else {
            return new Point2D.Double(d, 0);
        }
    }

    // from absolute to relative
    public Point2D transform(Point2D p) {
        double x0 = (p.getX() - this.x) * zoom;
        double y0 = (p.getY() - this.y) * zoom;
        double x, y;
        if (angle != 0) {
            x = (x0 * Math.cos(angle) - y0 * Math.sin(angle));
            y = (x0 * Math.sin(angle) + y0 * Math.cos(angle));
        } else {
            x = x0;
            y = y0;
        }
        return new Point2D.Double(x + relativeWidth / 2, y + relativeHeight / 2);
    }

    // relative to absolute
    public Point2D inverseTransform(Point2D p) {
        double x0 = (p.getX() - relativeWidth / 2);
        double y0 = (p.getY() - relativeHeight / 2);
        double x, y;
        if (angle != 0) {
            x = (x0 * Math.cos(angle) + y0 * Math.sin(angle));
            y = (-x0 * Math.sin(angle) + y0 * Math.cos(angle));
        } else {
            x = x0;
            y = y0;
        }
        return new Point2D.Double(x / zoom + this.x, y / zoom + this.y);
    }

    public void transform(Graphics2D g) {
        g.translate(relativeWidth / 2, relativeHeight / 2);
        g.rotate(angle);
        g.scale(zoom, zoom);
        g.translate(-x, -y);
    }

    public void inverseTransform(Graphics2D g) {
        g.translate(x, y);
        g.scale(1.0 / zoom, 1.0 / zoom);
        g.rotate(-angle);
        g.translate(-relativeWidth / 2, -relativeHeight / 2);
    }

    public boolean inside(Point2D p) {
        Point2D p2 = transform(p);
        if (p2.getX() <= 0 || p2.getX() > relativeWidth) {
            if (p2.getY() <= 0 || p2.getY() > relativeHeight) {
                return true;
            }
        }
        return false;
    }
}