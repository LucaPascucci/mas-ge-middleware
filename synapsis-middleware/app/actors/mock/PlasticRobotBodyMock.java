package actors.mock;

import java.util.ArrayList;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;
import models.Shared;

/**
 * PlasticRobotBodyMock
 */
public class PlasticRobotBodyMock extends MockActor {

   private PlasticRobotBodyMock(String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(PlasticRobotBodyMock.class, () -> new PlasticRobotBodyMock(type, name, eventBus));
   }

   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {
      switch (content) {
      case "robot_type":
         this.logMessage("TIPOLOGIA ROBOT APPLICATA -> " + params.get(0));
         break;
      case Shared.SEARCH_ACTION:
         switch ((String) params.get(0)) {
         case "garbage":
            this.foundPerception("plastic_garbage1", 1000);
            break;
         case "bin":
            this.foundPerception("plastic_bin1", 1000);
            break;
         }
         break;
      case Shared.PICK_UP_ACTION:
         this.pickedPerception((String) params.get(0), 1000);
         break;
      case Shared.GO_TO_ACTION:
         this.arrivedToPerception((String) params.get(0), 4000);
         break;
      case Shared.STOP_ACTION:
         this.stoppedPerception(0);
         break;
      case Shared.RELEASE_ACTION:
         this.releasedPerception((String) params.get(0), 1000);
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
