package com.github.noconnor.geotargeting.data;

import static java.util.stream.Collectors.toList;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
public class UserFactory {

    private final ObjectMapper mapper;

    public List<User> parseUsers(String filePath) throws IOException {
        return Files.readAllLines(Paths.get(filePath))
            .stream()
            .map(this::parseUser)
            .filter(Optional::isPresent)
            .map(Optional::get)
            .sorted(Comparator.comparing(User::getUserId)) // move to the presentation layer
            .collect(toList());
    }

    public Optional<User> parseUser(String userData) {
        try {
            return Optional.ofNullable(mapper.readValue(userData, User.class));
        } catch (IOException e) {
            log.error("Unexpected parse error for record {}", userData, e);
            return Optional.empty();
        }
    }
}
