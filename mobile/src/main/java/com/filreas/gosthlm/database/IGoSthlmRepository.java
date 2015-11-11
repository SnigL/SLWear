package com.filreas.gosthlm.database;

import com.filreas.gosthlm.database.model.TransportationOfChoice;

public interface IGoSthlmRepository {
    void insertTransportationOfChoice(TransportationOfChoice transportationOfChoice);

    void updateTransportationOfChoice(TransportationOfChoice transportationOfChoice);

    TransportationOfChoice getTransportationOfChoice();
}
