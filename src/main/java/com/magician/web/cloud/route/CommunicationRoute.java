package com.magician.web.cloud.route;

import com.magician.web.mvc.core.annotation.Route;
import com.magician.web.mvc.core.constant.ReqMethod;

@Route("/magician/cloud")
public class CommunicationRoute {

    @Route(value = "/dataExchange", requestMethod = ReqMethod.POST)
    public void dataExchange(){

    }

    @Route(value = "/selectCloudRouteList", requestMethod = ReqMethod.GET)
    public void selectCloudRouteList(){

    }
}
