package com.xxl.job.executor.service.jobhandler;

import com.alibaba.fastjson.JSONObject;
import com.clouddeer.core.view.RequestVo;
import com.xxl.job.core.biz.model.ReturnT;
import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.JobHandler;
import com.xxl.job.core.log.XxlJobLogger;
import com.xxl.job.executor.Client.AccountClient;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;

@JobHandler(value = "webJobHandler")
@Component
public class WeiboJobHandle extends IJobHandler {

    @Resource
    private AccountClient accountClient;

    @Override
    public ReturnT<String> execute(String param) throws Exception {
        RequestVo<String> vo = new RequestVo<>();
        vo.setParam(param);
        JSONObject jsonObject = accountClient.checkAccountStatus(vo);
        String code = jsonObject.getString("code");
        if (code.equals("0")) {
            return SUCCESS;
        } else {
            return FAIL;
        }
    }
}
