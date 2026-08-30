package com.project.mKajy.gateway.common;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

@Component
public class YamlPayloadBuilder {

    private final ObjectMapper objectMapper;
    private final ResourcePatternResolver resourceResolver;

    public YamlPayloadBuilder(ObjectMapper objectMapper, ResourcePatternResolver resourceResolver) {
        this.objectMapper = objectMapper;
        this.resourceResolver = resourceResolver;
    }

    public ObjectNode build(String yamlClasspathLocation, Map<String, String> sourceValues) {
        Map<String, String> fieldsMapping = loadMapping(yamlClasspathLocation);

        ObjectNode root = objectMapper.createObjectNode();
        fieldsMapping.forEach((targetPath, internalFieldName) -> {
            String value = sourceValues.get(internalFieldName);
            if (value != null) {
                setNestedValue(root, targetPath, value);
            }
        });
        return root;
    }

    public void assertMappingIsReadable(String yamlClasspathLocation) {
        loadMapping(yamlClasspathLocation);
    }

    @SuppressWarnings("unchecked")
    private Map<String, String> loadMapping(String yamlClasspathLocation) {
        Resource resource = resourceResolver.getResource(yamlClasspathLocation);
        if (!resource.exists()) {
            throw new IllegalStateException("Fichier de mapping introuvable : " + yamlClasspathLocation);
        }
        Yaml yaml = new Yaml();
        try (InputStream is = resource.getInputStream()) {
            Map<String, Object> rules = yaml.load(is);
            Map<String, String> fields = (Map<String, String>) rules.get("fields");
            return fields != null ? fields : Collections.emptyMap();
        } catch (Exception e) {
            throw new IllegalStateException("Mapping invalide : " + yamlClasspathLocation, e);
        }
    }

    private void setNestedValue(ObjectNode root, String dotPath, String value) {
        String[] parts = dotPath.split("\\.");
        ObjectNode current = root;
        for (int i = 0; i < parts.length - 1; i++) {
            JsonNode child = current.get(parts[i]);
            current = (child != null && child.isObject()) ? (ObjectNode) child : current.putObject(parts[i]);
        }
        current.put(parts[parts.length - 1], value);
    }
}