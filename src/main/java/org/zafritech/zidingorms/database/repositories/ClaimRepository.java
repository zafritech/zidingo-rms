/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.database.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import org.zafritech.zidingorms.core.commons.enums.ClaimType;
import org.zafritech.zidingorms.database.domain.Claim;
import org.zafritech.zidingorms.database.domain.User;

/**
 *
 * @author LukeS
 */
public interface ClaimRepository extends CrudRepository<Claim, Long> {
    
    List<Claim> findByUser(User user);
    
    List<Claim> findByUserAndClaimType(User user, String claimType);
    
    List<Claim> findByUserAndClaimTypeAndClaimValue(User user, String claimType, String claimValue);
    
    List<Claim> findByClaimTypeAndClaimValue(ClaimType claimType, String claimValue);
}
