package top.alau.alcanderian.musicplayer;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.os.Parcel;
import android.os.RemoteException;

public class MusicService extends Service {

    public static MediaPlayer mp = new MediaPlayer();
    public IBinder my_binder = new MusicBinder();
    public static int state = 3;

    public MusicService() {
        try {
            mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
            mp.prepare();
            mp.setLooping(true);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public IBinder onBind(Intent intent) {
        return my_binder;
    }

    public class MusicBinder extends Binder {
        @Override
        public boolean onTransact(int code, Parcel data, Parcel reply, int flags)
                throws RemoteException {
            switch (code) {
                case 1: // start/pause
                    if (mp.isPlaying()) {
                        mp.pause();
                        state = 1;
                        reply.writeInt(state);
                    } else {
                        mp.start();
                        state = 2;
                        reply.writeInt(state);
                    }
                    break;
                case 2: // stop
                    if (mp != null && state != 0) {
                        mp.stop();
                        state = 0;
                        reply.writeInt(state);
                        try {
                            mp.reset();
                            mp.setDataSource(Environment.getExternalStorageDirectory() + "/melt.mp3");
                            mp.prepare();
                            mp.setLooping(true);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    break;
                case 3: // exit
                    mp.release();
                    mp = null;
                    break;
                case 4: // refresh
                    reply.writeInt(mp.getCurrentPosition());
                    break;
                case 5: // move the bar
                    mp.seekTo(data.readInt());
                    break;
                case 6: // get max
                    reply.writeInt(mp.getDuration());
                    break;
                case 7:
                    reply.writeInt(state);
                    break;
            }
            return super.onTransact(code, data, reply, flags);
        }
    }
}
