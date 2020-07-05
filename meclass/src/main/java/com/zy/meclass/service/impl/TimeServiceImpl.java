package com.zy.meclass.service.impl;

import com.zy.meclass.service.TimeService;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
public class TimeServiceImpl implements TimeService {
    @Override
    public String getTime() {
        Date d = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd kk:mm:ss ");
        System.out.println("格式化输出：" + sdf.format(d));

        sdf.setTimeZone(TimeZone.getTimeZone("Asia/Shanghai"));
        System.out.println("Asia/Shanghai:" + sdf.format(d));
        return sdf.format(d);
    }
}
