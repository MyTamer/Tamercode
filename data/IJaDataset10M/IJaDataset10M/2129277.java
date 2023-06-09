package sdljava.x.swig;

import java.nio.*;

class SWIG_SDLMixerJNI {

    static {
        try {
            if (System.getProperty("sdljava.bootclasspath") == null) {
                System.loadLibrary("sdljava_mixer");
            }
        } catch (UnsatisfiedLinkError e) {
            System.err.println("Native code library failed to load. \n" + e);
            System.exit(1);
        }
    }

    public static final native void set_Mix_Chunk_allocated(long jarg1, int jarg2);

    public static final native int get_Mix_Chunk_allocated(long jarg1);

    public static final native void set_Mix_Chunk_alen(long jarg1, long jarg2);

    public static final native long get_Mix_Chunk_alen(long jarg1);

    public static final native void set_Mix_Chunk_volume(long jarg1, short jarg2);

    public static final native short get_Mix_Chunk_volume(long jarg1);

    public static final native long new_Mix_Chunk();

    public static final native void delete_Mix_Chunk(long jarg1);

    public static final native int Mix_OpenAudio(int jarg1, int jarg2, int jarg3, int jarg4);

    public static final native void Mix_CloseAudio();

    public static final native int Mix_QuerySpec(int[] jarg1, int[] jarg2, int[] jarg3);

    public static final native long SWIG_Mix_LoadWAV(String jarg1);

    public static final native long SWIG_Mix_LoadWAV_Buffer(Buffer jarg1, int jarg2);

    public static final native int Mix_VolumeChunk(long jarg1, int jarg2);

    public static final native void Mix_FreeChunk(long jarg1);

    public static final native int Mix_AllocateChannels(int jarg1);

    public static final native int Mix_Volume(int jarg1, int jarg2);

    public static final native int SWIG_Mix_PlayChannel(int jarg1, long jarg2, int jarg3);

    public static final native int Mix_PlayChannelTimed(int jarg1, long jarg2, int jarg3, int jarg4);

    public static final native int SWIG_Mix_FadeInChannel(int jarg1, long jarg2, int jarg3, int jarg4);

    public static final native int Mix_FadeInChannelTimed(int jarg1, long jarg2, int jarg3, int jarg4, int jarg5);

    public static final native void Mix_Pause(int jarg1);

    public static final native void Mix_Resume(int jarg1);

    public static final native int Mix_HaltChannel(int jarg1);

    public static final native int Mix_ExpireChannel(int jarg1, int jarg2);

    public static final native int Mix_FadeOutChannel(int jarg1, int jarg2);

    public static final native int Mix_Playing(int jarg1);

    public static final native int Mix_Paused(int jarg1);

    public static final native int Mix_FadingChannel(int jarg1);

    public static final native long Mix_GetChunk(int jarg1);

    public static final native int Mix_ReserveChannels(int jarg1);

    public static final native int Mix_GroupChannel(int jarg1, int jarg2);

    public static final native int Mix_GroupChannels(int jarg1, int jarg2, int jarg3);

    public static final native int Mix_GroupCount(int jarg1);

    public static final native int Mix_GroupAvailable(int jarg1);

    public static final native int Mix_GroupOldest(int jarg1);

    public static final native int Mix_GroupNewer(int jarg1);

    public static final native int Mix_FadeOutGroup(int jarg1, int jarg2);

    public static final native int Mix_HaltGroup(int jarg1);

    public static final native long Mix_LoadMUS(String jarg1);

    public static final native void Mix_FreeMusic(long jarg1);

    public static final native int Mix_PlayMusic(long jarg1, int jarg2);

    public static final native int Mix_FadeInMusic(long jarg1, int jarg2, int jarg3);

    public static final native int Mix_FadeInMusicPos(long jarg1, int jarg2, int jarg3, double jarg4);

    public static final native int Mix_VolumeMusic(int jarg1);

    public static final native void Mix_PauseMusic();

    public static final native void Mix_ResumeMusic();

    public static final native void Mix_RewindMusic();

    public static final native int Mix_SetMusicPosition(double jarg1);

    public static final native int Mix_SetMusicCMD(String jarg1);

    public static final native int Mix_HaltMusic();

    public static final native int Mix_FadeOutMusic(int jarg1);

    public static final native int Mix_GetMusicType(long jarg1);

    public static final native int Mix_PlayingMusic();

    public static final native int Mix_PausedMusic();

    public static final native int Mix_FadingMusic();

    public static final native int Mix_SetPanning(int jarg1, short jarg2, short jarg3);

    public static final native int Mix_SetDistance(int jarg1, short jarg2);

    public static final native int Mix_SetPosition(int jarg1, short jarg2, short jarg3);

    public static final native int Mix_SetReverseStereo(int jarg1, int jarg2);

    public static final native long SWIG_MIX_VERSION();
}
