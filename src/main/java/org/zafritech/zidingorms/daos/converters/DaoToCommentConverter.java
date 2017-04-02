/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.zafritech.zidingorms.daos.converters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.zafritech.zidingorms.daos.CommentDao;
import org.zafritech.zidingorms.domain.Item;
import org.zafritech.zidingorms.domain.ItemComment;
import org.zafritech.zidingorms.domain.User;
import org.zafritech.zidingorms.repositories.ItemCommentRepository;
import org.zafritech.zidingorms.repositories.ItemRepository;
import org.zafritech.zidingorms.repositories.UserRepository;
import org.zafritech.zidingorms.services.UserService;

/**
 *
 * @author LukeS
 */
@Component
public class DaoToCommentConverter implements Converter<CommentDao, ItemComment> {

    @Autowired
    ItemCommentRepository commentRepository;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Override
    public ItemComment convert(CommentDao commentDao) {

        // DEBUG CODE
        System.out.println("============================================================\n\r");
        System.out.println(commentDao.toString());

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        String name = userDetails.getUsername();

        // DEBUG CODE
        System.out.println("\n\rUser name: " + name + "\n\r");

        User user = userRepository.findByEmail(name);

        if (commentDao.getId() != null) {

            ItemComment comment = commentRepository.findOne(commentDao.getId());

            comment.setItem(itemRepository.findOne(commentDao.getItemId()));
            comment.setComment(commentDao.getComment());
            comment.setAuthor(user);

            return comment;

        } else {

            Item item = itemRepository.findOne(commentDao.getItemId());
            ItemComment comment = new ItemComment(item, commentDao.getComment());

            return comment;
        }
    }
}
