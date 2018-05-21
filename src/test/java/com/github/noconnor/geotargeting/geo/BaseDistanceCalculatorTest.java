package com.github.noconnor.geotargeting.geo;

import static java.lang.Math.toRadians;

import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.Ignore;

@Ignore
public abstract class BaseDistanceCalculatorTest {

    static final Pair<Double, Double> CELESTIAL_NORTH_POLE = ImmutablePair.of(toRadians(90.0), toRadians(0.0));
    static final Pair<Double, Double> CELESTIAL_SOUTH_POLE = ImmutablePair.of(toRadians(-90.0), toRadians(0.0));
    static final Pair<Double, Double> DUBLIN_AIRPORT = ImmutablePair.of(toRadians(53.4264), toRadians(-6.2499));
    static final Pair<Double, Double> NEW_YORK_JFK = ImmutablePair.of(toRadians(40.6413), toRadians(-73.7781));
    static final Pair<Double, Double> SYDNEY_AIRPORT = ImmutablePair.of(toRadians(-33.9399), toRadians(151.1753));

    // https://gps-coordinates.org/distance-between-coordinates.php
    static final double DUBLIN_TO_JFK_METRES = 5104046.37;
    static final double DUBLIN_TO_SYDNEY_METRES = 17213224.09;
    static final double NORTH_TO_SOUTH_POLE_METRES = 20015086.80;

    protected DistanceCalculator calculator;

    protected double calculateDistance(Pair<Double, Double> src, Pair<Double, Double> dst) {
        return calculator.calculateDistanceInMetres(src.getLeft(), src.getRight(), dst.getLeft(), dst.getRight());
    }

}
