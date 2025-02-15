package com.rayan.ecommerce.config;

import com.rayan.ecommerce.entity.Product;
import com.rayan.ecommerce.entity.ProductCategory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.http.HttpMethod;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

import jakarta.persistence.EntityManager;
import jakarta.persistence.metamodel.EntityType;

import java.util.*;

@Configuration
public class MyDataConfig implements RepositoryRestConfigurer {

    private EntityManager entityManager;


    @Autowired
    public MyDataConfig(EntityManager theEntityManager){
        this.entityManager = theEntityManager;
    }


    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        RepositoryRestConfigurer.super.configureRepositoryRestConfiguration(config, cors);

        HttpMethod[] theUnsupportedAction = {HttpMethod.PUT, HttpMethod.POST, HttpMethod.DELETE};

        // disable HTTP methods for Product: PUT, POST and Delete.
        config.getExposureConfiguration()
                .forDomainType(Product.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction));

        // disable HTTP methods for ProductCategory: PUT, POST and Delete.
        config.getExposureConfiguration()
                .forDomainType(ProductCategory.class)
                .withItemExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction))
                .withCollectionExposure((metdata, httpMethods) -> httpMethods.disable(theUnsupportedAction));

        // call an internal helper method
        exposeIds(config);
    }

    private void exposeIds(RepositoryRestConfiguration config) {
        //
        // expose entity ids
        //

        // - get a list of all entity classes from the entity manager.
        Set<EntityType<?>> entities = entityManager.getMetamodel().getEntities();
       // - create an array of the entity types.
        List<Class> entityClasses = new ArrayList<>();

        // - get the entity types for the entities.
        for(EntityType tempEntityType : entities){
            entityClasses.add(tempEntityType.getJavaType());

            // - expose th entity ids for the array of entity/domain types.
            Class[] domainType = entityClasses.toArray(new Class[0]);
            config.exposeIdsFor(domainType);
        }
    }
}
