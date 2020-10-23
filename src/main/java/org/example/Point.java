package org.example;

import static java.lang.Math.random;

public class Point {
    protected double x;
    protected double y;

    public Point() {
        this.x = random() * 2 - 1;
        this.y = random() * 2 - 1;
    }

    public Point(int x, int y) {
        this.x = x;
        this.y = y;
    }

    @Override
    public String toString() {
        return "Point{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }

    public static void main(String[] args) {
        Point point = new Point();
        System.out.println(point.toString());
    }
}
