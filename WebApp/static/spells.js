/*
 *  spells.js
 *  Patty Commins and Anna Meyer, 7 May 2017
 *
 *  The Javascript that connects our API with the user interface
 *  within the "Harry Potter spells" webapp for Carleton CS257,
 *  Spring Term 2017.
 */


/*
onTop10Button() is activated when the user clicks the button "See the top 10 spells." It gets the response text for the top 10 spells using out api, and sends this into the top10Callback.
*/
function onTop10Button() {
    var url = api_base_url + 'spells/top/10';
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            top10Callback(xmlHttpRequest.responseText);
        } 
    };
    
    xmlHttpRequest.send(null);
}

/*
top10Callback() uses the response text gotten during onTop10Button(), and creates a clickable table of the top 10 spells and their counts.
*/
function top10Callback(responseText) {
    var top10List = JSON.parse(responseText);
    var tableBody = '<tr><th>' + "Spell Name: Number of times used" + '</th></tr>';
    for (var k = 0; k < top10List.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td><a onclick="getSpell(' + top10List[k]['spell_id'] + ")\">" + top10List[k]['spell_name'] + '</a></td>';
        tableBody += '<td>' + top10List[k]['count'] + '</td>';
                
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_spells');
    resultsTableElement.innerHTML = tableBody;
}

/*
onSpellsButton() is activated when the user clicks the "browse spells" button. It gets a list of spells from our api, and if the request is ready, passes the list into spellsCallback()
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

/*
This function takes in a spellList as responseText, parses it, and creates a table of clickable spells by passing in information to getSpell
*/
function spellsCallback(responseText) {
    var spellsList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < spellsList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td><a onclick="getSpell(' + spellsList[k]['spell_id'] + ")\">" + spellsList[k]['spell_name'] + '</a></td>';
                
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_spells');
    resultsTableElement.innerHTML = tableBody;
}

/*
This function gets called when clicking on one of the spells in the table created in spellsCallback. It gets the list of characters for the given spell, and passes it into getCharactersForSpellCallback along with spellId
*/
function getSpell(spellID) {
    var url = api_base_url + 'characters_by_spell/spell_id/' + spellID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
            getCharactersForSpellCallback(spellID, xmlHttpRequest.responseText);
        }
    }; 

    xmlHttpRequest.send(null);
}

/*
This function takes the spellIDand responseTExt from getSpell, parses the list of characters from the responseText, and also gets the responseText for the given spellID. It passes this reponseText and characterList, as well as spellID into getCharacterForSpellCallback2.
*/
function getCharactersForSpellCallback(spellID, responseText){
    var characterList = JSON.parse(responseText);
    var url = api_base_url + 'spells/spell_id/' + spellID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);
    
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            getCharactersForSpellCallback2(spellID, xmlHttpRequest.responseText, characterList);
        }
    };
    
    xmlHttpRequest.send(null);
}

/*
This function gets the responseText for the spell_count for our spell of interest, and passes this, as well as the characterList, and parsed spellInfo into getCharactersForSpellCallback3.
*/
function getCharactersForSpellCallback2(spellID, responseText, characterList) {
    var spellInfo = JSON.parse(responseText);
    var url = api_base_url + 'spell_count/spell_id/' + spellID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get',url);
    
    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            getCharactersForSpellCallback3(xmlHttpRequest.responseText, characterList, spellInfo);
        }
    };
    
    xmlHttpRequest.send(null);
}

/*
This function parses the responseText we got for spellCount in getCharactersForSpellCallback2 into countResults. It also creates our table that should appear when a user clicks on a link to a spell - headed by the spell's name, its effect, and includes its count, and all the characters who used it (clickable). 
*/
function getCharactersForSpellCallback3(responseText, characterList, spellInfo) {
    var countResults = JSON.parse(responseText);
    var tableBody = '<tr><th>' + spellInfo['spell_name'] + ': ' + spellInfo['spell_effect'] + '</th></tr>';
    tableBody += '<tr><td>' + spellInfo['spell_name'] + ' was used ' + countResults + ' times by these people' + '</td></tr>';
    for (var k = 0; k < characterList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td><a onclick="getCharacter(' + characterList[k]['character_id'] + ",'"
                            + characterList[k]['first_name'] + "','" + characterList[k]['last_name'] +"')\">"
                            + characterList[k]['first_name'] + ' ' + characterList[k]['last_name'] + '</a></td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_spells');
    resultsTableElement.innerHTML = tableBody;
}

/*
This function gets called when a user clicks on the "Browse Characters" button. It gets the responseText for a list of characters (and their attributes) from our api, and then passes this responseText into charactersCallback().
*/
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

/*
This function parses the characterList taken in and creates a table full of clickable characters, that when clicked, activate the getCharacter() function. 
*/
function charactersCallback(responseText) {
    var characterList = JSON.parse(responseText);
    var tableBody = '';
    for (var k = 0; k < characterList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td><a onclick="getCharacter(' + characterList[k]['character_id'] + ",'"
                            + characterList[k]['first_name'] + "','" + characterList[k]['last_name'] +"')\">"
                            + characterList[k]['first_name'] + ' ' + characterList[k]['last_name'] + '</a></td>';
        
        tableBody += '</tr>';
    }
    var resultsTableElement = document.getElementById('results_table_chars');
    resultsTableElement.innerHTML = tableBody;
}

/*
getCharacter() is called when we click on a character link, it gets response text for the spells the given character used from our api, and calls getSpellsForCharacterCallback() with this information, as well as the first name and last name.
*/
function getCharacter(characterID, first_name, last_name) {
    //doesn't get here
    var url = api_base_url + 'spells_by_character/char_id/' + characterID;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get', url);
    xmlHttpRequest.onreadystatechange = function() {
            if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) { 
                getSpellsForCharacterCallback(first_name, last_name, xmlHttpRequest.responseText);
            } 
        }; 

    xmlHttpRequest.send(null);
}

/*
getSpellsForCharacterCallback() creates a clickable table of the spells used by the given character, headed by their name, and the rows are the spell names.
*/
function getSpellsForCharacterCallback(first_name, last_name, responseText) {
    var spellList = JSON.parse(responseText);
    var tableBody = '<tr><th>' + first_name + ' ' + last_name + '</th></tr>';
    for (var k = 0; k < spellList.length; k++) {
        tableBody += '<tr>';
        tableBody += '<td><a onclick="getSpell(' + spellList[k]['spell_id'] + ")\">" + spellList[k]['incantation'] + '</a></td>';
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_chars');
    resultsTableElement.innerHTML = tableBody;
}
 
/*
onBooksButton() is called when a user clicks the "Browse books" button. It gets a list of books from our api and sends it into booksCallback().
*/
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

/*
booksCallback() parses the bookList taken in and creates a clickable list of the books by title. When a given book is clicked, it calls the getBook() function.
*/
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

/*
getBook is called when the user clicks on a link. It uses the api to find the spells within the book, then passes this info into getSpellsForBookCallback()
*/
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

/*
getSpellsForBookCallback() parses the taken in spellList, then creates a table headed by the books title, with the rows being the name of the spells, which are clickable
*/
function getSpellsForBookCallback(title, responseText) {
    var spellList = JSON.parse(responseText);
    var tableBody = '<tr><th>' + title + '</th></tr>';
    for (var k = 0; k < spellList.length; k++) {
        tableBody += '<tr>';
        
        tableBody += '<td><a onclick="getSpell(' + spellList[k]['spell_id'] + ")\">" + spellList[k]                 ['incantation'] + '</a></td>';
        
        tableBody += '</tr>';
    }

    var resultsTableElement = document.getElementById('results_table_books');
    resultsTableElement.innerHTML = tableBody;
}

/*
onSearchButton() is called when a character uses our search bar, entering the "magic word". It first assumes it is a spell, and gets the list of characters that use this spell, and passes this inton spellsSearchCallback()
*/
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

/*
spellsSearchCallback() checks to see if magicword was indeed a spell (if so, the characrter list will be nonempy, and we will get the info on our spell and pass it into spellsSEarchCallback2()), (if not, character list will be empty, and we will assume it is a character, get this information on the character, and pass it into characterSearchCallback())
*/
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

/*
This function is reached if we conclude magicword is a spell. It finds the spell count for magic word, and passes this, along with our previous information into spellsSearchCallback3()
*/
function spellsSearchCallback2(responseText, charResults, magicword) {
    var spellResults = JSON.parse(responseText);
    var url = api_base_url + 'spell_count/' + magicword;
    xmlHttpRequest = new XMLHttpRequest();
    xmlHttpRequest.open('get',url);

    xmlHttpRequest.onreadystatechange = function() {
        if (xmlHttpRequest.readyState == 4 && xmlHttpRequest.status == 200) {
            spellsSearchCallback3(xmlHttpRequest.responseText, charResults, spellResults);
        }
    };

    xmlHttpRequest.send(null);
}

/*
spellsSearchCallback3() parses our count information, and creates a table headed by basic spell information, and follows with rows of clickable characters
*/
function spellsSearchCallback3(responseText, charResults, spellResults) {
    var countResults = JSON.parse(responseText);
    var tableBody = '<tr><th>' + spellResults[0]['spell_name'] + ': ' + spellResults[0]['spell_effect'] + '</th></tr>';
    tableBody += '<tr><td>' + spellResults[0]['spell_name'] + " was used " + countResults + " times by these people" + '</td></tr>';
    for (var k = 0; k < charResults.length; k++) {
        tableBody += '<tr>';

        tableBody += '<td><a onclick="getCharacter(' + charResults[k]['character_id'] + ",'"
                            + charResults[k]['first_name'] + "','" + charResults[k]['last_name'] + "')\">"
                            + charResults[k]['first_name'] + ' ' + charResults[k]['last_name'] + '</a></td>';

        tableBody += '</tr>';
    }
    
    var searchResults = document.getElementById('results_table_spells');
    searchResults.innerHTML = tableBody;
}

/*
charactersSearchCallback() is called when we have eliminated the possibliity of magicword being a spell. We parse the list of characters taken in. If this list is longer than 0, we assume it is a character and make a table of clickable results. Otherwise, we assume magicword is a book, and get the spells used in this book from our api.
*/
function charactersSearchCallback(responseText, magicword) {
    var charResults = JSON.parse(responseText);
    var tableBody = '';

    if (charResults.length > 0) {
        for (var k = 0; k < charResults.length; k++) {
            tableBody += '<tr>';

            tableBody += '<td><a onclick="getCharacter(' + charResults[k]['character_id'] + ",'"
                                + charResults[k]['first_name'] + "','" + charResults[k]['last_name'] + "')\">"
                                + charResults[k]['first_name'] + ' ' + charResults[k]['last_name'] + '</a></td>';

            tableBody += '</tr>';
        }

        var searchResults = document.getElementById('results_table_chars');
        searchResults.innerHTML = tableBody;
    }
    
    else if (charResults.length == 0) {
        var url = api_base_url + 'books/' + magicword;
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

/*
booksSearchCallback() parses our book result taken in, and if it is not a book (determined by whether spell results has length 0), we give an error message. Otherwise, create a table headed by the book table, and the rows are the spells used in the books, which are clickable. 
*/
function booksSearchCallback(responseText, magicword) {
    var bookResult = JSON.parse(responseText);

    if (Object.keys(bookResult).length == 0) {
        var tableBody = '<tr><th>' + "No spells, characters, or books match your search" + '</th></tr>';
        
        var searchResults = document.getElementById('search_results');
        searchResults.innerHTML = tableBody; 
    }
    else {
        var tableBody = '<tr><td><a onclick="getBook(' + bookResult['book_id'] + ",'" + bookResult['book_name'] + "')\">" + bookResult['book_name'] + '</a></td></tr>';

        var searchResults = document.getElementById('results_table_books');
        searchResults.innerHTML = tableBody;
    }
}

function onResetAllButton(){
    location.reload();
}

function onSpellsResetButton(){
    var spellResults = document.getElementById('results_table_spells');
    spellResults.innerHTML = "";
}

function onCharactersResetButton(){
    var charResults = document.getElementById('results_table_chars');
    charResults.innerHTML = "";
} 

function onBooksResetButton(){
    var bookResults = document.getElementById('results_table_books');
    bookResults.innerHTML = "";
}