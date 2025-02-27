package com.software.moviecatalogservice.controller;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import com.software.moviecatalogservice.models.CatalogItem;
import com.software.moviecatalogservice.models.Movie;
import com.software.moviecatalogservice.models.UserRating;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResource {

	@Autowired
	private RestTemplate restTemplate;
	
	 @Autowired
	 private WebClient.Builder webClientBuilder;
	
	@RequestMapping("/{userId}")
	public List<CatalogItem> getCatalog(@PathVariable("userId")String userId){
		
	
		
		//get all rated movie ids
//		List<Rating> ratings =  Arrays.asList(
//				new Rating("1234",4),
//				new Rating("5678",3)
//				
//		);
		
		
		/*UserRating ratings = webClientBuilder.build().get().uri("http://ratings-data-service/users/"+userId)
				.retrieve().bodyToMono(UserRating.class).block();*/
		
		UserRating userRating = restTemplate.getForObject("http://ratings-data-service/ratingsdata/user/" + userId, UserRating.class);
		
		//for each movie id call movie info service and get details
      return userRating.getUserRating().stream().map(rating ->{
    	  Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(),
    			  Movie.class);
    	  
//			Movie movie = webClientBuilder.build().get().uri("http://localhost:8081/movies/" + rating.getMovieId())
//					.retrieve().bodyToMono(Movie.class).block();
    	  
    	  
    	  return new CatalogItem(movie.getName(), "desc",rating.getRating());
      }).collect(Collectors.toList());
    		  
		
		
		//Put them all together
//		return Collections.singletonList(
//				new CatalogItem("Avengers","Test",5));
		
	}
}
