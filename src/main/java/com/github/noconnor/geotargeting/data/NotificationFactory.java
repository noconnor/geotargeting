package com.github.noconnor.geotargeting.data;

import static java.lang.Math.toRadians;
import static java.lang.String.format;
import static java.util.stream.Collectors.toList;

import com.github.noconnor.geotargeting.geo.DistanceCalculator;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Comparator;
import java.util.List;

@RequiredArgsConstructor
public class NotificationFactory {

    private static final Pair<Double, Double> HOTSPOT = ImmutablePair.of(toRadians(53.339428), toRadians(-6.257664));

    private static final String INVITED_MESSAGE = "%s is invited! (%,.0f kilometres away)";
    private static final String NOT_INVITED_MESSAGE = "%s is not invited (%,.0f kilometres away)";

    private final DistanceCalculator distanceCalculator;
    private final long requiredMinimumDistanceMetres;

    public List<Notification> createNotifications(List<User> users) {
        return users.stream()
            .map(u -> {
                double distance = distanceCalculator.calculateDistanceInMetres(
                    HOTSPOT.getLeft(),
                    HOTSPOT.getRight(),
                    toRadians(u.getLatitude()),
                    toRadians(u.getLongitude())
                );
                boolean isInvited = distance <= requiredMinimumDistanceMetres;
                String messageTemplate = isInvited ? INVITED_MESSAGE : NOT_INVITED_MESSAGE;
                String message = format(messageTemplate, u.getName(), (distance / 1_000.0));
                return new Notification(u, isInvited, distance, message);
            })
            .sorted(Comparator.comparing(n -> n.getUser().getUserId()))
            .collect(toList());
    }

}
