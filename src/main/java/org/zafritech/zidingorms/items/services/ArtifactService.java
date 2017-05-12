package org.zafritech.zidingorms.items.services;

import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zafritech.zidingorms.database.dao.ItemDao;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Item;

public interface ArtifactService {

    void initializeArtifact(Long id, String variableType);

    Item saveItemDao(ItemDao item);

    Artifact importExcel(Long id, String filePath);
    
    XSSFWorkbook DownloadExcel (Long id)  throws FileNotFoundException, IOException;
}
