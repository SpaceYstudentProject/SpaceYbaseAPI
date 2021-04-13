package com.krealll.SpaceY.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserQueryDTO {

    Map<String,Object> query;
    Map<String,Object> options;

}
