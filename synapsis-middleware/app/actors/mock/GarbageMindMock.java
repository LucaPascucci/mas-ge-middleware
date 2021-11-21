package actors.mock;

import java.util.ArrayList;
import java.util.Arrays;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;

/**
 * GarbageMindMock
 */
public class GarbageMindMock extends MockActor {

   private GarbageMindMock(String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(GarbageMindMock.class, () -> new GarbageMindMock(type, name, eventBus));
   }

   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {
   }

   @Override
   public void counterpartJoined() {
      super.counterpartJoined();
      this.sendResponse("garbage_type", new ArrayList<>(Arrays.asList("plastic")), 0);
   }

   @Override
   public void counterpartLeaved() {
      super.counterpartLeaved();
   }
}
