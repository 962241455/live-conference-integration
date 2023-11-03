package com.cmnt.dbpick.third.server.mongodb.repository;

import com.cmnt.dbpick.third.server.mongodb.document.SysMenu;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;


/**
 *
 */
public interface SysMenuRepository extends MongoRepository<SysMenu, String> {

    List<SysMenu> findTop50ByMenuBelongAndVisibleAndDeleted(String menuBelong, String visible, Boolean deleted, Sort sort);

}
