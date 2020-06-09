import java.util.Comparator;
import java.util.Objects;

public class Guest {

    private String firstName;
    private String lastName;
    private String email;
    private String phoneNumber;

    public static final Comparator<Guest> BY_NAME_ORDER = new NameComparator();
    public static final Comparator<Guest> BY_EMAIL_ORDER = new EmailComparator();
    public static final Comparator<Guest> BY_PHONE_ORDER = new PhoneComparator();

    public Guest(String firstName, String lastName, String email, String phoneNumber) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public boolean anyFieldContains(String string) {
        return this.firstName.toLowerCase().contains(string)
                || this.lastName.toLowerCase().contains(string)
                || this.email.toLowerCase().contains(string)
                || this.phoneNumber.contains(string);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || this.getClass() != obj.getClass()) {
            return false;
        }

        Guest other = (Guest) obj;
        return Objects.equals(this.phoneNumber, other.phoneNumber)
                && Objects.equals(this.firstName.toLowerCase(), other.firstName.toLowerCase())
                && Objects.equals(this.lastName.toLowerCase(), other.lastName.toLowerCase())
                && Objects.equals(this.email.toLowerCase(), other.email.toLowerCase());
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + Objects.hashCode(this.firstName.toLowerCase());
        result = prime * result + Objects.hashCode(this.lastName.toLowerCase());
        result = prime * result + Objects.hashCode(this.email.toLowerCase());
        result = prime * result + Objects.hashCode(this.phoneNumber);
        return result;
    }

    @Override
    public String toString() {
        return "Nume: " + this.lastName + " " + this.firstName +
                ", Email: " + this.email +
                ", Telefon: " + this.phoneNumber;
    }

    /**
     * Comparator of Guest using the first and last name fields
     */
    private static class NameComparator implements Comparator<Guest> {
        @Override
        public int compare(Guest g1, Guest g2) {
            int result = String.CASE_INSENSITIVE_ORDER.compare(g1.lastName, g2.lastName);
            if (result == 0) {
                result = String.CASE_INSENSITIVE_ORDER.compare(g1.firstName, g2.firstName);
            }
            return result;
        }
    }

    /**
     * Comparator of Guest using the email field
     */
    private static class EmailComparator implements Comparator<Guest> {
        @Override
        public int compare(Guest g1, Guest g2) {
            return String.CASE_INSENSITIVE_ORDER.compare(g1.email, g2.email);
        }
    }

    /**
     * Comparator of Guest using the phoneNumber field
     */
    private static class PhoneComparator implements Comparator<Guest> {
        @Override
        public int compare(Guest g1, Guest g2) {
            return String.CASE_INSENSITIVE_ORDER.compare(g1.phoneNumber, g2.phoneNumber);
        }
    }
}
