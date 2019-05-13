package com.shenchen.dao;

import com.shenchen.model.GameBaseData;

import java.util.List;

public interface IGameBaseDao {

    Integer insertGameBaseData(GameBaseData gameBaseData);

    Integer deleteAllGameBaseData();

    List<GameBaseData> queryHostGameBaseData(GameBaseData gameBaseData);

    List<GameBaseData> queryGuestGameBaseData(GameBaseData gameBaseData);
}