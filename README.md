#
##java.version: 11
##postgres: 13.1
##Backend by Spring.
##Authorization is performed using a JWT token.
</br>
</br>
##API:
##get cards:
###Get: /cards
###return: array of cards ([(int id, string name, id numberOfList, id pos)])
</br>
##get card by id:
###Get: /cards/{id}, id - int (~long )
###return: Card (int id, string name, id numberOfList, id pos)
</br>
##add cards:
###Post: /cards
###Body:  {int id, string name, id numberOfList, id pos}
###return: id of card
</br>
##sinch (get new cards): /cards/sinch
###Get: /cards/sinch?timestamp={last timestamp when sinch}
###return: array of cards ([(int id, string name, id numberOfList, id pos)])
</br>
##Edit a card:
###Put: /cards/{id} 
###Body: any field of the card
###return: id card
</br>
##Delete a card:
###DELETE: /cards/{id} 
###return: DELETED