package com.replica.replicaisland;

import com.replica.replicaisland.SoundSystem.Sound;

public class DoorAnimationComponent extends GameComponent {

    public static final class Animation {

        public static final int CLOSED = 0;

        public static final int OPEN = 1;

        public static final int CLOSING = 2;

        public static final int OPENING = 3;
    }

    protected static final int STATE_CLOSED = 0;

    protected static final int STATE_OPEN = 1;

    protected static final int STATE_CLOSING = 2;

    protected static final int STATE_OPENING = 3;

    protected static final float DEFAULT_STAY_OPEN_TIME = 5.0f;

    private SpriteComponent mSprite;

    private int mState;

    private ChannelSystem.Channel mChannel;

    private SolidSurfaceComponent mSolidSurface;

    private float mStayOpenTime;

    private Sound mCloseSound;

    private Sound mOpenSound;

    public DoorAnimationComponent() {
        super();
        setPhase(ComponentPhases.ANIMATION.ordinal());
        reset();
    }

    @Override
    public void reset() {
        mSprite = null;
        mState = STATE_CLOSED;
        mChannel = null;
        mSolidSurface = null;
        mStayOpenTime = DEFAULT_STAY_OPEN_TIME;
        mCloseSound = null;
        mOpenSound = null;
    }

    private void open(float timeSinceTriggered, GameObject parentObject) {
        if (mSprite != null) {
            final float openAnimationLength = mSprite.findAnimation(Animation.OPENING).getLength();
            if (timeSinceTriggered > openAnimationLength) {
                mSprite.playAnimation(Animation.OPEN);
                mState = STATE_OPEN;
                if (mSolidSurface != null) {
                    parentObject.remove(mSolidSurface);
                }
            } else {
                float timeOffset = timeSinceTriggered;
                if (mState == STATE_CLOSING) {
                    timeOffset = openAnimationLength - mSprite.getCurrentAnimationTime();
                } else {
                    if (mSolidSurface != null) {
                        parentObject.remove(mSolidSurface);
                    }
                }
                mState = STATE_OPENING;
                mSprite.playAnimation(Animation.OPENING);
                mSprite.setCurrentAnimationTime(timeOffset);
                if (mOpenSound != null) {
                    SoundSystem sound = sSystemRegistry.soundSystem;
                    if (sound != null) {
                        sound.play(mOpenSound, false, SoundSystem.PRIORITY_NORMAL);
                    }
                }
            }
        }
    }

    private void close(float timeSinceTriggered, GameObject parentObject) {
        if (mSprite != null) {
            final float closeAnimationLength = mSprite.findAnimation(Animation.CLOSING).getLength();
            if (timeSinceTriggered > mStayOpenTime + closeAnimationLength) {
                mSprite.playAnimation(Animation.CLOSED);
                mState = STATE_CLOSED;
                if (mSolidSurface != null) {
                    parentObject.add(mSolidSurface);
                }
            } else {
                float timeOffset = timeSinceTriggered - mStayOpenTime;
                if (mState == STATE_OPENING) {
                    timeOffset = closeAnimationLength - mSprite.getCurrentAnimationTime();
                }
                mState = STATE_CLOSING;
                mSprite.playAnimation(Animation.CLOSING);
                mSprite.setCurrentAnimationTime(timeOffset);
                if (mCloseSound != null) {
                    SoundSystem sound = sSystemRegistry.soundSystem;
                    if (sound != null) {
                        sound.play(mCloseSound, false, SoundSystem.PRIORITY_NORMAL);
                    }
                }
            }
        }
    }

    @Override
    public void update(float timeDelta, BaseObject parent) {
        if (mChannel != null) {
            if (mChannel.value != null && mChannel.value instanceof ChannelSystem.ChannelFloatValue) {
                final float lastPressedTime = ((ChannelSystem.ChannelFloatValue) mChannel.value).value;
                TimeSystem time = sSystemRegistry.timeSystem;
                final float gameTime = time.getGameTime();
                final float delta = gameTime - lastPressedTime;
                if (delta < mStayOpenTime && (mState == STATE_CLOSED || mState == STATE_CLOSING)) {
                    open(delta, (GameObject) parent);
                } else if (delta > mStayOpenTime && (mState == STATE_OPEN || mState == STATE_OPENING)) {
                    close(delta, (GameObject) parent);
                }
            }
        }
        if (mSprite != null) {
            if (mState == STATE_OPENING && mSprite.animationFinished()) {
                mSprite.playAnimation(Animation.OPEN);
                mState = STATE_OPEN;
            } else if (mState == STATE_CLOSING && mSprite.animationFinished()) {
                mSprite.playAnimation(Animation.CLOSED);
                mState = STATE_CLOSED;
                if (mSolidSurface != null) {
                    ((GameObject) parent).add(mSolidSurface);
                }
            }
            if (mSprite.getCurrentAnimation() == Animation.OPENING && mState == STATE_CLOSED) {
                mSprite.playAnimation(Animation.CLOSING);
                mState = STATE_CLOSING;
            }
        }
    }

    public void setSprite(SpriteComponent sprite) {
        mSprite = sprite;
    }

    public void setChannel(ChannelSystem.Channel channel) {
        mChannel = channel;
    }

    public void setSolidSurface(SolidSurfaceComponent surface) {
        mSolidSurface = surface;
    }

    public void setStayOpenTime(float time) {
        mStayOpenTime = time;
    }

    public void setSounds(Sound openSound, Sound closeSound) {
        mOpenSound = openSound;
        mCloseSound = closeSound;
    }
}
