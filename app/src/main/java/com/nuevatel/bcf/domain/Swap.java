package com.nuevatel.bcf.domain;

import com.nuevatel.cf.appconn.Name;
import com.nuevatel.common.util.Parameters;

/**
 * Created by asalazar on 6/5/15.
 */
public class Swap {

    private Integer id;

    private String swapName;

    private Name name;

    public Swap(Integer id, String swapName, String name, Byte type) {
        Parameters.checkNull(id, "id");
        Parameters.checkBlankString(name, "name");
        Parameters.checkNull(type, "type");

        this.id = id;
        this.swapName = swapName;
        this.name = new Name(name, type);
    }

    public Integer getId() {
        return id;
    }

    public String getSwapName() {
        return swapName;
    }

    public Name getName() {
        return name;
    }
}
