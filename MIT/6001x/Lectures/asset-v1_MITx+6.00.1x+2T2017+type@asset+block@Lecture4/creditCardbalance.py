#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
Created on Thu Sep 17 17:09:41 2020

@author: markanderson
"""


balance = float(input("balance: "))  

annualInterestRate = float(input("Annual Interest Rate: ")) 

monthlyPaymentRate = float(input ("Monthly Payment Rate: "))



monthlyInterestRate = float(annualInteresRate / 12) month = 0

while month < 12: month += 1 Minimummonthlypayment = balance * monthlyPaymentRate unpaidbalance = balance - Minimummonthlypayment Interest = unpaidbalance * monthlyInterestRate balance = unpaidbalance + Interest

print("Remaining balance: " + "%.2f" % (balance))