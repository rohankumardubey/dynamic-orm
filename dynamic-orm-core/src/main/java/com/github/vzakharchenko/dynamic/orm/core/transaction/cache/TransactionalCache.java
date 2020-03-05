package com.github.vzakharchenko.dynamic.orm.core.transaction.cache;

import com.github.vzakharchenko.dynamic.orm.core.cache.PrimaryKeyCacheKey;

import java.io.Serializable;
import java.util.Map;
import java.util.Set;

/**
 * Transaction Cache
 */
public interface TransactionalCache {

    <T> T getFromCache(Serializable key, Class<T> tClass);

    boolean isInCache(Serializable key);

    boolean isDeleted(Serializable key);

    void putToCache(Serializable key, Serializable value);

    void cacheEvict(Serializable key);

    void deleteModel(PrimaryKeyCacheKey key);

    void insertModel(PrimaryKeyCacheKey key);

    void updateModel(PrimaryKeyCacheKey key);

    Map<Serializable, Serializable> getInternalCache();

    Set<Serializable> getEvictObjects();

    Set<Serializable> getUpdatedObjects();

    Set<Serializable> getInsertedObjects();

    Set<Serializable> getDeletedObjects();

    <T> T getFromTargetCache(Serializable key, Class<T> tClass);

    void lock(Serializable key);

    void unLock(Serializable key);

    void clearAll();
}