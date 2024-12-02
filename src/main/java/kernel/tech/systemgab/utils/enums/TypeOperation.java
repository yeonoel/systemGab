package kernel.tech.systemgab.utils.enums;


/**
 *
 * @author yeonoel
 *
 */
public enum TypeOperation {
    RETRAIT("Retrait"),
    DEPOT("Dépôt");
    private final String libelle;
    TypeOperation(String libelle) {
        this.libelle = libelle;
    }
    public String getLibelle() {
        return libelle;
    }


}
