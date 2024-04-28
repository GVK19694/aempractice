package com.aem.practice.core.servlets;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ModifiableValueMap;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.servlets.HttpConstants;
import org.apache.sling.api.servlets.ServletResolverConstants;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.osgi.service.component.annotations.Component;

import javax.servlet.Servlet;
import javax.servlet.ServletException;
import java.io.IOException;
import java.util.Map;

@Component(
        service = Servlet.class,
        immediate = true,
        property = {
                ServletResolverConstants.SLING_SERVLET_METHODS + "="
                        + HttpConstants.METHOD_GET + ","
                        + HttpConstants.METHOD_POST + ","
                        + HttpConstants.METHOD_PUT + ","
                        + HttpConstants.METHOD_DELETE,
                ServletResolverConstants.SLING_SERVLET_PATHS + "=/bin/aempractice/servletexample",
        }
)
public class ServletsExample extends SlingAllMethodsServlet {
    private Gson gson = new Gson();

    @Override
    protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {

        String componentName = request.getParameter("name");
        response.setContentType("application/json");

        ResourceResolver resourceResolver = request.getResourceResolver();
        Resource resource = resourceResolver.getResource("/apps/aempractice/components" + (componentName != null ? "/" + componentName : ""));

        JsonObject jsonResponse = new JsonObject();
        JsonArray components = new JsonArray();

        if(resource != null) {
            if(componentName != null) {
                //Fetch Single Component (http://localhost:4502/bin/aempractice/servletexample?name=<%give the component name%>)
                jsonResponse.addProperty("path", resource.getPath());
                jsonResponse.add("properties", gson.toJsonTree(resource.getValueMap()));
            } else {
                //Fetch All Components (http://localhost:4502/bin/aempractice/servletexample)
                for(Resource component : resource.getChildren()) {
                    JsonObject compJson = new JsonObject();
                    compJson.addProperty("name", component.getName());
                    compJson.addProperty("path", component.getPath());
                    compJson.add("properties",gson.toJsonTree(component.getValueMap()));
                    components.add(compJson);
                }
                jsonResponse.add("components", components);
            }
        } else {
            jsonResponse.addProperty("Message", "Component not found");
        }
        response.getWriter().write(gson.toJson(jsonResponse));
    }
    @Override
    protected void doPost(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String componentName = request.getParameter("name");
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        response.setContentType("application/json");
        ResourceResolver resolver = request.getResourceResolver();

        try {
            //Create a component http://localhost:4502/bin/aempractice/servletexample?name=<%Componenet name%>
            /*
            {
                "jcr:primaryType": "cq:Component",
                "jcr:title": "Example Component",
                "componentGroup": "AEM-Practice - Content"
            }
            */
            Resource resource = resolver.create(resolver.getResource("/apps/aempractice/components"), componentName, gson.fromJson(jsonObject, Map.class));
            resolver.commit();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("message", "Created Component at: " + resource.getPath());
            response.getWriter().write(gson.toJson(jsonResponse));
        } catch (Exception e) {
            response.getWriter().write("Getting Error While Creating Component" + e.getMessage());

        }
    }
    @Override
    protected void doPut(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String componentName = request.getParameter("name");
        JsonObject jsonObject = gson.fromJson(request.getReader(), JsonObject.class);
        response.setContentType("application/json");
        ResourceResolver resolver = request.getResourceResolver();

        try{
            //Update a component http://localhost:4502/bin/aempractice/servletexample?name=<%Componenet name%>
            /*
            {
                "jcr:primaryType": "cq:Component",
                "jcr:title": "Example Component",
                "componentGroup": "AEM-Practice - Content"
            }
            */
            Resource resource = resolver.getResource("/apps/aempractice/components" + (componentName != null ? "/" + componentName : ""));
            if(resource != null) {
                ModifiableValueMap valueMap = resource.adaptTo(ModifiableValueMap.class);
                jsonObject.entrySet().forEach(entry -> valueMap.put(entry.getKey(), gson.fromJson(entry.getValue(), Object.class)));
                resolver.commit();
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("message", "Updated Component at: " + resource.getPath());
                response.getWriter().write(gson.toJson(jsonResponse));
            } else {
                response.getWriter().write("Component not found for update");
            }
        } catch (Exception e) {
            response.getWriter().write("Getting Error While Updating Component" + e.getMessage());
        }
    }
    @Override
    protected void doDelete(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException, IOException {
        String componentName = request.getParameter("name");
        response.setContentType("application/json");
        ResourceResolver resolver = request.getResourceResolver();

        try{
            //Delete a component http://localhost:4502/bin/aempractice/servletexample?name=<%Componenet name%>
            Resource resource = resolver.getResource("/apps/aempractice/components" + (componentName != null ? "/" + componentName : ""));
            if(resource != null) {
                resolver.delete(resource);
                resolver.commit();
                JsonObject jsonResponse = new JsonObject();
                jsonResponse.addProperty("message", "Deleted Component at: " + resource.getPath());
                response.getWriter().write(gson.toJson(jsonResponse));
            } else {
                response.getWriter().write("Component not found for deletion");
            }
        } catch (Exception e) {
            response.getWriter().write("Getting Error While Deleting Component" + e.getMessage());
        }
    }
}
