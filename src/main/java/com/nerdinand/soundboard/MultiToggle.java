/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nerdinand.soundboard;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author thomasritter
 */
public class MultiToggle {
    private String name;
    private List<MultiToggleRow> multiToggleRows;
    private List<Sound> collectedSounds;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public List<MultiToggleRow> getMultiToggleRows() {
        return multiToggleRows;
    }

    public void setMultiToggleRows(List<MultiToggleRow> multiToggleRows) {
        this.multiToggleRows = multiToggleRows;
    }

    public List<Sound> getSounds() {
        if (collectedSounds == null) {
            collectedSounds = new ArrayList<>();
            for (MultiToggleRow soundRow : getMultiToggleRows()) {
                collectedSounds.addAll(soundRow.getSounds());
            }
        }
        return collectedSounds;
    }
    
    public void buildConfigurationTree() {
        for (int i = 1; i <= getMultiToggleRows().size(); i++) {
            getMultiToggleRows().get(i - 1).setIndexes(i);
            getMultiToggleRows().get(i - 1).setParentObjects(this);
        }
    }
    
}
