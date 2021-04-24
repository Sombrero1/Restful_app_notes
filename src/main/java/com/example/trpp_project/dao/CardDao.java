package com.example.trpp_project.dao;

import com.example.trpp_project.models.Card;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Repository;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Repository
public class CardDao {
    ArrayList <Card> fakeArray = new ArrayList<>();
    int last_free_id = 40;
    {
        fakeArray.add(new Card(30,"evgeniy",2,1));
        fakeArray.add(new Card(40,"hello",1,3));
        fakeArray.add(new Card(20,"aloha",3,4));
        fakeArray.add(new Card(10,"buldog",4,8));
    }

    public Card getCard(int id){
        Card card = fakeArray.stream().filter(x -> (x.getId() == id)).findAny().orElse(new Card(-1, "не, хрен тебе",-1, -1));
        if (card.getId() == -1) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        return card;
    }
    public int updateCard(int id,Card card){
        Card editCard = getCard(id);
        if (editCard.getId() == -1) throw new ResponseStatusException(
                HttpStatus.NOT_FOUND, "entity not found"
        );
        if (card.getName()!="") editCard.setName(card.getName());
        if (card.getPos()!=0) editCard.setPos(card.getPos());
        if (card.getNumberOfList()!=0) editCard.setNumberOfList(card.getNumberOfList());
        card.setTimestamp(new Date().getTime());
        return editCard.getId();
    }

//    public int deleteCard(int id){
//        fakeArray.stream().filter(x -> x.getId()==id).dropWhile();
//
//    }

    public ArrayList<Card> getCards(){
        return fakeArray;
    }

    public Card addCard(Card card){
        System.out.println(new Date().getTime());
        card.setTimestamp(new Date().getTime());
        card.setId(last_free_id);
        fakeArray.add(card);
        last_free_id++;
        return card;
    }

    public List<Card> getNewCards(long timestamp){
        return fakeArray.stream().filter(x-> (x.getTimestamp()>timestamp)).collect(Collectors.toList());
    }
}
