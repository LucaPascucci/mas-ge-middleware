package models;

import com.google.gson.Gson;

import messages.Message;

public class ActorInfo {

   private ActorType actorType; //Real o Mock

   private final String entityType; //NOTE mind o body
   private final String entityName;

   private ConnectionStatus counterpartStatus = ConnectionStatus.DISCONNECTED;

   private int numberOfReceivedMessages = 0;
   private long totalReceiveTime = 0;
   private long totalComputationTime = 0;

   private static Gson GSON = new Gson();

   public ActorInfo(String type, String name) {
      this.entityType = type;
      this.entityName = name;
   }

   /**
    * @return the counterpartStatus
    */
   public ConnectionStatus getCounterpartStatus() {
      return counterpartStatus;
   }

   /**
    * @param counterpartStatus the counterpartStatus to set
    */
   public void setCounterpartStatus(ConnectionStatus counterpartStatus) {
      this.counterpartStatus = counterpartStatus;
   }

   /**
    * @return the actorType
    */
   public ActorType getActorType() {
      return actorType;
   }

   /**
    * @param actorType the actorType to set
    */
   public void setActorType(ActorType actorType) {
      this.actorType = actorType;
   }

   /**
    * @return the entityName
    */
   public String getEntityName() {
      return entityName;
   }

   /**
    * @return the entityType
    */
   public String getEntityType() {
      return entityType;
   }

   /**
    * @return the number of received message
    */
   public int getNumberOfReceivedMessages() {
      return this.numberOfReceivedMessages;
   }

   public void addNewMessage(Message message) {
      this.numberOfReceivedMessages++;
      this.totalReceiveTime += message.getTimeFromSenderToSynapsis();
      this.totalComputationTime += message.getSynapsisComputation();
   }

   public double getReceivedTimeAVG() {
      return this.totalReceiveTime / this.numberOfReceivedMessages;
   }

   public double getComputationTimeAVG() {
      return this.totalComputationTime / this.numberOfReceivedMessages;
   }

   public boolean checkCounterpartInfo(ActorInfo actorInfo){
      return !this.entityType.equals(actorInfo.getEntityType()) && this.entityName.equals(actorInfo.getEntityName());
   }

   @Override
   public String toString() {
      return GSON.toJson(this);
   }

   public static ActorInfo buildInfo(String jsonInfo) {
      return GSON.fromJson(jsonInfo, ActorInfo.class);
   }
}
