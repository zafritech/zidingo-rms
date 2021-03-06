package org.zafritech.zidingorms.items.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.core.commons.enums.ItemClass;
import org.zafritech.zidingorms.core.commons.enums.ItemStatus;
import org.zafritech.zidingorms.core.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.core.user.UserService;
import org.zafritech.zidingorms.database.dao.ItemCreateDao;
import org.zafritech.zidingorms.database.dao.ItemDao;
import org.zafritech.zidingorms.database.dao.ItemEditDao;
import org.zafritech.zidingorms.database.dao.converter.DaoToCreateConverter;
import org.zafritech.zidingorms.database.dao.converter.DaoToItemConverter;
import org.zafritech.zidingorms.database.dao.converter.ItemToEditDaoConverter;
import org.zafritech.zidingorms.database.domain.Artifact;
import org.zafritech.zidingorms.database.domain.Item;
import org.zafritech.zidingorms.database.domain.ItemCategory;
import org.zafritech.zidingorms.database.domain.ItemHistory;
import org.zafritech.zidingorms.database.domain.Link;
import org.zafritech.zidingorms.database.domain.Project;
import org.zafritech.zidingorms.database.domain.SystemVariable;
import org.zafritech.zidingorms.database.domain.VerificationMethod;
import org.zafritech.zidingorms.database.domain.VerificationReference;
import org.zafritech.zidingorms.database.repositories.ArtifactRepository;
import org.zafritech.zidingorms.database.repositories.ItemCategoryRepository;
import org.zafritech.zidingorms.database.repositories.ItemHistoryRepository;
import org.zafritech.zidingorms.database.repositories.ItemRepository;
import org.zafritech.zidingorms.database.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.database.repositories.LinkRepository;
import org.zafritech.zidingorms.database.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.database.repositories.VerificationMethodRepository;
import org.zafritech.zidingorms.database.repositories.VerificationReferenceRepository;
import org.zafritech.zidingorms.io.excel.ExcelFunctions;
import org.zafritech.zidingorms.items.services.CommentService;
import org.zafritech.zidingorms.items.services.ItemService;

@Service
public class ItemServiceImpl implements ItemService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ExcelFunctions excelFunctions;
    
    @Autowired
    private ItemRepository itemRepository;
     
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
       
    @Autowired
    private CommentService commentService;

    @Autowired
    public ItemTypeRepository itemTypeRepository;
    
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
    
    @Autowired
    private VerificationMethodRepository vvMethodRepository;
    
    @Autowired
    private VerificationReferenceRepository vvReferenceRepository;
               
    @Override
    public Item findById(Long id) {

        return itemRepository.findOne(id);
    }
    
    @Override
    public ItemCreateDao getDaoForFirstItemCreate(Long id) {
        
        DaoToCreateConverter createConverter = new DaoToCreateConverter();
        Item item = new Item();
        item.setArtifact(artifactRepository.findOne(id)); 
        ItemCreateDao createDao = createConverter.convert(item);
        
        createDao.setItemTypes(itemTypeRepository.findAllByOrderByItemTypeLongName());
        createDao.setIdentPrefices(sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(
                                                    id, 
                                                    "DOCUMENT", 
                                                    SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name()));
        
        return createDao;
    }
    
    @Override
    public ItemCreateDao findByIdForCreate(Long id) {
        
        DaoToCreateConverter createConverter = new DaoToCreateConverter();
        ItemCreateDao createDao = createConverter.convert(itemRepository.findOne(id));
        
        createDao.setItemTypes(itemTypeRepository.findAllByOrderByItemTypeLongName());
        createDao.setIdentPrefices(sysVarRepository.findByOwnerIdAndOwnerTypeAndVariableNameOrderByVariableValue(
                                                    createDao.getItem().getArtifact().getId(), 
                                                    "DOCUMENT", 
                                                    SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name()));
        
        return createDao;
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
    
    @Override
    public List<Item> findByItemCategory(Project project, ItemCategory category) {

        List<Item> items = itemRepository.findByItemCategory(category);
        
        return items;
    }
    
    @Override
    public List<Item> findByItemCategoryPaged(Project project, ItemCategory category, int pageSize, int pageNumber) {

        PageRequest request = new PageRequest(pageNumber -1, pageSize, Sort.Direction.ASC, "sortIndex");
        
        List<Item> items = itemRepository.findByItemCategory(request, category);
        
        return items;
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

    @Override
    public List<Item> getPagedRequirements(Long id, int pageSize, int pageNumber) {
        
        PageRequest request = new PageRequest(pageNumber -1, pageSize, Sort.Direction.ASC, "sortIndex");
        
        List<Item> items = itemRepository.findByArtifact(request, artifactRepository.findOne(id));
        
        return items;
    }
    
    @Override
    public int getNumberOfItems(Long id) {
        
        return itemRepository.findByArtifactIdOrderBySortIndexAsc(id).size();
    }
    
    @Override
    public  List<Integer> getPagesList(int currentPage, int lastPage) {
        
        List<Integer> pageList = new ArrayList<>();

        int startIndex = 1;
        int upperLimit = 1;
        
        if (lastPage < 9) {
            
            startIndex = 1;
            upperLimit = lastPage;
            
        } else {

            upperLimit = ((int) Math.ceil((double)currentPage / 9) * 9);
            upperLimit = (lastPage < upperLimit) ? lastPage : upperLimit;
            startIndex = upperLimit - 8;
        
        }
        
        for (int i = startIndex; i <= upperLimit; i++) {

            pageList.add(i);
        }
        
        return pageList;
    }
    
    @Override
    public Integer getPageWithItem(Long artifactId, Long itemId, int pageSize) {
        
        List<Item> items = itemRepository.findByArtifactIdOrderBySortIndexAsc(artifactId);
        Item item = itemRepository.findOne(itemId);
        
        int itemIndex = items.indexOf(item) + 1;
        
        return (int)Math.ceil(itemIndex / pageSize) + 1;
    }

    @Override
    public Integer importRequirementsFromExcel(String filePath) {

        Integer reqsCount = 0;
        
        try {
            
            FileInputStream inputStream = new FileInputStream(new File(filePath));
            
            Workbook workbook = excelFunctions.getExcelWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);
            
            int sortIndex = 0;
            int i = 1;  // Skip header row, i = 0
            
            while(i <= worksheet.getLastRowNum()) {
                
                Row row = worksheet.getRow(i++);
                
                String sysId = (String) excelFunctions.getExcelCellValue(row.getCell(0));
                
                if (sysId != null && !sysId.isEmpty()) {
                    
                    Item item = itemRepository.findBySysId(sysId);
                    
                    if (item == null) {
                        
                        ItemDao itemDao = new ItemDao();
                        
                        itemDao.setSysId(sysId);
                        
                        String ident = (String) excelFunctions.getExcelCellValue(row.getCell(1));
                        itemDao.setIdentifier((ident != null && !ident.isEmpty()) ? ident : "");
                        
                        String value = (String) excelFunctions.getExcelCellValue(row.getCell(2));
                        itemDao.setItemValue((value != null && !value.isEmpty()) ? value : "");
                        
                        String itemClass = (String) excelFunctions.getExcelCellValue(row.getCell(3));
                        itemDao.setItemClass((itemClass.equalsIgnoreCase("DEF")) ? ItemClass.REQUIREMENT.name() : itemClass.toUpperCase());
                        
                        itemDao.setItemType((itemClass.equalsIgnoreCase("DEF") || itemClass.equalsIgnoreCase("REQUIREMENT")) 
                                                                                ? itemTypeRepository.findByItemTypeName("Functional")
                                                                                : itemTypeRepository.findByItemTypeName("None"));
                
                        int level = (int) (double) excelFunctions.getExcelCellValue(row.getCell(14));
                        itemDao.setItemLevel(level);
                        
                        itemDao.setSortIndex(sortIndex++);
                        itemDao.setArtifactId(53L);
                        
                        Item newItem = itemRepository.save(daoToItem.convert(itemDao));
                        newItem.setItemCategory(getItemCategory(row)); 
                        newItem.setItemStatus(getItemStatus(row)); 
                        newItem = itemRepository.save(newItem);
                        
                        // Item comments
                        createItemComment(newItem, row);
                        
                        // Item verification data
                        createItemVerification(newItem, row);
                        
                        reqsCount++;
                        System.out.println("New Item: " + sysId + " ----> ID: " + newItem.getId());
                    }
                }
            }
                
            System.out.println("\n\rItems found: " + reqsCount);
            
        } catch (IOException e) {

            System.out.println(e.getMessage());
        }
        
        return reqsCount;
    }
    
    private void createItemComment(Item item, Row row) {
        
        // Item comments
        String clientComment = (row.getCell(11, Row.RETURN_BLANK_AS_NULL) != null) 
                             ? (String) excelFunctions.getExcelCellValue(row.getCell(11, Row.RETURN_BLANK_AS_NULL)) 
                             : "";
        String contractorComment = (row.getCell(12, Row.RETURN_BLANK_AS_NULL) != null) 
                             ? (String) excelFunctions.getExcelCellValue(row.getCell(12, Row.RETURN_BLANK_AS_NULL)) 
                             : "";

        // Save client comment
        if (clientComment != null && !clientComment.isEmpty()) {

            commentService.saveComment(item, clientComment, userService.findByEmail("client@astad.qa"));
        }

        // Save client comment
        if (contractorComment != null && !contractorComment.isEmpty()) {

            commentService.saveComment(item, contractorComment, userService.findByEmail("contractor@astad.qa"));
        }
    }
    
    private void createItemVerification(Item item, Row row) {
        
        String method = (String) excelFunctions.getExcelCellValue(row.getCell(7));
        String refs = (String) excelFunctions.getExcelCellValue(row.getCell(8));
        String edidence = (String) excelFunctions.getExcelCellValue(row.getCell(10));
        
        if (method != null && !method.isEmpty()) {
                            
            VerificationMethod vvMethod = vvMethodRepository.findByMethodCode(method);

            // Check if item already exists in Verification References
            VerificationReference vvReference = vvReferenceRepository.findFirstByItem(item);

            if (vvReference == null) {

                vvReference = new VerificationReference(item, vvMethod);

            } else {

                vvReference.setMethod(vvMethod); 
            }

            if (refs != null && !refs.isEmpty()) {

                vvReference.setVvReferences(refs);
            }

            if (edidence != null && !edidence.isEmpty()) {

                vvReference.setVvEvidence(edidence); 
            }

            vvReferenceRepository.save(vvReference); 
        }
    }
    
    private ItemCategory getItemCategory(Row row) {
        
        String lead = (String) excelFunctions.getExcelCellValue(row.getCell(4));
        String newLead;
        
        if (lead != null && !lead.isEmpty()) {
                    
            switch (lead) {
                case "PM":
                    newLead = "PJM";
                    break;
                case "COM_Radio":
                    newLead = "RAD";
                    break;
                case "QF":
                    newLead = "QFN";
                    break;
                default:
                    newLead = lead;
                    break;
            }

            return itemCategoryRepository.findFirstByCategoryCode(newLead);
        }
        
        return null;
    }
    
    private ItemStatus getItemStatus(Row row) {
        
        ItemStatus itemStatus = ItemStatus.NONE;
        String status = (String) excelFunctions.getExcelCellValue(row.getCell(6));
        
        if (status != null && !status.isEmpty()) {

            switch (status) {
                case "Closed":
                    itemStatus = ItemStatus.CLOSED;
                    break;
                case "Confirmed":
                    itemStatus = ItemStatus.CONFIRMED;
                    break;
                case "Defined":
                    itemStatus = ItemStatus.DEFINED;
                    break;
                case "Open":
                    itemStatus = ItemStatus.OPEN;
                    break;
                case "Selected":
                    itemStatus = ItemStatus.SELECTED;
                    break;
                default:
                    itemStatus = ItemStatus.NONE;
                    break;
            }
        }

        return itemStatus;
    }
}
