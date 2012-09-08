package com.welocally.gdrive.jms;

import java.io.ByteArrayOutputStream;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.TextMessage;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.StringRequestEntity;
import org.apache.commons.io.IOUtils;
import org.apache.log4j.Logger;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.orm.hibernate3.SessionFactoryUtils;
import org.springframework.orm.hibernate3.SessionHolder;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.service.IndexService;


public class PublishIndexMessageListener implements MessageListener { 

	static Logger logger = Logger.getLogger(PublishIndexMessageListener.class);

    private SessionFactory sessionFactory;
 
    private IndexService indexService;
    
    private String publishUrl;
      
    private Integer sleepInSeconds;
    
    /**
     * Implementation of <code>MessageListener</code>.
     */
    public void onMessage(Message message) {
        
        
        Session session = null;
        try {   
            
            //bind to thread            
            session = 
                SessionFactoryUtils.getSession(sessionFactory, true);
            
            TransactionSynchronizationManager.bindResource(
                    sessionFactory,
                    new SessionHolder(session));
            logger.debug("==== onMessage");
            
            //we are gong to do a thread sleep here to deal with rate limiting
            //this is expected to be processed in a synchronous queue
            //with a single worker
            Thread.sleep(sleepInSeconds*1000);
            
            if (message instanceof TextMessage) {
                TextMessage tm = (TextMessage)message;                                  
                  
                //publish the data via http POST
                JSONObject records = new JSONObject(tm.getText());
                executePost(publishUrl, records);
                                   
                //update the status of the index to published
                Index index = indexService.findByIndexId(records.getString("indexId"));
                index.setPublishStatus(Index.PublishStatus.PUBLISHED);
                indexService.save(index);
              
            }
            
                        
        } catch (JMSException e) {
            logger.error(e.getMessage(), e);
        } 
        catch (InterruptedException e) {
            logger.error(e.getMessage(), e);
        } catch (JSONException e) {
            logger.error(e.getMessage(), e);
        } finally {
            TransactionSynchronizationManager.unbindResource(sessionFactory);
            if(session != null)
                SessionFactoryUtils.releaseSession(
                        session, 
                        sessionFactory);
        }
    }
    
    public void executePost(String url, JSONObject records) {
               
        HttpClient client = new HttpClient();


        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        PostMethod method = new PostMethod(url); 
        try {
            
            method.setRequestHeader("Content-Type", "application/json");
            method.setRequestEntity(new StringRequestEntity(records.toString(), "application/json", null));
           
            
            logger.debug(method.getURI().toString());
            
            
            int returnCode = client.executeMethod(method);
        
            if (returnCode == HttpStatus.SC_NOT_IMPLEMENTED) {
                logger.error("The GET method is not implemented by this URI");
            
            } else {
                IOUtils.copy(
                        method.getResponseBodyAsStream(), baos);                
                baos.flush();
                
            }
        } catch (Exception e) {
            System.err.println(e);
        } finally {
            method.releaseConnection();
        }
        
    }


    public void setSessionFactory(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }


    public void setSleepInSeconds(Integer sleepInSeconds) {
        this.sleepInSeconds = sleepInSeconds;
    }

    public void setPublishUrl(String publishUrl) {
        this.publishUrl = publishUrl;
    }

    public void setIndexService(IndexService indexService) {
        this.indexService = indexService;
    }



    
}
