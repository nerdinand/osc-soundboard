/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.nerdinand.soundboard;

import java.util.List;

/**
 *
 * @author thomasritter
 */
public class MultiToggleRow {
    private List<Sound> sounds;
    private int rowIndex;
    private MultiToggle multiToggle;
    
    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    public int getRowIndex() {
        return rowIndex;
    }

    public void setRowIndex(int rowIndex) {
        this.rowIndex = rowIndex;
    }

    public String getMultiToggleName() {
        return multiToggle.getName();
    }

    public void setParentObjects(MultiToggle multiToggle) {
        this.multiToggle = multiToggle;
        for (Sound sound : getSounds()) {
            sound.setRowParent(this);
        }
    }

    void setIndexes(int rowIndex) {
        setRowIndex(rowIndex);
        for (int i = 1; i <= getSounds().size(); i++) {
            getSounds().get(i - 1).setCellIndex(i);
        }
    }
    
}
