package com.onlineServicesForEthnic.service.Impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.onlineServicesForEthnic.entity.Person;
import com.onlineServicesForEthnic.mapper.PersonMapper;
import com.onlineServicesForEthnic.service.PersonService;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl extends ServiceImpl<PersonMapper, Person> implements PersonService {
}
