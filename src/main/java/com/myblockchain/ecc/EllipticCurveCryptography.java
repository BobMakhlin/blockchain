package com.myblockchain.ecc;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class EllipticCurveCryptography {
    // y^2 = x^3 + ax + b
    // Bitcoin a = 0, b = 7.
    private final BigDecimal a;
    private final BigDecimal b;
    private static final BigDecimal TWO = new BigDecimal(2);
    private static final BigDecimal THREE = new BigDecimal(3);

    public EllipticCurveCryptography(BigDecimal a, BigDecimal b) {
        this.a = a;
        this.b = b;
    }

    public Point pointAddition(Point p, Point q) {
        var x1 = p.getX();
        var y1 = p.getY();
        var x2 = q.getX();
        var y2 = q.getY();
        BigDecimal m; // slope.
        var mc = new MathContext(20, RoundingMode.DOWN);

        if (isPointDoubling(p, q)) {
            //  (3 * x1 * x1 + a) / (2 * y1);
            m = (THREE.multiply(x1.pow(2), mc).add(a, mc)).divide(TWO.multiply(y1, mc), mc);
        } else {
            //  (y2 - y1) / (x2 - x1);
            m = (y2.subtract(y1)).divide(x2.subtract(x1), mc);
        }
        //  m * m - x2 - x1
        var x3 = m.pow(2, mc).subtract(x2, mc).subtract(x1, mc);
        //  m * (x1 - x3) - y1
        var y3 = m.multiply(x1.subtract(x3, mc), mc).subtract(y1, mc);

        return new Point(x3, y3);
    }

    // R(x1,y1) = n * P(x2,y2), n = ?
    // DLP = discrete logarithm problem
    // brute-forcing
    public int solveDLP(Point r, Point p) {
        var n = -1;
        Point temp;
        do {
            temp = doubleAndAdd(++n, p);
        } while(!temp.equals(r));

        return n;
    }

    // O(n) linear time
    public Point doubleAndAdd(int n, Point p) {
        var temp = new Point(p.getX(), p.getY());
        var nBinary = Integer.toBinaryString(n);
        for (var i = 1; i < nBinary.length(); i++) {
            var bit = Integer.parseInt("" + nBinary.charAt(i));
            if (bit == 1) {
                // double and add.
                temp = pointAddition(temp, temp);
                temp = pointAddition(temp, p);
            } else {
                // just double.
                temp = pointAddition(temp, temp);
            }
        }
        return temp;
    }

    private boolean isPointDoubling(Point p, Point q) {
        return p.getX().equals(q.getX()) && p.getY().equals(q.getY());
    }
}
