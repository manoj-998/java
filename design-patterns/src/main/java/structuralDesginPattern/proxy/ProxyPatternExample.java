package structuralDesginPattern.proxy;

import java.util.HashMap;
import java.util.Map;

/**
 * Demonstrates different Proxy Design Pattern types.
 *
 * Proxy Types:
 * 1. Protection Proxy -> Authorization
 * 2. Virtual Proxy    -> Lazy object creation
 * 3. Caching Proxy    -> Cache repeated results
 * 4. Logging Proxy    -> Log request/response
 * 5. Remote Proxy     -> Simulate remote service call
 */
public class ProxyPatternExample {

    /**
     * SUBJECT
     * Common interface used by the real service and proxies.
     */
    interface UserService {
        String getUser(int id);
    }

    /**
     * REAL SUBJECT
     * Performs the actual operation.
     */
    static class RealUserService implements UserService {

        RealUserService() {
            System.out.println("RealUserService created");
        }

        @Override
        public String getUser(int id) {
            System.out.println("Fetching user from database...");
            return "User-" + id;
        }
    }

    /**
     * 1. PROTECTION PROXY
     *
     * Checks authorization before allowing access
     * to the real service.
     */
    static class ProtectionProxy implements UserService {

        private final UserService service;
        private final String role;

        ProtectionProxy(UserService service, String role) {
            this.service = service;
            this.role = role;
        }

        @Override
        public String getUser(int id) {

            if (!"ADMIN".equals(role)) {
                return "Access Denied";
            }

            return service.getUser(id);
        }
    }

    /**
     * 2. VIRTUAL PROXY
     *
     * Creates RealUserService only when it is
     * actually required.
     */
    static class VirtualProxy implements UserService {

        private RealUserService service;

        @Override
        public String getUser(int id) {

            // Real object is created only on first use.
            if (service == null) {
                System.out.println("Creating real service lazily...");
                service = new RealUserService();
            }

            return service.getUser(id);
        }
    }

    /**
     * 3. CACHING PROXY
     *
     * Stores previously fetched users.
     * If data exists in cache, real service is not called.
     */
    static class CachingProxy implements UserService {

        private final UserService service;
        private final Map<Integer, String> cache = new HashMap<>();

        CachingProxy(UserService service) {
            this.service = service;
        }

        @Override
        public String getUser(int id) {

            if (cache.containsKey(id)) {
                System.out.println("Cache Hit");
                return cache.get(id);
            }

            System.out.println("Cache Miss");

            String user = service.getUser(id);
            cache.put(id, user);

            return user;
        }
    }

    /**
     * 4. LOGGING PROXY
     *
     * Adds logging before and after the actual call.
     */
    static class LoggingProxy implements UserService {

        private final UserService service;

        LoggingProxy(UserService service) {
            this.service = service;
        }

        @Override
        public String getUser(int id) {

            System.out.println("Request: getUser(" + id + ")");
            String result = service.getUser(id);
            System.out.println("Response: " + result);
            return result;
        }
    }

    /**
     * 5. REMOTE PROXY
     *
     * Represents a service running in another system.
     *
     * In a real application this could internally use:
     * REST, gRPC, HTTP, RMI, etc.
     */
    static class RemoteUserServiceProxy implements UserService {

        @Override
        public String getUser(int id) {

            System.out.println("Calling remote User Service...");

            // Simulated network call
            return "Remote-User-" + id;
        }
    }

    /**
     * CLIENT
     *
     * Shows how different proxies behave.
     */
    public static void main(String[] args) {

        /*
         * PROTECTION PROXY
         *
         * Admin -> request allowed.
         * User  -> request blocked.
         */
        UserService adminService = new ProtectionProxy(new RealUserService(), "ADMIN");
        System.out.println(adminService.getUser(1));
        UserService normalUserService = new ProtectionProxy(new RealUserService(), "USER");
        System.out.println(normalUserService.getUser(1));

        System.out.println("\n--- Virtual Proxy ---");
        /*
         * VIRTUAL PROXY
         *
         * At this point RealUserService is NOT created.
         */
        UserService virtualService = new VirtualProxy();
        /*
         * First call:
         * RealUserService is created lazily.
         */
        System.out.println(virtualService.getUser(2));
        /*
         * Second call:
         * Existing RealUserService is reused.
         */
        System.out.println(virtualService.getUser(3));


        System.out.println("\n--- Caching Proxy ---");
        UserService cachingService = new CachingProxy(new RealUserService());
        /*
         * First call:
         * Cache Miss -> call real service -> cache result.
         */
        System.out.println(cachingService.getUser(10));
        /*
         * Second call:
         * Cache Hit -> real service is NOT called.
         */
        System.out.println(cachingService.getUser(10));


        System.out.println("\n--- Logging Proxy ---");
        UserService loggingService =
                new LoggingProxy(new RealUserService());
        /*
         * Logs before and after calling real service.
         */
        loggingService.getUser(20);

        System.out.println("\n--- Remote Proxy ---");
        UserService remoteService = new RemoteUserServiceProxy();
        /*
         * Client thinks it is calling UserService,
         * while Proxy hides remote communication.
         */
        System.out.println(remoteService.getUser(30));
    }
}