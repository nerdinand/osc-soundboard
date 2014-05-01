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
    
    public List<Sound> getSounds() {
        return sounds;
    }

    public void setSounds(List<Sound> sounds) {
        this.sounds = sounds;
    }

    void generateOscAddressesFromPath(String name, int rowIndex) {
        for (int i = 1; i <= getSounds().size(); i++) {
            getSounds().get(i - 1).setOscAddress(name + "/" + i + "/" + rowIndex);
        }
    }
    
}
