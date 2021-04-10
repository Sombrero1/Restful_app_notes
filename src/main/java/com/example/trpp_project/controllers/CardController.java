package com.example.trpp_project.controllers;

import com.example.trpp_project.Views;
import com.example.trpp_project.dao.CardDao;
import com.example.trpp_project.models.Card;
import com.fasterxml.jackson.annotation.JsonView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/cards")
public class CardController {
    @Autowired
    CardDao cardDao;

    @GetMapping("/{id}")
    public Card getCard(@PathVariable("id") int id){
        Card card = cardDao.getCard(id);
        if (card.getId() == -1) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return card;
    }

    @PutMapping("/{id}")//отдельный режим для обновления карточки, не все поля обязательны и т.д.
    public int updateCard(@PathVariable("id") int id, @RequestBody Card card){
        Card editCard = cardDao.getCard(id);
        if (editCard.getId() == -1) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        if (card.getName()!="") editCard.setName(card.getName());
        if (card.getPos()!=0) editCard.setPos(card.getPos());
        if (card.getNumberOfList()!=0) editCard.setNumberOfList(card.getNumberOfList());

        return cardDao.getCard(id).getId();
    }
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
}
