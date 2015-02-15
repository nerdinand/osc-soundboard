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

import com.illposed.osc.OSCMessage;
import com.illposed.osc.OSCPortIn;
import com.illposed.osc.OSCPortOut;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.embed.swing.JFXPanel;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

/**
 *
 * @author Ferdinand Niedermann
 */
public class Soundboard {

    private static boolean exitRequested = false;
    private Config config;

    private static OSCPortOut sender;
    private static OSCPortIn receiver;

    static void sendOffMessage(PlaySoundListener playSoundListener) {
        String address = playSoundListener.getSound().getOscAddress();
        Logger.getLogger(PlaySoundListener.class.getName()).log(Level.INFO, "Sending OFF message: " + address);
        sendOffMessage(address);
    }

    private static void sendOffMessage(String address) {
        OSCMessage msg = new OSCMessage(address);
        msg.addArgument(0);
        try {
            sender.send(msg);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Off message could not be sent.", ex);
        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        // this is needed in order to properly initialize javafx media player stuff, 
        // otherwise we get an java.lang.IllegalStateException: Toolkit not initialized
        // when trying to play back media
        JFXPanel fxPanel = new JFXPanel();

        Soundboard soundBoard = new Soundboard(args[0]);
        soundBoard.go();

    }

    private Soundboard(String configPath) {
        try {
            this.config = loadConfig(configPath);

        } catch (Exception ex) {
            getLogger().log(Level.SEVERE, "A terrible error occured!", ex);
        }

    }

    private static Logger getLogger() {
        return Logger.getLogger(Soundboard.class.getName());
    }

    private void go() throws Exception {

        receiver = getReceiver(config.getIncomingPort());
        sender = getSender(config.getDeviceAddress(), config.getOutgoingPort());

        addListeners();
        initializeToggleButtons();

        getLogger().log(Level.INFO, "READY!");

        getLogger().log(Level.INFO, "If stuff does not work, check these settings:");
        getLogger().log(Level.INFO, "Sending to: " + config.getDeviceAddress() + ":" + config.getOutgoingPort());
        getLogger().log(Level.INFO, "Listening on port: " + config.getIncomingPort());

        receiver.startListening();

        while (!exitRequested) {
            try {
                Thread.sleep(5);
            } catch (InterruptedException ex) {
            }
            Thread.yield();
        }
    }

    private void addListeners() {
        getLogger().log(Level.INFO, "Adding Listeners for addresses...");
        for (Sound sound : config.getMultiToggle().getSounds()) {
            PlaySoundListener listener = new PlaySoundListener(sound);
            receiver.addListener(sound.getOscAddress(), listener);
        }
    }

    private void initializeToggleButtons() {
        getLogger().log(Level.INFO, "Initializing toggle buttons...");
        for (Sound sound : config.getMultiToggle().getSounds()) {
            sendOffMessage(sound.getOscAddress());
            sendLabelMessage(sound);
        }
    }

    private OSCPortOut getSender(String deviceAddress, int port) throws Exception {
        try {
            InetAddress address = InetAddress.getByName(deviceAddress);
            OSCPortOut sender = new OSCPortOut(address, port);
            return sender;
        } catch (UnknownHostException | SocketException ex) {
            throw new Exception("Could not open sending socket to " + deviceAddress + ":" + port, ex);
        }
    }

    private OSCPortIn getReceiver(int port) throws Exception {
        InetAddress address = InetAddress.getByName("0.0.0.0");

        try {
            DatagramSocket socket = new DatagramSocket(new InetSocketAddress(address, port));
            return new OSCPortIn(socket);
        } catch (SocketException ex) {
            throw new Exception("Could not open listening socket on" + address + " on port " + port, ex);
        }
    }

    private Config loadConfig(String configPath) throws Exception {
        Yaml yaml = new Yaml(new Constructor(Config.class));
        Config config;
        try {
            config = (Config) yaml.load(new FileInputStream(configPath));
        } catch (FileNotFoundException ex) {
            throw new Exception("Soundboard config file was not found!", ex);
        }

        config.buildConfigurationTree();

        return config;
    }

    private void debugSoundObjects(Config config) {
        System.out.println("sounds:");
        for (Sound sound : config.getMultiToggle().getSounds()) {
            System.out.println("sound = " + sound.toString());
        }
    }

    public static void sendLabelMessage(Sound sound) {
        String name = sound.getName();

        if (name == null) {
            name = "";
        }

        OSCMessage msg = new OSCMessage(sound.getLabelAddress());
        msg.addArgument(name);

        try {
            Thread.sleep(15);
        } catch (InterruptedException ex) {
            Logger.getLogger(Soundboard.class.getName()).log(Level.SEVERE, null, ex);
        }

        try {
            sender.send(msg);
        } catch (IOException ex) {
            getLogger().log(Level.SEVERE, "Label message could not be sent.", ex);
        }
    }
}
