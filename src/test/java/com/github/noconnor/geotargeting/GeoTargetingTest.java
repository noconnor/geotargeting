package com.github.noconnor.geotargeting;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class GeoTargetingTest {

    @Rule
    public ExpectedException exception = ExpectedException.none();

    @Test
    public void whenBuildingAnAssessment_andAlgorithmIsUnknown_thenExceptionShouldBeThrown() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("ERROR: Unknown algorithm:  [test]");
        new GeoTargeting( "test", "console");
    }

    @Test
    public void whenBuildingAnAssessment_andFormatIsUnknown_thenExceptionShouldBeThrown() {
        exception.expect(IllegalStateException.class);
        exception.expectMessage("ERROR: Unknown format:  [text]");
        new GeoTargeting("vincenty", "text" );
    }
}
