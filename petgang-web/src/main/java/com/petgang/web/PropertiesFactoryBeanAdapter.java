package com.petgang.web;

import java.io.IOException;

import org.springframework.beans.factory.config.PropertiesFactoryBean;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;

public class PropertiesFactoryBeanAdapter extends PropertiesFactoryBean {

    private String locationParttern = "";

    private PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();

    public String getLocationParttern() {
        return locationParttern;
    }

    public void setLocationParttern(String locationParttern) throws IOException {
        this.locationParttern = locationParttern;
        Resource[] resourcess = resolver.getResources(locationParttern);
        super.setLocations(resourcess);
    }
}
