/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.dao;

import java.util.List;
import org.zafritech.zidingorms.domain.Item;

/**
 *
 * @author LukeS
 */
public class SearchDao {
    
    private int resultsSize;
    
    private List<Item> itemsList;
    
    private int currentPage;
    
    private int pageSize;
    
    private int lastPage;
    
    private int lastDisplayed;
    
    private List<Integer> pageList;
    
    public SearchDao() {
        
    }
    
    public int getResultsSize() {
        return resultsSize;
    }

    public void setResultsSize(int resultsSize) {
        this.resultsSize = resultsSize;
    }

    public List<Item> getItemsList() {
        return itemsList;
    }

    public void setItemsList(List<Item> itemsList) {
        this.itemsList = itemsList;
    }

    public int getCurrentPage() {
        return currentPage;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public int getPageSize() {
        return pageSize;
    }

    public void setPageSize(int pageSize) {
        this.pageSize = pageSize;
    }

    public int getLastPage() {
        return lastPage;
    }

    public void setLastPage(int lastPage) {
        this.lastPage = lastPage;
    }

    public List<Integer> getPageList() {
        return pageList;
    }

    public void setPageList(List<Integer> pageList) {
        this.pageList = pageList;
    }

    public int getLastDisplayed() {
        return lastDisplayed;
    }

    public void setLastDisplayed(int lastDisplayed) {
        this.lastDisplayed = lastDisplayed;
    }
    
}
