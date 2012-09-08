package com.welocally.gdrive.dao;

import java.util.List;

import com.welocally.gdrive.domain.Search;
import com.welocally.gdrive.security.UserPrincipal;

public interface SearchDao extends BaseDao<Search> {

    public abstract List<Search> findByQuery(String query);

    public abstract List<Search> findByOwner(UserPrincipal up);

    public abstract Search findByIndexId(String indexId);

}