package com.jewellery.repository;
import com.jewellery.model.Message;
import org.springframework.data.mongodb.repository.MongoRepository;
import java.util.List;
public interface MessageRepository extends MongoRepository<Message,String> {
    List<Message> findByUserId(String userId);
}