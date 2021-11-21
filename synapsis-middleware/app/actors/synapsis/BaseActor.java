package actors.synapsis;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;

import akka.actor.ActorRef;
import akka.actor.ActorSystem;
import akka.actor.PoisonPill;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import controllers.SynapsisEventBus;
import messages.ForwardMessage;
import messages.JoinMessage;
import messages.LeaveMessage;
import messages.Message;
import models.ActorInfo;
import models.ActorType;
import models.ConnectionStatus;
import models.Shared;

// In futuro si potrebbe estendere solo a AbstractActor (però cambia il metodo OnReceive --> ReceiveBuilder)
public abstract class BaseActor extends UntypedAbstractActor {

   final ActorInfo actorInfo;

   protected final ActorSystem system = this.getContext().getSystem();
   private final SynapsisEventBus eventBus;

   private final ActorRef webSocketSession;
   private ActorRef counterpartActor = null;
   private LinkedList<Message> messagesToSendToCouterpart = new LinkedList<>();

   public BaseActor(ActorRef webSocketSession, String type, String name, SynapsisEventBus eventBus) {
      this.actorInfo = new ActorInfo(type, name);
      this.webSocketSession = webSocketSession;
      this.eventBus = eventBus;
   }

   @Override
   public void preStart() {
      this.logMessage("preStart -> PATH: " + getSelf().path().toString());
      this.eventBus.subscribe(getSelf(), Shared.JOIN_EVENT_KEY);
      this.eventBus.subscribe(getSelf(), Shared.LEAVE_EVENT_KEY);
      this.eventBus.publish(new JoinMessage(Shared.JOIN_EVENT_KEY, this.actorInfo.toString(), getSelf()));
   }

   @Override
   public void onReceive(Object msg) throws Exception {
      long currentMills = System.currentTimeMillis();
      if (msg instanceof String) { // Messaggio ricevuto dalla WebSocket
         Message message = Message.buildMessage(msg.toString());
         message.addTimeStat(currentMills);

         switch (message.getReceiver()) {
         case Shared.SYNAPSIS_MIDDLEWARE:
            if (Shared.CREATE_MOCK.equals(message.getContent())) {
               /**
                * Parametro del messaggio: Classe Java da utilizzare per istanziare il Mock Actor
                */
               String className = (String) message.getParameters().get(0);

               // istanzia il MockActor
               this.createMyMockActor(className);

            } else if (Shared.DELETE_MOCK.equals(message.getContent())) {
               this.logMessage("DELETE MOCK -->" + message.toString());
               // cancella
               this.deleteMyMockActor();
            }
            break;

         default:
            this.sendMessage(message); // Invia il messaggio alla controparte
            break;
         }

      } else if (msg instanceof JoinMessage) {
         JoinMessage message = (JoinMessage) msg;
         if (this.actorInfo.checkCounterpartInfo(ActorInfo.buildInfo(message.getContent())) && this.counterpartActor == null) {
            this.actorInfo.setCounterpartStatus(ConnectionStatus.CONNECTED);
            this.counterpartActor = message.getSender();
            this.rifleAllWaitingMessages(); // Invio tutti i messaggi per la controparte in attesa
            this.eventBus.publish(new JoinMessage(Shared.JOIN_EVENT_KEY, this.actorInfo.toString(), getSelf()));
            this.counterpartJoined();
         }

      } else if (msg instanceof LeaveMessage) {
         LeaveMessage message = (LeaveMessage) msg;
         if (this.actorInfo.checkCounterpartInfo(ActorInfo.buildInfo(message.getContent())) && this.counterpartActor != null) {
            this.actorInfo.setCounterpartStatus(ConnectionStatus.DISCONNECTED);
            this.counterpartActor = null;
            this.counterpartLeaved();
         }

      } else if (msg instanceof ForwardMessage) { // ricevuto messaggio dalla controparte
         ForwardMessage message = (ForwardMessage) msg;
         this.parseForwardMessage(message);
      } else {
         // errore messaggio non gestito
         this.logMessage("onReceive - Messaggio non gestito!!");
         unhandled(msg);
      }
   }

   // Metodo del lifecycle di un attore. Viene invocato in caso di disconnessione della websocket
   @Override
   public void postStop() throws Exception {
      this.actorInfo.setCounterpartStatus(ConnectionStatus.DISCONNECTED);
      this.eventBus.publish(new LeaveMessage(Shared.LEAVE_EVENT_KEY, this.actorInfo.toString(), getSelf()));

      // Unsubscribe dell'attore da tutti i topic a cui si è iscritto
      this.eventBus.unsubscribe(getSelf());
      this.logMessage("postStop");
   }

   public abstract void counterpartJoined();

   public abstract void counterpartLeaved();

   abstract void parseForwardMessage(ForwardMessage msg);

   void sendMessage(Message message) {
      if (ConnectionStatus.CONNECTED.equals(this.actorInfo.getCounterpartStatus())) {
         this.counterpartActor.tell(new ForwardMessage(message.toString()), getSelf());
      } else {
         this.messagesToSendToCouterpart.addLast(message);
      }
   }

   void sendMessageWithDelay(Message message, long mills) {
      this.system.scheduler().scheduleOnce(Duration.ofMillis(mills), new Runnable() {
         @Override
         public void run() {
            sendMessage(message);
         }
      }, system.dispatcher());
   }

   void sendMessageThroughWebSocket(Message message) {
      if (this.actorInfo.getActorType().equals(ActorType.REAL)) {
         message.addTimeStat(System.currentTimeMillis()); //aggiungo la statistica per il tempo di computazione del messaggio
         this.actorInfo.addNewMessage(message); // NOTE funzione per aggiornare le statistiche di invio/ricezione messaggi dell'attore
         this.webSocketSession.tell(message.toString(), getSelf());
      }
   }

   Message createMessage(String receiver, String content, ArrayList<Object> params) {
      Message message = new Message(this.actorInfo.getEntityName(), receiver, content, params);
      message.addTimeStat(System.currentTimeMillis());
      message.addTimeStat(System.currentTimeMillis());
      return message;
   }

   private void rifleAllWaitingMessages() {
      for (int i = 0; i < this.messagesToSendToCouterpart.size(); i++) {
         this.sendMessage(this.messagesToSendToCouterpart.removeFirst());
      }
   }

   private void createMyMockActor(String className) {
      try {
         Class classSource = Class.forName(Shared.MOCK_ACTORS_PACKAGE + className);
         Props props = null;
         switch (this.actorInfo.getEntityType()) {
         case "body":
            props = Props.create(classSource, "mind", this.actorInfo.getEntityName(), this.eventBus);
            break;
         case "mind":
            props = Props.create(classSource, "body", this.actorInfo.getEntityName(), this.eventBus);
            break;
         }
         if (props != null) {
            this.system.actorOf(props);
         }
      } catch (Exception e) {
         this.logMessage("Errore durante la creazione di runtime mock actor --> " + e.getLocalizedMessage());
      }
   }

   private void deleteMyMockActor() {
      if (this.counterpartActor != null) {
         this.counterpartActor.tell(PoisonPill.getInstance(), this.getSelf());
      } else {
         this.logMessage("MOCK non presente in synapsis");
      }
   }

   public void logMessage(String message) {
      String time = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()); // 12:08:43:543
      System.out.println(time + " - [" + this.actorInfo.getActorType().toString() + "_Actor - " + this.actorInfo.getEntityType() + " - " + this.actorInfo.getEntityName() + "]: " + message);
   }
}
