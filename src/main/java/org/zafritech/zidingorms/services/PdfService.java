/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.services;

import java.io.ByteArrayOutputStream;
import org.springframework.stereotype.Service;

/**
 *
 * @author LukeS
 */
@Service
public interface PdfService {
    
    ByteArrayOutputStream DownloadPDF(Long id);
}
