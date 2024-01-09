package com.microservice.obstacle.services.client;

import com.microservice.obstacle.services.wsdl.GetPlanetResponse;
import com.microservice.obstacle.services.wsdl.GetPlanetRequest;
import org.springframework.ws.client.core.support.WebServiceGatewaySupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ws.soap.client.core.SoapActionCallback;

public class PlanetClient extends WebServiceGatewaySupport {

    private static final Logger log = LoggerFactory.getLogger(PlanetClient.class);

    public GetPlanetResponse getPlanet(long planetId) {

        GetPlanetRequest request = new GetPlanetRequest();
        request.setPlanetId(planetId);

        log.info("Requesting location for " + planetId);

        GetPlanetResponse response = (GetPlanetResponse) getWebServiceTemplate()
                .marshalSendAndReceive("http://localhost:8080/ws/planets", request,
                        new SoapActionCallback(
                                "http://microservice.com/planets/GetPlanetRequest"));

        return response;
    }
}
