package com.github.noconnor.geotargeting.formatters;

import com.github.noconnor.geotargeting.data.Notification;

import java.io.IOException;
import java.util.List;

public interface OutputFormatter {

    void generateOutput(List<Notification> notifications, String outputDir) throws IOException;

}
