package actors.synapsis;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;

import akka.actor.Cancellable;
import akka.actor.PoisonPill;
import controllers.SynapsisEventBus;
import messages.ForwardMessage;
import messages.Message;
import models.ActorType;
import models.Shared;

public abstract class MockActor extends BaseActor {

   private Cancellable selfDestructionOnCounterpartLeaved = null;

   /**
    * Costruttore
    * @param type tipologia di entità collegata all'attore (mind / body)
    * @param name nome dell'entità collegata
    * @param eventBus EventBus per condividere informazioni generiche
    */
   public MockActor(final String type, final String name, final SynapsisEventBus eventBus) {
      super(null, type, name, eventBus);
      this.actorInfo.setActorType(ActorType.MOCK);
   }

   @Override
   void parseForwardMessage(ForwardMessage msg) {
      Message message = Message.buildMessage(msg.getContent());
      this.logMessage(message.toString());
      this.parseIncomingMessage(message.getContent(), message.getParameters());
   }

   @Override
   public void counterpartLeaved() {
      this.selfDestructionOnCounterpartLeaved = this.system.scheduler().scheduleOnce(Duration.ofMillis(6000), getSelf(),
            PoisonPill.getInstance(), this.system.dispatcher(), getSelf());
   }

   @Override
   public void counterpartJoined() {
      if (this.selfDestructionOnCounterpartLeaved != null) {
         this.selfDestructionOnCounterpartLeaved.cancel();
      }
   }

   /**
    * Metodo invocato ad ogni messaggio ricevuto
    * @param content contenuto principale del messaggio
    * @param params parametri del messaggio
    */
   public abstract void parseIncomingMessage(String content, ArrayList<Object> params);

   /**
    * Metodo per inviare un messaggio(risposta) generico alla controparte
    * @param content contenuto principale del messaggio
    * @param params parametri del messaggio
    * @param delay tempo di attesa (ms) prima dell'invio del messaggio
    */
   public void sendResponse(String content, ArrayList<Object> params, long delay) {
      if (delay <= 0) {
         this.sendMessage(this.createMessage(this.actorInfo.getEntityName(), content, params));
      } else {
         this.sendMessageWithDelay(this.createMessage(this.actorInfo.getEntityName(), content, params), delay);
      }
   }

   /**
   * Metodo per inviare un messaggio di auto-distruzione alla controparte
   * @param delay tempo di attesa (ms) prima dell'invio della risposta
   */
   public void selfDestruction(long delay) {
      this.sendResponse(Shared.SELF_DESTRUCTION, new ArrayList<>(), delay);
   }

   /**
   * Metodo per inviare una azione di ricerca alla controparte
   * @param target nome (anche parziale) dell'entità da cercare
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void searchAction(String target, long delay) {
      this.sendResponse(Shared.SEARCH_ACTION, new ArrayList<>(Arrays.asList(target)), delay);
   }

   /**
   * Metodo per inviare una azione di movimento alla controparte
   * @param destination nome dell'entità da raggiungere
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void goToAction(String destination, long delay) {
      this.sendResponse(Shared.GO_TO_ACTION, new ArrayList<>(Arrays.asList(destination)), delay);
   }

   /**
   * Metodo per inviare una azione di stop alla controparte
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void stopAction(long delay) {
      this.sendResponse(Shared.STOP_ACTION, new ArrayList<>(), delay);
   }

   /**
   * Metodo per inviare una azione di prelevamento alla controparte
   * @param target nome dell'entità da prendere
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void pickUpAction(String target, long delay) {
      this.sendResponse(Shared.PICK_UP_ACTION, new ArrayList<>(Arrays.asList(target)), delay);
   }

   /**
   * Metodo per inviare una azione di rilasio alla controparte
   * @param target nome dell'entità da rilasciare
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void releaseAction(String target, long delay) {
      this.sendResponse(Shared.RELEASE_ACTION, new ArrayList<>(Arrays.asList(target)), delay);
   }

   /**
    * Metodo per inviare una percezione di ritrovamento alla controparte
    * @param entityName nome dell'entità trovata
    * @param delay tempo di attesa (ms) prima dell'invio del messaggio
    */
   public void foundPerception(String entityName, long delay) {
      this.sendResponse(Shared.FOUND_PERCEPTION, new ArrayList<>(Arrays.asList(entityName)), delay);
   }

   /**
   * Metodo per inviare una percezione di arrivo alla controparte
   * @param destination nome dell'entità raggiunta
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void arrivedToPerception(String destination, long delay) {
      this.sendResponse(Shared.ARRIVED_TO_PERCEPTION, new ArrayList<>(Arrays.asList(destination)), delay);
   }

   /**
    * Metodo per inviare una percezione di stop alla controparte
    * @param delay tempo di attesa (ms) prima dell'invio del messaggio
    */
   public void stoppedPerception(long delay) {
      this.sendResponse(Shared.STOPPED_PERCEPTION, new ArrayList<>(), delay);
   }

   /**
    * Metodo per inviare una percezione di prelevamento alla controparte
    * @param entityName nome dell'entità prelevata
    * @param delay tempo di attesa (ms) prima dell'invio del messaggio
    */
   public void pickedPerception(String entityName, long delay) {
      this.sendResponse(Shared.PICKED_PERCEPTION, new ArrayList<>(Arrays.asList(entityName)), delay);
   }

   /**
   * Metodo per inviare una percezione di rilascio alla controparte
   * @param entityName nome dell'entità rilasciata
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void releasedPerception(String entityName, long delay) {
      this.sendResponse(Shared.RELEASED_PERCEPTION, new ArrayList<>(Arrays.asList(entityName)), delay);
   }

   /**
   * Metodo per inviare una percezione di contatto alla controparte
   * @param entityName nome dell'entità toccata
   * @param delay tempo di attesa (ms) prima dell'invio del messaggio
   */
   public void touchedPerception(String entityName, long delay) {
      this.sendResponse(Shared.TOUCHED_PERCEPTION, new ArrayList<>(Arrays.asList(entityName)), delay);
   }
}
