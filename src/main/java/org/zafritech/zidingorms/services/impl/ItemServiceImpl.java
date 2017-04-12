package org.zafritech.zidingorms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.ItemClass;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.dao.ItemCreateDao;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.dao.ItemEditDao;
import org.zafritech.zidingorms.dao.converter.DaoToCreateConverter;
import org.zafritech.zidingorms.dao.converter.DaoToItemConverter;
import org.zafritech.zidingorms.dao.converter.ItemToEditDaoConverter;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemHistory;
import org.zafritech.zidingorms.domain.Link;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemHistoryRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.LinkRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemHistoryRepository itemHistoryRepository;
    
    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private DaoToItemConverter daoToItem;

    @Autowired
    private SystemVariableRepository sysVarRepository;

    @Autowired
    private LinkRepository linkRepository;
    
    @Override
    public Item findById(Long id) {

        return itemRepository.findOne(id);
    }
    
    @Override
    public ItemCreateDao findByIdForCreate(Long id) {
        
        DaoToCreateConverter createConverter = new DaoToCreateConverter();
        
        return createConverter.convert(itemRepository.findOne(id));
    }
    
    @Override
    public ItemEditDao findByIdForEdit(Long id) {
        
        ItemToEditDaoConverter editConverter = new ItemToEditDaoConverter();
        
        return editConverter.convert(itemRepository.findOne(id));
    }
    
    @Override
    public Item saveItem(Item item) {
        
        Item saved = itemRepository.save(item);
        updateArtifactLastUpdateTime(saved.getArtifact().getId());

        return saved;
    }

    @Override
    public Item saveDao(ItemDao itemDao) {

        if (itemDao != null) {

            return saveItem(daoToItem.convert(itemDao));
        }

        return null;
    }

    @Override
    public Item saveNewDao(ItemDao itemDao) {

        if (itemDao != null) {

            refreshSortIndeces(itemDao.getArtifactId(), itemDao.getSortIndex());

            itemDao.setSysId(getNextSystemIdentifier(itemDao.getArtifactId()));
            return saveItem(daoToItem.convert(itemDao));
        }

        return null;
    }
 
    @Override 
    public Long deleteItem(Long id) {
        
        // Delete outgoing links first
        List<Link> links = linkRepository.findBySrcItem(itemRepository.findOne(id));
        
        for (Link link : links) {
            
            linkRepository.delete(link.getId()); 
            
            Item item = itemRepository.findOne(link.getDstItem().getId());
            item.setLinkCount(item.getLinkCount() - 1);  // Adjust linkCount down on destination item.
        }
        
        // Clear item history
        List<ItemHistory> histories = itemHistoryRepository.findAllByItemId(id);
        
        for (ItemHistory history : histories) {
            
            itemHistoryRepository.delete(history.getId());
        }
        
        // Delete the Item
        itemRepository.delete(id); 
        
        return id;
    }
    
    @Override
    public void updateItemHistory(Item item) {
        
        ItemHistory history = new ItemHistory(item, item.getSysId(), item.getItemValue(), item.getItemVersion());
        
        itemHistoryRepository.save(history);
    }
    
    @Override
    public void updateLinksChanged(Item item) {
        
        List<Link> links = linkRepository.findByDstItem(item);
        
        if (links != null) {
            
            for (Link link : links) {
                
                Item srcItem = itemRepository.findOne(link.getSrcItem().getId());
                
                srcItem.setLinkChanged(true);
                itemRepository.save(srcItem);
                
                link.setDstItemChanged(true);
                link.setDstHistoryValue(item.getItemValue()); 
                linkRepository.save(link);
            }
        }
    }
    
    @Override
    public void resetLinkChanged(Item item, Link link) {
        
        item.setLinkChanged(false);
        itemRepository.save(item);
        
        link.setDstItemChanged(false);
        linkRepository.save(link);
    }

    @Override
    public int incrementCommentCount(Long id) {
        
        Item item = itemRepository.findOne(id);
        int commentCount = item.getCommentCount();
        int newCount = commentCount + 1;
        
        item.setCommentCount(newCount);
        itemRepository.save(item);
        
        return newCount;
    }
    
    @Override
    public int moveUp(Long id) {

        Item currItem = itemRepository.findOne(id);
        Long artifactId = currItem.getArtifact().getId();

        int currentIndex = currItem.getSortIndex();
        int desiredIndex = currentIndex - 1;

        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(artifactId);

        for (Item item : items) {

            if (item.getSortIndex() == desiredIndex) {

                // Set this item in List to higher sortIndex (move it up the page)
                item.setSortIndex(currentIndex);
                itemRepository.save(item);

                // Set current item to lower sortIndex (move it down the page)
                currItem.setSortIndex(desiredIndex);
                itemRepository.save(currItem);
            }
        }

        return desiredIndex;
    }

    @Override
    public int moveDown(Long id) {

        Item currItem = itemRepository.findOne(id);
        Long artifactId = currItem.getArtifact().getId();

        int currentIndex = currItem.getSortIndex();
        int desiredIndex = currentIndex + 1;

        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(artifactId);

        for (Item item : items) {

            if (item.getSortIndex() == desiredIndex) {

                // Set this item in List to higher sortIndex (move it up the page)
                item.setSortIndex(currentIndex);
                itemRepository.save(item);

                // Set current item to lower sortIndex (move it down the page)
                currItem.setSortIndex(desiredIndex);
                itemRepository.save(currItem);
            }
        }

        return desiredIndex;
    }

    @Override
    public String getNextSystemIdentifier(Long id) {

        String template = getSystemIDTemplate(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_TEMPLATE.name());
        List<SystemVariable> digitsList = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name());
        String digits = digitsList.get(0).getVariableValue();
        String format = "%0" + digits + "d";

        String regex = "(\\d+$)";
        Pattern pattern = Pattern.compile(regex);

        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(id);
        List<String> list = new ArrayList<String>();

        for (Item item : items) {

            Matcher matcher = pattern.matcher(item.getSysId());

            if (matcher.find()) {

                String listItem = String.format(format, Integer.parseInt(matcher.group(1)));

                list.add(listItem);
            }
        }

        list = list.stream().sorted().collect(Collectors.toList());

        if (!list.isEmpty()) {

            return template + "-" + String.format(format, Integer.parseInt(list.get(list.size() - 1)) + 1);

        } else {

            return template + "-" + String.format(format, 1);
        }
    }

    @Override
    public String getNextRequirementIdentifier(Long id, String template) {

        List<SystemVariable> digitsList = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name());
        String digits = digitsList.get(0).getVariableValue();
        String format = "%0" + digits + "d";

        String regex = "(\\d+$)";
        Pattern pattern = Pattern.compile(regex);

        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(id);
        List<String> list = new ArrayList<String>();

        for (Item item : items) {

            if (item.getIdentifier() != null) {
                
                Matcher matcher = pattern.matcher(item.getIdentifier());

                if (matcher.find()) {

                    String listItem = String.format(format, Integer.parseInt(matcher.group(1)));

                    list.add(listItem);
                }
            }
        }

        list = list.stream().sorted().collect(Collectors.toList());

        if (!list.isEmpty()) {

            return template + "-" + String.format(format, Integer.parseInt(list.get(list.size() - 1)) + 1);

        } else {

            return template + "-" + String.format(format, 1);
        }
    }

    
    @Override
    public List<Item> findRequirements(Long id) {
     
        List<Item> requirements = new ArrayList();
        
        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(id);
        
        for (Item item : items) {
            
            if (item.getItemClass().equals(ItemClass.REQUIREMENT.name())) {
                
                requirements.add(item);
            }
        }
        
        return requirements;
    }
    
    
    private void refreshSortIndeces(Long artifactId, int index) {

        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(artifactId);

        for (Item item : items) {

            if (item.getSortIndex() >= index) {

                item.setSortIndex(item.getSortIndex() + 1);
                itemRepository.save(item);
            }
        }
    }

    private String getSystemIDTemplate(Long id, String ownerType, String name) {

        List<SystemVariable> sysVar = sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableName(id, ownerType, name);

        return sysVar.get(0).getVariableValue();
    }

    private void updateArtifactLastUpdateTime(Long id) {

        Artifact doc = artifactRepository.findOne(id);

        doc.setModifiedDate(new Timestamp(System.currentTimeMillis()));
        artifactRepository.save(doc);
    }
}
