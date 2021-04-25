package com.example.trpp_project.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.trpp_project.config.Const;
import com.example.trpp_project.models.Card;
import com.example.trpp_project.models.User;
import com.example.trpp_project.repo.CardRepository;
import com.example.trpp_project.repo.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.trpp_project.config.Const.TOKEN_PREFIX;

@RestController
@RequestMapping("/cards")
public class CardController {


    @Autowired
    UserRepository userRepository;


    private static String getUsernameFromJWTToken(String token){
        return JWT.require(Algorithm.HMAC256(Const.SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX,""))
                .getSubject();
    }


    @GetMapping("/{id}")
    public Card getCard(@PathVariable("id") int id,@RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);

        Card card = user.getCards().stream().filter(x -> (x.getId() == id)).findAny().orElse(null);
        if (card == null) throw new ResponseStatusException(//ВЫПОЛНИТЬ ПРОВЕРКУ
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return card;

    }

    @PutMapping("/{id}")
    public int updateCard(@PathVariable("id") long id, @RequestBody Card card,@RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);

        Card editCard = user.getCards().stream().filter(x -> (x.getId() == id)).findAny().orElse(null);

        if (editCard == null) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );

        if (card.getName()!="") editCard.setName(card.getName());
        if (card.getPos()!=0) editCard.setPos(card.getPos());
        if (card.getNumberOfList()!=0) editCard.setNumberOfList(card.getNumberOfList());
        card.setTimestamp(new Date().getTime());

        return editCard.getId();
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
    public List<Card> getCards(@RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        return user.getCards();
    }

    @PostMapping()
    public int createCard(@Validated @RequestBody() Card card, @RequestHeader("Authorization") String token){
        card.setTimestamp(new Date().getTime());

        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        user.getCards().add(card);
        userRepository.save(user);
        return user.getCards().get(user.getCards().size()-1).getId(); //id присвоенной базой данных
    }

    @GetMapping("/sinch")
    public List<Card> getNewCards(@RequestParam long timestamp, @RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        return user.getCards().stream().filter(x-> (x.getTimestamp()>timestamp)).collect(Collectors.toList());
    }

//    public String getCurrentUsername() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return auth.getName();
//    }
}
