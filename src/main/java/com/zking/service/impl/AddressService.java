package com.zking.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zking.dto.AddressDto;
import com.zking.dto.CommentDTO;
import com.zking.entity.Address;
import com.zking.entity.Comment;
import com.zking.entity.WebSocketUser;
import com.zking.repository.IAddressMapper;
import com.zking.repository.ICommentMapper;
import com.zking.service.IAddressService;
import com.zking.service.ICommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.websocket.Session;
import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executor;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class AddressService extends ServiceImpl<IAddressMapper, Address> implements IAddressService {

    private final IAddressMapper iAddressMapper;

    public List<AddressDto> getAddressByDeep(int deep)
    {
        List<Address> byDeep = iAddressMapper
                .findByDeep(deep);
        return byDeep
                .stream()
                .map(AddressDto::from)
                .collect(Collectors.toList());
    }

    public List<AddressDto> getAddressByParentId(int parentId)
    {
        return iAddressMapper
                .findByParentId(parentId)
                .stream()
                .map(AddressDto::from)
                .collect(Collectors.toList());
    }
}
