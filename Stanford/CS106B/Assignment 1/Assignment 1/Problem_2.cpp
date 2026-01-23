//
//  Problem_2.cpp
//  Assignment 1
//
//  Created by marka2 on 1/23/26.
//

cppstruct statsT {
    double min;
    double max;
    double average;
};

statsT ReadScores(string filename) {
    ifstream input;
    input.open(filename.c_str());
    
    statsT stats;
    stats.min = 100;  // Start with max possible
    stats.max = 0;    // Start with min possible
    
    double sum = 0;
    int count = 0;
    int score;
    
    while (input >> score) {
        sum += score;
        count++;
        
        if (score < stats.min) {
            stats.min = score;
        }
        if (score > stats.max) {
            stats.max = score;
        }
    }
    
    stats.average = sum / count;
    input.close();
    
    return stats;
}
