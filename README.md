# projekt_bazy_kino


Łukasz Kluska (kluska@student.agh.edu.pl)

Temat: Sprzedaż biletów do kina

Technologie: Java + MySQL

Uwaga: ponieważ urwał mi się kontakt z Mikołajem Bułką (mbulka@student.agh.edu.pl), robię projekt sam.

## Schemat bazy danych

![Schemat bazy danych](/diagram.png)

## Opis

Projekt ma na celu funkcjonować jako backend dla pewnego teoretycznego programu 
zarządzającemu nad sprzedarzą biletów kinowych. Komunikacja z aplikacją odbywa się przez RESTowe API.

Baza składa się z 5 tabel: Film, Genre, Screen, Showing, oraz Ticket. Tabele Film, Genre, Screen oraz 
Showing są tabelami zawierającymi informacje na temat stanu kina i planu wyświetlania filmów, przez 
co ich zawartość byłaby zarządzana przez managerów lub administrację, a tymczasem tabela Ticket zawiera 
wszystkie sprzedane bilety oraz ich status, i jest też ona tą tabelą z którą przeciętny pracownik kina
miałby styczność.

Przez API RESTowe jest też dostępny dostęp do niektórych statystyk, takich jak np. ilość biletów 
sprzedanych dla danego filmu.

##Opis API

Każda z 4 tabel opisujących funkcjonowanie kina (Film, Genre, Screen, Showing) ma zaimplementowane 
podstawowe operacje CRUD, zrealizowane przez odpowiednio mapping `POST`, `GET`, `PUT`, `DELETE`

Te endpoint to:

* `/film` 
* `/genre`
* `/screen`
* `/showings`

Podstawowa funkcjonalność, czyli kupowanie biletów odbywa się przez endpoint `/showing/{id}/tickets`.
Aby zakupić bilet należy przesłać na ten endpoint zapytanie `POST` z odpowiednim payloadem, 
zawierającym informacje o tym na które miejsce chcemy kupić bilet:
```json
{
    "seatRow": 2,
    "seatColumn": 4
}
```
Zmiana statusu biletu (np. anulowanie, lub skasowanie go) też odbywa się pod tym adresem, za pomocą metody `PATCH`.

## Kontrola nad sprzedażą biletów

Nie chcemy dopuścić do sytuacji, gdzie na jedno miejsce są zarezerwowane dwa bilety na raz. 
W tym celu zaimplementowałem w bazie MySQL trigger, który po insercie do tabeli Ticket, sprawdza 
czy na dane miejsce podczas danego seansu nie ma już istniejącego, aktywnego biletu, i jeśli tak to 
blokuje on tego inserta.

```mysql
delimiter $$
CREATE TRIGGER noRepeatTickets
BEFORE INSERT 
ON ticket
for each row
BEGIN
   if exists (SELECT * FROM ticket
   where seat_column = new.seat_column and seat_row = new.seat_row 
   and showing_id = new.showing_id) then
      signal sqlstate '45000'
      set message_text = 'Given seat is already reserved';
   end if;
END $$
delimiter ;
```

##Statystyki

Przez API RESTowe backendu istnieje też dostęp do pewnych statystyk. Opbywa się to za pomocą 
przesłania metodą `GET` odpowiedniego zapytania na endpoint pod mappingiem `/stats/`.

Dostępne endpointy: 
* `/stats/film/{filmId}` - pokazuje statystyki dla filmu o danym ID, pożna podać jako parametry datę początkową i 
końcową okresu, który nas interesuje
* `/stats/film/top` - pokazuje filmy z największą ilością sprzedanych biletów, podobnie jak wyżej przyjmuje 
początek i koniec pewnego interwału czasowego, ale też ilość pozycji do wyświetlenia (domyślnie 10)
* `/stats/genre/top` - działa analogicznie do `/stats/film/top`, tyko że dla gatunków kinowych
