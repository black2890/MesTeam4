package com.mesproject.constant;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public enum OrdersStatus {
    PENDINGSTORAGE,
    STORAGECOMPLETED,
    PRODUCTIONCOMPLETED,
    RETRIEVALCOMPLETED,
    DELIVERED
}
