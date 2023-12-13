package com.mylocation.resources;

import com.mylocation.client.LocationApiClient;
import com.mylocation.db.LocationDAO;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.QueryParam;

@Path("/distance")

    public class DistanceResource {
        private final LocationApiClient locationApiClient;
        private static final Logger LOGGER = LoggerFactory.getLogger(LocationApiClient.class);

        public DistanceResource(MyApplicationConfiguration configuration, LocationApiClient locationApiClient) {
            this.locationApiClient = locationApiClient;
        }



    @GET
        public String getDistance(@QueryParam("startLatitude") double startLatitude,
                                  @QueryParam("startLongitude") double startLongitude,
                                  @QueryParam("endLatitude") double endLatitude,
                                  @QueryParam("endLongitude") double endLongitude) {
            double distance = locationApiClient.getDistance(startLatitude, startLongitude, endLatitude, endLongitude);
        Location location = new Location();
        location.setStartLatitude(startLatitude);
        location.setStartLongitude(startLongitude);
        location.setEndLatitude(endLatitude);
        location.setEndLongitude(endLongitude);
        location.setDistance(distance);

        // Persiste a instância de Location no banco de dados usando Hibernate
        try (SessionFactory sessionFactory = new Configuration().configure().buildSessionFactory()) {
            LocationDAO locationDAO = new LocationDAO(sessionFactory);
            locationDAO.saveLocation(location);
        } catch (Exception e) {

            LOGGER.error("Erro ao salvar no banco de dados." ,e);
            return ("Erro ao salvar no banco de dados.");
        }

        return "Distância calculada entre os pontos: " + distance + " km. Dados salvos no banco de dados.";


        }
    }


