package com.xpq.cs.service.impl;

import com.xpq.cs.entity.TblPerson;
import com.xpq.cs.mapper.TblPersonMapper;
import com.xpq.cs.service.ITblPersonService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 人员表 服务实现类
 * </p>
 *
 * @author x
 * @since 2019-08-07
 */
@Service
public class TblPersonServiceImpl extends ServiceImpl<TblPersonMapper, TblPerson> implements ITblPersonService {

}
