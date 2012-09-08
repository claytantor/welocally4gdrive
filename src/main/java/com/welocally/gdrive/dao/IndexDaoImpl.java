
package com.welocally.gdrive.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.security.UserPrincipal;

@Repository
public class IndexDaoImpl 
	extends AbstractDao<Index> 
	implements IndexDao {

	static Logger logger = Logger.getLogger(IndexDaoImpl.class);
      
	public IndexDaoImpl() {
		super(Index.class);
	}

    
    public List<Index> findByKey(String key) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Index.class.getName()+
                "as e where e.key = :key");
        q.setString("key", key);
        return (List<Index>)q.list();
    }

    
    public List<Index> findByOwner(UserPrincipal up) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Index.class.getName()+
                " as e where e.owner.id = :ownerId");
        q.setLong("ownerId", up.getId());
        return (List<Index>)q.list();
    }

    
    public Index findByFeedUrl(String url) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Index.class.getName()+
                " as e where e.worksheetFeed = :url");
        q.setString("url", url);
        return (Index)q.uniqueResult();
    }

    
    public Index findByIndexId(String indexId) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Index.class.getName()+
                " as e where indexId = :indexId");
        q.setString("indexId", indexId);
        return (Index)q.uniqueResult();
    }
    
    
	  
}
