package com.example.trpp_project.controllers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.example.trpp_project.config.Const;
import com.example.trpp_project.models.Card;
import com.example.trpp_project.models.Status;
import com.example.trpp_project.models.Table;
import com.example.trpp_project.models.User;
import com.example.trpp_project.repo.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

import static com.example.trpp_project.config.Const.TOKEN_PREFIX;

@RestController
@RequestMapping("/cards")
@Slf4j
public class CardController {

    @Autowired
    private UserRepository userRepository;


    private static String getUsernameFromJWTToken(String token){
        return JWT.require(Algorithm.HMAC256(Const.SECRET.getBytes()))
                .build()
                .verify(token.replace(TOKEN_PREFIX,""))
                .getSubject();
    }

    public static Card getCard(User user, int tableId, int id){//ПРОВЕРКА НА DELETED
        try {
        return user.getTables().stream().filter(x -> (x.getId() == tableId && x.getStatus() != Status.DELETED))
                .findAny().orElseThrow().getCards()
                .stream().filter(x -> (x.getId() == id && x.getStatus() != Status.DELETED)).findAny().orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResponseStatusException(//ВЫПОЛНИТЬ ПРОВЕРКУ
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    public static Table getTable(User user, int tableId){//ПРОВЕРКА НА DELETED
        try {
            return user.getTables().stream().filter(x -> (x.getId() == tableId && x.getStatus() != Status.DELETED))
                    .findAny().orElseThrow();
        }
        catch (NoSuchElementException e){
            throw new ResponseStatusException(//ВЫПОЛНИТЬ ПРОВЕРКУ
                    HttpStatus.NOT_FOUND, "entity not found"
            );
        }
    }

    @GetMapping()
    public List<Table> getCards(@RequestHeader("Authorization") String token) throws CloneNotSupportedException {
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        log.info("try get cards by username {}",username);

        List<Table> tables = new ArrayList<>();
        for (Table t:user.getTables()
        ) {
            Table table = t.clone();
            if (table.getStatus() != Status.DELETED){
                table.setCards(new ArrayList<Card>());
                for (Card card:t.getCards()
                ) {
                    if (card.getStatus() != Status.DELETED) table.getCards().add(card);
                }
                if (table.getCards().size()>0) tables.add(table);
            }
        }

        return tables;//не возвращать удалённые
    }

    @GetMapping("/{tableId}/{id}")
    public Card getCard(@PathVariable("tableId") int tableId,@PathVariable("id") int id,@RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);

        log.info("try get card by username {} with id",username,id);

        Card card = getCard(user, tableId, id);

        return card;
    }

    @PutMapping("/{tableId}/{id}") //нет изменения таблицы
    public long updateCard(@PathVariable("tableId") int tableId,@PathVariable("id") int id, @RequestBody Card card,@RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        log.info("try update card by username {} with id {} card:{}",username,id,card);

        Card editCard = getCard(user, tableId, id);
        getTable(user,tableId).setTimestamp(new Date().getTime());

        if (card.getName()!=null) editCard.setName(card.getName());
        if (card.getPosition()>0) editCard.setPosition(card.getPosition());
        editCard.setTimestamp(new Date().getTime());
        editCard.setStatus(Status.CHANGED);
        userRepository.save(user);

        return editCard.getId();
    }

    @DeleteMapping("/{tableId}/{id}")
    public String deleteCard(@PathVariable("tableId") int tableId,@PathVariable("id") int id, @RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        log.info("try delete card by username {} with id {}",username,id);
        Card editCard = getCard(user,tableId,id);

        editCard.setStatus(Status.DELETED);
        editCard.setTimestamp(new Date().getTime());
        userRepository.save(user);

        return "DELETED";
    }

    @DeleteMapping("/{tableId}")
    public String deleteTable(@PathVariable("tableId") int tableId, @RequestHeader("Authorization") String token){
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        log.info("try delete table by username {} with table_id {}",username,tableId);
        Table editTable = getTable(user,tableId);

        editTable.setStatus(Status.DELETED);
        editTable.setTimestamp(new Date().getTime());

        for (Card card:editTable.getCards()
             ) {
            card.setTimestamp(new Date().getTime());
            card.setStatus(Status.DELETED);
        }
        userRepository.save(user);

        return "DELETED";
    }

//
    @PostMapping("/{tableId}")
    public long createCard(@PathVariable("tableId") int tableId,@Validated @RequestBody() Card card, @RequestHeader("Authorization") String token){
        card.setTimestamp(new Date().getTime());

        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        Table table = getTable(user, tableId);
        table.getCards().add(card);
        table.setTimestamp(new Date().getTime());
        userRepository.save(user);

        log.info("try create card by username {} card: {}",username,card);

        return getTable(user,tableId).getCards().get(table.getCards().size()-1  ).getId(); //id присвоенной базой данных
    }

    @PostMapping()
    public long createTable(@RequestBody Table table, @RequestHeader("Authorization") String token){//валидацию
        table.setTimestamp(new Date().getTime());

        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        user.getTables().add(table);
        userRepository.save(user);

        log.info("try create card by username {} table: {}",username,table);

        return user.getTables().get(user.getTables().size()-1).getId(); //id присвоенной базой данных
    }

    @GetMapping("/sinch")
    public List<Table> getNewCards(@RequestParam long timestamp, @RequestHeader("Authorization") String token) throws CloneNotSupportedException {
        String username = getUsernameFromJWTToken(token);
        User user =  userRepository.findByUsername(username);
        log.info("try sinch by username {} timestamp: {}",username, timestamp);

        List<Table> tables = new ArrayList<>();
        for (Table t:user.getTables()
             ) {
            Table table = t.clone();
            table.setCards(new ArrayList<Card>());
            for (Card card:t.getCards()
                 ) {
                if (card.getTimestamp()>timestamp) table.getCards().add(card);
            }
            if (table.getCards().size()>0) tables.add(table);
        }

        return tables;
    }
//    public String getCurrentUsername() {
//        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
//        return auth.getName();
//    }
}
