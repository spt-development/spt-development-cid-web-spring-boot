package com.spt.development.cid.web.spring.boot.autoconfigure;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * Configuration properties for
 * <a href="https://github.com/spt-development/spt-development-cid-web">spt-development/spt-development-cid-web</a>.
 */
@ConstructorBinding
@ConfigurationProperties(prefix = "spt.cid.web")
public class CidWebProperties {
    private final String cidHeader;
    private final Boolean useRequestHeader;
    private final MdcProperties mdc;
    private final RegistrationBean registrationBean;

    /**
     * Creates an object to encapsulate the spt.cid.web properties.
     *
     * @param cidHeader the name to use for the correlation ID header.
     * @param useRequestHeader flag to determine whether the {@link com.spt.development.cid.CorrelationId} should be
     *                         initialized from the correlation ID request header if it exists.
     * @param mdc object encapsulating the spt.cid.web.mdc properties.
     * @param registrationBean object encapsulating the spt.cid.web properties related to the
     *                         {@link com.spt.development.cid.web.filter.CorrelationIdFilter},
     *                         {@link org.springframework.boot.web.servlet.FilterRegistrationBean}.
     */
    public CidWebProperties(
            final String cidHeader,
            final Boolean useRequestHeader,
            final MdcProperties mdc,
            final RegistrationBean registrationBean) {

        this.cidHeader = cidHeader;
        this.useRequestHeader = useRequestHeader;
        this.mdc = Optional.ofNullable(mdc).orElse(new MdcProperties(null));
        this.registrationBean = Optional.ofNullable(registrationBean).orElse(new RegistrationBean(null, null, null));
    }

    /**
     * The name to use for the correlation ID header.
     *
     * @return correlation ID header name.
     */
    public String getCidHeader() {
        return cidHeader;
    }

    /**
     * A flag used to determine whether the {@link com.spt.development.cid.CorrelationId} should be initialized
     * from the correlation ID request header if it exists.
     *
     * @return the flag.
     */
    public Boolean getUseRequestHeader() {
        return useRequestHeader;
    }

    /**
     * An object encapsulating the spt.cid.web.mdc properties.
     *
     * @return the MDC properties.
     */
    public MdcProperties getMdc() {
        return mdc;
    }

    /**
     * An object encapsulating the spt.cid.web properties related to the
     * {@link org.springframework.boot.web.servlet.FilterRegistrationBean}.
     *
     * @return the registration bean properties.
     */
    public RegistrationBean getBean() {
        return registrationBean;
    }

    /**
     * Configuration properties for the spt.cid.web.mdc properties.
     */
    public static class MdcProperties {
        private final RegistrationBean registrationBean;

        /**
         * Creates an object to encapsulate the spt.cid.web.mdc properties.
         *
         * @param registrationBean object encapsulating the spt.cid.web.mdc properties related to the
         *                         {@link com.spt.development.cid.web.filter.MdcCorrelationIdFilter},
         *                         {@link org.springframework.boot.web.servlet.FilterRegistrationBean}.
         */
        public MdcProperties(final RegistrationBean registrationBean) {
            this.registrationBean = Optional.ofNullable(registrationBean)
                    .orElse(new RegistrationBean(null, null, null));
        }

        /**
         * An object encapsulating the spt.cid.web.mdc properties related to the
         * {@link org.springframework.boot.web.servlet.FilterRegistrationBean}.
         *
         * @return the registration bean properties.
         */
        public RegistrationBean getBean() {
            return registrationBean;
        }
    }

    /**
     * Configuration properties for {@link org.springframework.boot.web.servlet.FilterRegistrationBean}.
     */
    public static class RegistrationBean {
        private final String name;
        private final Integer order;
        private final List<String> urlPatterns;

        /**
         * Creates an object to encapsulate the spt.cid.web.registration-bean properties.
         *
         * @param name the name to assign the registration bean.
         * @param order the order of the registration bean.
         * @param urlPatterns the URL patterns that the filter will be registered against.
         */
        public RegistrationBean(final String name, final Integer order, final List<String> urlPatterns) {
            this.name = name;
            this.order = order;

            this.urlPatterns = Optional.ofNullable(urlPatterns)
                    .map(Collections::unmodifiableList)
                    .orElse(Collections.emptyList());
        }

        /**
         * The name to assign the registration bean.
         *
         * @return the name.
         */
        public String getName() {
            return name;
        }

        /**
         * The order of the registration bean.
         *
         * @return the order.
         */
        public Integer getOrder() {
            return order;
        }

        /**
         * The URL patterns that the filter will be registered against.
         *
         * @return the URL patterns.
         */
        public List<String> getUrlPatterns() {
            return urlPatterns;
        }
    }
}
