/*
 *  spells.js
 *  Patty Commins and Anna Meyer, 7 May 2017
 *
 *  A little bit of Javascript showing one small example of AJAX
 *  within the "Harry Potter spells" sample for Carleton CS257,
 *  Spring Term 2017.
 */

function onSpellsButton() {
    var url = api_base_url + 'spells';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            spellsCallback(xmlHttpRequest.responseText);
        } 
    };
    
    xmlHttpRequest.send(null);
}

function spellsCallback(responseText) {
    var spellsList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < spellsList.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getSpell(' + spellsList[k]['spell_id'] + ",'"
                            + spellsList[k]['spell_name'] + "')\">"
                            + spellsList[k]['spell_name'] + '</a></td>';
                
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_spells');
    resultsTableElement.innerHTML = tableBody;
}

function getSpell(spellID, spellName) {
    var urlChars = api_base_url + 'characters_by_spell/spell_id/' + spellID;
    var urlSpell = api_base_url + 'spells/spell_id/' + spellID;
    xmlHttpRequest1 = new XMLHttpRequest();
    xmlHttpRequest1.open('get', urlChars);
    xmlHttpRequest2 = new XMLHttpRequest();
    xmlHttpRequest2.open('get',urlSpell);

    xmlHttpRequest1.onreadystatechange = function() {
            if (xmlHttpRequest1.readyState == 4 && xmlHttpRequest1.status == 200
               && xmlHttpRequest2.readyState == 4 && xmlHttpRequest2.status == 200) { 
                getCharactersForSpellCallback(spellName, xmlHttpRequest1.responseText, xmlHttpRequest2.responseText);
            }
            else {
                console.log("there's a problem");
            }
        }; 

    xmlHttpRequest.send(null);
}

function getCharactersForSpellCallback(spellName, responseTextChar, responseTextSpell) {
    var characterList = JSON.parse(responseTextChar);
    var spellInfo = JSON.parse(responseTextSpell);
    var tableBody = '<tr><th>' + spellName + ': ' + spellInfo["spell_effect"] + '</th></tr>';
    for (var k = 0; k < characterList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td>' + characterList[k]['first_name'] + '</td>';
        tableBody += '<td>' + characterList[k]['last_name'] + '</td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_spells');
    resultsTableElement.innerHTML = tableBody;
}

function onCharactersButton() {
    var url = api_base_url + 'characters';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            charactersCallback(xmlHttpRequest.responseText);
        } 
    }; 

    xmlHttpRequest.send(null);
}

function charactersCallback(responseText) {
    var characterList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < characterList.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getCharacter(' + characterList[k]['character_id'] + ",'"
                            + characterList[k]['first_name'] + characterList[k][last_name] + "')\">"
                            + characterList[k]['first_name'] + '</a></td>';
                
        tableBody += '<td>' + characterList[k]['last_name'] + '<\td>';

        tableBody += '</tr>';
    }
    
    var resultsTableElement = document.getElementById('results_table_chars');
    resultsTableElement.innerHTML = tableBody;
}

function getCharacter(characterID, characterFirstName, characterLastName) {
    var url = api_base_url + 'spells_by_character/char_id/' + characterID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest1.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
                getSpellsForCharacterCallback(characterFirstName, characterLastName, xmlHttpRequest.responseText);
            } 
        }; 

    xmlHttpRequest.send(null);
}

function getSpellsForCharacterCallback(characterFirstName, characterLastName, responseText) {
    var spellList = JSON.parse(responseText);
    var tableBody = '<tr><th>' + characterFirstName + ' ' + characterLastName + '</th></tr>';
    for (var k = 0; k < spellList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td>' + spellList[k]['incantation'] + '</td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_chars');
    resultsTableElement.innerHTML = tableBody;
}
    
function onBooksButton() {
    var url = api_base_url + 'books';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            booksCallback(xmlHttpRequest.responseText);
        } 
    }; 

    xmlHttpRequest.send(null);
}

function booksCallback(responseText) {
    var bookList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < bookList.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getBook(' + bookList[k]['book_id'] + ",'"
                            + bookList[k]['title'] + "')\">"
                            + bookList[k]['title'] + '</a></td>';
            
        tableBody += '</tr>';
    }
    
    var resultsTableElement = document.getElementById('results_table_books');
    resultsTableElement.innerHTML = tableBody;
}

function getBook(bookID, title) {
    var url = api_base_url + 'books_spells/book_id/' + bookID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest1.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
                getSpellsForBookCallback(title, xmlHttpRequest.responseText);
            } 
        }; 

    xmlHttpRequest.send(null);
}

function getSpellsForBookCallback(title, responseText) {
    var spellList = JSON.parse(responseText);
    var tableBody = '<tr><th>' + title + '</th></tr>';
    for (var k = 0; k < spellList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td>' + spellList[k]['incantation'] + '</td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_books');
    resultsTableElement.innerHTML = tableBody;
}
