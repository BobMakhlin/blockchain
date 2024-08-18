package com.myblockchain.ecc;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

class EllipticCurveCryptographyTest {
    @Test
    void testPointAddition() {
        var sut = new EllipticCurveCryptography(BigDecimal.ZERO, BigDecimal.ONE);
        var p = new Point(new BigDecimal("3"), new BigDecimal("7"));
        var q = new Point(new BigDecimal("-1"), new BigDecimal("1"));
        var actual = sut.pointAddition(p, q);
        Assertions.assertEquals(new BigDecimal("0.25"), scale(actual.getX(), 2));
        Assertions.assertEquals(new BigDecimal("-2.875"), scale(actual.getY(), 3));
    }

    @Test
    void testPointDoubling() {
        var sut = new EllipticCurveCryptography(BigDecimal.ZERO, BigDecimal.ONE);
        var p = new Point(new BigDecimal("2"), new BigDecimal("5"));
        var actual = sut.pointAddition(p, p);
        Assertions.assertEquals(new BigDecimal("-2.56"), scale(actual.getX(), 2));
        Assertions.assertEquals(new BigDecimal("0.472"), scale(actual.getY(), 3));
    }

    @Test
    void testDoubleAndAddSmallScalar() {
        var sut = new EllipticCurveCryptography(BigDecimal.ZERO, new BigDecimal("7"));
        var p = new Point(new BigDecimal("3"), new BigDecimal("7"));
        var actual = sut.doubleAndAdd(3, p);
        Assertions.assertEquals(new BigDecimal("-0.1971"), scale(actual.getX(), 4));
        Assertions.assertEquals(new BigDecimal("-4.6895"), scale(actual.getY(), 4));
    }

    @Test
    void testDoubleAndAddLargerScalar() {
        var sut = new EllipticCurveCryptography(BigDecimal.ZERO, new BigDecimal("7"));
        var p = new Point(new BigDecimal("3"), new BigDecimal("7"));
        var actual = sut.doubleAndAdd(3, p);
        Assertions.assertEquals(new BigDecimal("-0.1971"), scale(actual.getX(), 4));
        Assertions.assertEquals(new BigDecimal("-4.6895"), scale(actual.getY(), 4));
    }

    private BigDecimal scale(BigDecimal d, int scale) {
        return d.setScale(scale, RoundingMode.DOWN);
    }
}