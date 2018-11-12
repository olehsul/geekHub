package com.owu.geekhub.service.generators;

import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class RandomUserIdentity {
    public Long createRandomUserIdentity(){
        int aStart =10000000;
        long aEnd = 99999999L;
        Random aRandom = new Random();
        if ( aStart > aEnd ) {
            throw new IllegalArgumentException("Start cannot exceed End.");
        }
        //get the range, casting to long to avoid overflow problems
        long range = aEnd - (long)aStart + 1;
        // compute a fraction of the range, 0 <= frac < range
        long fraction = (long)(range * aRandom.nextDouble());
        long randomNumber =  fraction + (long)aStart;
        return randomNumber;


    }
}