package com.owu.geekhub.service.generators;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class RandomVerificationNumber {

    public int getRandomVerificationNumber(){
        int aStart =10000;
        int aEnd = 99999;
        Random aRandom = new Random();
        if ( aStart > aEnd ) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        int range = aEnd - (int) aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        int fraction = (int) (range * aRandom.nextDouble());
        int randomNumber =  fraction + (int) aStart;
        return randomNumber;
    }
    
}
