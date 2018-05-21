package com.github.noconnor.geotargeting.geo;

import static java.lang.Math.abs;
import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.pow;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;

public class VincentyFormula implements DistanceCalculator {

    @Override
    public double calculateDistanceInMetres(double refLatRads, double refLongRads, double latRads, double longRads) {
        // https://en.wikipedia.org/wiki/Great-circle_distance
        double deltaLongRads = abs(refLongRads - longRads);

        double term1 = pow(cos(latRads) * sin(deltaLongRads), 2);
        double term2 = pow(cos(refLatRads) * sin(latRads) - sin(refLatRads) * cos(latRads) * cos(deltaLongRads), 2);
        double term3 = sin(refLatRads) * sin(latRads) + cos(refLatRads) * cos(latRads) * cos(deltaLongRads);

        return GeoConstants.EARTH_RADIUS_METRES * atan2(sqrt((term1 + term2)), term3);
    }

}
