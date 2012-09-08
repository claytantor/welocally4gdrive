package com.welocally.gdrive.jms;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.TextMessage;
import javax.servlet.ServletException;

import org.apache.log4j.Logger;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Component;

import com.welocally.gdrive.domain.Index;
import com.welocally.gdrive.mvc.model.GeodbIndexModel;
import com.welocally.gdrive.security.UserPrincipal;
import com.welocally.gdrive.service.IndexService;
import com.welocally.gdrive.spreadsheet.template.SpreadsheetTransformationUtils;
import com.welocally.gdrive.spreadsheet.template.WorksheetGrid;

@Component("publishIndexMessageProducer")
public class PublishIndexMessageProducer {
	
	static Logger logger = Logger.getLogger(PublishIndexMessageProducer.class);
	   
    @Autowired
    @Qualifier("publishIndexJmsTemplate")
    private JmsTemplate publishIndexJmsTemplate;
        
    @Autowired IndexService indexService;
    @Autowired SpreadsheetTransformationUtils spreadsheetTransformationUtils;


    public void generateMessage(WorksheetGrid grid, Index index,UserPrincipal up) 
    	throws JMSException, JsonGenerationException, JsonMappingException, IOException, ServletException, JSONException {
    		logger.debug("generating message");
    		

            index.setPublishStatus(Index.PublishStatus.QUEUED);
            
                              
            GeodbIndexModel model = spreadsheetTransformationUtils.makeIndexModel(grid, index);
    	  		
    		ByteArrayOutputStream baos = new ByteArrayOutputStream();
    		ObjectMapper jacksonMapper = new ObjectMapper();
    		jacksonMapper.writeValue(baos, model);
    		
            final String text = baos.toString();

            publishIndexJmsTemplate.send(new MessageCreator() {
                public Message createMessage(Session session) throws JMSException {
                    TextMessage message = session.createTextMessage(text);                 
                    return message;
                }
            });
            
            indexService.save(index);
    }


}
