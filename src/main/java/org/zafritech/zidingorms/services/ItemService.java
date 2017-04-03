package org.zafritech.zidingorms.services;

import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.daos.ItemDao;
import org.zafritech.zidingorms.domain.Item;

@Service
public interface ItemService {

    Item findById(Long id);

    Item saveItem(Item item);

    Item saveDao(ItemDao itemDao);

    Item saveNewDao(ItemDao itemDao);

    int moveUp(Long id);

    int moveDown(Long id);
    
    int incrementCommentCount(Long id);

    String getNextSystemIdentifier(Long id);

    String getNextRequirementIdentifier(Long id, String template);
}
