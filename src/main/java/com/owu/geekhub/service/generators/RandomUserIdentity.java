package com.owu.geekhub.service.generators;

import com.owu.geekhub.dao.UserDao;
import com.owu.geekhub.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Random;
@Service
public class RandomUserIdentity {
    @Autowired
    private UserDao userDao;

    public Long createRandomUserIdentity(){
        long aStart =10000000L;
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

    public void setRandomId(User user){
        Long randomIdentity;
        boolean identityAlreadyExist = false;
        do{
            randomIdentity = createRandomUserIdentity();
            if (!userDao.existsDistinctById(randomIdentity)){
                user.setId(randomIdentity);
            }
        }while (identityAlreadyExist);

    }
}