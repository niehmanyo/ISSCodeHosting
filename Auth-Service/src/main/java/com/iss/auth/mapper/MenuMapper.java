package com.iss.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iss.auth.domain.po.Menu;

import java.util.List;

public interface MenuMapper extends BaseMapper<Menu> {
    List<String> selectPermsByUserId (Long userId);
}
