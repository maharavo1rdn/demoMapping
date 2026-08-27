package adapter;

import model.canonical.CanonicalEvent;

// Le contrat : transformer un JSON brut Orange Money en evenement canonique.
// Plus de parametre "providerName" ici : ce projet est dedie a Orange Money,
// donc le "qui" n'a plus besoin d'etre precise a chaque appel.
public interface InboundAdapter {
    CanonicalEvent processInbound(String rawJsonPayload);
}
