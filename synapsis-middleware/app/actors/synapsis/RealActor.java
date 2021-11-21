package actors.synapsis;

import java.util.ArrayList;

import akka.actor.ActorRef;
import akka.actor.Props;
import controllers.SynapsisEventBus;
import messages.ForwardMessage;
import messages.Message;
import models.ActorType;
import models.Shared;

public class RealActor extends BaseActor {

   private RealActor(ActorRef webSocketSession, String type, String name, SynapsisEventBus eventBus) {
      super(webSocketSession, type, name, eventBus);
      this.actorInfo.setActorType(ActorType.REAL);
   }

   /* metodo utilizzato da Actor System per istanziare l'attore */
   /**
    * Metodo utilizzato da Actor System per istanziare l'attore
    * @param webSocketSession
    * @param type
    * @param name
    * @param eventBus
    * @return
    */
   public static Props props(ActorRef webSocketSession, String type, String name, SynapsisEventBus eventBus) {
      return Props.create(RealActor.class, () -> new RealActor(webSocketSession, type, name, eventBus));
   }

   @Override
   void parseForwardMessage(ForwardMessage msg) {
      Message message = Message.buildMessage(msg.getContent());
      this.logMessage(message.toString());
      this.sendMessageThroughWebSocket(message);
   }

   @Override
   public void counterpartJoined() {
      this.sendMessageThroughWebSocket(
            this.createMessage(Shared.SYNAPSIS_MIDDLEWARE, Shared.COUNTERPART_READY, new ArrayList<>()));
   }

   @Override
   public void counterpartLeaved() {
      this.sendMessageThroughWebSocket(
            this.createMessage(Shared.SYNAPSIS_MIDDLEWARE, Shared.COUNTERPART_UNREADY, new ArrayList<>()));
   }
}
