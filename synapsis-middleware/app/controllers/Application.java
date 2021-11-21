package controllers;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.inject.Inject;

import actors.synapsis.ControllerActor;
import actors.synapsis.MockActor;
import actors.synapsis.RealActor;
import akka.actor.ActorSystem;
import akka.actor.Props;
import akka.stream.Materializer;
import play.libs.streams.ActorFlow;
import play.mvc.Controller;
import play.mvc.Result;
import play.mvc.WebSocket;

/**
 * This controller contains an action to handle HTTP requests
 */
public class Application extends Controller {

   private final SynapsisEventBus synapsisEventBus = new SynapsisEventBus();

   private ActorSystem actorSystem;
   private Materializer materializer;

   /**
    *
    * @param actorSystem
    * @param materializer
    */
   @Inject
   public Application(ActorSystem actorSystem, Materializer materializer) {
      this.logging("Inizializzata applicazione");
      this.actorSystem = actorSystem;
      this.materializer = materializer;

      Props props = Props.create(ControllerActor.class, this.synapsisEventBus);
      this.actorSystem.actorOf(props);
   }

   /**
    * An action that renders an HTML page with a welcome message. The configuration
    * in the <code>routes</code> file means that this method will be called when
    * the application receives a <code>GET</code> request with a path of
    * <code>/</code>.
    */
   public Result index() {
      this.logging("homepage");
      return ok(views.html.index.render());
   }

   /**
    * Metodo invocato ad ogni connessione WebSocket sull'endpoint 'synapsiservice'
    * @param entityType tipologia di entità che ha instaurato la connessione
    * @param entityName nome dell'entità che ha instaurato la connessione
    * @return laconnessione WS instaurata
    */
   public WebSocket synapsisService(String entityType, String entityName) {
      // this.logging("service - parametri: " + entityType + " - " + entityName);
      return WebSocket.Text.accept(request -> {
         return ActorFlow.actorRef((actorRef) -> RealActor.props(actorRef, entityType, entityName, synapsisEventBus),
               actorSystem, materializer);
      });
   }

   /*public WebSocket synapsisServiceWithFilter() {
      return WebSocket.Text.acceptOrResult(request -> CompletableFuture.completedFuture(
                 req.session()
                     .getOptional("type")
                     .map(
                         user ->
                             F.Either.<Result, Flow<String, String, ?>>Right(
                                 ActorFlow.actorRef(
                                     MyWebSocketActor::props, actorSystem, materializer)))
                     .orElseGet(() -> F.Either.Left(forbidden()))));
   }*/

   /**
    * Metodo per istanziare un attore che estende MockActor
    * @param source Classe Java che rappresenta l'attore
    * @param type Tipologia di entità (mind / body)
    * @param name Nome dell'entità
    */
   private void spawnMockActor(Class<? extends MockActor> source, String type, String name) {
      Props props = Props.create(source, type, name, this.synapsisEventBus);
      this.actorSystem.actorOf(props);
   }

   /**
    * Metodo per istanziare N attori che estendono MockActor
    * @param source Classe Java che rappresenta l'attore
    * @param type Tipologia delle entità (mind / body)
    * @param baseName Nome base delle entità -> il nome risultato sara baseName1,...,baseNameN
    * @param number Numero di entità da istanziare
    */
   private void spawnMockActors(Class<? extends MockActor> source, String type, String baseName, int number) {
      for (int i = 1; i <= number; i++) {
         this.spawnMockActor(source, type, baseName + i);
      }
   }

   /**
    * Metodo per stampare a console messaggi generici
    * @param message messaggio
    */
   private void logging(String message) {
      String time = new SimpleDateFormat("HH:mm:ss:SSS").format(new Date()); // 12:08:43:543
      System.out.println(time + " - [Application]: " + message);
   }
}
