package com.onlineServicesForEthnic.utils;

import com.mybatisflex.core.query.QueryWrapper;
import com.onlineServicesForEthnic.entity.Person;
import com.onlineServicesForEthnic.mapper.PersonMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import static com.onlineServicesForEthnic.entity.table.PersonTableDef.PERSON;


/**
 * 对于Mapper的一些常用操作小工具
 */
@RequiredArgsConstructor
@Component
public class MapperUtil {
    private final PersonMapper personMapper;

    public Person getPersonByUsername(String username) {
        return personMapper.selectOneByQuery(QueryWrapper.create()
            .select(PERSON.DEFAULT_COLUMNS)
            .from(PERSON)
            .where(PERSON.USERNAME.eq(username)));
    }

    public Person getPersonById(Long id) {
        return personMapper.selectOneById(id);
    }


}
