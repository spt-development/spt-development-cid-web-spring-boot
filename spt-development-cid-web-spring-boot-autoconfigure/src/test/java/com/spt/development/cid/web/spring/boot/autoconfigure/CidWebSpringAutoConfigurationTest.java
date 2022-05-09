package com.spt.development.cid.web.spring.boot.autoconfigure;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.NoSuchBeanDefinitionException;
import org.springframework.boot.autoconfigure.context.PropertyPlaceholderAutoConfiguration;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.boot.web.servlet.context.AnnotationConfigServletWebApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.util.ReflectionTestUtils;

import javax.servlet.Filter;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import static com.spt.development.cid.web.filter.CorrelationIdFilter.CID_HEADER;
import static com.spt.development.cid.web.filter.MdcCorrelationIdFilter.MDC_CID_KEY;
import static com.spt.development.cid.web.spring.boot.autoconfigure.CidWebSpringAutoConfiguration.DEFAULT_URL_PATTERNS;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.CoreMatchers.nullValue;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class CidWebSpringAutoConfigurationTest {
    private AnnotationConfigServletWebApplicationContext context;

    @BeforeEach
    void init() {
        this.context = new AnnotationConfigServletWebApplicationContext();
    }

    @AfterEach
    void closeContext() {
        if (this.context != null) {
            this.context.close();
        }
    }

    @Nested
    class CorrelationIdFilterTest {
        private static final String BEAN_NAME = "correlationIdFilter";

        @Test
        void register_defaultConfig_shouldRegisterCorrelationIdFilterRegistrationBean() {
            context.register(CidWebSpringAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
            context.refresh();

            final FilterRegistrationBean<? extends Filter> result =
                    (FilterRegistrationBean<? extends Filter>) context.getBean(BEAN_NAME, FilterRegistrationBean.class);

            assertThat(result, is(notNullValue()));
            assertThat(result.getOrder(), is(Ordered.HIGHEST_PRECEDENCE));
            assertThat(result.getUrlPatterns(), is(new HashSet<>(DEFAULT_URL_PATTERNS)));
            assertThat(ReflectionTestUtils.getField(result, "name"), is(nullValue()));

            final Filter cidFilter = result.getFilter();

            assertThat(cidFilter, is(notNullValue()));
            assertThat(ReflectionTestUtils.getField(cidFilter, "cidHeader"), is(CID_HEADER));
            assertThat(ReflectionTestUtils.getField(cidFilter, "useRequestHeader"), is(false));
        }

        @Test
        void register_customConfig_shouldRegisterCorrelationIdFilterRegistrationBean() {
            final String cidHeader = "test-correlation-id";
            final Boolean useRequestHeader = true;
            final Integer order = 0;
            final List<String> urlPatterns = Arrays.asList("/api/v1.0/*", "/oauth/*");
            final String regBeanName = "testCidRegBeanName";

            TestPropertyValues.of(
                    "spt.cid.web.cid-header:" + cidHeader,
                    "spt.cid.web.use-request-header:" + useRequestHeader,
                    "spt.cid.web.registration-bean.name:" + regBeanName,
                    "spt.cid.web.registration-bean.order:" + order,
                    "spt.cid.web.registration-bean.url-patterns:" + String.join(",", urlPatterns)
            ).applyTo(context);

            context.register(CidWebSpringAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
            context.refresh();

            final FilterRegistrationBean<? extends Filter> result =
                    (FilterRegistrationBean<? extends Filter>) context.getBean(BEAN_NAME, FilterRegistrationBean.class);

            assertThat(result, is(notNullValue()));
            assertThat(result.getOrder(), is(order));
            assertThat(result.getUrlPatterns(), is(new HashSet<>(urlPatterns)));
            assertThat(ReflectionTestUtils.getField(result, "name"), is(regBeanName));

            final Filter cidFilter = result.getFilter();

            assertThat(cidFilter, is(notNullValue()));
            assertThat(ReflectionTestUtils.getField(cidFilter, "cidHeader"), is(cidHeader));
            assertThat(ReflectionTestUtils.getField(cidFilter, "useRequestHeader"), is(useRequestHeader));
        }
    }

    @Nested
    class MdcCorrelationIdFilterTest {
        private static final String BEAN_NAME = "mdcCorrelationIdFilter";

        @Test
        void register_defaultConfig_shouldRegisterMdcCorrelationIdFilterRegistrationBean() {
            context.register(CidWebSpringAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
            context.refresh();

            final FilterRegistrationBean<? extends Filter> result =
                    (FilterRegistrationBean<? extends Filter>) context.getBean(BEAN_NAME, FilterRegistrationBean.class);

            assertThat(result, is(notNullValue()));
            assertThat(result.getOrder(), is(Ordered.HIGHEST_PRECEDENCE + 1));
            assertThat(result.getUrlPatterns(), is(new HashSet<>(DEFAULT_URL_PATTERNS)));
            assertThat(ReflectionTestUtils.getField(result, "name"), is(nullValue()));

            final Filter mdcCidFilter = result.getFilter();

            assertThat(mdcCidFilter, is(notNullValue()));
            assertThat(ReflectionTestUtils.getField(mdcCidFilter, "cidKey"), is(MDC_CID_KEY));
        }

        @Test
        void register_customConfig_shouldRegisterMdcCorrelationIdFilterRegistrationBean() {
            final String cidKey = "test-correlation-id";
            final Integer order = 0;
            final List<String> urlPatterns = Arrays.asList("/api/v1.0/*", "/oauth/*");
            final String regBeanName = "testMdcCidRegBeanName";

            TestPropertyValues.of(
                    "spt.cid.mdc.disabled:false",
                    "spt.cid.mdc.cid-key:" + cidKey,
                    "spt.cid.web.mdc.registration-bean.name:" + regBeanName,
                    "spt.cid.web.mdc.registration-bean.order:" + order,
                    "spt.cid.web.mdc.registration-bean.url-patterns:" + String.join(",", urlPatterns)
            ).applyTo(context);

            context.register(CidWebSpringAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
            context.refresh();

            final FilterRegistrationBean<? extends Filter> result =
                    (FilterRegistrationBean<? extends Filter>) context.getBean(BEAN_NAME, FilterRegistrationBean.class);

            assertThat(result, is(notNullValue()));
            assertThat(result.getOrder(), is(order));
            assertThat(result.getUrlPatterns(), is(new HashSet<>(urlPatterns)));
            assertThat(ReflectionTestUtils.getField(result, "name"), is(regBeanName));

            final Filter cidFilter = result.getFilter();

            assertThat(cidFilter, is(notNullValue()));
            assertThat(ReflectionTestUtils.getField(cidFilter, "cidKey"), is(cidKey));
        }

        @Test
        void register_mdcDisabled_shouldNotRegisterMdcCorrelationIdFilterRegistrationBean() {
            TestPropertyValues.of(
                    "spt.cid.mdc.disabled:true"
            ).applyTo(context);

            context.register(CidWebSpringAutoConfiguration.class, PropertyPlaceholderAutoConfiguration.class);
            context.refresh();

            final NoSuchBeanDefinitionException result = assertThrows(
                    NoSuchBeanDefinitionException.class, () -> context.getBean(BEAN_NAME)
            );

            assertThat(result, is(notNullValue()));
            assertThat(result.getMessage(), is("No bean named 'mdcCorrelationIdFilter' available"));
        }
    }
}