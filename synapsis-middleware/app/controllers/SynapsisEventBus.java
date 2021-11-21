package controllers;

import messages.ISynapsisMessage;

import akka.actor.ActorRef;
import akka.event.japi.LookupEventBus;

/**
 * Publishes the payload of the JoinMessage when the topic of the JoinMessage
 * equals the String specified when subscribing.
 */
public class SynapsisEventBus extends LookupEventBus<ISynapsisMessage, ActorRef, String> {

   // is used for extracting the classifier from the incoming events
   @Override
   public String classify(ISynapsisMessage event) {
      return event.getTopic();
   }

   // will be invoked for each event for all subscribers which registered
   // themselves for the event’s classifier
   @Override
   public void publish(ISynapsisMessage event, ActorRef subscriber) {
      subscriber.tell(event, event.getSender());
   }

   // must define a full order over the subscribers, expressed as expected from
   // `java.lang.Comparable.compare`
   @Override
   public int compareSubscribers(ActorRef a, ActorRef b) {
      return a.compareTo(b);
   }

   // determines the initial size of the index data structure used internally (i.e.
   // the expected number of different classifiers)
   @Override
   public int mapSize() {
      return 128;
   }
}
