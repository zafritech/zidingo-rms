/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services.impl;

import com.itextpdf.io.font.FontConstants;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Div;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.pdf.Header;
import org.zafritech.zidingorms.pdf.PageXofY;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.services.PdfService;

/**
 *
 * @author LukeS
 */
@Service
public class PdfServiceImpl implements PdfService {
    
    @Autowired
    private ArtifactRepository artifactRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    private static Map<String, Style> styles;
    
    
    @Override
    public ByteArrayOutputStream DownloadPDF(Long id) {
        
        ByteArrayOutputStream  outputStream = new ByteArrayOutputStream();
        PdfWriter writer = new PdfWriter(outputStream);
        PdfDocument pdf = new PdfDocument(writer);
        PageSize pagesize = PageSize.A4;
        
        Artifact artifact = artifactRepository.findOne(id);
        
        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, new Header(artifact.getArtifactLongName()));

//        TableHeader handler = new TableHeader();
//        pdf.addEventHandler(PdfDocumentEvent.START_PAGE, handler); 
        
        PageXofY event = new PageXofY(pdf);
        pdf.addEventHandler(PdfDocumentEvent.END_PAGE, event);

        try (Document document = new Document(pdf, pagesize)) 
        {
            
            Div div = new Div();
            
            document.setMargins(70, 50, 70, 50);
            
            
            styles = createPdfStyles(document);
            
            List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(id);
            
            document.setTextAlignment(TextAlignment.CENTER);
            addEmptyLine(document, 20);
            document.add(new Paragraph(artifact.getArtifactProject().getProjectName()).addStyle(styles.get("Title")));
            document.add(new Paragraph(artifact.getArtifactType().getArtifactTypeLongName()).addStyle(styles.get("Subtitle")));
            document.add(new Paragraph(artifact.getIdentifier()).addStyle(styles.get("Normal")));
            
            addEmptyLine(document, 45);
            
            document.setTextAlignment(TextAlignment.LEFT);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            
            for (Item item : items ) {
                
                if (item.getItemClass().equals("HEADER")) {
                    
                    switch (item.getItemLevel()) {

                        case 1:
                            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Header1"))); 
                            break;

                        case 2: 
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Header2"))); 
                            break;

                        case 3: 
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Header3"))); 
                            break;

                        case 4: 
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Header4"))); 
                            break;

                        case 5: 
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Header5"))); 
                            break;

                        default:
                            document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Normal")));
                    }
                   
                } else if (item.getItemClass().equals("REQUIREMENT")) {

                    Paragraph p = new Paragraph();
                    p.add((item.getIdentifier()).trim() + ":").add(new Tab()).add((item.getItemValue()).trim());
                    document.add(p);
                    
                    addEmptyLine(document, 1);
                    
                } else {
                    
                    document.add(new Paragraph(item.getItemValue()).addStyle(styles.get("Normal")));
                }
            }
            
            document.add(div);
            event.writeTotal(pdf);
            document.close();

            return outputStream;
            
        } catch(Exception e) {
            
            return null;
        }
    }
    
    private static Map<String, Style> createPdfStyles(Document doc) throws IOException {
        
        Map<String, Style> styles = new HashMap<String, Style>();
        
        PdfFont font = PdfFontFactory.createFont(FontConstants.HELVETICA);
        PdfFont fontItalic = PdfFontFactory.createFont(FontConstants.TIMES_ITALIC);
        
        // Normal Style
        Style normalStyle = new Style();
        normalStyle.setFont(font).setFontSize(11);
        styles.put("Normal", normalStyle);
        
        // Title Style
        Style titleStyle = new Style();
        titleStyle.setFont(font).setFontSize(16);
        titleStyle.setBold();
        titleStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        styles.put("Title", titleStyle);
        
        // Subtitle Style
        Style subTitleStyle = new Style();
        subTitleStyle.setFont(font).setFontSize(14);
        subTitleStyle.setBold();
        subTitleStyle.setHorizontalAlignment(HorizontalAlignment.CENTER);
        styles.put("Subtitle", subTitleStyle);
        
        // Header1 Style
        Style header1Style = new Style();
        header1Style.setFont(font).setFontSize(14);
        header1Style.setBold();
        header1Style.setHorizontalAlignment(HorizontalAlignment.LEFT);
        styles.put("Header1", header1Style);
        
        // Header2 Style
        Style header2Style = new Style();
        header2Style.setFont(font).setFontSize(13);
        header2Style.setBold();
        header2Style.setHorizontalAlignment(HorizontalAlignment.LEFT);
        styles.put("Header2", header2Style);
        
        // Header3 Style
        Style header3Style = new Style();
        header3Style.setFont(font).setFontSize(12);
        header3Style.setBold();
        header3Style.setHorizontalAlignment(HorizontalAlignment.LEFT);
        styles.put("Header3", header3Style);
        
        // Header4 Style
        Style header4Style = new Style();
        header4Style.setFont(font).setFontSize(11);
        header4Style.setBold();
        header4Style.setHorizontalAlignment(HorizontalAlignment.LEFT);
        styles.put("Header4", header4Style);
        
        // Header5 Style
        Style header5Style = new Style();
        header5Style.setFont(fontItalic).setFontSize(11);
        header5Style.setBold();
        header5Style.setHorizontalAlignment(HorizontalAlignment.LEFT);
        styles.put("Header5", header5Style);
            
        return styles;
    }
    
    
    private static void addEmptyLine(Document document, int number) {
        
        for (int i = 0; i < number; i++) {
            
            document.add(new Paragraph(" "));
        }
    }
}
