package kernel.tech.systemgab.utils.enums;

/**
 *
 * @author yeo
 *
 */
public enum StatutTransaction {
    SUCCES("Succes"),
    ECHEC("Echec");
    private final String libelle;
    StatutTransaction(String libelle) {
        this.libelle = libelle;
    }
}