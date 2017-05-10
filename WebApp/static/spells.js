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
    var url = api_base_url + 'characters_by_spell/spell_id/' + spellID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            getCharactersForSpellCallback(spellID, spellName, xmlHttpRequest.responseText);
        }
    }; 

    xmlHttpRequest.send(null);
}

function getCharactersForSpellCallback(spellID, spellName, responseText){
    var characterList = JSON.parse(reponseText);
    var url = api_base_url + 'spells/spell_id/' + spellID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get',url);
    
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            getCharactersForSpellCallback2(xmlHttpRequest.responseText, characterList);
        }
    };
    
    xmlHttpRequest.send(null);
}

function getCharactersForSpellCallback2(responseText, characterList) {
    var spellInfo = JSON.parse(responseText);
    var tableBody = '<tr><th>' + spellInfo['spell_name'] + ': ' + spellInfo['spell_effect'] + '</th></tr>';
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
                            + characterList[k]['first_name'] + ",'" + characterList[k]["last_name"] + "')\">"
                            + characterList[k]['first_name'] + ' ' + characterList[k]['last_name'] + '</a></td>';
        
        tableBody += '</tr>';
    }
    
    var resultsTableElement = document.getElementById('results_table_chars');
    resultsTableElement.innerHTML = tableBody;
}

function getCharacter(characterID, characterFirstName, characterLastName) {
    var url = api_base_url + 'spells_by_character/char_id/' + characterID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
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

    xmlHttpRequest.onreadystatechange = function() {
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

function onSearchButton() {
    // check characters_by_spell/magicword
    // if empty, check spells_by_character/magicword
    // if empty, check books_spells/magicword
    var magicword = document.getElementById('magicword').value;
    var url = api_base_url + 'characters_by_spell/' + magicword;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);
    
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            spellsSearchCallback(xmlHttpRequest.responseText, magicword);
        } 
    };
    
    xmlHttpRequest.send(null);
}


function spellsSearchCallback(responseText, magicword) {
    var charResults = JSON.parse(responseText);
    if (charResults.length > 0) {
        var url = api_base_url + 'spells/' + magicword;
        xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.open('get', url);
        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                spellsSearchCallback2(xmlHttpRequest.responseText, charResults, magicword);
            }
        };
        
        xmlHttpRequest.send(null);
    }
    
    else if (charResults.length == 0) {
        var url = api_base_url + 'characters/' + magicword;
        xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.open('get',url);
        
        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                charactersSearchCallback(xmlHttpRequest.responseText, magicword);
            }
        };
        
        xmlHttpRequest.send(null);
    }
}

function spellsSearchCallback2(responseText, charResults, magicword) {
    var spellResults = JSON.parse(responseText);
    var tableBody = '<tr><th>' + spellResults['spell_name'] + ': ' + spellResults['spell_effect'] + '</th></tr>';
    for (var k = 0; k < charResults.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getCharacter(' + charResults[k]['char_id'] + ",'"
                            + charResults[k]['first_name'] + charResults[k]['last_name'] + "')\">"
                            + charResults[k]['first_name'] + ' ' + charResults[k]['last_name'] + '</a></td>';

        tableBody += '</tr>';
    }

    var searchResults = document.getElementById('searchResults');
    searchResults.innerHTML = tableBody;
}

function charactersSearchCallback(responseText, magicword) {
    var charResults = JSON.parse(responseText);
    var tableBody = '';

    //ideally we'll be able to click on a character and get a list of the spells they used
    if (charResults.length > 0) {
        for (var k = 0; k < charResults.length; k++) {
            tableBody += '<tr>';

            tableBody += '<td><a onclick="getCharacter(' + charResults[k]['character_id'] + ",'"
                                + charResults[k]['first_name'] + charResults[k]['last_name'] + "')\">"
                                + charResults[k]['first_name'] + ' ' + charResults[k]['last_name'] + '</a></td>';

            tableBody += '</tr>';
        }

        var searchResults = document.getElementById('searchResults');
        searchResults.innerHTML = tableBody;
    }
    
    else if (charResults.length == 0) {
        var url = api_base_url + 'books_spells/' + magicword;
        xmlHttpRequest = new XMLHttpRequest();
        xmlHttpRequest.open('get',url);
        
        xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
                booksSearchCallback(xmlHttpRequest.responseText, magicword);
            }
        };
        
        xmlHttpRequest.send(null);
    }
}

function booksSearchCallback(responseText, magicword) {
    var spellResults = JSON.parse(responseText);
    var url = api_base_url + 'books/' + magicword;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get',url);
        
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            booksSearchCallback2(xmlHttpRequest.responseText, spellResults, magicword);
        }
    };
    
    xmlHttpRequest.send(null);
}

function booksSearchCallback2(responseText, spellResults, magicword) {
    var bookResult = JSON.parse(responseText);
    var tableBody = '<tr><th>' + bookResult['book_name'] + '</th></tr>';

    for (var k = 0; k < spellResults.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getSpell(' + spellResults[k]['spell_id'] + ",'"
                            + spellResults[k]['incantation'] + "')\">"
                            + spellResults[k]['incantation'] + ' ' + spellResults[k]['purpose'] + '</a></td>';

        tableBody += '</tr>';
    }

    var searchResults = document.getElementById('searchResults');
    searchResults.innerHTML = tableBody;

}

