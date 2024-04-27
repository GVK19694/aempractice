package com.aem.practice.core.models;

import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public interface MultiFieldModel {
    @ChildResource
    List<User> getUser();

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    interface User {
        @ValueMapValue
        String getFname();
        @ValueMapValue
        String getLname();
        @ValueMapValue
        String getDob();
        @ValueMapValue
        String getGender();
        @ValueMapValue
        String getMobile();
        @ValueMapValue
        String getEmail();
        @ValueMapValue
        String getAddress();
    }
}