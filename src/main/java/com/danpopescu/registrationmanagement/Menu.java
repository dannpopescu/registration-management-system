package com.danpopescu.registrationmanagement;

/**
 * com.danpopescu.registrationmanagement.Main menu options
 */
public enum Menu {
    HELP("Afiseaza aceasta lista de comenzi"),
    ADD("Adauga o noua persoana (inscriere)"),
    CHECK("Verifica daca o persoana este inscrisa la eveniment"),
    REMOVE("Sterge o persoana existenta din lista"),
    UPDATE("Actualizeaza detaliile unei persoane"),
    GUESTS("Lista de persoane care participa la eveniment"),
    WAITLIST("Persoanele din lista de asteptare"),
    AVAILABLE("Numarul de locuri libere"),
    GUESTS_NO("Numarul de persoane care participa la eveniment"),
    WAITLIST_NO("Numarul de persoane din lista de asteptare"),
    SUBSCRIBE_NO("Numarul total de persoane inscrise"),
    SEARCH("Cauta toti invitatii conform sirului de caractere introdus"),
    RESET("Sterge complet toate datele stocate"),
    QUIT("Inchide aplicatia");

    private final String description;

    Menu(String description) {
        this.description = description;
    }

    public static void printMenu() {
        for (Menu option : Menu.values()) {
            System.out.printf("%-12s - " + option.description + "\n", option.name().toLowerCase());
        }
    }
}
