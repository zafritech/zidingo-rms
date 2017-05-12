/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.io.pdf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;

/**
 *
 * @author LukeS
 */
@Controller
public class PdfController {
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private PdfService pdfService;

    @RequestMapping("/pdf/download/{id}")
    public void downloadPDFArtifact(@PathVariable Long id, HttpServletResponse response) throws IOException {
        
        Artifact artifact = artifactRepository.findOne(id);
        
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String fileName = timeFormat.format(System.currentTimeMillis()) + "_" + artifact.getIdentifier() + "_Requirements.pdf";
        
        ByteArrayOutputStream document = pdfService.DownloadPDF(id);
        document.close();
        
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/pdf");
        response.setContentLength(document.size());
        
        ServletOutputStream fout = response.getOutputStream();
        fout.write(document.toByteArray());
        fout.flush();
        fout.close();
    }
}
