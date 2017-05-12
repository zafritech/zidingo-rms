package org.zafritech.zidingorms.database.dao.converter;

import org.springframework.core.convert.converter.Converter;
import org.zafritech.zidingorms.core.commons.enums.ItemClass;
import org.zafritech.zidingorms.database.dao.ItemDao;
import org.zafritech.zidingorms.database.domain.Item;

public class ItemToDaoConverter implements Converter<Item, ItemDao> {

    @Override
    public ItemDao convert(Item item) {

        ItemDao itemDao = new ItemDao();

        itemDao.setId(item.getId());
        itemDao.setUuId(item.getUuId());
        itemDao.setSysId(item.getSysId());
        itemDao.setItemClass(item.getItemClass());
        itemDao.setItemLevel(item.getItemLevel());
        itemDao.setItemValue(item.getItemValue());
        if (item.getItemClass().equals(ItemClass.REQUIREMENT.name())) {
            itemDao.setIdentifier(item.getIdentifier());
        }
        itemDao.setItemType(item.getItemType());
        itemDao.setMediaType(item.getMediaType());
        itemDao.setArtifactId(item.getArtifact().getId());
        itemDao.setSortIndex(item.getSortIndex());

        return itemDao;
    }
}
