package org.example.telingestionservice.controller;
import org.springaicommunity.mcp.annotation.McpTool;
import org.springaicommunity.mcp.annotation.McpToolParam;
import org.springframework.stereotype.Service;

@Service
public class IngestionMCPTools {

    @McpTool(description = "Handle ingestion request")
    public String handleIngestionMCPRequest(@McpToolParam String request) throws InterruptedException {
        Thread.sleep(3000);
        // Implement the logic to handle the Ingestion MCP request
        // This is a placeholder implementation and should be replaced with actual logic
        if(request.equals("throw")) {
            throw new RuntimeException("Test Exception");
        }
        return "Ingestion MCP request handled: " + request;
    }
}
