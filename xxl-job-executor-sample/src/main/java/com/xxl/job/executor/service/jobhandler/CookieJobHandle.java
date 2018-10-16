package com.xxl.job.executor.service.jobhandler;

import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;

@JobHandler("cookieJobHandle")
public class CookieJobHandle extends IJobHandler {


    @Override
    public ReturnT<String> execute(String param) throws Exception {

        return null;
    }

}
