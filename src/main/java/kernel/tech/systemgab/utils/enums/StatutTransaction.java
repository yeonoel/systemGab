package kernel.tech.systemgab.utils.enums;

public enum StatutTransaction {
    SUCCES("Succès"),
    ECHEC("Échec");

    private final String libelle;

    StatutTransaction(String libelle) {
        this.libelle = libelle;
    }

    public String getLibelle() {
        return libelle;
    }
}