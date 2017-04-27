/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.loader.DataInitializer;

/**
 *
 * @author LukeS
 */
@Component
public class StarterCLR implements CommandLineRunner {
    
    @Autowired
    private DataInitializer dataInitializer;
    
    @Override
    public void run(String... strings) throws Exception {

//        InitializeData();
    }
    
    private void InitializeData() {
        
        dataInitializer.initializeRoles();
        dataInitializer.initializeUsers();
        dataInitializer.initializeItemTypes();
        dataInitializer.initializeLinkTypes();
        dataInitializer.initializeArtifactTypes();
        dataInitializer.initializeProjects();
        dataInitializer.initializeSystemVariable();
    }
}
