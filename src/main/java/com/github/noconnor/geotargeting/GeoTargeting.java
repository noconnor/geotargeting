package com.github.noconnor.geotargeting;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Preconditions.checkState;
import static com.google.common.collect.Maps.newHashMap;
import static java.util.Objects.nonNull;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.noconnor.geotargeting.data.Notification;
import com.github.noconnor.geotargeting.data.NotificationFactory;
import com.github.noconnor.geotargeting.data.User;
import com.github.noconnor.geotargeting.data.UserFactory;
import com.github.noconnor.geotargeting.formatters.ConsoleFormatter;
import com.github.noconnor.geotargeting.formatters.GMapFormatter;
import com.github.noconnor.geotargeting.formatters.OutputFormatter;
import com.github.noconnor.geotargeting.geo.DistanceCalculator;
import com.github.noconnor.geotargeting.geo.HaversineFormula;
import com.github.noconnor.geotargeting.geo.VincentyFormula;
import org.apache.commons.cli.BasicParser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public class GeoTargeting {

    private static final Map<String, OutputFormatter> FORMATTERS = newHashMap();
    private static final Map<String, DistanceCalculator> CALCULATORS = newHashMap();

    static {
        FORMATTERS.put("gmaps", new GMapFormatter());
        FORMATTERS.put("console", new ConsoleFormatter());

        CALCULATORS.put("haversine", new HaversineFormula());
        CALCULATORS.put("vincenty", new VincentyFormula());
    }

    private final NotificationFactory notificationFactory;
    private final OutputFormatter formatter;
    private final UserFactory userFactory;

    GeoTargeting(String algorithm, String format) {
        DistanceCalculator calculator = CALCULATORS.get(algorithm);
        this.formatter = FORMATTERS.get(format);
        this.userFactory = new UserFactory(new ObjectMapper());
        checkState(nonNull(calculator), "ERROR: Unknown algorithm: ", algorithm);
        checkState(nonNull(formatter), "ERROR: Unknown format: ", format);
        this.notificationFactory = new NotificationFactory(calculator, 100_000);
    }


    private void execute(String inputFile, String outputDir) throws IOException {

        List<User> users = userFactory.parseUsers(inputFile);
        List<Notification> notifications = notificationFactory.createNotifications(users);

        formatter.generateOutput(notifications, outputDir);
    }

    public static void main(String[] args) throws Exception {

        Options options = new Options();
        // @formatter:off
        options.addOption("f",true,"Desired output format (gmaps or console - DEFAULT: console)");
        options.addOption("a",true,"Desired distance calculation algorithm (haversine or vincenty - DEFAULT: vincenty)");
        options.addOption("i",true,"Json user data input file (i.e. customers.txt)");
        options.addOption("o",true,"Desired output directory (DEFAULT: build dir)");
        options.addOption("h",false,"Print usage information");
        // @formatter:on

        HelpFormatter help = new HelpFormatter();

        try {

            CommandLineParser parser = new BasicParser();
            CommandLine line = parser.parse(options, args);

            if (line.hasOption("h")) {
                help.printHelp("java -jar geotargeting-1.0-SNAPSHOT.jar", options);
                return;
            }

            checkArgument(line.hasOption("i"), "ERROR: Input file is required!");

            GeoTargeting geoTargeting = new GeoTargeting(
              line.getOptionValue("a", "vincenty"),
              line.getOptionValue("f", "console")
            );
            geoTargeting.execute(line.getOptionValue("i"), line.getOptionValue("o"));
        } catch (Exception e) {
            help.printHelp("java -jar geotargeting-1.0-SNAPSHOT.jar", options);
            throw e;
        }
    }


}
