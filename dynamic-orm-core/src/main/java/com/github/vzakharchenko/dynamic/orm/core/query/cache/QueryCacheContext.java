package com.github.vzakharchenko.dynamic.orm.core.query.cache;

import com.google.common.collect.ImmutableList;
import com.querydsl.sql.RelationalPath;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

/**
 *
 */
public class QueryCacheContext implements Cloneable, CacheSupport<QueryCacheContext> {
    private Collection<RelationalPath> qRelatedTables = new ArrayList<>();

    public QueryCacheContext() {
    }

    @Override
    public QueryCacheContext registerRelatedTables(RelationalPath... qTables) {
        return registerRelatedTables(Arrays.asList(qTables));
    }

    @Override
    public QueryCacheContext registerRelatedTables(Collection<RelationalPath> qTables) {
        qRelatedTables.addAll(qTables);
        return this;
    }

    public Collection<RelationalPath> getqRelatedTables() {
        return ImmutableList.copyOf(qRelatedTables);
    }

    @Override
    protected QueryCacheContext clone() {
        try {
            QueryCacheContext queryCacheContext = (QueryCacheContext) super.clone();
            queryCacheContext.qRelatedTables = new ArrayList<>();
            return queryCacheContext;
        } catch (CloneNotSupportedException e) {
            throw new IllegalStateException(e);
        }
    }
}
