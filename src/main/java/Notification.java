/**
 * Class used to print responses for the user after making ADD, CHECK
 * and REMOVE operations on the GuestsList
 */
public class Notification {

    public static void showCreateMessage(int code) {
        switch (code) {
            case 0 -> System.out.println("Locul la eveniment este confirmat.");
            case -1 -> System.out.println("Persoana este deja inscrisa pe lista de participanti.");
            default -> System.out.println("Persoana a fost inscrisa cu succes pe lista de asteptare si a primit numarul de " +
                    "ordine " + code + ". Aceasta va fi mutata automat pe lista de participanti da un loc devine disponibil.");
        }
    }

    public static void showCheckMessage(int code) {
        switch (code) {
            case 0 -> System.out.println("Persoana se afla pe lista de participanti");
            case -1 -> System.out.println("Persoana nu se afla nici pe lista de participanti, " +
                    "nici pe lista de asteptare");
            default -> System.out.println("Persoana se afla pe lista de asteptare cu numarul de ordine " + code);
        }
    }

    public static void showRemoveMessage(int code) {
        switch (code) {
            case -1 -> System.out.println("Eroare: Persoana nu este inscrisa la eveniment.");
            case 0, 1 -> System.out.println("Stergerea persoanei s-a realizat cu succes.");
        }

    }

}
