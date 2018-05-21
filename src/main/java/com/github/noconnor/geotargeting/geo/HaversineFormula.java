package com.github.noconnor.geotargeting.geo;

import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class HaversineFormula implements DistanceCalculator {

    @Override
    public double calculateDistanceInMetres(double refLatRads, double refLongRads, double latRads, double longRads) {
        // https://en.wikipedia.org/wiki/Great-circle_distance
        double deltaLatRads = abs(refLatRads - latRads);
        double deltaLongRads = abs(refLongRads - longRads);

        double term1 = pow(sin(deltaLatRads / 2), 2);
        double term2 = cos(refLatRads) * cos(latRads) * pow(sin(deltaLongRads / 2), 2);

        return GeoConstants.EARTH_RADIUS_METRES * 2 * asin(sqrt(term1 + term2));
    }
}
