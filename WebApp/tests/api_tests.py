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
        url = "http://standardbookofspells.com/instances/Ron_Weasley/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["accio", "caveinimicum", "expelliarmus", "relashio", "riddikulus", "silencio", "tergeo",
                         "wingardiumleviosa"]
        #Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        #If so, we can do this
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in spell_list:
            self.assertTrue(spell in expected_list)

    # Tests to see if our input: characters, get: list of spells api query works with Ludo Bagman
    #Interesting case because he does not cast many spells - but casts the same spell several times
    #Want to make sure our list does not return repeats
    def testCharToSpells2(self):
        url = "http://standardbookofspells.com/instances/Ludo_Bagman/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["quietus", "sonorus"]
        # Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        # If so, we can do this
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in spell_list:
            self.assertTrue(spell in expected_list)

    #Tests our characters to list of spells api query on a chacater who only casted one spell, one time - Parvati Patil
    def testCharToSpells3(self):
        url = "http://standardbookofspells.com/instances/Parvati_Patil/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["riddikulus"]
        self.assertEqual(spell_list, expected_list)


    #Tests whether our input: book, get: list of spells api query works with Deathly Hallows
    def bookToSpells1(self):
        url = 'http://standardbookofspells.com/instances/Deathly_Hallows/'
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["avadakedavra", "caveinimicum", "confringo", "confundus", "crucio", "deprimo", "diffindo", "duro",
                         "engorgio", "erecto", "expectopatronum", "expelliarmus", "expulso", "fidelius","geminio", "glisseo",
                         "homenumrevelio", "impedimienta", "imperio", "impervius", "levicorpus", "liberacorpus", "lumos",
                         "meteolojinxrecanto", "muffliato", "nox", "obliviate", "obscuro", "petrificustotalus",
                         "piertotumlocomotor", "protego", "protegototalum", "reducio", "relashio", "reparo", "repellomuggletum",
                         "salviohexia", "sectumsepra", "stuepfy", "tergeo", "wingardiumleviosa"]
        # Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        # If so, we can do this
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in spell_list:
            self.assertTrue(spell in expected_list)

    # Tests whether our "input: book, get: list of spells" api query works with Chamber of Secrets
    def bookToSpells2(self):
        url = 'http://standardbookofspells.com/instances/Chamber_of_Secrets/'
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["expelliarmus", "aparecium", "eatslugs", "homorphus", "lumos", "obliviate", "peskipiksipesternomi"
                         "rictusempra", "serpensortia", "tarantallegra"]
        # Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        # If so, we can do this
        self.assertEqual(len(spell_list), len(expected_list))
        for spell in spell_list:
            self.assertTrue(spell in expected_list)

    #Tests our input: spell, output: purpose/type api query on the spell flagrate
    def spellToDefinition1(self):
        url = "http://standardbookofspells.com/spells/flagrate/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        #we should check to see that this is actually a dictionary
        spell_dict = json.loads(string_from_server)
        returned_type = spell_dict["type"]
        returned_purpose = spell_dict["purpose"]
        expected_type = "spell"
        expected_purpose = "Allows user to write on objects"
        self.assertEqual(returned_type, expected_type)
        self.assertEqual(returned_purpose, expected_purpose)

    # Tests our input: spell, output: purpose/type api query on the spell eat_slugs
    # Tests an edge case because this is the last spell in the CSV file
    def spellToDefinition2(self):
        url = "http://standardbookofspells.com/spell_names/spells/eat_slugs/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        # we should check to see that this is actually a dictionary
        spell_dict = json.loads(string_from_server)
        returned_type = spell_dict["type"]
        returned_purpose = spell_dict["purpose"]
        expected_type = "curse"
        expected_purpose = "Makes the opponent barf slugs"
        self.assertEqual(returned_type, expected_type)
        self.assertEqual(returned_purpose, expected_purpose)

    # Tests our api query of input:spell, output: int of how many times the spell was used, with the spell impedimenta
    def spellToNum1(self):
        url = "http://standardbookofspells.com/instances/impedimenta"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        #we should check to see that this is actually an int
        spell_num = json.loads(string_from_server)
        expected_value = 13
        self.assertEqual(spell_num, expected_value)

    # Tests our api query of input:spell, output: int of how many times the spell was used, with the spell anapneo
    # Tests an edge case because anapneo is only used once
    def spellToNum2(self):
        url = "http://standardbookofspells.com/spells/instances/anapneo"
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
        url = "http://standardbookofspells.com/spells/instances/accio"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 34
        self.assertEqual(spell_num, expected_value)

    #Tests our input:spell, output: which characters use that spell api query. We test on the spell Accio
    def spellToCharacters1(self):
        url = "http://standardbookofspells.com/instances/accio/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        expected_char_list = ["Harry Potter", "Molly Weasley", "Fred Weasley", "George Weasley", "Sirius Black",
                              "Bellatrix Lestrange", "Ron Weasley", "Hermione Granger", "Filius Flitwick"]
        self.assertEqual(char_list, expected_char_list)
        self.assertEqual(len(char_list), len(expected_char_list))
        for char in char_list:
            self.assertTrue(char in expected_char_list)

    #Tests our input:spell, output: which characters use that spell api query on a spell where there was no known caster
    def spellToCharacters2(self):
        url = "http://standardbookofspells.com/instances/conjunctivitis/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        self.assertTrue(len(char_list) == 0)

    #Tests our input: spell, ourput: which characters use that spell api query on anapneo
    #Edge case because only one caster - Horace Slughorn
    def spellToCharacters3(self):
        url = "http://standardbookofspells.com/instances/anapneo/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        char_list = json.loads(string_from_server)
        self.assertTrue(len(char_list) == 1)
        expected_char = "Horace Slughorn"
        self.assertEqual(char_list[1], expected_char)

    # tests our api query that takes in a spell and a book and gives the number of occurrences of that spell in that book
    # We test on the Goblet of Fire with the spell Lumos
    def spellAndBookToNum1(self):
        url = "standardbookofspells.com/instances/Lumos/Goblet_of_Fire/"
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
        url = "standardbookofspells.com/spells/instances/tarantallegra/3/"
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
        url = "standardbookofspells.com/spells/instances/engorgio/7/"
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
        url = "standardbookofspells.com/spells/instances/expecto_patronum/3/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_num = json.loads(string_from_server)
        if type(spell_num) != type(0):
            spell_num = int(spell_num)
        expected_value = 23
        self.assertEqual(spell_num, expected_value)









if __name__ == '__main__':
    unittest.main()



