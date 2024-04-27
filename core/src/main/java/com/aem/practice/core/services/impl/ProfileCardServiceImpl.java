package com.aem.practice.core.services.impl;

import com.aem.practice.core.configurations.ProfileCardConfig;
import com.aem.practice.core.services.ProfileCardService;
import org.osgi.framework.Constants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.metatype.annotations.Designate;

@Component(
        service = ProfileCardService.class,
        immediate = true,
        property = {
                Constants.SERVICE_ID + "=Practice Services & Config",
                Constants.SERVICE_DESCRIPTION + "=This service reads values from Profile Card Config",
        }
)
@Designate(ocd = ProfileCardConfig.class)
public class ProfileCardServiceImpl implements ProfileCardService {
    private ProfileCardConfig configuration;
    @Activate
    protected void activate(ProfileCardConfig config) {
        this.configuration = config;
    }
    @Override
    public String getFacebook() {
        return configuration.facebook();
    }

    @Override
    public String getTwitter() {
        return configuration.twitter();
    }

    @Override
    public String getLinkedin() {
        return configuration.linkedin();
    }

    @Override
    public String getInstagram() {
        return configuration.instagram();
    }

    @Override
    public String getGithub() {
        return configuration.github();
    }
}
