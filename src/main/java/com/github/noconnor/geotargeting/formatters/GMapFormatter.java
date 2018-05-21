package com.github.noconnor.geotargeting.formatters;

import static java.lang.System.getProperty;
import static java.util.Objects.nonNull;

import com.github.noconnor.geotargeting.data.Notification;
import lombok.extern.slf4j.Slf4j;
import org.jtwig.JtwigModel;
import org.jtwig.JtwigTemplate;
import org.jtwig.environment.EnvironmentConfiguration;
import org.jtwig.environment.EnvironmentConfigurationBuilder;
import org.jtwig.json.JsonExtension;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Slf4j
public class GMapFormatter implements OutputFormatter {

    private static final String DEFAULT_REPORT_PATH = getProperty("user.dir") + "/build/map.html";

    @Override
    public void generateOutput(List<Notification> notifications, String outputDir) throws IOException {
        EnvironmentConfiguration configuration = EnvironmentConfigurationBuilder.configuration()
            .extensions().add(JsonExtension.defaultJsonExtension()).and()
            .build();

        Path outputPath = nonNull(outputDir) ? Paths.get(outputDir, "map.html") : Paths.get(DEFAULT_REPORT_PATH);
        JtwigTemplate jtwigTemplate = JtwigTemplate.classpathTemplate("map.twig", configuration);
        JtwigModel model = JtwigModel.newModel().with("data", notifications);
        if (!outputPath.getParent().toFile().exists()) {
            Files.createDirectories(outputPath.getParent());
        }
        log.info("Rendering report to: {}", outputPath.toString());
        jtwigTemplate.render(model, Files.newOutputStream(outputPath));
    }

}
