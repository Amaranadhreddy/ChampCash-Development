package com.tms.govt.champcash.home.report;

import java.io.Serializable;

/**
 * Created by govt on 19-04-2017.
 */

public class SideMenu implements Serializable {

    String name;
    int icon;

    public SideMenu(String name, int icon){
        this.name = name;
        this.icon = icon;
    }

    public String getName(){
        return this.name;
    }

    public int getIcon(){
        return this.icon;
    }
}
