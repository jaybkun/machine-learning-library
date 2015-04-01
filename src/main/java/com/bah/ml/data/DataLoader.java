package com.bah.ml.data;

import java.util.List;

/**
 * Created by 573996 on 4/1/2015.
 */
public interface DataLoader {

    /**
     *
     * @param dataSource
     * @return
     */
    public DataCollection load(String dataSource);
}
