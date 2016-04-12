package com.igitras.custom;

import feign.RequestTemplate;
import feign.codec.EncodeException;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.web.HttpMessageConverters;
import org.springframework.cloud.netflix.feign.support.SpringEncoder;

import java.lang.reflect.Type;

/**
 * @author mason
 */
public class CustomSpringEncoder extends SpringEncoder {
    public CustomSpringEncoder(ObjectFactory<HttpMessageConverters> messageConverters) {
        super(messageConverters);
    }

    @Override
    public void encode(Object requestBody, Type bodyType, RequestTemplate request) throws EncodeException {
//        if(requestBody instanceof ){
//            request.queryLine();
//        }
        super.encode(requestBody, bodyType, request);
    }
}
