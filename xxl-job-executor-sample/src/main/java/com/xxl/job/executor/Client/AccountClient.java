package com.xxl.job.executor.Client;

import com.alibaba.fastjson.JSONObject;
import com.clouddeer.core.view.RequestVo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

@FeignClient("CD-ACCOUNT")
public interface AccountClient {

    @RequestMapping(value = "/account/weibo/sendWeiBo")
    String sendWeiBo(@RequestBody RequestVo<Integer> vo);

    @RequestMapping(value = "/account/weibo/checkAccountStatus")
    JSONObject checkAccountStatus(@RequestBody RequestVo<String> vo);
}
