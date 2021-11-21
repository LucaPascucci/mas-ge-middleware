package actors.mock;

import java.util.ArrayList;

import actors.synapsis.MockActor;
import akka.actor.Props;
import controllers.SynapsisEventBus;

/**
 * TestMock
 */
public class TestMock extends MockActor {

   /**
    * Costruttore
    * @param type
    * @param name
    * @param eventBus
    */
   private TestMock(final String type, String name, SynapsisEventBus eventBus) {
      super(type, name, eventBus);
   }

   /**
    *
    * @param type
    * @param name
    * @param eventBus
    * @return
    */
   public static Props props(String type, String name, SynapsisEventBus eventBus) {
      return Props.create(TestMock.class, () -> new TestMock(type, name, eventBus));
   }

   /**
    * Metodo invocato per ogni messaggio ricevuto dal MockActor
    * @param content contenuto principale del messaggio
    * @param params parametri del messaggio
    */
   @Override
   public void parseIncomingMessage(String content, ArrayList<Object> params) {

      // this.sendResponse(content, params, 1000);
   }

   /**
    * Metodo invocato alla connessione della controparte
    */
   @Override
   public void counterpartJoined() {
      super.counterpartJoined();
   }

   /**
    * Metodo invocato alla disconnessione della controparte
    */
   @Override
   public void counterpartLeaved() {
      super.counterpartLeaved();
   }
}
