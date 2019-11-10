package com.jsrdxzw.service.impl;

import com.jsrdxzw.mapper.CarouselMapper;
import com.jsrdxzw.pojo.Carousel;
import com.jsrdxzw.service.CarouselService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import tk.mybatis.mapper.entity.Example;

import java.util.List;

@Service
public class CarouselServiceImpl implements CarouselService {
    private final CarouselMapper carouselMapper;

    public CarouselServiceImpl(CarouselMapper carouselMapper) {
        this.carouselMapper = carouselMapper;
    }

    @Transactional(rollbackFor = Exception.class, propagation = Propagation.SUPPORTS)
    @Override
    public List<Carousel> queryAll(int isShow) {
        Example example = new Example(Carousel.class);
        // 倒序查询
        example.orderBy("sort").desc();

        Example.Criteria criteria = example.createCriteria();
        criteria.andEqualTo("isShow", isShow);

        return carouselMapper.selectByExample(example);
    }
}
