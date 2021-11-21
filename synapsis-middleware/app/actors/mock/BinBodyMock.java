package actors.mock;

import java.util.ArrayList;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;

/**
 * BinBodyMock
 */
public class BinBodyMock extends MockActor {

   private BinBodyMock(String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(BinBodyMock.class, () -> new BinBodyMock(type, name, eventBus));
   }

   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {
      switch (content) {
      case "bin_type":
         this.logMessage("TIPOLOGIA BIDONE APPLICATA -> " + params.get(0));
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
