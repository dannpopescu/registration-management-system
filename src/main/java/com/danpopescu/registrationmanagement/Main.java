package com.danpopescu.registrationmanagement;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.Set;

public class Main {

    private static GuestsList guestsList;

    private static Scanner scanner;

    private static Path path = Path.of("projects/registrationmanagement/src/main/resources/guestsList.dat");

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        scanner = new Scanner(System.in);

        if (Files.exists(path)) {
            restore();
        } else {
            int numberOfPlaces = Integer.parseInt(ask("Introduceti numarul de locuri disponibile:"));
            guestsList = new GuestsList(numberOfPlaces);
        }

        Menu menuCommand = Menu.HELP;
        Menu.printMenu();
        while (menuCommand != Menu.QUIT) {
            String action = ask("\nIntroduceti comanda: (help - Afiseaza lista de comenzi)");
            try {
                menuCommand = Menu.valueOf(action.toUpperCase());
            } catch (IllegalArgumentException e) {
                System.out.println("Comanda invalida. Incercati din nou.");
                continue;
            }

            switch (menuCommand) {
                case HELP -> Menu.printMenu();
                case ADD -> add();
                case CHECK -> check();
                case REMOVE -> remove();
                case UPDATE -> update();
                case GUESTS -> guests();
                case WAITLIST -> waitlist();
                case AVAILABLE -> available();
                case GUESTS_NO -> guest_no();
                case WAITLIST_NO -> waitlist_no();
                case SUBSCRIBE_NO -> subscribe_no();
                case SEARCH -> search();
                case RESET -> reset();
                case QUIT -> System.out.println("O zi frumoasa!");
            }
        }
    }

    public static void persist() throws IOException {
        try (ObjectOutputStream out = new ObjectOutputStream(new BufferedOutputStream(Files.newOutputStream(path)))) {
            out.writeObject(guestsList);
        }
    }

    public static void restore() throws IOException, ClassNotFoundException {
        try (ObjectInputStream in = new ObjectInputStream(new BufferedInputStream(Files.newInputStream(path)))) {
            guestsList = (GuestsList) in.readObject();
        }
    }

    private static void reset() throws IOException {
        if (Files.exists(path)) {
            Files.delete(path);
        }
        guestsList = new GuestsList(guestsList.getNumberOfPlaces());
        persist();
        System.out.println("List a fost resetata cu succes.");
    }

    /**
     * Register a new person. If the event is not booked up, the person is added
     * to the guests list, otherwise to the wait list.
     */
    private static void add() throws IOException {
        System.out.println("Pentru a adauga o noua persoana introduceti datele de mai jos:");
        String lastName = askLastName();
        String firstName = askFirstName();
        String email = askEmail();
        String phone = askPhone();

        int response = guestsList.add(new Guest(firstName, lastName, email, phone));
        switch (response) {
            case 0:
                System.out.println("Locul la eveniment este confirmat.");
                persist();
                break;
            case -1:
                System.out.println("Persoana este deja inscrisa pe lista de participanti.");
                break;
            default:
                System.out.println("Persoana a fost inscrisa cu succes pe lista de asteptare si a primit numarul de " +
                    "ordine " + response + ". Aceasta va fi mutata automat pe lista de participanti da un loc devine disponibil.");
                persist();
                break;
        }
    }

    /**
     * Ask the user how he would like to search for a person in the database:
     * by name, email or phone, and return the appropriate com.danpopescu.registrationmanagement.SearchMode.
     * @return one of the following SearchModes:
     *            BY_NAME
     *            BY_EMAIL
     *            BY_PHONE
     */
    private static SearchMode askSearchMode() {
        char mode = ask("Alegeti modul de cautare:" +
                "\n\t\"1\" - Nume si prenume" +
                "\n\t\"2\" - Email" +
                "\n\t\"3\" - Numar de telefon (format \"+40733386463\")").charAt(0);

        Set<Character> validInputs = Set.of('1', '2', '3');
        while (!validInputs.contains(mode)) {
            mode = ask("Input invalid. Incercati din nou.").charAt(0);
        }

        return SearchMode.values()[Character.getNumericValue(mode) - 1];
    }

    /**
     * Check if a person is registered for the event (either on the guests list
     * or on the waitlist) and print the corresponding message.
     */
    private static void check() {
        SearchMode mode = askSearchMode();
        int responseCode = -1;
        switch (mode) {
            case BY_NAME -> {
                String lastName = askLastName();
                String firstName = askFirstName();
                responseCode = guestsList.checkByName(firstName, lastName);
            }
            case BY_EMAIL -> {
                String email = askEmail();
                responseCode = guestsList.checkByEmail(email);
            }
            case BY_PHONE -> {
                String phone = askPhone();
                responseCode = guestsList.checkByPhone(phone);
            }
        }
        Notification.showCheckMessage(responseCode);
    }

    /**
     * Remove a registered person from the database or print an error message if the
     * requested guest has not been found.
     */
    private static void remove() throws IOException {
        SearchMode mode = askSearchMode();
        int responseCode = -1;
        switch (mode) {
            case BY_NAME -> {
                String lastName = askLastName();
                String firstName = askFirstName();
                responseCode = guestsList.removeByName(firstName, lastName);
            }
            case BY_EMAIL -> {
                String email = askEmail();
                responseCode = guestsList.removeByEmail(email);
            }
            case BY_PHONE -> {
                String phone = askPhone();
                responseCode = guestsList.removeByPhone(phone);
            }
        }
        switch (responseCode) {
            case -1:
                System.out.println("Eroare: Persoana nu este inscrisa la eveniment.");
                break;
            case 0:
            case 1:
                System.out.println("Stergerea persoanei s-a realizat cu succes.");
                List<Guest> guests = guestsList.getGuestsList();
                Guest transferredGuest = guests.get(guests.size() - 1);
                System.out.println(transferredGuest.getLastName() + " " + transferredGuest.getFirstName() +
                        "a fost transferat pe lista de participanti.");
                persist();
                break;
        }
    }

    /**
     * Update the personal details of a guest or print an error message if the
     * requested guest has not been found in the database.
     */
    private static void update() throws IOException {
        SearchMode mode = askSearchMode();
        Optional<Guest> guestOptional = Optional.empty();
        switch (mode) {
            case BY_NAME -> {
                String lastName = askLastName();
                String firstName = askFirstName();
                guestOptional = guestsList.getByName(firstName, lastName);
            }
            case BY_EMAIL -> {
                String email = askEmail();
                guestOptional = guestsList.getByEmail(email);
            }
            case BY_PHONE -> {
                String phone = askPhone();
                guestOptional = guestsList.getByPhone(phone);
            }
        }

        if (guestOptional.isEmpty()) {
            System.out.println("Eroare: Persoana nu este inregistrata.");
            return;
        }

        char field = ask("Alegeti campul de actualizat, tastand:" +
                "\n\t\"1\" - Nume" +
                "\n\t\"2\" - Prenume" +
                "\n\t\"3\" - Email" +
                "\n\t\"4\" - Numar de telefon (format „+40733386463“)").charAt(0);

        Set<Character> validInputs = Set.of('1', '2', '3', '4');
        while (!validInputs.contains(field)) {
            field = ask("Input invalid. Incercati din nou:").charAt(0);
        }

        switch (field) {
            case '1' -> {
                String lastName = askLastName();
                guestOptional.get().setLastName(lastName);
            }
            case '2' -> {
                String firstName = askFirstName();
                guestOptional.get().setFirstName(firstName);
            }
            case '3' -> {
                String email = askEmail();
                guestOptional.get().setEmail(email);
            }
            case '4' -> {
                String phone = askPhone();
                guestOptional.get().setPhoneNumber(phone);
            }
        }
        persist();
        System.out.println("Campul a fost actualizat cu succes.");
    }

    /**
     * Search and print the guests that contain the search key in any one of their fields
     */
    private static void search() {
        String searchKey = ask("Introduceti sirul de caractere cautat:");
        List<Guest> result = guestsList.search(searchKey);
        if (result.isEmpty()) {
            System.out.println("Nici o persoana nu corespunde sirului introdus.");
        } else {
            printList(result);
        }
    }

    /**
     * Print the persons that are on the guests list
     */
    private static void guests() {
        List<Guest> list = Main.guestsList.getGuestsList();
        if (list.isEmpty()) {
            System.out.println("Lista de participanti este goala...");
        } else {
            printList(list);
        }
    }

    /**
     * Print the persons that are on the waitlist
     */
    private static void waitlist() {
        List<Guest> list = Main.guestsList.getWaitList();
        if (list.isEmpty()) {
            System.out.println("Lista de asteptare este goala...");
        } else {
            printList(list);
        }
    }

    /**
     * Print the elements of the list
     * @param list a List of elements to be printed
     */
    private static <E> void printList(List<E> list) {
        int index = 1;
        for (E element : list) {
            System.out.println(index + ". " + element);
            index++;
        }
    }

    /**
     * Print the number of available places
     */
    private static void available() {
        System.out.println("Numarul de locuri ramase: " + guestsList.getNoAvailablePlaces());
    }

    /**
     * Print the number of guests
     */
    private static void guest_no() {
        System.out.println("Numarul de participanti: " + guestsList.getTotalNoParticipants());
    }

    /**
     * Print the number of persons on the wait list
     */
    private static void waitlist_no() {
        System.out.println("Numarul de participanti pe lista de asteptare: " + guestsList.getTotalNoWaiting());
    }

    /**
     * Print the total number of registered persons
     */
    private static void subscribe_no() {
        System.out.println("Numarul total de persoane: " + guestsList.getTotalNoRegistered());
    }

    /**
     * Get the input from the user in the form:
     * Question:
     * >>> [...]
     * @param question to ask the user
     * @return a String representing the user's input
     */
    private static String ask(String question) {
        System.out.println(question);
        System.out.print(">>> ");
        return scanner.nextLine();
    }

    /**
     * Prompt the user to enter his first name
     * @return user's first name
     */
    private static String askFirstName() {
        return ask("Prenume:");
    }

    /**
     * Prompt the user to enter his last name
     * @return user's last name
     */
    private static String askLastName() {
        return ask("Nume:");
    }

    /**
     * Prompt the user to enter his email
     * @return user's email
     */
    private static String askEmail() {
        return ask("Email:");
    }

    /**
     * Prompt the user to enter his phone number
     * @return user's first phone number
     */
    private static String askPhone() {
        return ask("Telefon (format „+40733386463“):");
    }

}
