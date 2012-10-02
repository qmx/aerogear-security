/*
 * JBoss, Home of Professional Open Source
 * Copyright 2012, Red Hat, Inc., and individual contributors
 * by the @authors tag. See the copyright.txt in the distribution for a
 * full listing of individual contributors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.jboss.aerogear.security.config;

import org.jboss.picketlink.idm.internal.JPAIdentityStore;
import org.jboss.picketlink.idm.internal.jpa.JPATemplate;
import org.jboss.picketlink.idm.spi.IdentityStore;
import org.picketbox.cdi.config.CDIConfigurationBuilder;
import org.picketbox.core.config.ConfigurationBuilder;

import javax.enterprise.inject.Produces;
import javax.enterprise.inject.spi.BeanManager;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

/**
 * <p>Bean responsible for producing the {@link org.picketbox.cdi.config.CDIConfigurationBuilder}.</p>
 *
 * @author <a href="mailto:psilva@redhat.com">Pedro Silva</a>
 */
public class PicketBoxConfigurer {

    public static final int TIMEOUT_IN_MINUTES = 30;

    @Produces
    @PersistenceContext
    private EntityManager entityManager;

    @Inject
    private BeanManager beanManager;

    /**
     * <p>Produces the {@link org.picketbox.core.config.ConfigurationBuilder}.</p>
     *
     * @return
     */
    //TODO must be configurable by user
    @Produces
    public ConfigurationBuilder produceConfiguration() {
        CDIConfigurationBuilder builder = new CDIConfigurationBuilder(beanManager);

        builder
                .authentication()
                .idmAuthentication()
                .identityManager()
                .providedStore()
                .sessionManager()
                .sessionTimeout(TIMEOUT_IN_MINUTES)
                .inMemorySessionStore();

        return builder;
    }

    @Produces
    public IdentityStore createIdentityStore() {
        JPAIdentityStore identityStore = new JPAIdentityStore();

        JPATemplate jpaTemplate = new JPATemplate();

        jpaTemplate.setEntityManager(entityManager);

        identityStore.setJpaTemplate(jpaTemplate);

        return identityStore;
    }
}