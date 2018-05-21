package com.github.noconnor.geotargeting.formatters;

import static java.lang.String.format;

import com.github.noconnor.geotargeting.data.Notification;
import lombok.extern.slf4j.Slf4j;

import java.util.Comparator;
import java.util.List;

@Slf4j
public class ConsoleFormatter implements OutputFormatter {

    @Override
    public void generateOutput(List<Notification> notifications, String outputDir) {
        notifications.stream()
            .filter(Notification::isInvited)
            .sorted(Comparator.comparing(n -> n.getUser().getUserId()))
            .forEach(n -> {
                String message = format("Name: %s, id:%s, distance:%,.0fkm", n.getUser().getName(),
                                        n.getUser().getUserId(),
                                        n.getDistance() / 1_000);
                log.info(message);
            });
    }
}
