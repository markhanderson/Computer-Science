//
//  Problem_2.h
//  Assignment 1
//
//  Created by marka2 on 1/23/26.
//

cppvoid CountLetters(string filename) {
    Vector<int> counts(26, 0);  // 26 letters, all initialized to 0
    
    ifstream input;
    input.open(filename.c_str());
    
    char ch;
    while (input.get(ch)) {
        // Convert to lowercase
        ch = tolower(ch);
        
        // Check if it's a letter
        if (ch >= 'a' && ch <= 'z') {
            int index = ch - 'a';  // 'a' maps to 0, 'b' to 1, etc.
            counts[index]++;
        }
    }
    
    input.close();
    
    // Print results
    for (int i = 0; i < 26; i++) {
        char letter = 'a' + i;
        cout << letter << ": " << counts[i] << endl;
    }
}
