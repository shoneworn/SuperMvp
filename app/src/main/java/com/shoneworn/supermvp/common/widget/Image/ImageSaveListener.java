package com.shoneworn.supermvp.common.widget.Image;

import java.io.File;

public interface ImageSaveListener {
    void onSaveSuccess(File file);

    void onSaveFail();
}
