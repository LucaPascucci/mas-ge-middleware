package messages;

import akka.actor.ActorRef;

public class ForwardMessage implements ISynapsisMessage{

   private static final String TOPIC = "forward";

   private String topic;
   private String content;
   private ActorRef sender;

   public ForwardMessage(final String content){
      this.topic = TOPIC;
      this.content = content;
      this.sender = null;
   }

   public String getTopic(){
      return this.topic;
   }

   public String getContent() {
      return this.content;
   }

   public ActorRef getSender(){
      return this.sender;
   }



}
