package model.canonical;

// Contrat commun a tout evenement canonique (transaction, souscription, futur type).
// C'est grace a cette interface que le dispatcher peut publier n'importe
// quel evenement sans savoir lequel precisement (pas de if/instanceof).
public interface CanonicalEvent {
    String getProvider();
    String getExternalRef();
}
