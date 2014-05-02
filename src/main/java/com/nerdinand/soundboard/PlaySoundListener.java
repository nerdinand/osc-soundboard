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

import com.illposed.osc.OSCListener;
import com.illposed.osc.OSCMessage;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.media.Media;
import javafx.scene.media.MediaPlayer;

/**
 *
 * @author Ferdinand Niedermann
 */
public class PlaySoundListener implements OSCListener {

    private final Sound sound;
    private MediaPlayer mediaPlayer;

    public PlaySoundListener(Sound sound) {
        this.sound = sound;
        try {
            URL url = new URL("file://" + sound.getSoundPath());

            Logger.getLogger(PlaySoundListener.class.getName()).log(Level.INFO, "Loading sound: " + url.toString());

            Media media = new Media(url.toString());
            mediaPlayer = new MediaPlayer(media);
            mediaPlayer.setOnEndOfMedia(createOnEndOfMedia());
        } catch (MalformedURLException ex) {
            Logger.getLogger(PlaySoundListener.class.getName()).log(Level.SEVERE, "Could not load sound", ex);
        }
    }

    @Override
    public void acceptMessage(Date date, OSCMessage oscm) {
        float value = (float) oscm.getArguments().get(0);

        if (value == 1.0) {
            Logger.getLogger(PlaySoundListener.class.getName()).log(Level.INFO, "Received from " + oscm.getAddress() + ": ON");
            stopSound();
            playSound();
        } else {
            Logger.getLogger(PlaySoundListener.class.getName()).log(Level.INFO, "Received from " + oscm.getAddress() + ": OFF");
            stopSound();
        }

        Soundboard.sendLabelMessage(getSound());
    }

    private void playSound() {
        Logger.getLogger(PlaySoundListener.class.getName()).log(Level.INFO, "Playing " + getSound().getSoundPath());
        mediaPlayer.play();
    }

    private void stopSound() {
        mediaPlayer.stop();
    }

    private Runnable createOnEndOfMedia() {
        return () -> {
            Soundboard.sendOffMessage(this);
        };
    }

    public Sound getSound() {
        return this.sound;
    }

}
