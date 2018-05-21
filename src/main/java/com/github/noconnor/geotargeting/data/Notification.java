package com.github.noconnor.geotargeting.data;

import lombok.Value;

@Value
public class Notification {
    User user;
    boolean isInvited;
    double distance;
    String message;
}
