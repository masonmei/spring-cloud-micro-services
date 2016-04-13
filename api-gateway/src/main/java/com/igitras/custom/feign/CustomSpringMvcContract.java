//package com.igitras.custom.feign;
//
//import static feign.Util.checkState;
//import static feign.Util.emptyToNull;
//import static org.springframework.core.annotation.AnnotatedElementUtils.findMergedAnnotation;
//
//import feign.Feign;
//import feign.MethodMetadata;
//import feign.Param;
//import org.springframework.cloud.netflix.feign.AnnotatedParameterProcessor;
//import org.springframework.cloud.netflix.feign.annotation.PathVariableParameterProcessor;
//import org.springframework.cloud.netflix.feign.annotation.RequestHeaderParameterProcessor;
//import org.springframework.cloud.netflix.feign.annotation.RequestParamParameterProcessor;
//import org.springframework.cloud.netflix.feign.support.SpringMvcContract;
//import org.springframework.core.DefaultParameterNameDiscoverer;
//import org.springframework.core.ParameterNameDiscoverer;
//import org.springframework.core.convert.ConversionService;
//import org.springframework.core.convert.support.DefaultConversionService;
//import org.springframework.util.Assert;
//import org.springframework.web.bind.annotation.RequestMapping;
//
//import java.lang.annotation.Annotation;
//import java.lang.reflect.Method;
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.Collection;
//import java.util.Collections;
//import java.util.HashMap;
//import java.util.LinkedHashMap;
//import java.util.List;
//import java.util.Map;
//
///**
// * @author mason
// */
//public class CustomSpringMvcContract extends SpringMvcContract {
//
//    private static final String ACCEPT = "Accept";
//    private static final String CONTENT_TYPE = "Content-Type";
//
//    private static final ParameterNameDiscoverer PARAMETER_NAME_DISCOVERER = new DefaultParameterNameDiscoverer();
//
//    private final Map<Class<? extends Annotation>, AnnotatedParameterProcessor> annotatedArgumentProcessors;
//    private final Map<String, Method> processedMethods = new HashMap<>();
//
//    private final ConversionService conversionService;
//    private final Param.Expander expander;
//
//    public CustomSpringMvcContract() {
//        this(Collections.emptyList());
//    }
//
//    public CustomSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors) {
//        this(annotatedParameterProcessors, new DefaultConversionService());
//    }
//
//    public CustomSpringMvcContract(List<AnnotatedParameterProcessor> annotatedParameterProcessors,
//            ConversionService conversionService) {
//        Assert.notNull(annotatedParameterProcessors, "Parameter processors can not be null.");
//        Assert.notNull(conversionService, "ConversionService can not be null.");
//
//        List<AnnotatedParameterProcessor> processors;
//        if (!annotatedParameterProcessors.isEmpty()) {
//            processors = new ArrayList<>(annotatedParameterProcessors);
//        } else {
//            processors = getDefaultAnnotatedArgumentsProcessors();
//        }
//        this.annotatedArgumentProcessors = toAnnotatedArgumentProcessorMap(processors);
//        this.conversionService = conversionService;
//        this.expander = new ConvertingExpander(conversionService);
//    }
//
//    @Override
//    public MethodMetadata parseAndValidateMetadata(Class<?> targetType, Method method) {
//        this.processedMethods.put(Feign.configKey(targetType, method), method);
//        MethodMetadata md = super.parseAndValidateMetadata(targetType, method);
//
//        RequestMapping classAnnotation = findMergedAnnotation(targetType, RequestMapping.class);
//        if (classAnnotation != null) {
//            // Prepend path from class annotation if specified
//            if (classAnnotation.value().length > 0) {
//                String pathValue = emptyToNull(classAnnotation.value()[0]);
//                checkState(pathValue != null, "RequestMapping.value() was empty on type %s",
//                        method.getDeclaringClass().getName());
//                if (!pathValue.startsWith("/")) {
//                    pathValue = "/" + pathValue;
//                }
//                md.template().insert(0, pathValue);
//            }
//
//            // produces - use from class annotation only if method has not specified this
//            if (!md.template().headers().containsKey(ACCEPT)) {
//                parseProduces(md, method, classAnnotation);
//            }
//
//            // consumes -- use from class annotation only if method has not specified this
//            if (!md.template().headers().containsKey(CONTENT_TYPE)) {
//                parseConsumes(md, method, classAnnotation);
//            }
//
//            // headers -- class annotation is inherited to methods, always write these if
//            // present
//            parseHeaders(md, method, classAnnotation);
//        }
//        return md;
//
//    }
//
//    @Override
//    protected void processAnnotationOnMethod(MethodMetadata data, Annotation methodAnnotation, Method method) {
//        if (!(methodAnnotation instanceof RequestMapping)) {
//            return;
//        }
//
//        RequestMapping methodMapping = findMergedAnnotation(method, RequestMapping.class);
//        // HTTP Method
//        checkOne(method, methodMapping.method(), "method");
//        data.template().method(methodMapping.method()[0].name());
//
//        // path
//        checkAtMostOne(method, methodMapping.value(), "value");
//        if (methodMapping.value().length > 0) {
//            String pathValue = emptyToNull(methodMapping.value()[0]);
//            if (pathValue != null) {
//                // Append path from @RequestMapping if value is present on method
//                if (!pathValue.startsWith("/")
//                        && !data.template().toString().endsWith("/")) {
//                    pathValue = "/" + pathValue;
//                }
//                data.template().append(pathValue);
//            }
//        }
//
//        // produces
//        parseProduces(data, method, methodMapping);
//
//        // consumes
//        parseConsumes(data, method, methodMapping);
//
//        // headers
//        parseHeaders(data, method, methodMapping);
//
//        parseRequestParams(data, method, methodMapping);
//
//        parseRequestHeaders(data, method, methodMapping);
//
//        data.indexToExpander(new LinkedHashMap<Integer, Param.Expander>());
//    }
//
//    private void parseRequestHeaders(MethodMetadata data, Method method, RequestMapping methodMapping) {
//
//    }
//
//    private void parseRequestParams(MethodMetadata data, Method method, RequestMapping methodMapping) {
//
//    }
//
//    @Override
//    protected boolean processAnnotationsOnParameter(MethodMetadata data, Annotation[] annotations, int paramIndex) {
//        return super.processAnnotationsOnParameter(data, annotations, paramIndex);
//    }
//
//    private Map<Class<? extends Annotation>, AnnotatedParameterProcessor> toAnnotatedArgumentProcessorMap(
//            List<AnnotatedParameterProcessor> processors) {
//        Map<Class<? extends Annotation>, AnnotatedParameterProcessor> result = new HashMap<>();
//        for (AnnotatedParameterProcessor processor : processors) {
//            result.put(processor.getAnnotationType(), processor);
//        }
//        return result;
//    }
//
//    private List<AnnotatedParameterProcessor> getDefaultAnnotatedArgumentsProcessors() {
//
//        List<AnnotatedParameterProcessor> annotatedArgumentResolvers = new ArrayList<>();
//
//        annotatedArgumentResolvers.add(new PathVariableParameterProcessor());
//        annotatedArgumentResolvers.add(new RequestParamParameterProcessor());
//        annotatedArgumentResolvers.add(new RequestHeaderParameterProcessor());
//
//        return annotatedArgumentResolvers;
//    }
//
//    private void parseProduces(MethodMetadata md, Method method, RequestMapping annotation) {
//        checkAtMostOne(method, annotation.produces(), "produces");
//        String[] serverProduces = annotation.produces();
//        String clientAccepts = serverProduces.length == 0 ? null : emptyToNull(serverProduces[0]);
//        if (clientAccepts != null) {
//            md.template()
//                    .header(ACCEPT, clientAccepts);
//        }
//    }
//
//    private void parseConsumes(MethodMetadata md, Method method, RequestMapping annotation) {
//        checkAtMostOne(method, annotation.consumes(), "consumes");
//        String[] serverConsumes = annotation.consumes();
//        String clientProduces = serverConsumes.length == 0 ? null : emptyToNull(serverConsumes[0]);
//        if (clientProduces != null) {
//            md.template()
//                    .header(CONTENT_TYPE, clientProduces);
//        }
//    }
//
//    private void parseHeaders(MethodMetadata md, Method method, RequestMapping annotation) {
//        // TODO: only supports one header value per key
//        if (annotation.headers() != null && annotation.headers().length > 0) {
//            for (String header : annotation.headers()) {
//                int index = header.indexOf('=');
//                md.template().header(header.substring(0, index),
//                        header.substring(index + 1).trim());
//            }
//        }
//    }
//
//    private void checkAtMostOne(Method method, Object[] values, String fieldName) {
//        checkState(values != null && (values.length == 0 || values.length == 1),
//                "Method %s can only contain at most 1 %s field. Found: %s", method.getName(), fieldName,
//                values == null ? null : Arrays.asList(values));
//    }
//
//    private void checkOne(Method method, Object[] values, String fieldName) {
//        checkState(values != null && values.length == 1,
//                "Method %s can only contain 1 %s field. Found: %s", method.getName(),
//                fieldName, values == null ? null : Arrays.asList(values));
//    }
//
//}
