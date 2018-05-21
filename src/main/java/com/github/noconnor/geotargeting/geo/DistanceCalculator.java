package com.github.noconnor.geotargeting.geo;

public interface DistanceCalculator {

    /**
     * Calculates the distance in metres between two sets of coordinates
     *
     * @param refLatRads - starting point latitude in radians
     * @param refLongRads - starting point longitude in radians
     * @param latRads - target latitude in radians
     * @param longRads - target longitude in radians
     * @return distance between the two coordinate sets in metres
     */
    double calculateDistanceInMetres(double refLatRads, double refLongRads, double latRads, double longRads);
}
