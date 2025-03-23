package com.asssessment.coreshield.service;
import com.asssessment.coreshield.model.Location;
import com.asssessment.coreshield.model.Metadata;
import com.asssessment.coreshield.repository.LocationRepository;
import com.asssessment.coreshield.repository.MetadataRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class MapDataService {


    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private MetadataRepository metadataRepository;

    private final ObjectMapper objectMapper = new ObjectMapper();

    public List<Map<String, Object>> mergeData(List<Location> locations, List<Metadata> metadataList) {
        Map metadataMap = metadataList.stream()
                .collect(Collectors.toMap(Metadata::getId, meta -> meta));

        List<Map<String, Object>> mergedData = new ArrayList<>();
        for (Location location : locations) {
            Map<String, Object> merged = new HashMap<>();
            merged.put("id", location.getId());
            merged.put("latitude", location.getLatitude());
            merged.put("longitude", location.getLongitude());

            Metadata metadata = (Metadata) metadataMap.get(location.getId());
            if (metadata != null) {
                merged.put("type", metadata.getType());
                merged.put("rating", metadata.getRating());
                merged.put("reviews", metadata.getReviews());
            } else {
                merged.put("type", "Unknown");
                merged.put("rating", 0.0);
                merged.put("reviews", 0);
            }
            mergedData.add(merged);
        }
        return mergedData;
    }

    public Map<String, Long> countByType(List<Map<String, Object>> mergedData) {
        return mergedData.stream()
                .filter(data -> !data.get("type").equals("Unknown"))
                .collect(Collectors.groupingBy(data -> (String) data.get("type"), Collectors.counting()));
    }

    public Map<String, Double> averageRatingByType(List<Map<String, Object>> mergedData) {
        return mergedData.stream()
                .filter(data -> !data.get("type").equals("Unknown") && (Double) data.get("rating") > 0)
                .collect(Collectors.groupingBy(
                        data -> (String) data.get("type"),
                        Collectors.averagingDouble(data -> (Double) data.get("rating"))
                ));
    }

    public Map<String, Object> locationWithMostReviews(List<Map<String, Object>> mergedData) {
        return mergedData.stream()
                .max(Comparator.comparingInt(data -> (int) data.get("reviews")))
                .orElse(null);
    }

    public List<Map<String, Object>> findIncompleteData(List<Map<String, Object>> mergedData) {
        return mergedData.stream()
                .filter(data -> data.get("type").equals("Unknown") || (Double) data.get("rating") == 0.0)
                .collect(Collectors.toList());
    }

    // Upload and save Locations
    public String uploadFiles(MultipartFile locationFile,MultipartFile metadataFile) throws IOException {
        List<Location> locations = objectMapper.readValue(locationFile.getInputStream(), new TypeReference<>() {});
        locationRepository.saveAll(locations);

        List<Metadata> metadata = objectMapper.readValue(metadataFile.getInputStream(), new TypeReference<>() {});
        metadataRepository.saveAll(metadata);
        return "Files uploaded successfully.";
    }

    public List<Map<String, Object>> mergeDataFromRepo() {
        List<Location> locations = locationRepository.findAll();
        Map<String,Metadata> metadataMap = metadataRepository.findAll().stream()
                .collect(Collectors.toMap(Metadata::getId, meta -> meta));

        List<Map<String, Object>> mergedData = new ArrayList<>();
        for (Location location : locations) {
            Map<String, Object> merged = new HashMap<>();
            merged.put("id", location.getId());
            merged.put("latitude", location.getLatitude());
            merged.put("longitude", location.getLongitude());

            Metadata metadata = (Metadata) metadataMap.get(location.getId());
            if (metadata != null) {
                merged.put("type", metadata.getType());
                merged.put("rating", metadata.getRating());
                merged.put("reviews", metadata.getReviews());
            } else {
                merged.put("type", "Unknown");
                merged.put("rating", 0.0);
                merged.put("reviews", 0);
            }
            mergedData.add(merged);
        }
        return mergedData;
    }

    public Map<String, Long> countByTypeFromRepo() {
        return metadataRepository.findAll().stream()
                .collect(Collectors.groupingBy(Metadata::getType, Collectors.counting()));
    }

    public Map<String, Double> averageRatingByTypeFromRepo() {
        return metadataRepository.findAll().stream()
                .filter(meta -> meta.getRating() > 0)
                .collect(Collectors.groupingBy(
                        Metadata::getType,
                        Collectors.averagingDouble(Metadata::getRating))
                );
    }

    public Map<String,Object> locationWithMostReviewsFromRepo() {

        return mergeDataFromRepo().stream()
                .max(Comparator.comparingInt(data -> (int) data.get("reviews")))
                .orElse(null);
    }

    public List<Map<String,Object>> findIncompleteDataFromRepo() {
        return mergeDataFromRepo().stream().
                filter(data -> data.get("type").equals("unknown") ||
                        (Double) data.get("rating") == 0.0 ||
                        (int) data.get("reviews") == 0)
                .collect(Collectors.toList());
    }


}