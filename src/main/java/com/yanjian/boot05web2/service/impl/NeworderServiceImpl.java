package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.Neworder;
import com.yanjian.boot05web2.mapper.NeworderMapper;
import com.yanjian.boot05web2.service.NeworderService;
import org.springframework.stereotype.Service;

@Service
public class NeworderServiceImpl extends ServiceImpl<NeworderMapper, Neworder> implements NeworderService {
}
