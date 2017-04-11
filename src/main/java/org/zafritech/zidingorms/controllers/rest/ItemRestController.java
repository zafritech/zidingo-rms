package org.zafritech.zidingorms.controllers.rest;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.zafritech.zidingorms.commons.enums.ItemClass;
import org.zafritech.zidingorms.commons.enums.MediaType;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.dao.ItemDao;
import org.zafritech.zidingorms.dao.ItemEditDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemType;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ItemService;
import org.zafritech.zidingorms.services.impl.ItemServiceImpl;

@SuppressWarnings("Convert2Diamond")
@RestController
public class ItemRestController {

    private ItemService itemService;

    @Autowired
    public SystemVariableRepository sysVarRepository;

    @Autowired
    public ItemTypeRepository itemTypeRepository;

    @Autowired
    public ItemRepository itemRepository;

    @Autowired
    public void setItemService(ItemServiceImpl itemService) {

        this.itemService = itemService;
    }

    @RequestMapping(value = "/api/item/{id}", method = RequestMethod.GET)
    public Item getItem(@PathVariable(value = "id") Long id) {

        return itemService.findById(id);
    }

    @RequestMapping(value = "/api/item/edit/{id}", method = RequestMethod.GET)
    public ItemEditDao getItemForEdit(@PathVariable(value = "id") Long id) {

        ItemEditDao editDao = itemService.findByIdForEdit(id);
        
        editDao.setItemTypes(itemTypeRepository.findAllByOrderByItemTypeLongName());
        editDao.setIdentPrefices(sysVarRepository
                                 .findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(
                                         editDao.getItem().getArtifact().getId(), 
                                         "DOCUMENT", 
                                         SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name())
                                );
                
        return editDao;
    }

    @RequestMapping(value = "/api/items/identifiers/{id}", method = RequestMethod.GET)
    public List<SystemVariable> getIdentifierTemplatesTypes(@PathVariable(value = "id") Long id) {

        return sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(id, "DOCUMENT", SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name());
    }

    @RequestMapping(value = "/api/items/itemtypes", method = RequestMethod.GET)
    public List<ItemType> getItemTypes() {

        return itemTypeRepository.findAllByOrderByItemTypeLongName();
    }

    @RequestMapping(value = "/api/items/classes", method = RequestMethod.GET)
    public List<ItemClass> getItemClasses() {

        return Arrays.asList(ItemClass.values());
    }

    @RequestMapping(value = "/api/items/mediatypes", method = RequestMethod.GET)
    public List<MediaType> getMediaTypes() {

        return Arrays.asList(MediaType.values());
    }

    @RequestMapping(value = "/api/items/nextsystemid/{id}", method = RequestMethod.GET)
    public ResponseEntity<String> getNextSystemID(@PathVariable(value = "id") Long id) {

        String sysIdentifier = itemService.getNextSystemIdentifier(id);

        return new ResponseEntity<String>(sysIdentifier, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/items/nextidentifier", method = RequestMethod.GET)

    public ResponseEntity<String> getNextRequirementID(@RequestParam(value = "id", required = true) Long id,
            @RequestParam(value = "template", required = true) String template) {

        String reqIdentifier = itemService.getNextRequirementIdentifier(id, template);

        return new ResponseEntity<String>(reqIdentifier, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/items/new", method = RequestMethod.POST)
    public ResponseEntity<Item> newItem(@RequestBody ItemDao itemDao) {

        Item item = itemService.saveNewDao(itemDao);

        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/items/save", method = RequestMethod.POST)
    public ResponseEntity<Item> saveItem(@RequestBody ItemDao itemDao) {

        if (itemDao.getId() != null) {
            
            Item oldItem = itemRepository.findOne(itemDao.getId());
            
            // Update Links, and Version only when itemValue text has changed
            if (!oldItem.getItemValue().equals(itemDao.getItemValue())) {

                itemService.updateLinksChanged(oldItem);
                itemService.updateItemHistory(oldItem);
                itemDao.setItemVersion(oldItem.getItemVersion() + 1);
            }
        
        }
         
        Item item = itemService.saveDao(itemDao);
        
        System.out.println("\n\rSaved Item: " + item);
            
        return new ResponseEntity<Item>(item, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/items/moveup/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> moveItemUp(@PathVariable(value = "id") Long id) {

        int sortIndex = itemService.moveUp(id);

        return new ResponseEntity<Long>((long) sortIndex, HttpStatus.OK);
    }

    @RequestMapping(value = "/api/items/movedown/{id}", method = RequestMethod.GET)
    public ResponseEntity<Long> moveItemDown(@PathVariable(value = "id") Long id) {

        int sortIndex = itemService.moveDown(id);

        return new ResponseEntity<Long>((long) sortIndex, HttpStatus.OK);
    }
}
