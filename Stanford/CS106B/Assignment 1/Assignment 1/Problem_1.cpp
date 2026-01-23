//
//  Untitled.swift
//  Assignment 1
//
//  Created by marka2 on 1/23/26.
//

cpp

string CensorString1(string text, string remove) {
    string result = "";
    
    for (int i = 0; i < text.length(); i++) {
        bool shouldKeep = true;
        
        // Check if current character is in the remove string
        for (int j = 0; j < remove.length(); j++) {
            if (text[i] == remove[j]) {
                shouldKeep = false;
                break;
            }
        }
        
        if (shouldKeep) {
            result += text[i];
        }
    }
    
    return result;
}

cppvoid CensorString2(string & text, string remove) {
    int writePos = 0;
    
    for (int i = 0; i < text.length(); i++) {
        bool shouldKeep = true;
        
        for (int j = 0; j < remove.length(); j++) {
            if (text[i] == remove[j]) {
                shouldKeep = false;
                break;
            }
        }
        
        if (shouldKeep) {
            text[writePos] = text[i];
            writePos++;
        }
    }
    
    text.erase(writePos);  // Remove excess characters
}
