package org.zafritech.zidingorms.services;

import org.zafritech.zidingorms.daos.ItemDao;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;

public interface ArtifactService {

    void initializeArtifact(Long id, String variableType);
    
    Item saveItemDao(ItemDao item);
    
    Artifact importExcel(Long id, String filePath);
}
