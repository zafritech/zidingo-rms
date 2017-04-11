/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.controllers;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.services.ArtifactService;
import org.zafritech.zidingorms.services.PdfService;

/**
 *
 * @author LukeS
 */
@Controller
public class ApplicationController {

    private String UPLOAD_FOLDER = "F://Projects//Uploads//";

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ArtifactService artifactService;

    @Autowired
    private PdfService pdfService;

    public void setArtifactRepository(ArtifactRepository artifactRepository) {

        this.artifactRepository = artifactRepository;
    }

    @PostMapping("/excel/import")
    public String importFromExcel(@RequestParam("file") MultipartFile file,
            @RequestParam("artifactId") Long id,
            RedirectAttributes redirectAttributes) {

        if (file.isEmpty()) {

            redirectAttributes.addFlashAttribute("message", "Please select a file to upload");
            return "redirect:uploadStatus";
        }

        try {

            byte[] bytes = file.getBytes();

            Path path = Paths.get(UPLOAD_FOLDER + file.getOriginalFilename());
            Files.write(path, bytes);

            artifactService.importExcel(id, path.toString());

            File excelFile = new File(path.toString());
            excelFile.delete();

        } catch (IOException e) {
            
        }

        return "redirect:/artifacts/" + id;
    }
    
    @RequestMapping("/excel/download/{id}")
    public void downloadExcelArtifact(@PathVariable Long id, HttpServletResponse response) throws IOException {
        
        Artifact artifact = artifactRepository.findOne(id);
        
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String fileName = timeFormat.format(System.currentTimeMillis()) + "_" + artifact.getIdentifier() + "_Requirements.xlsx";
        
        XSSFWorkbook workbook = artifactService.DownloadExcel(id);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        workbook.write(baos); 
        baos.close();
        
        response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setContentLength(baos.size());
        
        ServletOutputStream fout = response.getOutputStream();
        fout.write(baos.toByteArray());
        fout.flush();
        fout.close();
    }
    
    @RequestMapping("/pdf/download/{id}")
    public void downloadPDFArtifact(@PathVariable Long id, HttpServletResponse response) throws IOException {
        
        Artifact artifact = artifactRepository.findOne(id);
        
        DateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd-HHmmss");
        String fileName = timeFormat.format(System.currentTimeMillis()) + "_" + artifact.getIdentifier() + "_Requirements.pdf";
        
//        ByteArrayOutputStream document = artifactService.DownloadPDF(id);
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
