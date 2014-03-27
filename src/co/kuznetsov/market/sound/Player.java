package co.kuznetsov.market.sound;

import javax.sound.sampled.*;
import java.io.IOException;
import java.io.InputStream;

public class Player {

    private final static int EXTERNAL_BUFFER_SIZE = 524288; // 128Kb

    private final static Player PLAYER_INSTANCE = new Player();

    enum Position {
        LEFT, RIGHT, NORMAL
    }

    public void play(String wavResource) throws IOException {
        play(wavResource, Position.NORMAL);
    }

    public void play(String wavResource, Position position) throws IOException {
        try (InputStream snd = ClassLoader.getSystemClassLoader().getResourceAsStream(wavResource);
             AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(snd)) {

            AudioFormat format = audioInputStream.getFormat();
            SourceDataLine auline = null;
            DataLine.Info info = new DataLine.Info(SourceDataLine.class, format);

            try {
                auline = (SourceDataLine) AudioSystem.getLine(info);
                auline.open(format);
                if (auline.isControlSupported(FloatControl.Type.PAN)) {
                    FloatControl pan = (FloatControl) auline.getControl(FloatControl.Type.PAN);
                    switch (position) {
                        case RIGHT:
                            pan.setValue(1.0f);
                            break;
                        case LEFT:
                            pan.setValue(-1.0f);
                            break;
                        default:
                    }
                }

                auline.start();
                int nBytesRead = 0;
                byte[] abData = new byte[EXTERNAL_BUFFER_SIZE];

                while (nBytesRead != -1) {
                    nBytesRead = audioInputStream.read(abData, 0, abData.length);
                    if (nBytesRead >= 0)
                        auline.write(abData, 0, nBytesRead);
                }
            } finally {
                if (auline != null) {
                    auline.drain();
                    auline.close();
                }
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
    }

    public static void playAsync(final String wavResource) {
        synchronized (PLAYER_INSTANCE) {
            new Thread() {
                @Override
                public void run() {
                    try {
                        synchronized (PLAYER_INSTANCE) {
                            PLAYER_INSTANCE.play(wavResource);
                        }
                    } catch (IOException e) {
                        e.printStackTrace(System.err);
                    }
                }
            }.start();
        }
    }
} 