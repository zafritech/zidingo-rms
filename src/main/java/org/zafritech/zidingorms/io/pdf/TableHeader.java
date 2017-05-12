/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.io.pdf;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.renderer.TableRenderer;
import java.io.ByteArrayOutputStream;

/**
 *
 * @author LukeS
 */
public class TableHeader implements IEventHandler {
    
    protected Table table;
    protected float tableHeight;
    protected Document doc;
    
    public void TableHeaderEventHandler(Document doc) {
        
        this.doc = doc;
        this.table = new Table(1);
        table.setWidth(523);
        table.addCell("Header Roow 1");
        table.addCell("Header Roow 2");
        table.addCell("Header Roow 3");
        TableRenderer renderer  = (TableRenderer) table.createRendererSubTree();
        renderer.setParent(new Document(new PdfDocument(new PdfWriter(new ByteArrayOutputStream()))).getRenderer());
        this.tableHeight = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4))).getOccupiedArea().getBBox().getHeight();
    }

    @Override
    public void handleEvent(Event event) {

        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
        Rectangle rect = new Rectangle(pdfDoc.getDefaultPageSize().getX() + doc.getLeftMargin(), 
                                       pdfDoc.getDefaultPageSize().getTop() - doc.getTopMargin(),
                                       100,
                                       getTableheight());
        
        new Canvas(canvas, pdfDoc, rect).add(table);
                
    }
    
    public float getTableheight() {
        
        return tableHeight;
    }
}
