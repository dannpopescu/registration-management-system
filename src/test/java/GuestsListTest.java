import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class GuestsListTest {

    GuestsList list;
    Guest guest1;
    Guest guest2;
    Guest guest3;
    Guest guest4;

    @BeforeEach
    void beforeEach() {
        list = new GuestsList(2);
        guest1 = new Guest("Vali", "Ionescu", "vali@gmail.com", "+40775534075");
        guest2 = new Guest("Alex", "Poiana", "alex@gmail.com", "+40721234567");
        guest3 = new Guest("Diana", "Croitoru", "diana@gmail.com", "+40751234567");
        guest4 = new Guest("Stefan", "Dumitru", "stefan@gmail.com", "+40771234567");
    }

    @Nested
    class Add {

        @Test
        void returnZero_WhenSuccessfullyAdded() {
            int response = list.add(guest1);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(0, response,
                            "add() should return int {0} if the guest was successfully added to the guests list."),
                    () -> assertTrue(list.getGuestsList().contains(guest1),
                            "If the return value of add() is {0}, the guest should be on the guests list.")
            );
        }

        @Test
        void returnMinusOne_WhenAlreadyAdded() {
            list.add(guest1);
            int response = list.add(guest1);
            Assertions.assertEquals(-1, response,
                    "add() should return int {-1} if the added guest is already on the guests list.");
        }

        @Test
        void returnOrderInWaitList_WhenAddedInWaitList() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);
            int response = list.add(guest4);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(2, response,
                            "add() should return the place on the wait list when there are no free spots"),
                    () -> assertTrue(list.getWaitList().contains(guest4),
                            "The guest should be added on the wait list when there are no free spots."),
                    () -> assertFalse(list.getGuestsList().contains(guest4),
                            "The guest should not be added on the guests list when there are no free spots.")
            );
        }

        @Test
        void returnOrderInWaitList_WhenAlreadyInWaitList() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);
            list.add(guest4);
            int response = list.add(guest3);
            Assertions.assertAll(
                    () -> Assertions.assertEquals(1, response,
                            "add() should return the place on the wait list when adding a new guest that was " +
                                    "already on the wait list."),
                    () -> assertTrue(list.getWaitList().contains(guest3),
                            "The guest should be added on the wait list when there are no free spots."),
                    () -> assertFalse(list.getGuestsList().contains(guest3),
                            "The guest should not be added on the guests list when there are no free spots.")
            );

        }
    }

    @Nested
    class Check {

        @Test
        void returnZero_WhenInGuestsList() {
            list.add(guest1);
            list.add(guest2);

            int responseByName = list.checkByName("Vali", "Ionescu");
            int responseByEmail = list.checkByEmail("alex@gmail.com");
            int responseByPhone = list.checkByPhone("+40775534075");

            Assertions.assertAll(
                    () -> Assertions.assertEquals(0, responseByName,
                            "searchByName() should return zero if a guest with the provided first and last names" +
                                    "is on the guests list"),
                    () -> Assertions.assertEquals(0, responseByEmail,
                            "searchByEmail() should return zero if a guest with the provided email is on the guests list"),
                    () -> Assertions.assertEquals(0, responseByPhone,
                            "searchByPhone() should return zero if a guest with the provided phone number is on" +
                                    "the guests list")
            );
        }

        @Test
        void returnOrderInWaitList_WhenInWaitList() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);

            int responseByName = list.checkByName("diana", "croitoru");
            int responseByEmail = list.checkByEmail("diana@gmail.com");
            int responseByPhone = list.checkByPhone("+40751234567");

            Assertions.assertAll(
                    () -> Assertions.assertEquals(1, responseByName,
                            "searchByName() should return place on the wait list if a person with the provided " +
                                    "first and last names is on the wait list"),
                    () -> Assertions.assertEquals(1, responseByEmail,
                            "searchByEmail() should return place on the wait list if a person with the provided " +
                                    "email is on the wait list"),
                    () -> Assertions.assertEquals(1, responseByPhone,
                            "searchByPhone() should return place on the wait list if a person with the provided " +
                                    "phone number is on the wait list")
            );
        }

        @Test
        void returnMinusOne_WhenNotFound() {
            list.add(guest1);

            int responseByName = list.checkByName("Alex", "Poiana");
            int responseByEmail = list.checkByEmail("alex@gmail.com");
            int responseByPhone = list.checkByPhone("+40721234567");

            Assertions.assertAll(
                    () -> Assertions.assertEquals(-1, responseByName,
                            "searchByName() should return -1 if a person with the provided " +
                                    "first and last names is not on the guests list or the wait list"),
                    () -> Assertions.assertEquals(-1, responseByEmail,
                            "searchByEmail() should return -1 if a person with the provided " +
                                    "email is not on the guests list or the wait list"),
                    () -> Assertions.assertEquals(-1, responseByPhone,
                            "searchByPhone() should return -1 if a person with the provided " +
                                    "phone number is not on the guests list or the wait list")
            );
        }
    }

    @Nested
    class Remove {
        @Test
        void returnZero_WhenRemovedFromGuestsList() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);

            int responseByName = list.removeByName("Vali", "Ionescu");
            Assertions.assertAll(
                    () -> assertFalse(list.getGuestsList().contains(guest1),
                            "Guest with first and last names that match the ones provided as arguments should be " +
                                    "deleted from the guests list."),
                    () -> Assertions.assertEquals(0, responseByName,
                            "Method removeByName() should return zero if it deleted a guest from the guests list.")
            );

            int responseByEmail = list.removeByEmail("alex@gmail.com");
            Assertions.assertAll(
                    () -> assertFalse(list.getGuestsList().contains(guest2),
                            "Guest with email that match the one provided as argument should be " +
                                    "deleted from the guests list."),
                    () -> Assertions.assertEquals(0, responseByEmail,
                            "Method removeByEmail() should return true if it deleted a guest from the guests list.")
            );

            int responseByPhone = list.removeByPhone("+40751234567");
            Assertions.assertAll(
                    () -> assertFalse(list.getGuestsList().contains(guest2),
                            "Guest with phone number that match the one provided as argument should be " +
                                    "deleted from the guests list."),
                    () -> Assertions.assertEquals(0, responseByPhone,
                            "Method removeByPhone() should return true if it deleted a guest from the guests list.")
            );
        }

        @Test
        void returnZero_WhenRemovedFromWaitList() {
            list.add(guest1);
            list.add(guest2);

            list.add(guest3);
            int responseByName = list.removeByName("diana", "croitoru");
            Assertions.assertAll(
                    () -> assertFalse(list.getWaitList().contains(guest1),
                            "Guest with first and last names that match the ones provided as arguments should be " +
                                    "deleted from the wait list."),
                    () -> Assertions.assertEquals(0, responseByName,
                            "Method removeByName() should return true if it deleted a guest from the wait list.")
            );

            list.add(guest3);
            int responseByEmail = list.removeByEmail("diana@gmail.com");
            Assertions.assertAll(
                    () -> assertFalse(list.getWaitList().contains(guest3),
                            "Guest with email that match the one provided as argument should be " +
                                    "deleted from the wait list."),
                    () -> Assertions.assertEquals(0, responseByEmail,
                            "Method removeByEmail() should return true if it deleted a guest from the wait list.")
            );

            list.add(guest3);
            int responseByPhone = list.removeByPhone("+40751234567");
            Assertions.assertAll(
                    () -> assertFalse(list.getWaitList().contains(guest3),
                            "Guest with phone number that match the one provided as argument should be " +
                                    "deleted from the wait list."),
                    () -> Assertions.assertEquals(0, responseByPhone,
                            "Method removeByPhone() should return true if it deleted a guest from the wait list.")
            );
        }

        @Test
        void returnMinusOne_WhenNotFound() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);

            int responseByName = list.removeByName("Alex", "Ionescu");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(-1, responseByName,
                            "Method removeByName() should not remove a guest if the name provided doesn't match " +
                                    "with the name of a guest"),
                    () -> assertEquals(2, list.getTotalNoParticipants(),
                            "Guest list size should remain the same if no guest was removed from the list"),
                    () -> assertEquals(1, list.getTotalNoWaiting(),
                            "Wait list size should remain the same if no guest was removed from the list")
            );

            int responseByEmail = list.removeByEmail("vali.ionescu@gmail.com");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(-1, responseByEmail,
                            "Method removeByEmail() should not remove a guest if the email provided doesn't match " +
                                    "with the email of a guest"),
                    () -> assertEquals(2, list.getTotalNoParticipants(),
                            "Guest list size should remain the same if no guest was removed from the list"),
                    () -> assertEquals(1, list.getTotalNoWaiting(),
                            "Wait list size should remain the same if no guest was removed from the list")
            );

            int responseByPhone = list.removeByPhone("+40771234567");
            Assertions.assertAll(
                    () -> Assertions.assertEquals(-1, responseByPhone,
                            "Method removeByPhone() should not remove a guest if the email provided doesn't match " +
                                    "with the phone number of a guest"),
                    () -> assertEquals(2, list.getTotalNoParticipants(),
                            "Guest list size should remain the same if no guest was removed from the list"),
                    () -> assertEquals(1, list.getTotalNoWaiting(),
                            "Wait list size should remain the same if no guest was removed from the list")
            );
        }

        @Test
        void moveFirstOnWaitListToGuestList_WhenRemovedFromGuestsList() {
            list.add(guest1);
            list.add(guest2);
            list.add(guest3);

            list.removeByName("Vali", "Ionescu");
            Assertions.assertAll(
                    () -> assertTrue(list.getGuestsList().contains(guest3),
                            "First guest on the waitlist should be moved on the guests list if one guest has " +
                                    "been removed from the guests list."),
                    () -> assertEquals(0, list.getTotalNoWaiting(),
                            "The guest that is moved from the waitlist to the guests list should not figure " +
                                    "on the waitlist anymore.")
            );
        }
    }

    @Nested
    class Get {
        @Test
        void get_ShouldReturnTheObject_IfTheArgumentsMatchItsFields() {
            GuestsList list = new GuestsList(1);
            Guest guest1 = new Guest("Vali", "Ionescu", "vali@gmail.com", "+40775534075");
            Guest guest2 = new Guest("Alex", "Poiana", "alex@gmail.com", "+40721234567");

            list.add(guest1);
            list.add(guest2);

            Optional<Guest> getByNameReturn = list.getByName("alex", "poiana");
            assertTrue(getByNameReturn.isPresent(),
                    "Method getByName() should return the guest if the provided name matches the guest's name");

            Optional<Guest> getByEmailReturn = list.getByEmail("alex@gmail.com");
            assertTrue(getByEmailReturn.isPresent(),
                    "Method getByEmail() should return the guest if the provided email matches the guest's email");


            Optional<Guest> getByPhoneReturn = list.getByPhone("+40775534075");
            assertTrue(getByPhoneReturn.isPresent(),
                    "Method getByPhone() should return the guest if the provided phone number matches " +
                            "the guest's phone number");
        }

        @Test
        void get_ShouldReturnEmptyOptional_IfTheArgumentsDontMatchFieldsOfAnyObject() {
            GuestsList list = new GuestsList(1);
            Guest guest1 = new Guest("Vali", "Ionescu", "vali@gmail.com", "+40775534075");
            Guest guest2 = new Guest("Alex", "Poiana", "alex@gmail.com", "+40721234567");

            list.add(guest1);
            list.add(guest2);

            Optional<Guest> getByNameReturn = list.getByName("alex", "ionescu");
            assertTrue(getByNameReturn.isEmpty(),
                    "Method getByName() should return empty optional if the provided name doesn't match the name of any guest");

            Optional<Guest> getByEmailReturn = list.getByEmail("diana@gmail.com");
            assertTrue(getByEmailReturn.isEmpty(),
                    "Method getByEmail() should return empty optional if the provided email doesn't match the email of any guest");


            Optional<Guest> getByPhoneReturn = list.getByPhone("+40772314575");
            assertTrue(getByPhoneReturn.isEmpty(),
                    "Method getByPhone() should return empty optional if the provided phone number doesn't match " +
                            "the phone number of any guest");
        }
    }

    @Nested
    class Search {
        @Test
        void search_ShouldReturnListOfGuests_IfSearchIsSuccessful() {
            GuestsList list = new GuestsList(1);
            Guest guest1 = new Guest("Vali", "Ionescu", "vali@gmail.com", "+40775534075");
            Guest guest2 = new Guest("Alex", "Poiana", "alex@gmail.com", "+40721234567");
            Guest guest3 = new Guest("Diana", "Croitoru", "diana@gmail.com", "+40751234567");
            Guest guest4 = new Guest("Stefan", "Dumitru", "stefan@gmail.com", "+40771234567");

            list.add(guest1);
            list.add(guest2);
            list.add(guest3);
            list.add(guest4);

            Assertions.assertAll(
                    () -> assertEquals(2, list.search("al").size()),
                    () -> assertEquals(2, list.search("ru").size()),
                    () -> assertEquals(4, list.search("@gmail.com").size()),
                    () -> assertEquals(4, list.search("+40").size())
            );
        }

        @Test
        void search_ShouldReturnEmptyList_IfSearchIsUnsuccessful() {
            GuestsList list = new GuestsList(1);
            Guest guest1 = new Guest("Vali", "Ionescu", "vali@gmail.com", "+40775534075");
            Guest guest2 = new Guest("Alex", "Poiana", "alex@gmail.com", "+40721234567");
            Guest guest3 = new Guest("Diana", "Croitoru", "diana@gmail.com", "+40751234567");
            Guest guest4 = new Guest("Stefan", "Dumitru", "stefan@gmail.com", "+40771234567");

            list.add(guest1);
            list.add(guest2);
            list.add(guest3);
            list.add(guest4);

            Assertions.assertAll(
                    () -> assertEquals(0, list.search("valentin").size()),
                    () -> assertEquals(0, list.search("popescu").size()),
                    () -> assertEquals(0, list.search("@yahoo.com").size()),
                    () -> assertEquals(0, list.search("+373").size())
            );
        }
    }
}