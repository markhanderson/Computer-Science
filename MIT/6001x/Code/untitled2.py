#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Mon Sep 14 16:53:13 2020

@author: markanderson
"""


def iterPower(base, exp):
    result = 1
    for i in range(0, exp):
        result *= base
    return result



def recurPower(base, exp):
    if exp <= 0:
        return 1

    return base * recurPower(base, exp - 1)