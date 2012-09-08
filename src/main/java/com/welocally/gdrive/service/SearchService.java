package com.welocally.gdrive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welocally.gdrive.dao.SearchDao;
import com.welocally.gdrive.domain.Search;
import com.welocally.gdrive.security.UserPrincipal;

@Service
@Transactional
public class SearchService {
    
    @Autowired SearchDao searchDao;
    
    public List<Search> findAllByOwner(UserPrincipal up){
        return searchDao.findByOwner(up);
    }
    
    public Search findById(Long id){
        return searchDao.findByPrimaryKey(id);
    }
    
    
    public Long save(Search e){
        searchDao.save(e);
        return e.getId();
    }

}
