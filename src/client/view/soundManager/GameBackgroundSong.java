package client.view.soundManager;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class GameBackgroundSong implements LineListener {
    private Clip audioClip;
    private FloatControl volumeControl;

    public GameBackgroundSong() {
        try {
            File audioFile = new File("resources/songs/background_sound.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
            AudioFormat format = audioStream.getFormat();
            DataLine.Info info = new DataLine.Info(Clip.class, format);
            audioClip = (Clip) AudioSystem.getLine(info);
            audioClip.addLineListener(this);
            audioClip.open(audioStream);
            volumeControl = (FloatControl) audioClip.getControl(FloatControl.Type.MASTER_GAIN);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (audioClip != null && !audioClip.isRunning()) {
            audioClip.setFramePosition(0);
            audioClip.start();
            audioClip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void setVolume(int volume) {
        if (volumeControl != null) {
            float range = volumeControl.getMaximum() - volumeControl.getMinimum();
            float gain = (range * volume / 100) + volumeControl.getMinimum();
            volumeControl.setValue(gain);
        }
    }

    @Override
    public void update(LineEvent event) {
        LineEvent.Type type = event.getType();
        if (type == LineEvent.Type.STOP) {
            audioClip.setFramePosition(0);
            audioClip.start();
        }
    }
}

