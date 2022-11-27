package com.spt.development.cid.web.spring.boot.autoconfigure;

import com.spt.development.cid.web.filter.CorrelationIdFilter;
import com.spt.development.cid.web.filter.MdcCorrelationIdFilter;
import jakarta.servlet.Servlet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.boot.autoconfigure.web.servlet.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.core.Ordered;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.spt.development.cid.web.filter.CorrelationIdFilter.CID_HEADER;
import static com.spt.development.cid.web.filter.MdcCorrelationIdFilter.MDC_CID_KEY;

/**
 * {@link AutoConfiguration Auto-Configuration} for
 * <a href="https://github.com/spt-development/spt-development-cid-web">spt-development/spt-development-cid-web</a>.
 */
@AutoConfiguration(after = WebMvcAutoConfiguration.class)
@ConditionalOnWebApplication(
        type = ConditionalOnWebApplication.Type.SERVLET
)
@ConditionalOnClass({ Servlet.class })
@EnableConfigurationProperties(CidWebProperties.class)
public class CidWebSpringAutoConfiguration {
    static final List<String> DEFAULT_URL_PATTERNS = Collections.singletonList("/*");

    private final CidWebProperties cidWebProperties;

    /**
     * Creates a new instance of the configuration bean with the spt.cid.web configurable properties.
     *
     * @param cidWebProperties an object encapsulating the spt.cid.web properties.
     */
    public CidWebSpringAutoConfiguration(final CidWebProperties cidWebProperties) {
        this.cidWebProperties = cidWebProperties;
    }

    /**
     * Creates a {@link FilterRegistrationBean} for {@link CorrelationIdFilter} based on configuration.
     *
     * @return a new {@link FilterRegistrationBean} bean.
     */
    @Bean
    public FilterRegistrationBean<CorrelationIdFilter> correlationIdFilter() {
        final FilterRegistrationBean<CorrelationIdFilter> filterRegBean = new FilterRegistrationBean<>(
                new CorrelationIdFilter(
                        Optional.ofNullable(cidWebProperties.getCidHeader()).orElse(CID_HEADER),
                        Optional.ofNullable(cidWebProperties.getUseRequestHeader()).orElse(false)
                )
        );

        if (cidWebProperties.getBean().getName() != null) {
            filterRegBean.setName(cidWebProperties.getBean().getName());
        }
        filterRegBean.setOrder(Optional.ofNullable(cidWebProperties.getBean().getOrder()).orElse(Ordered.HIGHEST_PRECEDENCE));
        filterRegBean.setUrlPatterns(
                cidWebProperties.getBean().getUrlPatterns().isEmpty() ?
                        DEFAULT_URL_PATTERNS :
                        cidWebProperties.getBean().getUrlPatterns()
        );

        return filterRegBean;
    }

    /**
     * Creates a {@link FilterRegistrationBean} for {@link MdcCorrelationIdFilter} based on configuration.
     *
     * @return a new {@link FilterRegistrationBean} bean.
     */
    @Bean
    @ConditionalOnProperty(name = "spt.cid.mdc.disabled", havingValue = "false", matchIfMissing = true)
    public FilterRegistrationBean<MdcCorrelationIdFilter> mdcCorrelationIdFilter(@Value("${spt.cid.mdc.cid-key:#{null}}") String mdcCidKey) {
        final FilterRegistrationBean<MdcCorrelationIdFilter> filterRegBean = new FilterRegistrationBean<>(
                new MdcCorrelationIdFilter(Optional.ofNullable(mdcCidKey).orElse(MDC_CID_KEY))
        );

        final CidWebProperties.RegistrationBean mdcFilterRegBeanProperties = cidWebProperties.getMdc().getBean();

        if (mdcFilterRegBeanProperties.getName() != null) {
            filterRegBean.setName(mdcFilterRegBeanProperties.getName());
        }

        // If the order is not explicitly set, it should come after the CorrelationIdFilter.
        filterRegBean.setOrder(Optional.ofNullable(mdcFilterRegBeanProperties.getOrder()).orElse(
                Optional.ofNullable(cidWebProperties.getBean().getOrder()).orElse(Ordered.HIGHEST_PRECEDENCE) + 1)
        );

        filterRegBean.setUrlPatterns(
                mdcFilterRegBeanProperties.getUrlPatterns().isEmpty() ?
                        DEFAULT_URL_PATTERNS :
                        mdcFilterRegBeanProperties.getUrlPatterns()
        );

        return filterRegBean;
    }
}
