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
    def testCharToSpells(self):
        # Should change spell_char in url to just characters?
        url = "http://standardbookofspells.com/spell_char/Ron_Weasley/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ["accio", "caveinimicum", "expelliarmus", "relashio", "riddikulus", "silencio", "tergeo",
                         "wingardiumleviosa"]
        #Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        #If so, we can do this
        self.assertEqual(spell_list.size(), expected_list.size())
        for spell in spell_list:
            self.assertTrue(expected_list.contains(spell))

    #Tests whether our input: book, get: list of spells api query works with Deathly Hallows
    def bookToSpells(self):
        #Should change spell_book in url to just book?
        url = 'http://standardbookofspells.com/spell_book/Deathly_Hallows/'
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
        self.assertEqual(spell_list.size(), expected_list.size())
        for spell in spell_list:
            self.assertTrue(expected_list.contains(spell))


if __name__ == '__main__':
    unittest.main()
