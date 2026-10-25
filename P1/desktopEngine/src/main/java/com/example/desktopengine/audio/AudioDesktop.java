package com.example.desktopengine.audio;
import java.io.File;
import java.io.IOException;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioFormat;
import com.example.engine.audio.AudioInterface;
import com.example.engine.audio.EngSound;
import javax.sound.sampled.*;
import javax.sound.sampled.Clip;


public class AudioDesktop implements AudioInterface {

    private AudioPool pool;

    final AudioFormat audioFormat = new AudioFormat(44100, 8, 1, true, true);

    final int bufferSize = 1024*8;
    public AudioDesktop()
    {
        pool = new AudioPool();
    }
    private int addClip(File audioFile, int loops)
    {
        try {
            AudioInputStream original =AudioSystem.getAudioInputStream(audioFile);
            AudioInputStream converted = AudioSystem.getAudioInputStream(audioFormat, original);


            Clip auxiliarClip = AudioSystem.getClip();
            auxiliarClip.open(converted);
            auxiliarClip.setFramePosition(0);
            if(loops>=0){
                auxiliarClip.loop(loops);
            }
            else
            {
                auxiliarClip.loop(Clip.LOOP_CONTINUOUSLY);
            }
            return pool.addSound(auxiliarClip);
        } catch (Exception e) {
            System.err.println("Couldn't load audio file");
        }
        return -1;
    }

    @Override
    public SoundDesktop addSound(String soundName, int loops) {
        int id=-1;
        String path = "assets/audio/";
        File audioFile = new File(path+soundName);
        SoundDesktop sound =new SoundDesktop(path+soundName,id,loops);
        //need to figure out the size of the file
        if(audioFile.length()>=4*1024*1024)
        {
            try {
                AudioInputStream original =AudioSystem.getAudioInputStream(audioFile);
                AudioInputStream converted = AudioSystem.getAudioInputStream(audioFormat, original);
                DataLine.Info info = new DataLine.Info(SourceDataLine.class,converted.getFormat());
                SourceDataLine line = (SourceDataLine) AudioSystem.getLine(info);
                sound.setLine(line);
                sound.isBigFile = true;
            }catch (LineUnavailableException | UnsupportedAudioFileException | IOException e) {
                throw new RuntimeException(e);
            }

        }
        else{
        sound.id = addClip(audioFile,loops);
        }
        return sound;
    }
    private void playClip(SoundDesktop soundDesktop)
    {
        if(soundDesktop.id>=0&&pool.clips[soundDesktop.id]!=null){
        pool.clips[soundDesktop.id].start();
        }
    }
    private void playBigSound(SoundDesktop soundDesktop)
    {
        Thread thread = new Thread(() -> {
            File file = new File(soundDesktop.audioPath);

            try{
                AudioInputStream audio = AudioSystem.getAudioInputStream(file);

                soundDesktop.line.open(audioFormat);
                soundDesktop.line.start();
                byte[] buffer = new byte[bufferSize];
                int bytesRead;

                while ((bytesRead = audio.read(buffer, 0, buffer.length)) != -1) {
                    soundDesktop.line.write(buffer, 0, bytesRead);
                }

                soundDesktop.line.drain();
            } catch (UnsupportedAudioFileException e) {
                System.err.println("Formato no compatible: " + soundDesktop.audioPath);
            } catch (IOException e) {
                System.err.println("Error leyendo el audio: " + soundDesktop.audioPath);
            } catch (LineUnavailableException e) {
                throw new RuntimeException(e);
            }
        });

        thread.setDaemon(true);
        thread.start();
    }
    public void stopSound(EngSound sound)
    {
        SoundDesktop desktopSound = (SoundDesktop) sound;
        if(desktopSound.isBigFile){
        desktopSound.line.stop();}
        else
        {
            pool.clips[desktopSound.getId()].stop();
        }
    }
    @Override
    public void playSound(EngSound sound) {
        SoundDesktop desktopSound = (SoundDesktop) sound;
        if(desktopSound.isBigFile)
        {
            playBigSound(desktopSound);
        }
        else{
            playClip(desktopSound);
        }
    }
}
