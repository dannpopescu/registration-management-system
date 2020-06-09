import java.util.List;
import java.util.ArrayList;
import java.util.Comparator;

public class GuestsList {

    private final int numberOfPlaces;
    private final List<Guest> guestsList;
    private final List<Guest> waitList;

    public GuestsList(int numberOfPlaces) {
        this.numberOfPlaces = numberOfPlaces;
        this.guestsList = new ArrayList<>();
        this.waitList = new ArrayList<>();
    }

    /**
     * Add a guest to the guests list if there are free places and the guest
     * hasn't registered before, otherwise add him to the waitlist.
     * @param guest with valid first and last names, email and phone number
     * @return an integer with the following possible values:
     *              -1 - the person has been already registered
     *               0 - successfully added the guest to the guests list
     *               X - the person was added to the waitlist with order number X
     *                   OR the guest was already on the waitlist with order number X
     */
    public int add(Guest guest) {
        // already registered
        if (guestsList.contains(guest)) {
            return -1;
        }

        // already on the waitlist
        int index = waitList.indexOf(guest);
        if (index >= 0) {
            return index + 1;
        }

        // add to guests list if there are free spots
        if (guestsList.size() < numberOfPlaces) {
            guestsList.add(guest);
            return 0;
        }

        // add to waitlist by default
        waitList.add(guest);
        return waitList.size();
    }

    /**
     * Return the index of the element that is similar to the key.
     * The similarity is based on the provided comparator.
     * @param list in which to search for the elements similar to the key
     * @param key a similar element to the one we are looking for
     * @param comparator for elements comparison
     * @param <E> TODO complete the description of <E>
     * @return index    of the element that is similar to the key based on the provided comparator
     *            -1    if no similar element has been found
     */
    private static <E> int indexOfSimilar(List<E> list, E key, Comparator<E> comparator) {
        int index = 0;
        for (E element : list) {
            if (comparator.compare(element, key) == 0) {
                return index;
            }
            index++;
        }
        return -1;
    }

    /**
     * Search for a guest similar to the similarGuest in the guests list/waitlist.
     * The similarity is based on the provided comparator.
     * @param similarGuest guest against which the similarity is tested
     * @param comparator Comparator of Guest objects that will determine if two Guests are similar
     * @return an integer with the following meanings:
     *            -1 - no similar guest has been found in the guests list/waitlist
     *             0 - a similar guest is on the guests list
     *             X - a similar guest is on the waitlist with order number X
     */
    private int checkSimilar(Guest similarGuest, Comparator<Guest> comparator) {
        if (indexOfSimilar(guestsList, similarGuest, comparator) >= 0) {
            return 0;
        }

        int indexInWaitList = indexOfSimilar(waitList, similarGuest, comparator);
        if (indexInWaitList >= 0) {
            return indexInWaitList + 1;
        }

        return -1;
    }

    /**
     * Search for a guest in the guests list/waitlist based on the first and last name.
     * The search is case insensitive.
     * @param firstName of the person
     * @param lastName of the person
     * @return an integer with the following meanings:
     *            -1 - the person is not on the guests list nor the waitlist
     *             0 - the person is on the guests list
     *             X - the person is on the waitlist with order number X
     */
    public int checkByName(String firstName, String lastName) {
        return checkSimilar(new Guest(firstName, lastName, "", ""), Guest.BY_NAME_ORDER);
    }

    /**
     * Search for a guest in the guests list/waitlist list based on the email.
     * The search is case insensitive.
     * @param email of the person
     * @return an integer with the following meanings:
     *            -1 - the person is not on the guests list nor the waitlist
     *             0 - the person is on the guests list
     *             X - the person is on the waitlist with order number X
     */
    public int checkByEmail(String email) {
        return checkSimilar(new Guest("", "", email, ""), Guest.BY_EMAIL_ORDER);
    }

    /**
     * Search for a guest in the guests list/waitlist based on the phone number.
     * @param phoneNumber in the format +40733386463
     * @return an integer with the following meanings:
     *            -1 - the person is not on the guests list nor the waitlist
     *             0 - the person is on the guests list
     *             X - the person is on the waitlist with order number X
     */
    public int checkByPhone(String phoneNumber) {
        return checkSimilar(new Guest("", "", "", phoneNumber), Guest.BY_PHONE_ORDER);
    }

    /**
     * Delete a guest similar to the similarGuest in the guests list/waitlist.
     * The similarity is based on the provided comparator.
     * @param similarGuest guest against which the similarity is tested
     * @param comparator Comparator of Guest objects that will determine if two Guests are similar
     * @return -1   if no similar object has been deleted
     *          0   if a similar Guest object has been removed from the waitlist OR from guests
     *              list and the waitlist was empty
     *          1   if a similar Guest object has been removed from the guests list AND the next
     *              guest on the waitlist was transferred to the guests list
     */
    private int removeSimilar(Guest similarGuest, Comparator<Guest> comparator) {
        int index = indexOfSimilar(guestsList, similarGuest, comparator);
        if (index >= 0) {
            guestsList.remove(index);
            if (!waitList.isEmpty()) {
                Guest firstOnWaitList = waitList.remove(0);
                guestsList.add(firstOnWaitList);
                return 1;
            }
            return 0;
        }

        index = indexOfSimilar(waitList, similarGuest, comparator);
        if (index >= 0) {
            waitList.remove(index);
            return 0;
        }

        return -1;
    }

    /**
     * Delete a guest from the guests list/waitlist based on the first and last name.
     * Search is case insensitive.
     * @param firstName of the guest to be deleted
     * @param lastName of the guest to be deleted
     * @return -1   if no similar object has been deleted
     *          0   if a similar Guest object has been removed from the waitlist OR from guests
     *              list and the waitlist was empty
     *          1   if a similar Guest object has been removed from the guests list AND the next
     *              guest on the waitlist was transferred to the guests list
     */
    public int removeByName(String firstName, String lastName) {
        return removeSimilar(new Guest(firstName, lastName, "", ""), Guest.BY_NAME_ORDER);
    }

    /**
     * Delete a guest from the guests list/waitlist based on the email.
     * Search is case insensitive.
     * @param email of the guest to be deleted
     * @return -1   if no similar object has been deleted
     *          0   if a similar Guest object has been removed from the waitlist OR from guests
     *              list and the waitlist was empty
     *          1   if a similar Guest object has been removed from the guests list AND the next
     *              guest on the waitlist was transferred to the guests list
     */
    public int removeByEmail(String email) {
        return removeSimilar(new Guest("", "", email, ""), Guest.BY_EMAIL_ORDER);
    }

    /**
     * Delete a guest from the guests list/waitlist based on the phone number.
     * @param phoneNumber of the guest to be deleted
     * @return -1   if no similar object has been deleted
     *          0   if a similar Guest object has been removed from the waitlist OR from guests
     *              list and the waitlist was empty
     *          1   if a similar Guest object has been removed from the guests list AND the next
     *              guest on the waitlist was transferred to the guests list
     */
    public int removeByPhone(String phoneNumber) {
        return removeSimilar(new Guest("", "", "", phoneNumber), Guest.BY_PHONE_ORDER);
    }

    /**
     * Get a guest similar to the similarGuest in the guests list/waitlist.
     * The similarity is based on the provided comparator.
     * @param similarGuest guest against which the similarity is tested
     * @param comparator Comparator of Guest objects that will determine if two Guests are similar
     * @return Guest    if a similar object has been found in the guests list/waitlist
     *         null     if no similar object has been found
     */
    private Guest getSimilar(Guest similarGuest, Comparator<Guest> comparator) {
        int index = indexOfSimilar(guestsList, similarGuest, comparator);
        if (index >= 0) {
            return guestsList.get(index);
        }

        index = indexOfSimilar(waitList, similarGuest, comparator);
        if (index >= 0) {
            return waitList.get(index);
        }

        return null;
    }

    /**
     * Get a Guest object from the guests/wait list based on the first and last name.
     * Search is case insensitive.
     * @param firstName of the searched guest
     * @param lastName of the searched guest
     * @return Guest    if a Guest with the provided first and last name has been found
     *         null     if no Guest has been found
     */
    public Guest getByName(String firstName, String lastName) {
        return getSimilar(new Guest(firstName, lastName, "", ""), Guest.BY_NAME_ORDER);
    }

    /**
     * Get a Guest object from the guests list/waitlist based on the first and last name.
     * Search is case insensitive.
     * @param email of the searched guest
     * @return Guest    if a Guest with the provided email has been found
     *         null     if no Guest has been found
     */
    public Guest getByEmail(String email) {
        return getSimilar(new Guest("", "", email, ""), Guest.BY_EMAIL_ORDER);
    }

    /**
     * Get a Guest object from the guests list/waitlist based on the first and last name.
     * Search is case insensitive.
     * @param phoneNumber of the searched guest
     * @return Guest    if a Guest with the provided phone number has been found
     *         null     if no Guest has been found
     */
    public Guest getByPhone(String phoneNumber) {
        return getSimilar(new Guest("", "", "", phoneNumber), Guest.BY_PHONE_ORDER);
    }

    /**
     * Search and return the Guests that contain the given string
     * in any of their fields.
     * Note: search is case insensitive
     * @param str a String to be looked up
     * @return a list of Guest if there has been any match, or
     *         an empty list if no match has been found
     */
    public List<Guest> search(String str) {
        str = str.toLowerCase();

        List<Guest> result = new ArrayList<>();

        for (Guest guest : guestsList) {
            if (guest.anyFieldContains(str)) {
                result.add(guest);
            }
        }
        for (Guest guest : waitList) {
            if (guest.anyFieldContains(str)) {
                result.add(guest);
            }
        }

        return result;
    }

    /**
     * Return the number of available spots for the event
     * @return an integer representing available spots. Zero if the event is fully booked.
     */
    public int getNoAvailablePlaces() {
        return numberOfPlaces - guestsList.size();
    }

    /**
     * Return the total number of participants (on the guests list)
     * @return an integer representing the number of participants
     */
    public int getTotalNoParticipants() {
        return guestsList.size();
    }

    /**
     * Return the total number of person on the wait list
     * @return an integer representing the number of person on the wait list
     */
    public int getTotalNoWaiting() {
        return waitList.size();
    }

    /**
     * Return the total number of person who registered for the event: the
     * guests list and wait list combined.
     * @return an integer representing the total number of registrants
     */
    public int getTotalNoRegistered() {
        return guestsList.size() + waitList.size();
    }

    /**
     * Return a copy of the guests list
     * @return a List of Guest who are attending the event
     */
    public List<Guest> getGuestsList() {
        return new ArrayList<>(guestsList);
    }

    /**
     * Return a copy of the wait list
     * @return a List of Guest who are on the wait list for the event
     */
    public List<Guest> getWaitList() {
        return new ArrayList<>(waitList);
    }

}
