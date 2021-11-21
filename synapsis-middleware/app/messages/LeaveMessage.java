package messages;

import akka.actor.ActorRef;

public class LeaveMessage implements ISynapsisMessage{ 

   private final String topic;

   private final String content;

   private final ActorRef sender;

   public LeaveMessage(String topic, String content, ActorRef sender){
      this.topic = topic;
      this.content = content;
      this.sender = sender;
   }

   public String getTopic(){
      return this.topic;
   }

   public String getContent(){
      return this.content;
   }

   public ActorRef getSender() {
      return this.sender;
   }
}
