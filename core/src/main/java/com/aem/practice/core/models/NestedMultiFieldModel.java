package com.aem.practice.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;

import javax.inject.Inject;
import java.util.List;

@Model(
      adaptables = {Resource.class},
      defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public interface NestedMultiFieldModel {
    @Inject
    List<User> getUser();

    @Model(
            adaptables = {Resource.class},
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    interface User {
        @Inject
        String getFname();

        @Inject
        String getLname();

        @Inject
        String getAddress();

        @Inject
        String getDob();

        @Inject
        String getGender();

        @Inject
        List<Contact> getContact();

        @Model(
                adaptables = {Resource.class},
                defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
        )
        interface Contact {
            @Inject
            String getMobile();

            @Inject
            String getEmail();
        }
    }
}
