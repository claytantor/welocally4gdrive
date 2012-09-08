package com.welocally.gdrive.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.welocally.gdrive.dao.IndexDao;
import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.security.UserPrincipal;

@Service
@Transactional
public class IndexService {
    
    @Autowired IndexDao indexDao;
    
    public List<Index> findAllByOwner(UserPrincipal up){
        return indexDao.findByOwner(up);
    }
    
    public Index findByWorksheetFeed(String feedUrl){
        return indexDao.findByFeedUrl(feedUrl);
    }
    
    public Index findByIndexId(String indexId){
        return indexDao.findByIndexId(indexId);
    }
    
    public Long save(Index e){
        indexDao.save(e);
        return e.getId();
    }
    
    public void delete(Index e){
        indexDao.delete(e);
    }

}
