package com.aem.practice.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = {Resource.class},
        adapters = UserDetailsModel.class,
        resourceType = "practice/components/userdetails",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class UserDetailsModel {
    @ValueMapValue
    String fname;
    @ValueMapValue
    String lname;
    @ValueMapValue
    String dob;
    @ValueMapValue
    String gender;
    @ValueMapValue
    String mobile;
    @ValueMapValue
    String email;
    @ValueMapValue
    String address;

    public String getFname() {
        return fname;
    }

    public String getLname() {
        return lname;
    }

    public String getDob() {
        return dob;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public String getEmail() {
        return email;
    }

    public String getAddress() {
        return address;
    }
}
