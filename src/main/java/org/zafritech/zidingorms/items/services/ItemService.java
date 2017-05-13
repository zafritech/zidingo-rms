package org.zafritech.zidingorms.items.services;

import java.util.List;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.database.dao.ItemCreateDao;
import org.zafritech.zidingorms.database.dao.ItemDao;
import org.zafritech.zidingorms.database.dao.ItemEditDao;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.Link;
import org.zafritech.zidingorms.database.domain.Project;

@Service
public interface ItemService {

    Item findById(Long id);
    
    ItemCreateDao getDaoForFirstItemCreate(Long id);
    
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
    
    List<Item> findByItemCategory(Project project, ItemCategory category);  
    
    List<Item> findByItemCategoryPaged(Project project, ItemCategory category, int pageSize, int pageNumber);
    
    List<Item> getPagedRequirements(Long id, int pageSize, int pageNumber); 
    
    int getNumberOfItems(Long id);
    
    List<Integer> getPagesList(int currentPage, int lastPage);
    
    Integer getPageWithItem(Long artifactId, Long itemId, int pageSize);
    
    Integer importRequirementsFromExcel(String filePath);
}
