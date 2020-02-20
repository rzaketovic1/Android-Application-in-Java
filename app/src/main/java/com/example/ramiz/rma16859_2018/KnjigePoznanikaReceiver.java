package com.example.ramiz.rma16859_2018;

import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;

/**
 * Created by Ramiz on 19.5.2018.
 */

public class KnjigePoznanikaReceiver extends ResultReceiver {
    private Receiver receiver;

    public KnjigePoznanikaReceiver(Handler handler) {
        super(handler);
    }

    @Override
    protected void onReceiveResult(int resultCode, Bundle resultData) {
        if (receiver != null) {
            receiver.onReceiverResult(resultCode, resultData);
        }
    }

    public void setReceiver(Receiver receiver) {
        this.receiver = receiver;
    }

    public interface Receiver{
        public void onReceiverResult(int resultCode,Bundle resulData);
    }
}
