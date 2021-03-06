package com.soomla.cocos2dx.common;

import android.opengl.GLSurfaceView;

import com.soomla.BusProvider;
import com.soomla.events.RewardGivenEvent;
import com.soomla.events.RewardTakenEvent;
import com.squareup.otto.Subscribe;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This bridge is used to populate events from the store to cocos2dx (through JNI).
 */
public class CoreEventHandlerBridge {

    private GLSurfaceView mGLThread;

    public CoreEventHandlerBridge() {
        BusProvider.getInstance().register(this);
    }

    @Subscribe
    public void onRewardGivenEvent(final RewardGivenEvent rewardGivenEvent) {
        mGLThread.queueEvent(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject parameters = new JSONObject();
                    parameters.put("method", CommonConsts.EVENT_REWARD_GIVEN);
                    parameters.put("reward", DomainHelper.getInstance().domainToJsonObject(rewardGivenEvent.Reward));
                    NdkGlue.getInstance().sendMessageWithParameters(parameters);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    @Subscribe
    public void onRewardGivenEvent(final RewardTakenEvent rewardTakenEvent) {
        mGLThread.queueEvent(new Runnable() {
            @Override
            public void run() {
                try {
                    JSONObject parameters = new JSONObject();
                    parameters.put("method", CommonConsts.EVENT_REWARD_TAKEN);
                    parameters.put("reward", DomainHelper.getInstance().domainToJsonObject(rewardTakenEvent.Reward));
                    NdkGlue.getInstance().sendMessageWithParameters(parameters);
                } catch (JSONException e) {
                    throw new IllegalStateException(e);
                }
            }
        });
    }

    public void setGlSurfaceView(GLSurfaceView glSurfaceView) {
        this.mGLThread = glSurfaceView;
    }
}
