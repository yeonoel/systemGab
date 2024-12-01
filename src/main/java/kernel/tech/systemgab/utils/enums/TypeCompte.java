package kernel.tech.systemgab.utils.enums;

public enum TypeCompte {
    COURANT("Compte Courant"),
    EPARGNE("Compte Épargne");

    private final String libelle;

    TypeCompte(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static boolean isValidLibelle(String libelle) {
        for (TypeCompte type : values()) {
            if (type.getLibelle().equalsIgnoreCase(libelle)) {
                return true;
            }
        }
        return false;
    }

}
