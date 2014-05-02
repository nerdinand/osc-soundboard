/*
 * The MIT License
 *
 * Copyright 2014 Ferdinand Niedermann.
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package com.nerdinand.soundboard;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Sound {

    private String name;
    private String soundPath;
    private MultiToggleRow rowParent;
    private int cellIndex;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOscAddress() {
        return getRowParent().getMultiToggleName() + "/" + getCellIndex() + "/" + getRowParent().getRowIndex();
    }

    public String getSoundPath() {
        return soundPath;
    }

    public void setSoundPath(String soundPath) {
        this.soundPath = soundPath;
    }

    @Override
    public String toString() {
        return "osc: " + getOscAddress() + ", soundPath: " + getSoundPath();
    }

    public MultiToggleRow getRowParent() {
        return rowParent;
    }

    void setRowParent(MultiToggleRow multiToggleRow) {
        this.rowParent = multiToggleRow;
    }

    void setCellIndex(int cellIndex) {
        this.cellIndex = cellIndex;
    }

    public int getCellIndex() {
        return cellIndex;
    }

    public String getLabelAddress() {
        return "/1/label" + getRowParent().getRowIndex() + "" + getCellIndex();
    }

}
