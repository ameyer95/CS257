#api.py
#!/usr/bin/env python3
'''
    api.py
    Patty Commins and Anna Meyer, 26 April 2016

    Runs the API for our Harry Potter spell database
'''

import sys
import flask
import json
import config
import psycopg2

app = flask.Flask(__name__,static_folder='static',template_folder = 'template')

def _fetch_all_rows_for_query(query):
    '''
       Returns a list of rows obtained from the database by the specified SQL
       query. If the query fails for any reason, an empty list is returned.

       Note that this is not necessarily the right error-handling choice. Would users
       of the API like to know the nature of the error? Do we as API implementors
       want to share that information? There are many considerations to balance.
       '''
    try:
        connection = psycopg2.connect(database=config.database, user=config.user, password=config.password)
    except Exception as e:
        print('Connection error:', e, file=sys.stderr)
        return []

    rows = []
    try:
        cursor = connection.cursor()
        cursor.execute(query)
        rows = cursor.fetchall()  # This can be trouble if your query results are really big.
    except Exception as e:
        print('Error querying database:', e, file=sys.stderr)

        connection.close()
        return rows

        # related to user interface only
        # determines what programs can call your API


@app.after_request
def set_headers(response):
    response.headers['Access-Control-Allow-Origin'] = '*'
    return response

@app.route('/characters/<character_name>')
def get_char_id_by_name(character_name):
    ''' 
    Given a character's first or last name (or both), return the
    ID number associated with that character.
    If character cannot be found in our database, will return an
    empty list.
    '''
    query = '''SELECT id, first_name, last_name 
                FROM characters
                WHERE UPPER(last_name) LIKE UPPER('%{0}%)
                or UPPER(first_name) LIKE UPPER('%{0}%)
                or UPPER(first_name + ' ' + last_name) LIKE UPPER('%')
                ORDER BY last_name, first_name'''.format(character_name)

    character_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_character_by_id', character_id = row[0], _external = True)
        character = {'character_id':row[0], 'first_name':row[1], 'last_name':row[2],
                     'url':url}
        character_list.append(character)

    return json.dumps(character_list)

@app.route('characters/<character_id>')
def get_character_by_id(character_id):
    ''' 
    Returns the character associated with the specified ID.
    The character resource will be represented as a JSON dictionary
    with keys 'first_name' (string), 'last_name'(string), 
    'character_id' (int), and 'url' (string). The URL can be used to access
     this character in the future.
     '''
    query = '''SELECT id, first_name, last_name
                FROM characters WHERE id = {0}'''.format(character_id)

    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_char_by_id', character_id = row[0], _external = True)
        character = {'character_id':row[0], 'first_name':row[1], 'last_name':row[2],
                     'url':url}
        return json.dumps(character)

    return json.dumps({})

@app.route('/spells/<spell_id>')
def get_spell_by_id(spell_id):
    ''' 
    Returns the spell associated with the specified ID.
    The spell resource will be represented as a JSON dictionary
    with keys 'spell_name' (string), 'type_id'(int), 
    'spell_effect' (string), 'spell_id' (int) and 'url' (string). The URL can be used to access
     this spell in the future.
     '''
    query = '''SELECT id, incantation, type_id, purpose
                FROM spells WHERE id = {0}'''.format(spell_id)

    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_spell_by_id', spell_id = row[0], _external = True)
        spell = {'spell_id':row[0], 'spell_name':row[1], 'type_id':row[2],
                     'spell_effect':row[3], 'url':url}
        return json.dumps(spell)

    return json.dumps({})

@app.route('/spells/<spell_name>')
def get_spell_by_name(spell_name):
    ''' 
    Given a spell name, return the
    ID number associated with that spell.
    If the spell cannot be found in our database, will return an
    empty list.
    '''
    query = '''SELECT id, incantation, type_id, purpose 
                FROM spells
                WHERE UPPER(incantation) LIKE UPPER('%{0}%)
                ORDER BY incantation'''.format(spell_name)

    spell_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_spell_by_id', spell_id = row[0], _external = True)
        spell = {'spell_id':row[0], 'spell_name':row[1], 'type_id':row[2],
                     'spell_effect':row[3], 'url':url}
        spell_list.append(spell)

    return json.dumps(spell_list)

@app.route('/characters/<character_id>')
def get_spells_by_character(character_id):
    ''' 
    Returns a list of all the spells that a given 
    character used. 
    '''
    query = '''SELECT spells.id, spells.incantation, spells.type_id, spells.purpose
                FROM spells, instances, characters
                WHERE spells.id = instances.spell_id
                AND characters.id = instances.character_id
                AND characters.id = {0}
                ORDER BY spells.incantation'''.format({0})

    spell_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_spell_by_id', spell_id = row[0], _external = True)
        spell = {'spell_id':row[0], 'incantation':row[1], 'type_id':row[2], 'purpose':row[3],
                 'url':url}
        spell_list.append(spell)

    return json.dumps(spell_list)

@app.route('/characters/<character_name>')
def get_spells_by_character_name(character_name):
    '''
    Returns a list of all the spells that a given character used.
    Allows the user to input the character's name as a string, then 
    finds the associated ID and calls get_spells_by_character
    '''
    character_id = get_char_id_by_name(character_name)
    return get_spells_by_character(character_id)

@app.route('/books/<book_id>')
def get_book_by_id(book_id):
    ''' 
    Returns the book associated with the specified ID.
    The book resource will be represented as a JSON dictionary
    with keys 'book_name' (string), 'book_id' (int) and 'url' (string). The URL can be used to access
     this book in the future.
     '''
    query = '''SELECT id, name
                FROM books WHERE id = {0}'''.format(book_id)

    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_book_by_id', book_id = row[0], _external = True)
        book = {'book_id':row[0], 'book_name':row[1], 'url':url}
        return json.dumps(book)

    return json.dumps({})

@app.route('/books/<book_name>')
def get_book_id_by_name(book_name):
    ''' 
    Returns the book idassociated with the specified ID.
    The book resource will be represented as a JSON dictionary
    with keys 'book_name' (string), 'book_id' (int) and 'url' (string). The URL can be used to access
     this book in the future.
     '''
    query = '''SELECT id, name
                FROM books WHERE name = {0}'''.format(book_name)

    rows = _fetch_all_rows_for_query(query)
    if len(rows) > 0:
        row = rows[0]
        url = flask.url_for('get_book_by_id', book_id = row[0], _external = True)
        book = {'book_id':row[0], 'book_name':row[1], 'url':url}
        return json.dumps(book)

    return json.dumps({})


@app.route('/books/<book_id>')
def get_spells_by_book(book_id):
    ''' 
    Returns a list of all the spells used in a given book. 
    '''
    query = '''SELECT spells.id, spells.incantation, spells.type_id, spells.purpose
                FROM spells, instances, books
                WHERE spells.id = instances.spell_id
                AND books.id = instances.book_id
                AND books.id = {0}
                ORDER BY spells.incantation'''.format({0})

    spell_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_spell_by_id', spell_id=row[0], _external=True)
        spell = {'spell_id': row[0], 'incantation': row[1], 'type_id': row[2], 'purpose': row[3],
                 'url': url}
        spell_list.append(spell)

    return json.dumps(spell_list)

@app.route('/books/<book_name>')
def get_spells_by_book_name(book_name):
    '''
    Returns a list of all the spells used in a given book.
    Allows the user to input the book name (as a string), then converts
    that string to the book ID and calls the get_spells_by_book method
    '''

    book_id = get_book_id_by_name(book_name)
    return get_spells_by_book(book_id)

@app.route('/spells/<spell_id>')
def get_characters_by_spell(spell_id):
    '''
    Returns a list of all characters who used the given spell.
    '''
    query = '''SELECT characters.id, characters.first_name, characters.last_name
                FROM spells, instances, characters
                WHERE spells.id = instances.spell_id
                AND characters.id = instances.character_id
                AND spells.id = {0}
                ORDER BY characters.last_name, characters.first_name'''.format({0})

    character_list = []
    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_characters_by_id', character_id=row[0], _external=True)
        character = {'character_id': row[0], 'first_name': row[1], 'last_name': row[2],
                 'url': url}
        character_list.append(character)

    return json.dumps(character_list)

@app.route('/spells/<incantation>')
def get_characters_by_spell_name(incantation):
    ''' 
    Returns a list of all the characters who used a given spell. 
    Allows the user to input the spell name as a string, then finds the associated ID and
    calls get_characters_by_spell using that ID'''
    spell_id = get_spell_by_name(incantation)
    return get_characters_by_spell(spell_id)

@app.route('/spells/<spell_id>')
def get_spell_count(spell_id):
    ''' 
    Returns the number of time a given spell (input as an ID number) was
    used across all books
    '''
    query = '''SELECT COUNT(*)
                FROM instances
                WHERE spell_id = {0}'''.format({0})

    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_spell_count', spell_id={0}, _external=True)
        count = row[0]
    return json.dumps(count)

@app.route('/spells/<incantation>')
def get_spell_count_by_name(incantation):
    ''' 
    Return the number of time a given spell was used across all books.
    Allows the user to input the spell name as a string, then finds the associated
    ID number and calls the get_spell_count method.
    '''
    spell_id = get_spell_by_name(incantation)
    return get_spell_count(spell_id)





@app.route('/spells/<spell_id>')
def get_spell_count_by_book(spell_id, book_id):
    ''' 
    Returns the number of time a given spell (input as an ID number) was
    used across a given books
    '''
    query = '''SELECT COUNT(*)
                FROM instances
                WHERE spell_id = {0}
                AND book_id = {1}
                '''.format({0})

    for row in _fetch_all_rows_for_query(query):
        url = flask.url_for('get_spell_count_by_book', spell_id={0}, _external=True)
        count = row[0]
    return json.dumps(count)

@app.route('/spells/<book_name>/<incantation>')
def get_spell_count_by_book_by_names(incantation, book_name):
    ''' 
    Return the number of time a given spell was used across a given books.
    Allows the user to input the spell name as a string and the book name as a string, then finds the associated
    ID numbers and calls the get_spell_count_by_book method.
    '''
    spell_id = get_spell_by_name(incantation)
    book_id = get_book_id_by_name(book_name)
    return get_spell_count_by_book(spell_id, book_id)

@app.route('/help')
def help():
    rule_list = []
    for rule in app.url_map.iter_rules():
        rule_text = rule.rule.replace('<', '&lt;').replace('>','&gt;')
        rule_list.append(rule_text)
    return json.dump(rule_list)

if __name__ == '__main__':
    if len(sys.argv) != 3:
        print('Usage: {0} host port'.format(sys.argv[0]), file=sys.stderr)
        exit()

    host = sys.argv[1]
    port = sys.argv[2]
    app.run(host=host, port=port)