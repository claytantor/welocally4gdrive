
package com.welocally.gdrive.dao;

import java.util.List;

import org.apache.log4j.Logger;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;

import com.welocally.gdrive.domain.Search;
import com.welocally.gdrive.security.UserPrincipal;

@Repository
public class SearchDaoImpl 
	extends AbstractDao<Search> implements SearchDao {

	static Logger logger = Logger.getLogger(SearchDaoImpl.class);
      
	public SearchDaoImpl() {
		super(Search.class);
	}

    
    /* (non-Javadoc)
     * @see com.welocally.admin.dao.SeachDao#findByQuery(java.lang.String)
     */
    public List<Search> findByQuery(String query) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Search.class.getName()+
                "as e where e.query = :query");
        q.setString("query", query);
        return (List<Search>)q.list();
    }

    
    /* (non-Javadoc)
     * @see com.welocally.admin.dao.SeachDao#findByOwner(com.welocally.admin.security.UserPrincipal)
     */
    public List<Search> findByOwner(UserPrincipal up) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Search.class.getName()+
                " as e where e.index.owner.id = :ownerId");
        q.setLong("ownerId", up.getId());
        return (List<Search>)q.list();
    }

    
   
    
    /* (non-Javadoc)
     * @see com.welocally.admin.dao.SeachDao#findByIndexId(java.lang.String)
     */
    public Search findByIndexId(String indexId) {
        Query q = super.getCurrentSession().createQuery(
                "select e from "+Search.class.getName()+
                " as e where e.index.indexId = :indexId");
        q.setString("indexId", indexId);
        return (Search)q.uniqueResult();
    }
    
    
	  
}
