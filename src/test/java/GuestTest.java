import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class GuestTest {

    /**
     * Equal fields check
     */
    @Test
    void equalTrueTest1() {
        Guest guest1 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        assertEquals(guest1, guest2);
        assertEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash codes should be the same for equal objects");
    }

    /**
     * Check different case for first and last name
     */
    @Test
    void equalTrueTest2() {
        Guest guest1 = new Guest("VALentin", "IoNescu",
                "vali@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        assertEquals(guest1, guest2);
        assertEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash codes should be the same for equal objects");
    }

    /**
     * Check different first name
     */
    @Test
    void equalFalseTest1() {
        Guest guest1 = new Guest("Dan", "Ionescu",
                "vali@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        assertNotEquals(guest1, guest2);
        assertNotEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash codes should be the different for not equal objects");
    }

    /**
     * Check different last name
     */
    @Test
    void equalFalseTest2() {
        Guest guest1 = new Guest("Valentin", "Marus",
                "vali@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        assertNotEquals(guest1, guest2);
        assertNotEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash code should be the different for not equal objects");

    }

    /**
     * Check different email
     */
    @Test
    void equalFalseTest3() {
        Guest guest1 = new Guest("Valentin", "Ionescu",
                "vali_ionescu@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        assertNotEquals(guest1, guest2);
        assertNotEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash code should be the different for not equal objects");
    }

    /**
     * Check different phone number
     */
    @Test
    void equalFalseTest4() {
        Guest guest1 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40775534074");
        Guest guest2 = new Guest("Valentin", "Ionescu",
                "vali@gmail.con", "+40725521064");
        assertNotEquals(guest1, guest2);
        assertNotEquals(guest1.hashCode(), guest2.hashCode(),
                "Hash code should be the different for not equal objects");
    }
}