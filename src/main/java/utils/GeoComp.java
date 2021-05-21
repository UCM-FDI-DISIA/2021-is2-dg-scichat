package utils;

import edu.emory.mathcs.backport.java.util.Collections;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import logic.Cell;

public class GeoComp {

    public static class Point {
        private double x = 0;
        private double y = 0;

        public Point(double x, double y) {
            this.x = x;
            this.y = y;
        }

        public Point(Cell c) {
            this.x = c.getCol();
            this.y = c.getRow();
        }

        public double getX() {
            return x;
        }

        public double getY() {
            return y;
        }

        public Point add(Point other) {
            return new Point(this.getX() + other.getX(), this.getY() + other.getY());
        }

        public Point minus(Point other) {
            return new Point(this.getX() - other.getX(), this.getY() - other.getY());
        }

        public Point mult(double factor) {
            return new Point(this.getX() * factor, this.getY() * factor);
        }

        public Point div(double factor) {
            return new Point(this.getX() / factor, this.getY() / factor);
        }

        public boolean equals(Point other) {
            return this.getX() == other.getX() && this.getY() == other.getY();
        }

        public boolean lt(Point other) {
            if (Math.abs(this.getX() - other.getX()) < 1e-5) return (
                this.getY() < other.getY()
            ); else return this.getX() < other.getX();
        }

        public double dot(Point other) {
            return this.getX() * other.getX() + this.getY() * other.getY();
        }

        @Override
        public String toString() {
            return String.format("(%d,%d)", x, y);
        }

        public Point translate(Point vector) {
            return this.add(vector);
        }

        public Point scale(double factor, Point center) {
            return center.add(this.minus(center).mult(factor));
        }

        public Point rotate0(double angle) {
            return new Point(
                this.getX() * Math.cos(angle) - this.getY() * Math.sin(angle),
                this.getX() * Math.sin(angle) - this.getY() * Math.cos(angle)
            );
        }

        public Point rotate(Point center, double angle) {
            return this.translate(center.mult(-1)).rotate0(angle).translate(center);
        }

        public double norm2() {
            return this.dot(this);
        }

        public double abs() {
            return Math.sqrt(this.norm2());
        }

        public double dist(Point other) {
            return this.minus(other).norm2();
        }

        public Point perp() {
            return new Point(-this.getY(), this.getX());
        }

        boolean isPerp(Point other) {
            return this.dot(other) == 0;
        }

        double angle(Point other) {
            double cosTheta = this.dot(other) / this.abs() / other.abs();
            return Math.acos(Math.max(-1, Math.min(1, cosTheta)));
        }

        double cross(Point other) {
            return this.perp().dot(other);
            // return this.getX() * other.getY() - this.getY() * other.getX();
        }

        /**
         * CUIDADO. Se usa como (3).orient((1),(2))
         */
        double orient(Point a, Point b) {
            return b.minus(a).cross(this.minus(a));
        }

        double orient(Segment s) {
            return s.getEnd().minus(s.getBeginning()).cross(this.minus(s.getBeginning()));
        }

        double orientedAngle(Point b, Point c) {
            double res = b.minus(this).angle(c.minus(this));
            if (c.orient(this, b) < 0) res = 2 * Math.PI - res;
            return res;
        }

        boolean half() {
            return this.getY() > 0 || (this.getY() == 0 && this.getX() < 0);
        }

        public boolean inDisk(Segment s) {
            return s.getBeginning().minus(this).dot(s.getEnd().minus(this)) <= 0;
        }
    }

    public static void polarSort(List<Point> v) {
        v.sort(
            new Comparator<Point>() {

                public int compare(Point p, Point q) { // p - q > 0
                    if (p.half() && !q.half()) return 1; else {
                        if (p.equals(q)) return 0; else {
                            return (p.cross(q) < 0 ? 1 : -1);
                        }
                    }
                }
            }
        );
    }

    public static class Line {
        private Point dir;
        private double offset;

        public Point getDir() {
            return dir;
        }

        public double getOffset() {
            return offset;
        }

        public Line(Point v, double c) {
            this.dir = v;
            this.offset = c;
        }

        public Line(double a, double b, double c) {
            this.dir = new Point(b, -a);
            this.offset = c;
        }

        public Line(Point p, Point q) {
            this.dir = q.minus(p);
            this.offset = this.dir.cross(p);
        }

        public Line(Segment s) {
            this(s.getBeginning(), s.getEnd());
        }

        public double side(Point p) {
            return dir.cross(p) - offset;
        }

        public double dist(Point p) {
            return Math.abs(side(p)) / dir.abs();
        }

        public Line perpThrough(Point p) {
            return new Line(p, p.add(dir.perp()));
        }

        public Line translate(Point t) {
            return new Line(dir, offset + dir.cross(t));
        }

        public Point proj(Point p) {
            return p.minus(dir.perp().mult(side(p)).div(dir.norm2()));
        }

        public Point refl(Point p) {
            return p.minus(dir.perp().mult(2).mult(side(p)).div(dir.norm2()));
        }

        public boolean cmpProj(Point p, Point q) {
            return dir.dot(p) < dir.dot(q);
        }

        public Point inter(Line other) {
            double d = this.getDir().cross(other.getDir());
            if (d == 0) return null;
            return (
                other
                    .getDir()
                    .mult(this.getOffset())
                    .minus(this.getDir().mult(other.getOffset()))
            );
        }
    }

    public static class Segment {
        Point a, b;

        public Segment(Point a, Point b) {
            this.a = a;
            this.b = b;
        }

        public Point getBeginning() {
            return a;
        }

        public Point getEnd() {
            return b;
        }

        // onSegment
        public boolean cointains(Point p) {
            return p.orient(a, b) == 0 && p.inDisk(this);
        }

        public Point lineInter(Segment other) {
            return (new Line(this)).inter(new Line(other));
        }

        public Point properInter(Segment other) {
            double oa = this.getBeginning().orient(other.getBeginning(), other.getEnd());
            double ob = this.getEnd().orient(other.getBeginning(), other.getEnd());
            double oc = other.getBeginning().orient(this.getBeginning(), this.getEnd());
            double od = other.getEnd().orient(this.getBeginning(), this.getEnd());

            Point res = null;
            if (oa * ob < 0 && oc * od < 0) res =
                a.mult(ob).minus(b.mult(oa)).div(ob - oa);
            return res;
        }

        public double dist(Point p) {
            if (!a.equals(b)) {
                Line l = new Line(a, b);
                if (l.cmpProj(a, p) && l.cmpProj(p, b)) return l.dist(p);
            }
            return Math.min(p.minus(a).abs(), p.minus(b).abs());
        }

        public double dist(Segment other) {
            Point inter = this.properInter(other);
            if (inter == null) return 0; else return Math.min(
                Math.min(this.dist(other.getBeginning()), this.dist(other.getEnd())),
                Math.min(other.dist(this.getBeginning()), other.dist(this.getEnd()))
            );
        }

        public boolean counterClockWise(Point r) {
            return r.orient(this) > 0;
        }
    }

    public static double areaTriangle(Point a, Point b, Point c) {
        return Math.abs(b.minus(a).cross(c.minus(a)) / 2.0);
    }

    public static class Poligono {
        List<Point> data = new ArrayList<>();

        void setData(List<Point> _data) {
            data = _data;
        }

        public Poligono(Iterable<Point> it) {
            for (Point i : it) if (i != null) data.add(i);
        }

        public void add(Point p) {
            data.add(p);
        }

        public double area() {
            double area = 0;
            for (int i = 0; i < (data.size() - 1); ++i) area +=
                data.get(i).cross(data.get(i + 1));
            return Math.abs(area) / 2.0;
        }

        public Poligono convexHull() {
            int n = data.size(), k = 0;
            List<Point> H = new ArrayList<>();
            for (int i = 0; i < 2 * n; ++i) H.add(null);
            //new ArrayList<Point>(2*n);
            data.sort(
                new Comparator<Point>() {

                    public int compare(Point p, Point q) {
                        return (p.lt(q) ? 1 : -1);
                    }
                }
            );
            // build lower hull
            for (int i = 0; i < n; ++i) {
                while (k >= 2) {
                    Segment s = new Segment(H.get(k - 2), H.get(k - 1));
                    if (!s.counterClockWise(data.get(i))) --k; else break;
                }

                H.set(k, data.get(i));
                k++;
            }
            // build upper hull
            for (int i = n - 2, t = k + 1; i >= 0; --i) {
                while (k >= t) {
                    Segment s = new Segment(H.get(k - 2), H.get(k - 1));
                    if (!s.counterClockWise(data.get(i))) --k; else break;
                }

                H.set(k, data.get(i));
                k++;
            }
            return new Poligono(H);
        }

        public Poligono cut(Segment s) {
            List<Point> res = new ArrayList<>();
            Point c;
            Point a = s.getBeginning(), b = s.getEnd();
            for (int i = 0; i < data.size() - 1; ++i) {
                double left1 = b.minus(a).cross(data.get(i).minus(a));
                double left2 = b.minus(a).cross(data.get(i + 1).minus(a));

                if (left1 >= 0) res.add(data.get(i));
                if (left1 * left2 < 0) res.add(
                    s.lineInter(new Segment(data.get(i), data.get(i + 1)))
                );
            }

            if (!res.isEmpty()) res.add(res.get(0));

            return new Poligono(res);
        }
    }
}
