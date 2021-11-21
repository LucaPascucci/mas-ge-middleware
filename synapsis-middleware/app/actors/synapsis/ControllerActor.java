package actors.synapsis;

import java.text.SimpleDateFormat;
import java.util.Date;

import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.actor.UntypedAbstractActor;
import controllers.SynapsisEventBus;
import models.Shared;

/**
 * ControllerActor che gestirà la GUI e altre cose...
 */
public class ControllerActor extends UntypedAbstractActor{

   private final ActorSystem system = this.getContext().getSystem();
   private final SynapsisEventBus eventBus;

   public ControllerActor(SynapsisEventBus eventBus) {
      this.eventBus = eventBus;
   }

   public static Props props(SynapsisEventBus eventBus) {
      return Props.create(ControllerActor.class, () -> new ControllerActor(eventBus));
   }

   @Override
   public void preStart() {
      this.logMessage("preStart -> PATH: " + getSelf().path().toString());
      //TODO controllare se deve registrarsi ad altri eventi
      this.eventBus.subscribe(getSelf(), Shared.JOIN_EVENT_KEY);
      this.eventBus.subscribe(getSelf(), Shared.LEAVE_EVENT_KEY);
   }

   @Override
   public void onReceive(Object message) throws Throwable {}

   // Metodo del lifecycle di un attore.
   @Override
   public void postStop() throws Exception {
      // Unsubscribe dell'attore da tutti i topic a cui si è iscritto
      this.eventBus.unsubscribe(getSelf());
      this.logMessage("postStop");
   }

   public void logMessage(String message) {
      String time = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()); // 12:08:43:543
      System.out.println(time + " - [ControllerActor]: " + message);
   }

}
