# Smart Campus API

## Overview of API Design
The Smart Campus API is a robust, highly available RESTful service designed to manage campus infrastructure, specifically Rooms and Sensors. Built using Java and JAX-RS (Jakarta RESTful Web Services), it provides a seamless interface for facilities managers and automated systems.

**Key Architectural Decisions:**
- **Resource Hierarchy**: The API models the physical structure of the campus, allowing deep nesting via sub-resources (e.g., `/sensors/{id}/readings`).
- **Data Integrity**: Enforces strict business rules, such as preventing the deletion of a room that contains active sensors.
- **Advanced Error Handling**: Utilizes Exception Mappers to prevent internal stack traces from leaking and maps business logic errors to semantic HTTP status codes (e.g., 409 Conflict, 422 Unprocessable Entity, 403 Forbidden).
- **Generic DAO Pattern**: Abstracted data access layers ensuring clean separation between business logic and data persistence (currently using a mocked in-memory database).

## Build and Launch Instructions
Follow these steps to build and run the API locally:

### Prerequisites:
- Java JDK 11 or higher
- Apache Maven
- Apache Tomcat (version 9 or 10)

### Steps:
1. **Navigate to the Project Directory:**
   "
   cd SmartCampus
   "

2. **Build the Project:**
   Build the `.war` package using Maven.
   "
   mvn clean package
   "

3. **Deploy to Tomcat:**
   Copy the generated `SmartCampus.war` (from the `target/` directory) into your Tomcat's `webapps` folder.
   "
   cp target/SmartCampus.war /path/to/tomcat/webapps/
   "
   *(Note: The exact path depends on your local Tomcat installation directory)*

4. **Start the Server:**
   Launch Tomcat by running its startup script.
   "
   /path/to/tomcat/bin/startup.sh   # On macOS/Linux
   \path\to\tomcat\bin\startup.bat  # On Windows
   "

5. **Verify Deployment:**
   The API will be accessible at: `http://localhost:8080/SmartCampus/api/v1`

## Sample API Interactions (cURL)

Below are five sample commands demonstrating interactions with the API:

**1. Discovery Endpoint (GET)**
"
curl -X GET http://localhost:8080/SmartCampus/api/v1
"

**2. Create a New Room (POST)**
"
curl -X POST http://localhost:8080/SmartCampus/api/v1/rooms \
-H "Content-Type: application/json" \
-d '{
  "id": "LIB-301",
  "name": "Library Quiet Study",
  "capacity": 50
}'
"

**3. Get All Rooms (GET)**
"
curl -X GET http://localhost:8080/SmartCampus/api/v1/rooms
"

**4. Register a Sensor in a Room (POST)**
"
curl -X POST http://localhost:8080/SmartCampus/api/v1/sensors \
-H "Content-Type: application/json" \
-d '{
  "id": "TEMP-001",
  "type": "Temperature",
  "status": "ACTIVE",
  "roomId": "LIB-301"
}'
"

**5. Filter Sensors by Type (GET)**
"
curl -X GET "http://localhost:8080/SmartCampus/api/v1/sensors?type=Temperature"
"

---

# Conceptual Report

**Part 1: Service Architecture & Setup**

**Q: Explain the default lifecycle of a JAX-RS Resource class. Is a new instance instantiated for every incoming request, or does the runtime treat it as a singleton? Elaborate on how this architectural decision impacts the way you manage and synchronize your in-memory data structures (maps/lists) to prevent data loss or race conditions.**

Answer: By default, JAX-RS Resource classes are request-scoped. The JAX-RS runtime instantiates a brand-new object of the Resource class for every single incoming HTTP request, and the instance is garbage-collected once the response is returned. This decision heavily impacts state management: because resource instances do not persist between calls, standard instance variables are reset every time. To maintain state across the API, data-holding structures must be decoupled from the resource instances (e.g., by utilizing singleton service layers or static collections). Furthermore, because multiple concurrent HTTP requests spawn separate instances accessing shared state simultaneously, we must utilize thread-safe structures like ConcurrentHashMap or synchronization blocks to prevent race conditions.

**Q: Why is the provision of "Hypermedia" (links and navigation within responses) considered a hallmark of advanced RESTful design (HATEOAS)? How does this approach benefit client developers compared to static documentation?**

Answer: Hypermedia as the Engine of Application State (HATEOAS) transitions an API from a static interface to a dynamic, self-discoverable system. By including links (such as "self," "next," or "delete") directly inside JSON responses, the server guides the client on what actions are currently permitted. This benefits client developers by decoupling them from hardcoded URLs. If the backend URI structure changes, clients reading hypermedia links automatically adapt without code modifications. Additionally, the server can use links to dictate state transitions—for example, omitting a "delete" link if a room still contains sensors, preventing the client from attempting an invalid operation.

**Part 2: Room Management**

**Q: When returning a list of rooms, what are the implications of returning only IDs versus returning the full room objects? Consider network bandwidth and client side processing.**

Answer: Returning full room objects increases payload size and network bandwidth consumption, which can impact performance on slow networks. However, it improves client-side efficiency because the client receives all necessary data in a single HTTP round-trip, avoiding the "N+1 queries" problem. Conversely, returning only room IDs significantly reduces the initial payload size. However, this increases the burden on the client: if the UI needs room names or capacities, the client must execute many subsequent GET requests, increasing overall latency and server load. A common middle ground is returning a "summary" object that includes the ID and basic identifying information.

**Q: Is the DELETE operation idempotent in your implementation? Provide a detailed justification by describing what happens if a client mistakenly sends the exact same DELETE request for a room multiple times.**

Answer: Yes, the DELETE operation is idempotent. Idempotency guarantees that executing the identical request multiple times yields the same final state on the server as executing it once. If a client sends the same DELETE /rooms/LIB-301 request multiple times, the first request successfully removes the room and returns 204 No Content. Subsequent requests will fail to find the room and return 404 Not Found. While the HTTP status codes differ, the actual state of the server remains unchanged after the first execution (the room remains deleted), satisfying the requirements for idempotency.

**Part 3: Sensor Operations & Linking**
**Q: We explicitly use the @Consumes(MediaType.APPLICATION_JSON) annotation on the POST method. Explain the technical consequences if a client attempts to send data in a different format, such as text/plain or application/xml. How does JAX-RS handle this mismatch?**

Answer: The @Consumes(MediaType.APPLICATION_JSON) annotation acts as a contract for the endpoint. If a client sends a POST request with an unsupported Content-Type header (such as text/plain), the JAX-RS runtime intercepts the request before it reaches the resource method. Because the resource cannot process that media type, JAX-RS automatically responds with an HTTP 415 Unsupported Media Type error. This ensures that the application logic is protected from incorrectly formatted data and does not waste resources attempting to parse invalid payloads.

**Q: You implemented this filtering using @QueryParam. Contrast this with an alternative design where the type is part of the URL path (e.g., /api/v1/sensors/type/CO2). Why is the query parameter approach generally considered superior for filtering and searching collections?**

Answer: In RESTful design, the URL path represents a hierarchy of resources (e.g., /sensors), while query parameters provide modifiers like filtering or sorting. Using a path variable like /sensors/type/CO2 establishes a rigid hierarchy that implies "type" is a child resource. The query parameter approach (/sensors?type=CO2) is superior because it is composable. If a client needs to filter by multiple criteria (such as type AND status), they can simply append parameters (?type=CO2&status=ACTIVE). Achieving this flexibility with path variables would require complex, non-standard URL structures that are difficult to maintain.

**Part 4: Deep Nesting with Sub-Resources**
**Q: Discuss the architectural benefits of the Sub-Resource Locator pattern. How does delegating logic to separate classes help manage complexity in large APIs compared to defining every nested path (e.g., sensors/{id}/readings/{rid}) in one massive controller class?**

Answer: The Sub-Resource Locator pattern allows a parent class (SensorResource) to delegate the handling of nested paths to dedicated classes (SensorReadingResource). This provides significant benefits regarding the Separation of Concerns. If every nested operation (GET, POST, DELETE for readings) were defined within SensorResource, it would become an unmaintainable "God Object." By delegating to separate classes, the system is broken down into small, cohesive, single-responsibility files. This modularity makes the API easier to debug, test, and scale without increasing the complexity of individual controllers.

**Part 5: Advanced Error Handling, Exception Mapping & Logging**

**Q: Why is HTTP 422 often considered more semantically accurate than a standard 404 when the issue is a missing reference inside a valid JSON payload?**

Answer: HTTP 404 Not Found typically indicates that the requested endpoint itself does not exist. However, if a client successfully reaches POST /sensors with a valid JSON payload, but the roomId inside that payload refers to a non-existent room, a 404 is misleading—the endpoint was found. HTTP 422 Unprocessable Entity is more accurate because it signals that the server understood the request and the syntax was correct, but it could not process the instructions due to semantic errors (such as a referential integrity failure).

**Q: From a cybersecurity standpoint, explain the risks associated with exposing internal Java stack traces to external API consumers. What specific information could an attacker gather from such a trace?**

Answer: Exposing internal Java stack traces is a critical Information Exposure vulnerability. Stack traces provide a roadmap of the backend architecture for attackers. A malicious actor could gather:

- **Dependency Details:** Traces reveal specific frameworks and version numbers (e.g., Jersey 2.32), allowing attackers to target known vulnerabilities (CVEs) associated with those versions.
- **System Structure:** They expose internal package names and file paths, revealing the application's logic and design.
- **Configuration Data:** Detailed error messages can leak database schemas, table names, or internal server constraints, which can be leveraged for further attacks like SQL injection. 

By utilizing Exception Mappers to return sanitized JSON responses, these risks are eliminated.

**Q: Why is it advantageous to use JAX-RS filters for cross-cutting concerns like logging, rather than manually inserting Logger.info() statements inside every single resource method?**

Answer: It is advantageous because it follows the principle of **Separation of Concerns** and avoids code duplication (the **DRY** principle).

- **Centralized Logic:** By implementing `ContainerRequestFilter` and `ContainerResponseFilter`, you write the logging logic exactly once, rather than duplicating it across every single endpoint.
- **Clean Resources:** Your resource methods remain clean and focused purely on business logic (e.g., saving a room) instead of being cluttered with boilerplate logging code.
- **Consistency:** A filter guarantees that every request and response is consistently logged, removing the risk of a developer forgetting to add logging statements to a newly created method.
- **Maintainability:** If the logging format needs to be changed later, you only have to update a single filter class, instead of hunting down and modifying `Logger.info()` statements scattered throughout hundreds of resource methods.
