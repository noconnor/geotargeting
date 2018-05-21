package com.github.noconnor.geotargeting.geo;

import static org.hamcrest.Matchers.closeTo;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class VincentyFormulaTest extends BaseDistanceCalculatorTest {

    @Before
    public void setup() {
        calculator = new VincentyFormula();
    }

    @Test
    public void whenCalculatingDistancesBetweenNonAntipodalCoordinates_thenCorrectDistanceShouldBeCalculated() {
        assertThat(calculateDistance(DUBLIN_AIRPORT, NEW_YORK_JFK), closeTo(DUBLIN_TO_JFK_METRES, 0.005));
        assertThat(calculateDistance(DUBLIN_AIRPORT, SYDNEY_AIRPORT), closeTo(DUBLIN_TO_SYDNEY_METRES, 0.005));
    }

    @Test
    public void whenCalculatingDistancesBetweenAntipodalCoordinates_thenCorrectDistanceShouldBeCalculated() {
        assertThat(calculateDistance(CELESTIAL_NORTH_POLE, CELESTIAL_SOUTH_POLE), closeTo(NORTH_TO_SOUTH_POLE_METRES, 0.005));
    }

}
