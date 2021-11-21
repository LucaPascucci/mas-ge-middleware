package messages;

import akka.actor.ActorRef;

public interface ISynapsisMessage {

   public String getTopic();

   public String getContent();

   public ActorRef getSender();
}
