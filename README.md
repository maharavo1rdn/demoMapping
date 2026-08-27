# demoMapping — version corrigée

## Comment lancer

```bash
mvn spring-boot:run
```

L'API démarre sur `http://localhost:8080`. C'est un `@RestController` Spring
classique : Orange Money (ou toi, pour tester) appelle cet endpoint en `POST`
avec le JSON en corps de requête.

## Tester avec curl

Une transaction :
```bash
curl -X POST http://localhost:8080/api/orangemoney/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "transaction_id": "OM-TX-998877",
    "amount": "5000",
    "currency": "XOF",
    "sender_phone": "2250700000000",
    "receiver_iban": "CI93CI0080123456789012345678"
  }'
```

Une souscription (notez : format JSON complètement différent, et pourtant
zéro ligne de code Java à changer, juste un fichier YAML différent) :
```bash
curl -X POST http://localhost:8080/api/orangemoney/notifications \
  -H "Content-Type: application/json" \
  -d '{
    "subscription_id": "OM-SUB-445566",
    "customer_msisdn": "2250700000000",
    "bank_account_number": "CI93CI0080123456789012345678"
  }'
```

Rejoue exactement la même requête transaction une deuxième fois : elle sera
rejetée (`IllegalStateException: Doublon détecté`).

## Bugs corrigés par rapport à la version originale du repo

1. `model.canonical.interface` → renommé `model.canonical` (mot réservé Java interdit dans un nom de package)
2. `CanonicalTransaction.java` / `CanonicalSubscription.java` étaient physiquement dans `model/canonical/` mais déclaraient `package model;` → corrigé, imports mis à jour partout
3. Dossier `disptacher` (faute de frappe) → renommé `dispatcher` pour correspondre au `package dispatcher;` déclaré
4. `MappingConfigRegistry` cherchait `classpath:mappings/**/*.yml` (avec un "s") alors que les fichiers sont dans `mapping/orangemoney/` (sans "s") → corrigé, sinon aucun mapping n'était jamais chargé
5. `GenericMobileMoneyAdapter` ne déclarait pas `implements InboundAdapter` → corrigé et renommé
6. `FeeEngine` avait perdu son annotation `@Service` → restaurée
7. Aucun `pom.xml` ne déclarait les dépendances → ajouté

## Adaptations pour un projet dédié à Orange Money

- `GenericMobileMoneyAdapter` renommé `OrangeMoneyInboundAdapter`, paramètre
  `providerName` supprimé partout (inutile avec un seul opérateur).
- `MappingConfigRegistry.getDefinitionsForProvider(provider)` simplifié en
  `getAllDefinitions()`.

## Architecture, en une phrase par couche

- `controller/OrangeMoneyController.java` — reçoit l'appel HTTP d'Orange Money, ne fait que transmettre au dispatcher (controller "fin", pas de logique ici).
- `dispatcher/WebhookEventDispatcher.java` — sauvegarde le payload brut en base, déclenche le mapping, publie l'événement canonique.
- `adapter/OrangeMoneyInboundAdapter.java` — devine le type de message (transaction ou souscription) et délègue à la bonne stratégie.
- `mapping/strategy/*` — transforme le JSON brut en objet canonique, selon les fichiers YAML.
- `mapping/config/*.yml` — le mapping champ-à-champ, modifiable sans recompiler.
- `service/transaction/TransactionProcessingService.java` et `service/subscription/SubscriptionProcessingService.java` — écoutent le bus d'événements Spring et appliquent la logique métier (frais, idempotence, envoi au CBS).
- `adapter/CbsAdapter.java` — traduit vers le format du CBS.

## Ajout : persistance des payloads bruts (pattern Inbox)

`model/RawInboundMessage.java` + `repository/RawPayloadRepository.java`,
branchés dans `WebhookEventDispatcher.dispatch(...)` :
- chaque payload reçu est enregistré **avant** toute transformation (statut `PENDING`)
- passe à `PROCESSED` si tout s'est bien passé, ou `FAILED` (avec la raison) sinon — mais reste toujours conservé, rejouable plus tard.

Actuellement un simple `ConcurrentHashMap` en mémoire — à remplacer par un
vrai `@Repository` Spring Data JPA le jour où une base réelle est branchée ;
aucune autre classe n'aurait à changer.

## Limite importante

Ce code a été relu et corrigé ligne par ligne, mais n'a **pas pu être
compilé** dans mon environnement (pas d'accès réseau à Maven Central ici).
Lance `mvn clean compile` chez toi pour confirmer — si une erreur apparaît,
montre-la-moi et je corrige.
