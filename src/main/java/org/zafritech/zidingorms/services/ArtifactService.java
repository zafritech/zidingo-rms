package org.zafritech.zidingorms.services;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;

public interface ArtifactService {

    void initializeArtifact(Long id, String variableType);

    Item saveItemDao(ItemDao item);

    Artifact importExcel(Long id, String filePath);
    
    XSSFWorkbook DownloadExcel (Long id)  throws FileNotFoundException, IOException;
    
    ByteArrayOutputStream DownloadPDF(Long id);
}
