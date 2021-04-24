package com.example.trpp_project.controllers;

import com.example.trpp_project.dao.CardDao;
import com.example.trpp_project.models.Card;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    CardDao cardDao;



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
    public ArrayList<Card> getCards(){
        return cardDao.getCards();
    }

    @PostMapping()
    public int createCard(@Validated @RequestBody() Card card){
        return cardDao.addCard(card).getId();
    }

    @GetMapping("/sinch")
    public List<Card> getNewCards(@RequestParam long timestamp){
        return cardDao.getNewCards(timestamp);
    }

    public String getCurrentUsername() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return auth.getName();
    }
}
