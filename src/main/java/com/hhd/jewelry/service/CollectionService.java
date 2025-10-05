package com.hhd.jewelry.service;

import com.hhd.jewelry.entity.Collection;

import java.util.List;
import java.util.Optional;

public interface CollectionService {
    Collection getCollectionByName(String collectionName);
    List<Collection> getAllCollections();
    void save(Collection collection);
    void delete(Collection collection);
}
