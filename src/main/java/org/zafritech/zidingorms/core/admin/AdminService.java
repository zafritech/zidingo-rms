/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.core.admin;

import org.springframework.stereotype.Service;

/**
 *
 * @author LukeS
 */
@Service
public interface AdminService {
    
    boolean loadPMSLeads(String filePath);
    
    boolean updateASTADLeads(String filePath);
    
    boolean updateItemStatuses(String filePath);
    
    boolean updateConfirmationRequests(String filePath);
    
    boolean updateVandVMethods(String filePath);
}
