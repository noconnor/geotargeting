package com.github.noconnor.geotargeting.data;

import static java.util.Collections.emptyList;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.collection.IsCollectionWithSize.hasSize;
import static org.mockito.Matchers.anyDouble;
import static org.mockito.Mockito.when;

import com.github.noconnor.geotargeting.geo.DistanceCalculator;
import com.google.common.collect.ImmutableList;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

public class NotificationFactoryTest {

    private static final double DISTANCE_LIMIT = 100_000;

    @Mock
    private DistanceCalculator distanceCalculatorMock;

    private NotificationFactory factory;

    public NotificationFactoryTest() {
        MockitoAnnotations.initMocks(this);
    }

    @Before
    public void setup() {
        factory = new NotificationFactory(distanceCalculatorMock, (long) DISTANCE_LIMIT);
    }

    @Test
    public void whenCreatingNotificationsWithAnEmptyUserList_thenAnEmptyNotificationLIstShoudlBeReturned() {
        List<Notification> notifications = factory.createNotifications(emptyList());
        assertThat(notifications, is(emptyList()));
    }

    @Test
    public void whenCreatingNotificationsWithAValidUserList_andAllUsersAreWithinRequiredDistance_thenNotificationsShouldBeCreatedPerUser() {
        mockAllUsersWithinRequiredDistance();
        List<Notification> notifications = factory.createNotifications(userList());
        assertThat(notifications, hasSize(2));
        // Notifications sorted by userId (see userList() for order)
        assertThat(notifications.get(0).isInvited(), is(true));
        assertThat(notifications.get(0).getMessage(), is("bob is invited! (99 kilometres away)"));
        assertThat(notifications.get(1).isInvited(), is(true));
        assertThat(notifications.get(1).getMessage(), is("alice is invited! (99 kilometres away)"));
    }

    @Test
    public void whenCreatingNotificationsWithAValidUserList_andAllUsersExactlyAtRequiredDistance_thenNotificationsShouldBeCreatedPerUser() {
        mockAllUsersAtsRequiredDistance();
        List<Notification> notifications = factory.createNotifications(userList());
        assertThat(notifications, hasSize(2));
        // Notifications sorted by userId (see userList() for order)
        assertThat(notifications.get(0).isInvited(), is(true));
        assertThat(notifications.get(0).getMessage(), is("bob is invited! (100 kilometres away)"));
        assertThat(notifications.get(1).isInvited(), is(true));
        assertThat(notifications.get(1).getMessage(), is("alice is invited! (100 kilometres away)"));
    }

    @Test
    public void whenCreatingNotificationsWithAValidUserList_andAllUsersAreOutsideRequiredDistance_thenNotificationsShouldBeCreatedPerUser() {
        mockAllUsersOutsideRequiredDistance();
        List<Notification> notifications = factory.createNotifications(userList());
        assertThat(notifications, hasSize(2));
        assertThat(notifications.get(0).isInvited(), is(false));
        assertThat(notifications.get(0).getMessage(), is("bob is not invited (101 kilometres away)"));
        assertThat(notifications.get(1).isInvited(), is(false));
        assertThat(notifications.get(1).getMessage(), is("alice is not invited (101 kilometres away)"));
    }

    @Test
    public void whenCreatingNotificationsWithAValidUserList_andSomeUsersAreOutsideRequiredDistance_thenNotificationsShouldBeCreatedPerUser() {
        mockMixUsersInsideAndOutsideRequiredDistance();
        List<Notification> notifications = factory.createNotifications(userList());
        assertThat(notifications, hasSize(2));
        // Notifications sorted by userId (see userList() for order)
        assertThat(notifications.get(0).isInvited(), is(false));
        assertThat(notifications.get(0).getMessage(), is("bob is not invited (101 kilometres away)"));
        assertThat(notifications.get(1).isInvited(), is(true));
        assertThat(notifications.get(1).getMessage(), is("alice is invited! (99 kilometres away)"));
    }

    private void mockAllUsersWithinRequiredDistance() {
        when(distanceCalculatorMock.calculateDistanceInMetres(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(DISTANCE_LIMIT - 1_000);
    }

    private void mockAllUsersAtsRequiredDistance() {
        when(distanceCalculatorMock.calculateDistanceInMetres(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(DISTANCE_LIMIT);
    }

    private void mockAllUsersOutsideRequiredDistance() {
        when(distanceCalculatorMock.calculateDistanceInMetres(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(DISTANCE_LIMIT + 1_000);
    }

    private void mockMixUsersInsideAndOutsideRequiredDistance() {
        when(distanceCalculatorMock.calculateDistanceInMetres(anyDouble(), anyDouble(), anyDouble(), anyDouble()))
            .thenReturn(DISTANCE_LIMIT + 1_000).thenReturn(DISTANCE_LIMIT - 1_000);
    }

    private List<User> userList() {
        return ImmutableList.of(
            new User(1, 2, 33, "bob"),
            new User(-1, 22, 35, "alice")
        );
    }

}
