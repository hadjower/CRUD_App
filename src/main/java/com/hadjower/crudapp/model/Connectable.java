package com.hadjower.crudapp.model;

import java.sql.*;


public interface Connectable {
    void connect();
    void closeConnection();
}
