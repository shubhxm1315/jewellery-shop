package com.jewellery.model;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document("messages")
public class Message {
    @Id private String id;
    private String userId, productId, message, reply, status="OPEN";
    private LocalDateTime createdAt=LocalDateTime.now();
    public Message(){}
    public String getId(){return id;} public void setId(String v){id=v;}
    public String getUserId(){return userId;} public void setUserId(String v){userId=v;}
    public String getProductId(){return productId;} public void setProductId(String v){productId=v;}
    public String getMessage(){return message;} public void setMessage(String v){message=v;}
    public String getReply(){return reply;} public void setReply(String v){reply=v;}
    public String getStatus(){return status;} public void setStatus(String v){status=v;}
    public LocalDateTime getCreatedAt(){return createdAt;} public void setCreatedAt(LocalDateTime v){createdAt=v;}
}