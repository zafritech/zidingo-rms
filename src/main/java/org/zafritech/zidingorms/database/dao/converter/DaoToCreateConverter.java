/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.dao.converter;

import java.util.Arrays;
import org.springframework.core.convert.converter.Converter;
import org.zafritech.zidingorms.core.commons.enums.ItemClass;
import org.zafritech.zidingorms.core.commons.enums.MediaType;
import org.zafritech.zidingorms.database.dao.ItemCreateDao;
import org.zafritech.zidingorms.database.domain.Item;

/**
 *
 * @author LukeS
 */
public class DaoToCreateConverter implements Converter<Item, ItemCreateDao> {

    @Override
    public ItemCreateDao convert(Item item) {

        ItemCreateDao createDao = new ItemCreateDao(item);
        
        createDao.setItemLevels(new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9, 10}); 
        createDao.setItemClasses(Arrays.asList(ItemClass.values())); 
        createDao.setMediaTypes(Arrays.asList(MediaType.values())); 
        
        // List<MediaType> mediaTypes and List<SystemVariable> identPrefices 
        // will be added by the Controller. For some reason JPA doesn't work 
        // in this class -> returns null Exceptions.
        
        return createDao;
    }
}
