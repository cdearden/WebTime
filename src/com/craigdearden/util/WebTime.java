/* Author:      Craig Dearden
 * Date:        Jun 29, 2016
 * Name:        WebTime.java
 * Description: 
 */
package com.craigdearden.util;

import java.io.IOException;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

/**
 * This class creates a thread that queries a website for the time at a
 * specified frequency
 */
public final class WebTime implements Runnable {

    /**
     * The thread that this runs in.
     */
    private Thread _t = null;

    /**
     * The name of the thread.
     */
    private String _threadName = null;

    /**
     * Holds the time retrieved from the website
     */
    private String _time = null;

    /**
     * The frequency at which WebTime queries the website (in seconds).
     */
    private long _frequency;

    WebTime() {
        this("webtimeThread", 1);
    }

    WebTime(String threadName, long frequency) {
        _threadName = threadName;
        _frequency = frequency * 1000;
        retrieveTime();
    }

    /**
     * Retrieves the time from http://time.is/ and stores it in {@link #_time}.
     * If this method is unable to connect to the site "Time unavailable..." is
     * stored in {@link #_time}.
     */
    private void retrieveTime() {
        String url = "http://time.is/";
        Document doc = null;
        try {
            doc = Jsoup.connect(url).get();
        } catch (IOException ex) {
            System.out.println("Oops");
        }

        try {
            _time = doc.getElementById("twd").text();
        } catch (NullPointerException ex) {
            _time = "Time unavailable...";
        }
    }

    /**
     * Invokes the <code>retrieveTime</code> and <code>displayTime</code> at
     * frequency {@link #_frequency}.
     */
    public void run() {
        try {
            while (true) {
                retrieveTime();
                displayTime();
                Thread.sleep(_frequency);
            }
        } catch (InterruptedException ex) {
            System.out.println("Thread interrupted.");
        }
    }

    /**
     * New thread is created and started.
     */
    public void start() {
        if (_t == null) {
            _t = new Thread(this, _threadName);
            _t.start();
        } else {
            _t.start();
        }
    }

    /**
     * Interrupts the thread.
     */
    public void stop() {
        _t.interrupt();
    }

    /**
     * @return the frequency
     */
    public long getFrequency() {
        return _frequency / 1000;
    }

    /**
     * @param _frequency the frequency to set
     */
    public void setFrequency(long frequency) {
        _frequency = frequency * 1000;
    }

    /**
     * Displays the time in the console.
     */
    public void displayTime() {
        System.out.println(_time);
    }
}
