package com.yanjian.boot05web2.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.yanjian.boot05web2.bean.Nopay;
import com.yanjian.boot05web2.mapper.NopayMapper;
import com.yanjian.boot05web2.service.NopayService;
import org.springframework.stereotype.Service;

@Service
public class NopayServiceImpl extends ServiceImpl<NopayMapper, Nopay> implements NopayService {
}
