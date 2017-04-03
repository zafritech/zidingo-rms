package org.zafritech.zidingorms.services.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.zafritech.zidingorms.commons.enums.ItemClass;
import org.zafritech.zidingorms.commons.enums.SystemVariableTypes;
import org.zafritech.zidingorms.daos.ItemDao;
import org.zafritech.zidingorms.daos.converters.DaoToItemConverter;
import org.zafritech.zidingorms.domain.Artifact;
import org.zafritech.zidingorms.domain.ArtifactHierachy;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.domain.SystemVariable;
import org.zafritech.zidingorms.repositories.ArtifactHierachyRepository;
import org.zafritech.zidingorms.repositories.ArtifactRepository;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.ItemTypeRepository;
import org.zafritech.zidingorms.repositories.SystemVariableRepository;
import org.zafritech.zidingorms.services.ArtifactService;
import org.zafritech.zidingorms.services.ItemService;
import org.zafritech.zidingorms.services.UserService;

@Service
public class ArtifactServiceImpl implements ArtifactService {

    @Autowired
    private ArtifactRepository artifactRepository;

    @Autowired
    private ArtifactHierachyRepository hierachyRepository;

    @Autowired
    private SystemVariableRepository sysVarRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemTypeRepository itemTypeRepository;
    
    @Autowired
    private ItemCommentRepository commentRepository;
    
    @Autowired
    private ItemService itemService;
    
    @Autowired
    private UserService userService;

    @Autowired
    private DaoToItemConverter daoToItem;

    @Override
    public Item saveItemDao(ItemDao itemDao) {

        if (itemDao != null) {

            return itemRepository.save(daoToItem.convert(itemDao));
        }

        return null;
    }

    @Override
    public void initializeArtifact(Long id, String variableType) {

        switch (variableType) {

            case "ITEM_UUID_TEMPLATE":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_TEMPLATE.name(), "ZID-REF", "DOCUMENT", id));
                break;

            case "REQUIREMENT_ID_TEMPLATE":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-COM", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-GEN", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-REL", "DOCUMENT", id));
                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_TEMPLATE.name(), "SYS-STA", "DOCUMENT", id));
                break;

            case "ITEM_UUID_NUMERIC_DIGITS":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.ITEM_UUID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", id));
                break;

            case "REQUIREMENT_ID_NUMERIC_DIGITS":

                sysVarRepository.save(new SystemVariable(SystemVariableTypes.REQUIREMENT_ID_NUMERIC_DIGITS.name(), "4", "DOCUMENT", id));
                break;

        }

    }

    @Override
    public Artifact importExcel(Long artifactId, String filePath) {

        Artifact artifact = artifactRepository.findOne(artifactId);

        // Item sort index
        int index = 0;

        // Prepare parent items
        Item currLevel1Item = null;
        Item currLevel2Item = null;
        Item currLevel3Item = null;
        Item currLevel4Item = null;
        Item currLevel5Item = null;
        Item currLevel6Item = null;
        Item currLevel7Item = null;
        Item currLevel8Item = null;

        try {

            FileInputStream inputStream = new FileInputStream(new File(filePath));

            int i = 1;  // Skip header row, i = 0

            Workbook workbook = getWorkbook(inputStream, filePath);
            Sheet worksheet = workbook.getSheetAt(0);

//            while(i <= worksheet.getLastRowNum()) {
            while (i <= 101) {

                ItemDao itemDao = new ItemDao();
                Item item = new Item();
                Row row = worksheet.getRow(i++);

                // Item System ID
                itemDao.setSysId((String) getCellValue(row.getCell(0)));

                // Item Identifier
                String ident = (String) getCellValue(row.getCell(1));
                itemDao.setIdentifier((ident != null && !ident.isEmpty()) ? ident : "");

                // Item value
                String value = (String) getCellValue(row.getCell(2));
                itemDao.setItemValue((value != null && !value.isEmpty()) ? value : "");

                // Is the Item a Requirement
                String itemClass = (String) getCellValue(row.getCell(3));
                itemDao.setItemClass((itemClass.equalsIgnoreCase("DEF")) ? ItemClass.REQUIREMENT.name() : ItemClass.PROSE.name());

                // Set the Requirement type
                itemDao.setItemType((itemClass.equalsIgnoreCase("DEF")) ? itemTypeRepository.findByItemTypeName("Unclassified")
                        : itemTypeRepository.findByItemTypeName("Prose"));
                
                // Item level in hierachy
                int level = (int) (double) getCellValue(row.getCell(14));
                itemDao.setItemLevel(level);

                itemDao.setSortIndex(index++);

                itemDao.setArtifactId(artifact.getId());

                switch (level) {

                    case 1:
                        currLevel1Item = this.saveItemDao(itemDao);
                        item = currLevel1Item;
                        ArtifactHierachy hierachyEntry1 = new ArtifactHierachy(artifact, currLevel1Item, null);
                        hierachyRepository.save(hierachyEntry1);
                        break;

                    case 2:
                        currLevel2Item = this.saveItemDao(itemDao);
                        item = currLevel2Item;
                        ArtifactHierachy hierachyEntry2 = new ArtifactHierachy(artifact, currLevel2Item, currLevel1Item);
                        hierachyRepository.save(hierachyEntry2);
                        break;

                    case 3:
                        currLevel3Item = this.saveItemDao(itemDao);
                        item = currLevel3Item;
                        ArtifactHierachy hierachyEntry3 = new ArtifactHierachy(artifact, currLevel3Item, currLevel2Item);
                        hierachyRepository.save(hierachyEntry3);
                        break;

                    case 4:
                        currLevel4Item = this.saveItemDao(itemDao);
                        item = currLevel4Item;
                        ArtifactHierachy hierachyEntry4 = new ArtifactHierachy(artifact, currLevel4Item, currLevel3Item);
                        hierachyRepository.save(hierachyEntry4);
                        break;

                    case 5:
                        currLevel5Item = this.saveItemDao(itemDao);
                        item = currLevel5Item;
                        ArtifactHierachy hierachyEntry5 = new ArtifactHierachy(artifact, currLevel5Item, currLevel4Item);
                        hierachyRepository.save(hierachyEntry5);
                        break;

                    case 6:
                        currLevel6Item = this.saveItemDao(itemDao);
                        item = currLevel6Item;
                        ArtifactHierachy hierachyEntry6 = new ArtifactHierachy(artifact, currLevel6Item, currLevel5Item);
                        hierachyRepository.save(hierachyEntry6);
                        break;

                    case 7:
                        currLevel7Item = this.saveItemDao(itemDao);
                        item = currLevel7Item;
                        ArtifactHierachy hierachyEntry7 = new ArtifactHierachy(artifact, currLevel7Item, currLevel7Item);
                        hierachyRepository.save(hierachyEntry7);
                        break;

                    case 8:
                        currLevel8Item = this.saveItemDao(itemDao);
                        item = currLevel8Item;
                        ArtifactHierachy hierachyEntry8 = new ArtifactHierachy(artifact, currLevel8Item, currLevel7Item);
                        hierachyRepository.save(hierachyEntry8);
                        break;

                    default:
                        item = this.saveItemDao(itemDao);
                }
                
                // Item comments
                String clientComment = (String) getCellValue(row.getCell(11));
                String contractorComment = (String) getCellValue(row.getCell(12));
                
                // Save client comment
                if (clientComment != null && !clientComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, clientComment, userService.findByEmail("client@zafritech.org"));
                    commentRepository.save(comment);
                    itemService.incrementCommentCount(item.getId());
                }
                
                // Save client comment
                if (contractorComment != null && !contractorComment.isEmpty()) {
                    
                    ItemComment comment = new ItemComment(item, contractorComment, userService.findByEmail("contractor@zafritech.org"));
                    commentRepository.save(comment);
                    itemService.incrementCommentCount(item.getId());
                }
            }

            workbook.close();
            inputStream.close();

        } catch (IOException e) {

            e.printStackTrace();
        }

        return null;
    }

    // Excel 2003 or 2007
    private Workbook getWorkbook(FileInputStream inputStream, String excelFilePath) throws IOException {

        Workbook workbook = null;

        if (excelFilePath.endsWith("xlsx")) {

            workbook = new XSSFWorkbook(inputStream);

        } else if (excelFilePath.endsWith("xls")) {

            workbook = new HSSFWorkbook(inputStream);

        } else {

            throw new IllegalArgumentException("The specified file is not Excel file");
        }

        return workbook;
    }

    @SuppressWarnings("deprecation")
    private Object getCellValue(Cell cell) {

        switch (cell.getCellTypeEnum()) {

            case STRING:
                return cell.getStringCellValue();

            case BOOLEAN:
                return cell.getBooleanCellValue();

            case NUMERIC:
                return cell.getNumericCellValue();

            default:
                return null;
        }
    }
}
