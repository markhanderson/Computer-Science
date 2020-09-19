#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Wed Sep  9 15:25:20 2020

@author: markanderson
"""


high = 100
low = 0
x = 'a'


print("Please think of a number between 0 and 100!")


while  x != 'c':
    guess = (low + high)//2
    print("Is your secret number " + str(guess) + "?")
    x = str(input("Enter 'h' to indicate the guess is too high. Enter 'l' to indicate the guess is too low. Enter 'c' to indicate I guessed correctly.: "))
    
    if x == 'h':
        high = guess

    elif x == 'l':
        low = guess

    elif x == 'c':
        break

    else:
        print("Sorry, I did not understand your input")

if x == 'c':
    print("Game over. Your secret number was:", int(guess))



# high = 100
# low = 0
# x = 'a'


# print("Please think of a number between 0 and 100!")


# while  x != 'c':
#     guess = (low + high)//2
#     print("Is your secret number " + str(guess) + "?") 
#     print("Enter 'h' to indicate the guess is too high. Enter 'l' to indicate the guess is too low. Enter 'c' to indicate I guessed correctly.")
#     x = str(input())

#     if x == 'h':
#         high = guess

#     elif x == 'l':
#         low = guess

#     elif x == 'c':
#         break

#     else:
#         print("Sorry, I did not understand your input")

# if x == 'c':
#     print("Game over. Your secret number was:", int(guess))




# low = 0
# high = 100
# ans = (low + high)//2
# print("Please think of a number between 0 and 100!")
# print("is your secret number" + str(ans) + "?")
# usr = input("Enter 'h' to indicate the guess is too high. Enter 'l' to indicate the guess is too low. Enter 'c' to indicate I guessed correctly.")


# while usr == 'c':
#     if usr == 'h':
#         high = ans
#     if usr == 'I':
#         low = ans()
#     else:
#         print("Sorry, I did not understand your imput.")
#     ans = (low+high)//2
#     print("is your secret number" + str(ans) + "?")
#     usr = input("Enter 'h' to indicate the guess is too high. Enter 'I' to indicate the guess is too low. Enter 'c' to indicate I guessed correctly.")
# print("Game over. Your secret nu8mber was:", ans)


# low_value = 0.0 #Starting variables
# high_value = 100
# guess = (high_value + low_value)/2.0

# print ("Please think of a number between 0 and 100!") #Asking user for input
# number = input("Enter your number:")

# if (number < 0) or (number > 100):  #Ensure starting number is valid
#     print("Please enter a valid starting number")

# checking = True
# while checking == True:
#     print('Is your secret number ' + str(guess) + '?') #first guess will start at 50
#     answer = input("Enter h to indicate the guess is too high. Enter l to indicate the guess is too low.\
#  Enter c to indicate I guessed correctly:")
#     if answer == 'h':
#         high_value = guess #Setting the new high value
#         guess = (low_value + guess)/2 #halving and guessing again

#     elif answer == 'l':
#         low_value = guess #setting the new low value
#         guess = (high_value + guess)/2 #halving and guessing again

#     elif answer == 'c':   #Check to see if guess is correct
#         checking = False

#     else:
#         print("Please enter a valid answer.")
# print("Game Over. Your number was " + str(guess)) #Printing the final guess for the user