package com.bluepaint.bdlmod.client;

import com.google.common.collect.Lists;
import org.apache.logging.log4j.LogManager;

import java.util.ArrayList;
import java.util.List;

public class LightConfig {
    private final List<String> lights = new ArrayList<>();

    public void addString(String s) {
        this.lights.add(s);
    }

    public List<String> getList() {
        return this.lights;
    }
}
