package kernel.tech.systemgab.utils.enums;

public enum TypeOperation {
    RETRAIT("Retrait"),
    DEPOT("Dépôt"),
    CONSULTATION("Consultation");

    private final String libelle;

    TypeOperation(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }

    public static TypeOperation fromLibelle(String libelle) {
        for (TypeOperation type : values()) {
            if (type.getLibelle().equalsIgnoreCase(libelle)) {
                return type;
            }
        }
        throw new IllegalArgumentException("Type d'opération invalide : " + libelle);
    }

}
