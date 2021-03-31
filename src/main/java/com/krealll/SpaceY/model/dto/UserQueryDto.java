package com.krealll.SpaceY.model.dto;

import lombok.Data;

import java.util.Map;

@Data
public class UserQueryDto {

    Map<String,Object> query;
    Map<String,Object> options;

}
