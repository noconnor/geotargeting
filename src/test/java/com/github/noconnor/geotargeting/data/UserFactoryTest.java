package com.github.noconnor.geotargeting.data;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.fasterxml.jackson.databind.ObjectMapper;
import junitparams.JUnitParamsRunner;
import junitparams.Parameters;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;

import java.io.IOException;
import java.util.List;
import java.util.Optional;

@RunWith(JUnitParamsRunner.class)
public class UserFactoryTest {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    private UserFactory factory;

    @Before
    public void setup() {
        factory = new UserFactory(new ObjectMapper());
    }

    @Test
    public void whenParsingAValidJsonRecord_thenUserDataFieldsShouldBeSet() {
        String record = "{\"latitude\":\"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}";
        Optional<User> user = factory.parseUser(record);
        assertThat(user.isPresent(), is(true));
        assertThat(user.get().getUserId(), is(12L));
        assertThat(user.get().getLatitude(), is(52.986375d));
        assertThat(user.get().getLongitude(), is(-6.043701d));
        assertThat(user.get().getName(), is("Christina McArdle"));
    }

    @Test
    @Parameters(method = "invalidJson")
    public void whenParsingAJsonRecord_andRequiredParamsAreMissing_thenEmptyRecordShouldBeReturned(String record) {
        Optional<User> user = factory.parseUser(record);
        assertThat(user.isPresent(), is(false));
    }

    @Test
    public void whenParsingAJsonRecord_andExtraFieldsArePresent_thenAdditionalFieldsShouldBeIgnored() {
        String record = "{ \"latitude\":\"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\", \"extra\":\"field\"}";
        Optional<User> user = factory.parseUser(record);
        assertThat(user.isPresent(), is(true));
    }

    @Test
    public void whenParsingAnEmptyFile_thenAnEmptyListOfUsersShouldBeReturned() throws IOException {
        String path = getClass().getClassLoader().getResource("empty.txt").getPath();
        List<User> users = factory.parseUsers(path);
        assertThat(users, is(emptyList()));
    }

    @Test
    public void whenParsingAValidFile_thenAnListOfUsersShouldBeReturned() throws IOException {
        String path = getClass().getClassLoader().getResource("valid_data.txt").getPath();
        List<User> users = factory.parseUsers(path);
        assertThat(users.size(), is(2));
    }

    @Test
    public void whenParsingAnInValidFile_thenAnEmptyListOfUsersShouldBeReturned() throws IOException {
        String path = getClass().getClassLoader().getResource("invalid_data.txt").getPath();
        List<User> users = factory.parseUsers(path);
        assertThat(users, is(emptyList()));
    }

    @Test
    public void whenParsingAFileWithAMixOfValidAndInvalidData_thenAnListOfValidUsersShouldBeReturned() throws IOException {
        String path = getClass().getClassLoader().getResource("mix_valid_invalid.txt").getPath();
        List<User> users = factory.parseUsers(path);
        assertThat(users.size(), is(1));
    }

    @Test
    public void whenParsingAnInvalidFilePath_thenAnExceptionShouldBeThrown() throws IOException {
        expectedException.expect(IOException.class);
        factory.parseUsers("/does/not/exist.txt");
    }


    @SuppressWarnings("unused")
    private Object[] invalidJson() {
        // @formatter:off
        return new Object[]{
            new Object[]{"{                           \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}"},
            new Object[]{"{\"latitude\":\"52.986375\",                 \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\"}"},
            new Object[]{"{\"latitude\":\"52.986375\", \"user_id\": 12,                                 \"longitude\": \"-6.043701\"}"},
            new Object[]{"{\"latitude\":\"52.986375\", \"user_id\": 12, \"name\": \"Christina McArdle\"                             }"},
            new Object[]{"{ \"latitude\":\"tuesday\", \"user_id\": 12, \"name\": \"Christina McArdle\", \"longitude\": \"-6.043701\", \"extra\":\"field\"}"},
            new Object[]{"{}"},
            new Object[]{"{"},
            new Object[]{"{\"field\""}
        };
        // @formatter:on
    }
}
