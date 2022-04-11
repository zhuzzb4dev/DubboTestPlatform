package com.example.dubbotestplatform;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DubboInfoReq {

    private String address;

    private String interFaceName;

    private String version;

    private String method;

    private String[] parameterTypes;

    private Object[] args;
}
