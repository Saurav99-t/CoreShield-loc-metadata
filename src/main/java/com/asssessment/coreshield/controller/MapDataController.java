package com.asssessment.coreshield.controller;

import com.asssessment.coreshield.model.Location;
import com.asssessment.coreshield.model.Metadata;
import com.asssessment.coreshield.service.MapDataService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/map-data")
public class MapDataController {

    @Autowired
    private MapDataService mapDataService;

    @PostMapping("/process")
    public Map<String, Object> processMapData(@RequestBody Map<String, List<Map<String, Object>>> requestData) {
        List<Location> locations = requestData.get("locations").stream()
                .map(map -> new Location(
                        (String) map.get("id"),
                        (Double) map.get("latitude"),
                        (Double) map.get("longitude")
                ))
                .toList();

        List<Metadata> metadata = requestData.get("metadata").stream()
                .map(map -> new Metadata(
                        (String) map.get("id"),
                        (String) map.get("type"),
                        map.get("rating") != null ? ((Number) map.get("rating")).doubleValue() : 0.0,
                        map.get("reviews") != null ? ((Number) map.get("reviews")).intValue() : 0
                ))
                .toList();

        List<Map<String, Object>> mergedData = mapDataService.mergeData(locations, metadata);

        Map<String, Long> countByType = mapDataService.countByType(mergedData);
        Map<String, Double> averageRatings = mapDataService.averageRatingByType(mergedData);
        Map<String, Object> mostReviews = mapDataService.locationWithMostReviews(mergedData);
        List<Map<String, Object>> incompleteData = mapDataService.findIncompleteData(mergedData);

        return Map.of(
                "countByType", countByType,
                "averageRatings", averageRatings,
                "mostReviews", mostReviews,
                "incompleteData", incompleteData
        );
    }


    // Endpoint to upload Metadata JSON file
    @PostMapping("/upload/location-metadata")
    public String uploadFiles(@RequestParam("location") MultipartFile locationFile,@RequestParam("metadata") MultipartFile metadataFile) {
        try {
            mapDataService.uploadFiles(locationFile, metadataFile);
        } catch (Exception e) {
            return "Failed to upload metadata: " + e.getMessage();
        }
        return "Successfully Uploaded";
    }

    @GetMapping("/merged-data")
    public List<Map<String,Object>> getMergedData(){
        return mapDataService.mergeDataFromRepo();
    }
    @GetMapping("/count-by-type")
    public Map<String,Long> countByType(){
        return mapDataService.countByTypeFromRepo();
    }
    @GetMapping("/average-rating")
    public Map<String,Double> averageRatingByType(){
        return mapDataService.averageRatingByTypeFromRepo();
    }
    @GetMapping("/most-reviews")
    public Map<String,Object> locationWithMostReviews(){
        return mapDataService.locationWithMostReviewsFromRepo();
    }
    @GetMapping("/incomplete-data")
    public List<Map<String,Object>> findIncompleteData(){
        return mapDataService.findIncompleteDataFromRepo();
    }


}