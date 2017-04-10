package org.zafritech.zidingorms.dao.converter;

import java.sql.Timestamp;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.commons.enums.MediaType;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemType;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.ItemTypeRepository;

@Component
public class DaoToItemConverter implements Converter<ItemDao, Item> {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;

    @Override
    public Item convert(ItemDao itemDao) {

        if (itemDao.getId() != null) {

            ItemType itemType = itemTypeRepository.findByItemTypeName(itemDao.getItemType().getItemTypeName());

            Item item = itemRepository.findOne(itemDao.getId());

            item.setSysId(itemDao.getSysId());
            item.setItemType(itemType);
            item.setIdentifier(itemDao.getIdentifier());
            item.setItemValue(itemDao.getItemValue());
            item.setItemClass(itemDao.getItemClass());
            item.setItemLevel(itemDao.getItemLevel());
            item.setItemVersion(itemDao.getItemVersion());
            item.setArtifact(artifactRepository.findOne(itemDao.getArtifactId()));
            item.setModifiedDate(new Timestamp(System.currentTimeMillis()));

            return item;

        } else {

            ItemType itemType = new ItemType();

            if (itemDao.getItemType() != null) {

                itemType = itemTypeRepository.findByItemTypeName(itemDao.getItemType().getItemTypeName());

            } else {

                itemType = null;
            }

            Item item = new Item(itemDao.getSysId(), // To be fixed
                    itemDao.getIdentifier(), // To be fixed
                    itemDao.getItemValue(),
                    itemType,
                    MediaType.TEXT,
                    artifactRepository.findOne(itemDao.getArtifactId()));

            item.setItemClass(itemDao.getItemClass());
            item.setItemLevel(itemDao.getItemLevel());
            item.setSortIndex(itemDao.getSortIndex());

            return item;
        }
    }
}
