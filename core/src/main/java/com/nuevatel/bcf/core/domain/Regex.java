package com.nuevatel.bcf.core.domain;

import java.util.regex.Pattern;

/**
 * Created by asalazar on 6/5/15.
 */
public class Regex {

    private Integer id;

    private String name;

    private Pattern pattern;

    private Media newMedia;

    private Swap swap;

    public Regex(Integer id,
                 String name,
                 Pattern pattern,
                 Media newMedia,
                 Swap swap) {
        this.id = id;
        this.name = name;
        this.pattern = pattern;
        this.newMedia = newMedia;
        this.swap = swap;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Pattern getPattern() {
        return pattern;
    }

    public Media getNewMedia() {
        return newMedia;
    }

    public Swap getSwap() {
        return swap;
    }
}
