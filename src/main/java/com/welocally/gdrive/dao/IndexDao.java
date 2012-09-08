package com.welocally.gdrive.dao;

import java.util.List;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.security.UserPrincipal;

public interface IndexDao extends BaseDao<Index> {
    public List<Index> findByKey(String key);
    public List<Index> findByOwner(UserPrincipal up);
    public Index findByFeedUrl(String url);
    public Index findByIndexId(String indexId);
}
