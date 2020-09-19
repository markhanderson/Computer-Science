#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Sep  7 13:21:48 2020

@author: markanderson
"""


# count = 0
# phrase = "hello, world"
# for iteration in range(5):
#     while True:
#         count += len(phrase)
#         break
#     print("Iteration " + str(iteration) + "; count is: " + str(count))
    
    
    
# count = 0
# phrase = "hello, world"
# for iteration in range(5):
#     count += len(phrase)
#     print("Iteration " + str(iteration) + "; count is: " + str(count))
    
    
    
s = 'azcbobobegghakl'

sub = "bob"

ln = len(sub)

print('Number of times bob occurs is:', + sum(sub == s[i:i+ln] for i in range(len(s)-(ln-1))))