package com.krealll.SpaceY.service;

import com.krealll.SpaceY.model.DtoParameters;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryValidator {

    private final static String NUM_PATTERN = "^[1-9][0-9]*|^0";
    private final static String STATUS_PATTERN = "ACTIVE|BLOCKED";
    private final static String USERNAME_PATTER = "[.]+"; //TODO username pattern
    public static boolean validateNum(String value){
        Pattern pattern = Pattern.compile(NUM_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateStatus(String value){
        Pattern pattern = Pattern.compile(STATUS_PATTERN);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateUsername(String value){
        Pattern pattern = Pattern.compile(USERNAME_PATTER);
        Matcher matcher = pattern.matcher(value);
        return matcher.matches();
    }

    public static boolean validateQuery(Map<String,Object> query, Map<String,Object> options){
        boolean result = true;
        for (Map.Entry<String, Object> entry : query.entrySet()) {
            result = validateNum((String) entry.getValue());
            if(!result){
                return false;
            }
        }
        if(options.containsKey(DtoParameters.STATUS)){
            String status = (String)options.get(DtoParameters.STATUS);
            result = validateStatus(status);
            if(result){
                if(options.containsKey(DtoParameters.USERNAME)){
                    String name = (String)options.get(DtoParameters.USERNAME);
                    result = validateUsername(name);
                    return result;
                }
                return result;
            }
            return false;
        }
        return result;
    }

}
