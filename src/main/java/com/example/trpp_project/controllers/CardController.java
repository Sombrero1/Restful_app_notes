package com.example.trpp_project.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.trpp_project.config.Const;
import com.example.trpp_project.dao.CardDao;
import com.example.trpp_project.models.Card;
import com.example.trpp_project.models.User;
import com.example.trpp_project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.example.trpp_project.config.Const.TOKEN_PREFIX;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    CardDao cardDao;

    @Autowired
    UserRepository userRepository;



    @GetMapping("/{id}")
    public Card getCard(@PathVariable("id") int id){
        return cardDao.getCard(id);
    }

    @PutMapping("/{id}")
    public int updateCard(@PathVariable("id") int id, @RequestBody Card card){
        return cardDao.updateCard(id,card);
    }

//    @DeleteMapping("/{id}") //подумать про удаление - как оповестить пользователя
//    public Card deleteCard(@PathVariable("id") int id){
//        Card card = cardDao.getCard(id);
//        if (card.getId() == -1) throw new ResponseStatusException(
//                HttpStatus.NOT_FOUND, "entity not found"
//        );
//        return card;
//    }


    @GetMapping()
    public ArrayList<Card> getCards(@RequestHeader("Authorization") String token){
        String user = JWT.require(Algorithm.HMAC256(Const.SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX,""))
                .getSubject();
        System.out.println(user);
        System.out.println(userRepository.findByUsername(user).getCards());
        return cardDao.getCards();
    }

    @PostMapping()
    public int createCard(@Validated @RequestBody() Card card, @RequestHeader("Authorization") String token){
        String user = JWT.require(Algorithm.HMAC256(Const.SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX,""))
                .getSubject();
        User user1 =  userRepository.findByUsername(user);

        user1.getCards().add(card);

        userRepository.save(user1);

        return cardDao.addCard(card).getId();
    }

    @GetMapping("/sinch")
    public List<Card> getNewCards(@RequestParam long timestamp){
        return cardDao.getNewCards(timestamp);
    }

//    public String getCurrentUsername() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return auth.getName();
//    }
}
