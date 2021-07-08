package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.Address;
import com.yanjian.boot05web2.mapper.AddressMapper;
import com.yanjian.boot05web2.service.AddressService;
import org.springframework.stereotype.Service;

@Service
public class AddressServiceImpl extends ServiceImpl<AddressMapper, Address> implements AddressService {
}
