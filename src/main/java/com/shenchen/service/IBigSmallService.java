package com.shenchen.service;

import java.util.List;
import java.util.Map;

public interface IBigSmallService {

    void analyseBigSmall();

    void analyseBigSmall(String league_name_simply, boolean reverse, double hostGet, double hostLost, double guestGet, double guestLost);

    List<Map<String, Object>> analyseBigSmall(String league_name_simply);

    List<Map<String, Object>> analyseBigSmallEfficient(String league_name_simply, boolean reverse);
}