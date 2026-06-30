package com.banking.dao;

import com.mongodb.client.MongoClient;
import com.mongodb.client.MongoClients;
import com.mongodb.client.MongoDatabase;

/**
 * Database connection utility for MongoDB.
 * Wraps MongoClient/MongoDatabase setup — same single-responsibility
 * pattern as a JDBC DBConnection, different client underneath.
 */
public class DBConnection {
    private static final String CONNECTION_STRING = "mongodb://localhost:27017";
    private static final String DATABASE_NAME = "bankingDb";

    private static MongoClient mongoClient;

    /**
     * Returns the shared MongoDatabase instance, creating the MongoClient
     * on first call (lazy initialization).
     */
    public static MongoDatabase getDatabase() {
        if (mongoClient == null) {
            mongoClient = MongoClients.create(CONNECTION_STRING);
        }
        return mongoClient.getDatabase(DATABASE_NAME);
    }

    /**
     * Closes the MongoClient and releases resources.
     * Call this on application shutdown.
     */
    public static void close() {
        if (mongoClient != null) {
            mongoClient.close();
            mongoClient = null;
        }
    }
}
