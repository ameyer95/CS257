'''
api_tests.py
Anna Meyer and Patty Commins, 21 May 2017
'''

import unittest
import sys
import argparse
import json
import urllib

class apiTester(unittest.TestCase):
    def setUp(self):
        pass
    def tearDown(self):
        pass
    # Tests to see if our input: characters, get: list of spells api query works with Ron Weasley
    def testCharToSpells1(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spells_by_character/Ron_Weasley/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        string_from_server = string_from_server.replace(" ","")
        expected_list = ["accio", "caveinimicum", "expelliarmus", "relashio", "riddikulus", "silencio", "tergeo",
                         "wingardiumleviosa"]
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in expected_list:
            self.assertTrue(spell in string_from_server)

    # Tests to see if our input: characters, get: list of spells api query works with Ludo Bagman
    #Interesting case because he does not cast many spells - but casts the same spell several times
    #Want to make sure our list does not return repeats
    def testCharToSpells2(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spells_by_character/Ludo_Bagman/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        string_from_server = string_from_server.replace(" ","")
        expected_list = ["quietus", "sonorus"]
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in expected_list:
            self.assertTrue(spell in string_from_server)

    #Tests our characters to list of spells api query on a chacater who only casted one spell, one time - Parvati Patil
    def testCharToSpells3(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spells_by_character/Parvati_Patil/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        string_from_server = string_from_server.replace(" ","")
        expected_spell = "riddikulus"
        self.assertTrue(expected_spell in string_from_server)
        self.assertTrue(len(spell_list)==1)


    #Tests whether our input: book, get: list of spells api query works with Deathly Hallows
    def bookToSpells1(self):
        url = 'http://thacker.mathcs.carleton.edu:5208/books_spells/Deathly_Hallows/'
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        returned_list = json.loads(string_from_server)
        string_from_server = string_from_server.replace(" ", "")
        expected_list = ["avadakedavra", "caveinimicum", "confringo", "confundus", "crucio", "deprimo", "diffindo", "duro",
                         "engorgio", "erecto", "expectopatronum", "expelliarmus", "expulso", "fidelius","geminio", "glisseo",
                         "homenumrevelio", "impedimienta", "imperio", "impervius", "levicorpus", "liberacorpus", "lumos",
                         "meteolojinxrecanto", "muffliato", "nox", "obliviate", "obscuro", "petrificustotalus",
                         "piertotumlocomotor", "protego", "protegototalum", "reducio", "relashio", "reparo", "repellomuggletum",
                         "salviohexia", "sectumsepra", "stuepfy", "tergeo", "wingardiumleviosa"]
        assertEqual(len(returned_list), len(expected_list))
        #Test if each spell in expected_list is contained in string from server.
        for spell in expected_list:
            self.assertTrue(spell in string_from_server)

    # Tests whether our "input: book, get: list of spells" api query works with Chamber of Secrets
    def bookToSpells2(self):
        url = 'http://thacker.mathcs.carleton.edu:5208/books_spells/Chamber_of_Secrets/'
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        returned_list = json.loads(string_from_server)
        string_from_server = string_from_server.replace(" ", "")
        expected_list = ["expelliarmus", "aparecium", "eatslugs", "homorphus", "lumos", "obliviate", "peskipiksipesternomi"
                         "rictusempra", "serpensortia", "tarantallegra"]
        assertEqual(len(expected_list), len(returned_list))
        #Test if each spell in expected_list is contained in string from server.
        for spell in expected_list:
            self.assertTrue(spell in string_from_server)

    #Tests our input: spell, output: purpose/type api query on the spell flagrate
    def spellToDefinition1(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spells/flagrate/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_dict = json.loads(string_from_server)[0]
        returned_type_id = spell_dict["type_id"]
        returned_purpose = spell_dict["spell_effect"]
        expected_type_id = 2
        expected_purpose = "Allows user to write on objects"
        self.assertEqual(returned_type_id, expected_type_id)
        self.assertEqual(returned_purpose, expected_purpose)

    # Tests our input: spell, output: purpose/type api query on the spell eat_slugs
    # Tests an edge case because this is the last spell in the CSV file
    def spellToDefinition2(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spells/eat_slugs/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_dict = json.loads(string_from_server)[0]
        returned_type_id = spell_dict["type_id"]
        returned_purpose = spell_dict["spell_effect"]
        expected_type_id = 3
        expected_purpose = "Makes the opponent barf slugs"
        self.assertEqual(returned_type_id, expected_type_id)
        self.assertEqual(returned_purpose, expected_purpose)

    # Tests our api query of input:spell, output: int of how many times the spell was used, with the spell impedimenta
    def spellToNum1(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spell_count/impedimenta"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        expected_value = 13
        self.assertEqual(spell_num, expected_value)

    # Tests our api query of input:spell, output: int of how many times the spell was used, with the spell anapneo
    # Tests an edge case because anapneo is only used once
    def spellToNum2(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spell_count/anapneo"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 1
        self.assertEqual(spell_num, expected_value)

    # Tests our api query of input:spell, output: int of how many times the spell was used, with the spell accio
    # Tests an edge case because accio is the most frequently used spell.
    def spellToNum3(self):
        url = "http://thacker.mathcs.carleton.edu:5208/spell_count/accio"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 34
        self.assertEqual(spell_num, expected_value)

    #Tests our input:spell, output: which characters use that spell api query. We test on the spell Accio
    def spellToCharacters1(self):
        url = "http://thacker.mathcs.carleton.edu:5208/characters_by_spell/accio/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        expected_char_list = ["Harry Potter", "Molly Weasley", "Fred Weasley", "George Weasley", "Sirius Black",
                              "Bellatrix Lestrange", "Ron Weasley", "Hermione Granger", "Filius Flitwick"]
        self.assertEqual(len(char_list), len(expected_char_list))
        for char in expected_char_list:
            self.assertTrue(char in string_from_server)

    #Tests our input:spell, output: which characters use that spell api query on a spell where there was no known caster
    def spellToCharacters2(self):
        url = "http://thacker.mathcs.carleton.edu:5208/characters_by_spell/conjunctivitis/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        self.assertTrue(len(char_list) == 0)

    #Tests our input: spell, ourput: which characters use that spell api query on anapneo
    #Edge case because only one caster - Horace Slughorn
    def spellToCharacters3(self):
        url = "http://thacker.mathcs.carleton.edu:5208/characters_by_spell/anapneo/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        self.assertTrue(len(char_list) == 1)
        expected_char = "Horace Slughorn"
        self.assertTrue(expected_char in string_from_server)

    # tests our api query that takes in a spell and a book and gives the number of occurrences of that spell in that book
    # We test on the Goblet of Fire with the spell Lumos
    def spellAndBookToNum1(self):
        url = "http://thacker.mathcs.carleton.edu:5208/books_spell_count/Goblet_of_Fire/Lumos"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        # we should check to see that this is actually an int
        spell_num = json.loads(string_from_server)
        expected_value = 4
        self.assertEqual(spell_num, expected_value)

    # tests our api query that takes in a spell and a book and gives the number of occurrences of that spell in that book
    # We test on the Prisoner of Azkaban with the spell tarantallegra.
    # Tests an edge case because tarantallegra is not used in this book
    def spellAndBookToNum2(self):
        url = "http://thacker.mathcs.carleton.edu:5208/books_spell_count/Prisoned_of_Azkaban/tarantallegra/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 0
        self.assertEqual(spell_num, expected_value)

    # tests our api query that takes in a spell and a book and gives the number of occurrences of that spell in that book
    # We test on the Deathly Hallows with the spell engorgio.
    # Tests an edge case because engorigio is used only once in this book
    def spellAndBookToNum3(self):
        url = "http://thacker.mathcs.carleton.edu:5208/books_spell_count/Deathly_Hallows/engorgio"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 1
        self.assertEqual(spell_num, expected_value)

    # tests our api query that takes in a spell and a book and gives the number of occurrences of that spell in that book
    # We test on the Prisoner of Azkaban with the spell expecto patronum.
    # Tests an edge case because expecto patronum is used frequently in this book
    def spellAndBookToNum4(self):
        url = "http://thacker.mathcs.carleton.edu:5208/books_spell_count/Prisoner_of_Azkaband/expecto_patronum/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 23
        self.assertEqual(spell_num, expected_value)

if __name__ == '__main__':
    unittest.main()



