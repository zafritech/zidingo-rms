/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.scheduling;

import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

/**
 *
 * @author LukeS
 */
@Component
public class ScheduledTasks {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ScheduledTasks.class);
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("HH:mm:ss");
             
//    @Scheduled(fixedRate = 5000)            // Every 5 seconds
//    public void initializeApplication() {
//        
//        LOGGER.info("Scheduled time task :" + DATE_FORMAT.format(new Date())); 
//    }
//    
//    @Scheduled(cron = "0 0 0 * * *")        // 12 midnight daily
//    public void resetDatabase() {
//        
//        LOGGER.info("Scheduled cron task :" + DATE_FORMAT.format(new Date()));
//    }
}
