package actors.mock;

import java.util.ArrayList;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;

/**
 * GarbageBodyMock
 */
public class GarbageBodyMock extends MockActor {

   private GarbageBodyMock(String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(GarbageBodyMock.class, () -> new GarbageBodyMock(type, name, eventBus));
   }

   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {
      switch (content) {
      case "garbage_type":
         this.logMessage("TIPOLOGIA SPAZZATURA APPLICATA -> " + params.get(0));
         break;
      case "recycle_me":
         this.logMessage("Mi devo riciclare --> self destruction");
         break;
      }
   }

   @Override
   public void counterpartJoined() {
      super.counterpartJoined();
   }

   @Override
   public void counterpartLeaved() {
      super.counterpartLeaved();
   }
}
