package com.jewellery.controller;
import com.jewellery.model.Message;
import com.jewellery.model.User;
import com.jewellery.repository.MessageRepository;
import com.jewellery.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/messages")
public class MessageController {
    private final MessageRepository messages; private final UserRepository users;
    public MessageController(MessageRepository m,UserRepository u){messages=m;users=u;}
    @PostMapping("/send")
    public String send(@RequestParam String productId,@RequestParam String message,Authentication auth){
        User u=users.findByEmail(auth.getName()).orElseThrow();
        Message m=new Message(); m.setUserId(u.getId()); m.setProductId(productId); m.setMessage(message); messages.save(m);
        return "redirect:/products/"+productId;
    }
}