package messages;

import java.util.ArrayList;
import java.util.LinkedList;

import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

public class Message {

   @SerializedName("Sender")
   private String sender;

   @SerializedName("Receiver")
   private String receiver;

   @SerializedName("Content")
   private String content;

   @SerializedName("Parameters")
   private ArrayList<Object> parameters;

   @SerializedName("TimeStats")
   private LinkedList<Long> timeStats;

   private static Gson GSON = new Gson();

   public Message(final String sender, final String receiver, final String content) {
      this.setSender(sender);
      this.setReceiver(receiver);
      this.setContent(content);
      this.setParameters(new ArrayList<>());
      this.setTimeStats(new LinkedList<>());
   }

   public Message(final String sender, final String receiver, final String content, final ArrayList<Object> parameters) {
      this.setSender(sender);
      this.setReceiver(receiver);
      this.setContent(content);
      this.setParameters(parameters);
      this.setTimeStats(new LinkedList<>());
   }

   public Message(final String sender, final String receiver, final String content, final ArrayList<Object> parameters, final LinkedList<Long> timeStats) {
      this.setSender(sender);
      this.setReceiver(receiver);
      this.setContent(content);
      this.setParameters(parameters);
      this.setTimeStats(timeStats);
   }

   public String getSender() {
      return sender;
   }

   public void setSender(String sender) {
      this.sender = sender;
   }

   public String getReceiver() {
      return receiver;
   }

   public void setReceiver(String receiver) {
      this.receiver = receiver;
   }

   public String getContent() {
      return content;
   }

   public void setContent(String content) {
      this.content = content;
   }

   public ArrayList<Object> getParameters() {
      return parameters;
   }

   public void setParameters(ArrayList<Object> parameters) {
      this.parameters = parameters;
   }

   public LinkedList<Long> getTimeStats() {
      return timeStats;
   }

   public void setTimeStats(LinkedList<Long> timeStats) {
      this.timeStats = timeStats;
   }

   public void addTimeStat(long time) {
      this.timeStats.add(time);
   }

   /**
   * Rimuove tutte le statistiche temporali del messaggio
   */
   public void clearTimeStats() {
      this.timeStats.clear();
   }

   @Override
   public String toString() {
      return GSON.toJson(this);
   }

   public static Message buildMessage(String jsonMessage) {
      return GSON.fromJson(jsonMessage, Message.class);
   }

   // Contenuto array --> [timestamp invio entità, timestamp ricezione su Synapsis, timestamp invio da Synapsis, timestamp ricezione entità]

   public long getTimeFromSenderToSynapsis() {
      return (this.timeStats.get(1) - this.timeStats.getFirst());
   }

   public long getSynapsisComputation() {
      return (this.timeStats.get(2) - this.timeStats.get(1));
   }

   // S2S = Sender to Synapsis
   // SC = Synapsis Computation

   public String getCalculatedTimeStats() {
      return "Message TimeStats -> mills - S2S: " + this.getTimeFromSenderToSynapsis() + " mills - SC: " + this.getSynapsisComputation();
   }
}
