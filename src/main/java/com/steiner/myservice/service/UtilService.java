package com.steiner.myservice.service;

import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class UtilService {

    private final Logger log = LoggerFactory.getLogger(UtilService.class);

    public String cleanString(String mystring) {

        mystring = mystring.toLowerCase().trim();

        //Remove some common contractions 
        //Cases with 'd e 's are not removed and will be contracted without ' symbol
        mystring = mystring.replaceAll("can't", "can not");
        mystring = mystring.replaceAll("n't", " not");
        mystring = mystring.replaceAll("'re", " are");
        mystring = mystring.replaceAll("'m", " am");
        mystring = mystring.replaceAll("'ll", " will");
        mystring = mystring.replaceAll("'ve", " have");
        mystring = mystring.replaceAll("\\.", " ");
        mystring = mystring.replaceAll(",", " ");
        mystring = mystring.replaceAll("\\?", " ");
        mystring = mystring.replaceAll("!", " ");

        //Remove all char not in this range. Replace with no space. There will be some contractions
        mystring = mystring.replaceAll("[^a-zA-Z|\\s]", "");
        mystring = mystring.replaceAll("[\\t]", "");

        //Remove some  contractions
        mystring = mystring.replaceAll("didnt", "did not");
        mystring = mystring.replaceAll("doesnt", "does not");
        mystring = mystring.replaceAll("wasnt", "was not");
        mystring = mystring.replaceAll("havent", "have not");
        mystring = mystring.replaceAll("wouldnt", "would not");
        mystring = mystring.replaceAll("couldnt", "could not");
        mystring = mystring.replaceAll("shouldnt", "should not");

        //use some regex to replace with safety
        mystring = mystring.replaceAll("^.?cant$", "can not");
        mystring = mystring.replaceAll("^.?its$", "it is");
        mystring = mystring.replaceAll("^.?thats$", "that is");

        //Remove urls
        mystring = mystring.replaceAll("\\\\ (?:(?:https?):\\\\/\\\\/www\\\\.\\\\.*?.*?) ", " ");

        //Remove multiple white spaces
        mystring = mystring.trim().replaceAll(" +", " ");

        return mystring;
    }

    public Map<String, Integer> CountWords(String cleanreviewstring) {

        Map<String, Integer> myMap = new HashMap<>();

        if (cleanreviewstring != null) {
            cleanreviewstring = cleanreviewstring.trim();
            String[] tokens = cleanreviewstring.split(" ");
            for (String word : tokens) {
                if (myMap.containsKey(word)) {
                    int count = myMap.get(word.trim());
                    myMap.put(word.trim(), count + 1);
                } else {
                    myMap.put(word.trim(), 1);
                }
            }
        } else {

            myMap = null;

        }
        return myMap;
    }

}
