package org.zafritech.zidingorms.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.dao.ItemCreateDao;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.dao.ItemEditDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.Link;

@Service
public interface ItemService {

    Item findById(Long id);
    
    ItemCreateDao findByIdForCreate(Long id);
    
    ItemEditDao findByIdForEdit(Long id);

    Item saveItem(Item item);

    Item saveDao(ItemDao itemDao);

    Item saveNewDao(ItemDao itemDao);
    
    Long deleteItem(Long id);
    
    void updateItemHistory(Item item);
    
    void updateLinksChanged(Item item);
    
    void resetLinkChanged(Item item, Link link);

    int moveUp(Long id);

    int moveDown(Long id);
    
    int incrementCommentCount(Long id);

    String getNextSystemIdentifier(Long id);

    String getNextRequirementIdentifier(Long id, String template);
    
    List<Item> findRequirements(Long id); 
}
