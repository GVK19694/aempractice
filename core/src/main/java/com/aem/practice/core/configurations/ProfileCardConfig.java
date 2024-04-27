package com.aem.practice.core.configurations;

import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;

@ObjectClassDefinition(
        name = "Practice Profile Card",
        description = "Practice Profile Card Configuration"
)
public @interface ProfileCardConfig {
    @AttributeDefinition(
            name = "Facebook",
            description = "Facebook URL"
    )
    String facebook();
    @AttributeDefinition(
            name = "Instagram",
            description = "Instagram URL"
    )
    String instagram();
    @AttributeDefinition(
            name = "Twitter",
            description = "Twitter URL"
    )
    String twitter();
    @AttributeDefinition(
            name = "LinkedIn",
            description = "LinkedIn URL"
    )
    String linkedin();
    @AttributeDefinition(
            name = "GitHub",
            description = "GitHub URL"
    )
    String github();
}
