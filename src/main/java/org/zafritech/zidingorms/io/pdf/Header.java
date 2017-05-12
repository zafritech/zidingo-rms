/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.io.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.property.TextAlignment;

/**
 *
 * @author LukeS
 */
public class Header implements IEventHandler {

    String header;
    
    public Header(String header) {
        
        this.header = header;
    }
    
    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdf = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        
        // No Header on first page
        if (pdf.getPageNumber(page) == 1) return;
        
        Rectangle pageSize = page.getPageSize();
        
        PdfCanvas pdfCanvas = new PdfCanvas(page.getLastContentStream(), page.getResources(), pdf);
        
        Canvas canvas = new Canvas(pdfCanvas, pdf, pageSize);
        
        canvas.showTextAligned(header, pageSize.getWidth() / 2, pageSize.getTop() - 40, TextAlignment.CENTER);
    }
}
