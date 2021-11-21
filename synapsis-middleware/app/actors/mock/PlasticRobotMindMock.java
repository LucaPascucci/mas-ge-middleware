package actors.mock;

import java.util.ArrayList;
import java.util.Arrays;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;
import models.Shared;

/**
 * PlasticRobotMindMock
 */
public class PlasticRobotMindMock extends MockActor {

   private String garbageName = "";
   private String binName = "";

   private PlasticRobotMindMock(String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(PlasticRobotMindMock.class, () -> new PlasticRobotMindMock(type, name, eventBus));
   }

   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {
      switch (content) {

      case Shared.FOUND_PERCEPTION:
         this.goToAction((String) params.get(0), 1000);
         break;

      case Shared.PICKED_PERCEPTION:
         this.searchAction("bin", 3000);
         break;

      case Shared.ARRIVED_TO_PERCEPTION:
         if (this.garbageName.equals("")) {
            this.garbageName = (String) params.get(0);
            this.pickUpAction(this.garbageName, 2000);
         } else {
            this.releaseAction(this.garbageName, 1000);
            this.garbageName = "";
         }
         break;
      }

   }

   @Override
   public void counterpartJoined() {
      super.counterpartJoined();
      this.sendResponse("robot_type", new ArrayList<>(Arrays.asList("plastic")), 0);
      this.searchAction("garbage", 3000);
   }

   @Override
   public void counterpartLeaved() {
      super.counterpartLeaved();
   }
}
