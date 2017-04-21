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

    def tearDown(self):

    '''Tests to see if our input characters, get spells api query works
    with Ron Weasley'''
    def testCharToSpells(self):
        url = "http://standardbookofspells.com/spell_char/Ron_Weasley/"
        data_from_api = urllib.urlopen(url).read()
        string_from_server = data_from_api.decode('utf-8')
        spell_list = json.loads(string_from_server)
        expected_list = ['accio", "caveinimicum", "expelliarmus", "relashio", "riddikulus", "silencio", "tergeo", "wingardiumleviosa"]
        #Is object stuff going to matter?
        self.assertEqual(spell_list, expected_list)
        #If so, we can do this
        self.assertEqual(spell_list.size(), expected_list.size())
        for spell in spell_list:
            self.assertTrue(expected_list.contains(spell))

if _name_ == '_main_':
    unittest.main()
