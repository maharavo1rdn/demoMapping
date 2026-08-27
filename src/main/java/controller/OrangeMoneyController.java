package controller;

import dispatcher.WebhookEventDispatcher;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// Controller Spring tout ce qu'il y a de plus classique. Orange Money fait
// un POST ici (avec le JSON de la transaction ou de la souscription dans
// le corps de la requete), exactement comme n'importe quel client HTTP
// appellerait n'importe quelle API Spring.
//
// Ce controller ne contient AUCUNE logique metier : il recoit juste la
// requete et la transmet telle quelle au dispatcher. C'est le dispatcher
// (et tout ce qu'il y a derriere : mapping, idempotence, CBS...) qui fait
// le vrai travail. Un controller fin, qui delegue, est la bonne pratique.
@RestController
@RequestMapping("/api/orangemoney")
public class OrangeMoneyController {

    private final WebhookEventDispatcher dispatcher;

    public OrangeMoneyController(WebhookEventDispatcher dispatcher) {
        this.dispatcher = dispatcher;
    }

    // Orange Money appelle : POST /api/orangemoney/notifications
    // avec le JSON brut (transaction ou souscription) dans le corps.
    @PostMapping("/notifications")
    public ResponseEntity<String> receiveNotification(@RequestBody String rawJsonPayload) {
        dispatcher.dispatch(rawJsonPayload);
        return ResponseEntity.ok("Notification recue et traitee.");
    }
}
