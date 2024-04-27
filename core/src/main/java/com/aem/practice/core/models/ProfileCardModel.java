package com.aem.practice.core.models;

import com.aem.practice.core.services.ProfileCardService;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.OSGiService;

import javax.annotation.PostConstruct;

@Model(
        adaptables = {Resource.class},
        adapters = ProfileCardModel.class,
        resourceType = "aempractice/components/osgiconfigservices",
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProfileCardModel {
    @OSGiService
    private ProfileCardService profileCardService;
    private String facebook;
    private String twitter;
    private String linkedin;
    private String instagram;
    private String github;

    public String getFacebook() {
        return facebook;
    }

    public String getTwitter() {
        return twitter;
    }

    public String getLinkedin() {
        return linkedin;
    }

    public String getInstagram() {
        return instagram;
    }

    public String getGithub() {
        return github;
    }

    @PostConstruct
    public void init() {
        facebook = profileCardService.getFacebook();
        twitter = profileCardService.getTwitter();
        linkedin = profileCardService.getLinkedin();
        instagram = profileCardService.getInstagram();
        github = profileCardService.getGithub();
    }
}
