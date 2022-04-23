package maqplan.com.observer;

import java.util.List;

import indoortec.com.entity.PlayList;

public interface Execute {
    void execute(Observer<List<PlayList>> observer);
}
