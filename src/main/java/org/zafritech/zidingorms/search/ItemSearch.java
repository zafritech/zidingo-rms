/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.transaction.Transactional;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.query.dsl.QueryBuilder;
import org.springframework.stereotype.Repository;
import org.zafritech.zidingorms.dao.SearchDao;
import org.zafritech.zidingorms.domain.Item;

/**
 *
 * @author LukeS
 */
@Repository
@Transactional
public class ItemSearch {
    
    @PersistenceContext
    private EntityManager entityManager;
    
    public SearchDao search(String text, Long pageSize, Long page) {
        
        // get the full text entity manager
        FullTextEntityManager fullTextEntityManager = org.hibernate.search.jpa.Search.getFullTextEntityManager(entityManager);
    
        // create the query using Hibernate Search query DSL
        QueryBuilder queryBuilder = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(Item.class).get();
    
        // a very basic query by keywords
        org.apache.lucene.search.Query query = queryBuilder.keyword().onFields("itemValue", "sysId", "identifier")
                                                                     .matching(text)
                                                                     .createQuery();
        
        // wrap Lucene query in an Hibernate Query object
        org.hibernate.search.jpa.FullTextQuery jpaQuery = fullTextEntityManager.createFullTextQuery(query, Item.class);
        
        // execute search and return results (sorted by relevance as default)
        @SuppressWarnings("unchecked")
                
        int size = jpaQuery.getResultSize();
        jpaQuery.setFirstResult((pageSize.intValue() * (page.intValue() - 1) + 1)); 
        jpaQuery.setMaxResults(pageSize.intValue());

        List<Item> list = jpaQuery.getResultList();
        
        int lastPage = ((size % pageSize.intValue()) == 0) ? (size / pageSize.intValue()) : (size / pageSize.intValue()) + 1;
                
        SearchDao searchResults = new SearchDao();
        
        searchResults.setItemsList(list);
        searchResults.setResultsSize(size);
        searchResults.setPageSize(pageSize.intValue()); 
        searchResults.setCurrentPage(page.intValue());
        searchResults.setLastPage(lastPage); 
        searchResults.setPageList(getPagesList(page.intValue(), lastPage)); 
        searchResults.setLastDisplayed(Collections.max(getPagesList(page.intValue(), lastPage)));
        
        return searchResults;
    }
    
    private List<Integer> getPagesList(int currentPage, int lastPage) {
        
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
}
