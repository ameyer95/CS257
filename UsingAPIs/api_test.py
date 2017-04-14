#!/usr/bin/env python3
'''
    api_test.py
    Anna Meyer and Patty Commins, 14 April 2017

    An example for CS 257 Software Design. How to retrieve results
    from an HTTP-based API, parse the results (JSON in this case),
    and manage the potential errors.
'''

import sys
import argparse
import json
import urllib

def get_recipes(keywords):
    '''
    Returns a list of the 30 highest rated recipes that use the specified
    ingredient. The recipes are represented as
    dictionaries of the form
    
       {'title':name, 'source_url':url, 'recipe_id':idNum}

    For example, the results for get_recipes('chocolate')
    would include:

       [{'title':'Cookie Monster cupcakes', 'source_url':
       'http://www.bbcgoodfood.com/recipes/873655/cookie-monster-cupcakes',
       'recipe_id':'9089e3'}, {'title':'Guinness Chocolate Cheesecake', 
       'source_url':'http://www.closetcooking.com/2011/03/guinness-chocolate-cheesecake.html', 
       'recipe_id':35354}]

    The keyword must be one of the approximately 40 choices listed in the 
    user statement.

    Raises exceptions on network connection errors and on data
    format errors.
    '''
    base_url = 'http://food2fork.com/api/search?key=f346852b9502a7f9d891ca17d0b51eb0&q={0}'
    url = base_url.format(keywords)
    
    # We use cutoff_index and cleaner_string to remove the non-recipe 
    # portion of the string returned by the API
    data_from_server = urllib.urlopen(url).read()
    string_from_server = data_from_server.decode('utf-8')
    cutoff_index = string_from_server.find("[")
    cleaner_string = string_from_server[cutoff_index:len(string_from_server)-1]
    recipe_list = json.loads(cleaner_string)
    result_list = []
    for recipe_dictionary in recipe_list:
        name = recipe_dictionary['title']
        url = recipe_dictionary['source_url']
        idNum = recipe_dictionary['recipe_id']
        if type(name) != type(''):
            name = name.encode('utf-8')
            if type(name) != type(''):
                raise Exception('recipe name has wrong type: "{0}"'.format(name))
        if type(url) != type(''):
            url = url.encode('utf-8')
            if type(url) != type(''):
                raise Exception('url has wrong type: "{0}"'.format(url))
        if type(idNum) != type(''):
            idNum = idNum.encode('utf-8')
            if type(idNum) != type(''):
                raise Exception('recipe id has wrong type: "{0}"'.format(idNum))
        result_list.append({'Recipe Name':name, 'Link to Recipe':url, 'Recipe ID':idNum})
    return result_list

def get_recipe_details(keywords):
    '''
    Returns a details about the single highest rated recipe that 
    uses the specified ingredient. The recipe is represented as
    a single-element dictionary of the form
    
       {'name':name, 'publisher':publisher, 'source_url':url, 
       'imageUrl':image_url, 'social_rank':rating}

    For example, the result for get_recipe_details('chocolate')
    would be:

       {'title':'Cookie Monster cupcakes', 'publisher':'BBC Good Food', 
       'source_url': 'http://www.bbcgoodfood.com/recipes/873655/cookie-monster-cupcakes',
       'imageUrl':'http://static.food2fork.com/604133_mediumd392.jpg',
       'social_rank':'100.0'}

    The keyword must be one of the approximately 40 choices listed in the 
    user statement.

    Raises exceptions on network connection errors and on data
    format errors.
    '''
    base_url = 'http://food2fork.com/api/search?key=f346852b9502a7f9d891ca17d0b51eb0&q={0}'
    url = base_url.format(keywords)

    data_from_server = urllib.urlopen(url).read()
    string_from_server = data_from_server.decode('utf-8')
    cutoff_index = string_from_server.find("[")
    cleaner_string = string_from_server[cutoff_index:len(string_from_server)-1]
    recipe_list = json.loads(cleaner_string)

    result_list = []
    name = recipe_list[0].get('title')
    publisher = recipe_list[0].get('publisher')
    url = recipe_list[0].get('source_url')
    imageUrl = recipe_list[0].get('image_url')
    rating = recipe_list[0].get('social_rank')
    
    if type(name) != type(''):
        name = name.encode('utf-8')
        if type(name) != type(''):
            raise Exception('name has wrong type: "{0}"'.format(name))
    if type(publisher) != type(''):
        publisher = publisher.encode('utf-8')
        if type(publisher) != type(''):
            raise Exception('publisher has wrong type: "{0}"'.format(publisher))
    if type(url) != type(''):
        url = url.encode('utf-8')
        if type(url) != type(''):
            raise Exception('url has wrong type: "{0}"'.format(url))
    if type(imageUrl) != type(''):
        imageUrl = imageUrl.encode('utf-8')
        if type(imageUrl) != type(''):
            raise Exception('imageUrl has wrong type: "{0}"'.format(imageUrl))
    if type(rating) != type(''):
        rating = str(rating)
        if type(rating) != type(''):
            raise Exception('rating has wrong type: "{0}"'.format(rating))
    result_list.append({'Recipe Name':name, 'Publisher':publisher, 'Link to Recipe':url, 'Link to Photo':imageUrl, 'User Rating':rating})

    return result_list

def main(args):
    if args.action == 'list':
        recipe_ingredients = get_recipes(args.keywords)
        for recipe_ingredient in recipe_ingredients:
            name = recipe_ingredient['Recipe Name']
            url = recipe_ingredient['Link to Recipe']
            idNum = recipe_ingredient['Recipe ID']
            print('{0} {1} {2}'.format(name, url, idNum))
            
    elif args.action == 'details':
        recipes = get_recipe_details(args.keywords)
        for recipe in recipes:
            name = recipe['Recipe Name']
            publisher = recipe['Publisher']
            url = recipe['Link to Recipe']
            imageUrl = recipe['Link to Photo']
            rating = recipe['User Rating']
            print('Name: {0} \n Recipe source: {1} \n Recipe URL: {2} \n Link to image: {3} \n Recipe Rating (100=max): {4}'.format(name, publisher, url, imageUrl, rating))

if __name__ == '__main__':
    # When I use argparse to parse my command line, I usually
    # put the argparse setup here in the global code, and then
    # call a function called main to do the actual work of
    # the program.
    parser = argparse.ArgumentParser(description='Get keyword info from the Recipe API')

    parser.add_argument('action',
                        metavar='action',
                        help='action to perform a recipe search ("list" or "details")',
                        choices=['list', 'details'])

    parser.add_argument('keywords',
                        metavar='keywords',
                        help='the recipe ingredient you want to use. You can choose from almond, avocado, banana, blueberry, bread, broccoli, brownies, burger, burrito, cake, caramel, carrot, casserole, cheese, chicken, chickpea, chocolate, coconut, cupcake, curry, egg, fish, lasagna, lentil, muffin, mushroom, nutella, pasta, pie, pizza, potato, pumpkin, quinoa, raspberry, rice, risotto, salad, soup, spinach, squash, strawberry, taco, tofu, tomato, vegetable, watermelon',
                        choices=['chocolate', 'strawberry', 'cheese', 'cake', 'brownies', 'coconut', 'caramel',
                                'nutella', 'banana', 'tomato', 'avocado', 'egg', 'bread', 'soup', 'vegetable',
                                'pasta', 'pizza', 'cupcake', 'potato', 'risotto', 'rice', 'taco', 'burrito', 
                                'lasagna', 'casserole', 'salad', 'spinach', 'broccoli', 'almond', 'chicken', 'tofu',
                                'burger', 'chickpea', 'lentil', 'curry', 'cake', 'fish', 'muffin', 'pie', 'blueberry',
                                'raspberry', 'pumpkin', 'squash', 'watermelon', 'mushroom', 'quinoa', 'carrot'])

    args = parser.parse_args()
    main(args)
