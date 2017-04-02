package org.zafritech.zidingorms.services.impl;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.daos.ItemDao;
import org.zafritech.zidingorms.daos.converters.DaoToItemConverter;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ItemService;

@Service
public class ItemServiceImpl implements ItemService {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private DaoToItemConverter daoToItem;

    @Autowired
    private SystemVariableRepository sysVarRepository;

    @Override
    public Item findById(Long id) {

        return itemRepository.findOne(id);
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

            Matcher matcher = pattern.matcher(item.getIdentifier());

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
